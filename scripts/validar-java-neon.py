#!/usr/bin/env python3
"""Boot sequencial Java prod contra Neon, em contêineres locais descartáveis."""
import importlib.util
import json
import os
from pathlib import Path
import shlex
import subprocess
import time
import urllib.request
import uuid

ROOT = Path(__file__).resolve().parents[1]
spec = importlib.util.spec_from_file_location('cloud', ROOT / 'scripts/operar-cloud.py')
cloud = importlib.util.module_from_spec(spec)
spec.loader.exec_module(cloud)


def main():
    credentials = cloud.env_file(ROOT / '.env.neon')
    production = cloud.env_file(ROOT / '.env.producao')
    local = cloud.env_file(ROOT / '.env')
    uri = cloud.connection(local)
    report = {'result': 'PASS', 'services': [], 'publication_render': False}
    for service, migrations in [('relatorios', 2), ('escolar', 9)]:
        jar = ROOT / f'services/{service}/target/{service}-service-1.0.0.jar'
        if not jar.exists():
            raise RuntimeError('JAR atual ausente: ' + service)
        name = 'atena-neon-java-' + uuid.uuid4().hex[:10]
        env = {key: credentials[service.upper() + '_' + key] for key in ['DB_URL', 'DB_USER', 'DB_PASSWORD']}
        env.update(SPRING_PROFILES_ACTIVE='prod', JAVA_TOOL_OPTIONS='-Xmx320m -XX:MaxMetaspaceSize=128m',
                   PORT='10000', SERVICE_TOKEN=production['SERVICE_TOKEN'])
        if service == 'escolar':
            env.update({key: production[key] for key in ['ADMIN_EMAIL', 'ADMIN_PASSWORD', 'IMPORT_ENCRYPTION_KEY']})
            env['REPORTS_URL'] = 'https://pending.invalid'
        else:
            env['SCHOOL_URL'] = 'https://pending.invalid'
        script = '\n'.join('export ' + key + '=' + shlex.quote(value) for key, value in env.items()) + '\nexec java -jar /app/app.jar\n'
        logpath = ROOT / ('.atena-cloud-private/' + name + '.log')
        descriptor = os.open(logpath, os.O_CREAT | os.O_EXCL | os.O_WRONLY, 0o600)
        with os.fdopen(descriptor, 'wb') as log:
            process = subprocess.Popen(['docker', 'run', '--rm', '-i', '--name', name, '--memory', '512m', '--cpus', '1',
                                        '-p', '127.0.0.1::10000', '-v', str(jar) + ':/app/app.jar:ro',
                                        '--entrypoint', 'sh', 'eclipse-temurin:21-jre'], stdin=subprocess.PIPE, stdout=log, stderr=log)
            process.stdin.write(script.encode())
            process.stdin.close()
            try:
                url = None
                for _ in range(150):
                    if process.poll() is not None:
                        raise RuntimeError('Boot prod falhou: ' + service + '; log privado preservado.')
                    if url is None:
                        port = subprocess.run(['docker', 'port', name, '10000/tcp'], capture_output=True, text=True, timeout=10)
                        if port.returncode == 0 and port.stdout.strip():
                            url = 'http://' + port.stdout.strip()
                    if url:
                        try:
                            with urllib.request.urlopen(url + '/actuator/health', timeout=5) as response:
                                health = json.load(response)
                            if health.get('status') == 'UP':
                                break
                        except Exception:
                            pass
                    time.sleep(1)
                else:
                    raise RuntimeError('Boot prod excedeu prazo: ' + service)
                pgenv = cloud.pg_env(uri, service, service, credentials[service.upper() + '_DB_PASSWORD'])
                count = int(cloud.postgres(pgenv, 'SELECT count(*) FROM flyway_schema_history WHERE success;'))
                if count != migrations:
                    raise RuntimeError('Migrações divergentes: ' + service)
                item = {'name': service, 'health': 'UP', 'flyway_migrations': count, 'jdbc_verify_full': True}
                if service == 'escolar':
                    # Não expor e-mail/hash/PII; somente cardinalidade inicial privada.
                    admin_email = production['ADMIN_EMAIL'].replace("'", "''")
                    admins = int(cloud.postgres(pgenv, f"SELECT count(*) FROM pessoa WHERE email='{admin_email}' AND ativo AND senha_hash IS NOT NULL;"))
                    if admins != 1:
                        raise RuntimeError('Bootstrap Java privado não confirmado.')
                    item['private_bootstrap_count'] = admins
                report['services'].append(item)
            finally:
                subprocess.run(['docker', 'rm', '-f', name], capture_output=True, timeout=60)
                process.wait(timeout=30)
    (ROOT / 'evidence/neon-java-prod.json').write_text(json.dumps(report, indent=2) + '\n')
    print(json.dumps(report, indent=2))


if __name__ == '__main__':
    try:
        main()
    except Exception as error:
        print('Validação interrompida: ' + (str(error) if isinstance(error, RuntimeError) else type(error).__name__))
        raise SystemExit(1)
