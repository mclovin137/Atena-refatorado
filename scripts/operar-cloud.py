#!/usr/bin/env python3
"""Operação Atena: leitura segura e bootstrap Neon explicitamente autorizado.

Não carrega .env como shell, não mostra credenciais e não cria recursos Render.
"""
import argparse
import json
import os
from pathlib import Path
import secrets
import shlex
import subprocess
import urllib.error
import urllib.parse
import urllib.request

ROOT = Path(__file__).resolve().parents[1]
PROJECT = "blue-bonus-15349315"
BRANCH = "br-weathered-smoke-b5brhfga"
DATABASES = ("escolar", "relatorios", "legado")


def env_file(path):
    values = {}
    for line in path.read_text().splitlines():
        if line.strip() and not line.lstrip().startswith("#") and "=" in line:
            key, value = line.split("=", 1)
            values[key.strip()] = value.strip().strip("\"'")
    return values


def api(values, path):
    request = urllib.request.Request(
        "https://console.neon.tech/api/v2" + path,
        headers={"Authorization": "Bearer " + values["NEON_API_KEY"]},
    )
    with urllib.request.urlopen(request, timeout=30) as response:
        return json.load(response)


def connection(values):
    query = urllib.parse.urlencode({"branch_id": BRANCH, "database_name": "neondb", "role_name": "neondb_owner"})
    return urllib.parse.urlsplit(api(values, f"/projects/{PROJECT}/connection_uri?{query}")["uri"])


def pg_env(uri, database="neondb", role=None, password=None):
    return {"PGHOST": uri.hostname, "PGPORT": str(uri.port or 5432),
            "PGUSER": role or urllib.parse.unquote(uri.username),
            "PGPASSWORD": password or urllib.parse.unquote(uri.password),
            "PGDATABASE": database, "PGSSLMODE": "verify-full",
            "PGSSLROOTCERT": "/certs/ca-certificates.crt", "PGCONNECT_TIMEOUT": "30"}


def postgres(env, sql=None, command="psql", binary=False):
    # Segredos seguem apenas no stdin privado, nunca argumentos/env de Docker.
    script = "\n".join("export " + key + "=" + shlex.quote(value) for key, value in env.items()) + "\n"
    if command == "pg_dump":
        script += "exec pg_dump --format=custom --no-owner --no-acl\n"
    else:
        script += "exec psql -X -q -A -t -v ON_ERROR_STOP=1 <<'ATENA_SQL'\n" + sql + "\nATENA_SQL\n"
    result = subprocess.run(
        ["docker", "run", "--rm", "-i", "-v", "/etc/ssl/certs:/certs:ro", "postgres:18-alpine", "sh"],
        input=script.encode(), capture_output=True, timeout=120,
    )
    if result.returncode:
        # SQL/erros podem conter valores sensíveis; não propagá-los para logs.
        raise RuntimeError("PostgreSQL recusou a etapa; saída sensível suprimida.")
    return result.stdout if binary else result.stdout.decode().strip()


def inspect(values):
    org_response = api(values, "/organizations/" + values["NEON_ORG"])
    org = org_response.get("organization", org_response)
    project = api(values, f"/projects/{PROJECT}")["project"]
    branches = api(values, f"/projects/{PROJECT}/branches")["branches"]
    databases = api(values, f"/projects/{PROJECT}/branches/{BRANCH}/databases")["databases"]
    roles = api(values, f"/projects/{PROJECT}/branches/{BRANCH}/roles")["roles"]
    uri = connection(values)
    count = postgres(pg_env(uri), "BEGIN READ ONLY; SELECT count(*) FROM pg_class c JOIN pg_namespace n ON n.oid=c.relnamespace WHERE n.nspname NOT IN ('pg_catalog','information_schema') AND n.nspname NOT LIKE 'pg_toast%' AND n.nspname NOT LIKE 'pg_temp%'; COMMIT;")
    report = {"project_id": project["id"], "region": project["region_id"], "plan": org["plan"],
              "branch_ids": [branch["id"] for branch in branches],
              "databases": [database["name"] for database in databases],
              "roles": [role["name"] for role in roles],
              "neondb_non_system_objects": int(count), "libpq_verify_full": True,
              "render_api_key_present": bool(values.get("RENDER_API_KEY"))}
    print(json.dumps(report, indent=2))
    return report, uri


def private_write(path, content):
    descriptor = os.open(path, os.O_CREAT | os.O_EXCL | os.O_WRONLY, 0o600)
    with os.fdopen(descriptor, "wb") as stream:
        stream.write(content)


def bootstrap(values, resume=False):
    report, uri = inspect(values)
    if report["plan"] != "free" or report["neondb_non_system_objects"] != 0:
        raise RuntimeError("Bootstrap exige plano free e base inicial vazia.")
    if any(name in report["databases"] or name in report["roles"] for name in DATABASES):
        raise RuntimeError("Nome Atena já existe; nenhuma substituição ou rotação automática autorizada.")
    credentials = ROOT / ".env.neon"
    if credentials.exists() and not resume:
        raise RuntimeError("Arquivo privado já existe; preserve-o e revise eventual execução parcial.")
    if resume and not credentials.exists():
        raise RuntimeError("Retomada exige credenciais privadas já geradas.")
    saved = env_file(credentials) if resume else {}
    passwords = {name: saved[name.upper() + "_DB_PASSWORD"] if resume else secrets.token_urlsafe(36) for name in DATABASES}
    lines = ["NEON_PROJECT_ID=" + PROJECT, "NEON_BRANCH_ID=" + BRANCH]
    for name in DATABASES:
        prefix = name.upper()
        lines.extend([f"{prefix}_DB_USER={name}", f"{prefix}_DB_PASSWORD={passwords[name]}",
                      f"{prefix}_DB_URL=jdbc:postgresql://{uri.hostname}:5432/{name}?sslmode=verify-full"])
    lines.append(f"LEGADO_DB_DSN=pgsql:host={uri.hostname};port=5432;dbname=legado;sslmode=verify-full;sslrootcert=/etc/ssl/certs/ca-certificates.crt")
    # Registrar antes da primeira mutação evita perda de credenciais em falha parcial.
    if not resume:
        private_write(credentials, ("\n".join(lines) + "\n").encode())
    backup = ROOT / ".atena-cloud-private"
    backup.mkdir(parents=True, exist_ok=True, mode=0o700)
    os.chmod(backup, 0o700)
    private_write(backup / "neondb-before.dump", postgres(pg_env(uri), command="pg_dump", binary=True))
    for name in DATABASES:
        password_literal = passwords[name].replace("'", "''")
        postgres(pg_env(uri), f"BEGIN; CREATE ROLE {name} LOGIN NOSUPERUSER NOCREATEROLE NOCREATEDB NOREPLICATION PASSWORD '{password_literal}'; GRANT {name} TO neondb_owner; COMMIT;")
        postgres(pg_env(uri), f"CREATE DATABASE {name} OWNER {name};")
        postgres(pg_env(uri), f"BEGIN; REVOKE ALL ON DATABASE {name} FROM PUBLIC; GRANT CONNECT,TEMPORARY ON DATABASE {name} TO {name}; REVOKE {name} FROM neondb_owner; COMMIT;")
        postgres(pg_env(uri, name, name, passwords[name]), "SELECT 1;")
        print("Base/role criada e conexão própria validada: " + name)
    privileged = postgres(pg_env(uri), "SELECT count(*) FROM pg_roles WHERE rolname IN ('escolar','relatorios','legado') AND (rolsuper OR rolcreaterole OR rolcreatedb OR rolreplication OR pg_has_role(rolname,'neon_superuser','MEMBER') OR pg_has_role(rolname,'pg_read_all_data','MEMBER') OR pg_has_role(rolname,'pg_write_all_data','MEMBER'));")
    if privileged != "0":
        raise RuntimeError("Role excedeu os privilégios permitidos; interrompida publicação.")
    for name in DATABASES:
        for other in DATABASES:
            if name != other:
                allowed = postgres(pg_env(uri, name, name, passwords[name]), f"SELECT has_database_privilege(current_user, '{other}', 'CONNECT');")
                if allowed != "f":
                    raise RuntimeError("Isolamento entre bases falhou.")
    postgres(pg_env(uri, "legado", "legado", passwords["legado"]), (ROOT / "infra/legado/schema.sql").read_text())
    for name in DATABASES:
        private_write(backup / (name + "-bootstrap.dump"), postgres(pg_env(uri, name, name, passwords[name]), command="pg_dump", binary=True))
    print("Bootstrap Neon concluído: isolamento conferido, legado vazio sem fixtures, backups privados; Flyway Java/restore/publicação ainda pendentes.")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--bootstrap-neon", action="store_true", help="Executa plano Neon revisado; exige autorização de operação na sessão.")
    parser.add_argument("--resume-before-mutation", action="store_true", help="Reusa credenciais após falha ANTES de qualquer role/base; nunca retoma parcialmente.")
    args = parser.parse_args()
    values = env_file(ROOT / ".env")
    try:
        if args.bootstrap_neon:
            bootstrap(values, args.resume_before_mutation)
        else:
            inspect(values)
    except urllib.error.HTTPError as error:
        parser.exit(1, f"API Neon recusou a etapa: HTTP {error.code}; nenhum segredo exibido.\n")
    except (RuntimeError, KeyError, OSError, ValueError, subprocess.TimeoutExpired) as error:
        message = str(error) if isinstance(error, RuntimeError) else type(error).__name__
        parser.exit(1, "Operação interrompida: " + message + "\n")


if __name__ == "__main__":
    main()
