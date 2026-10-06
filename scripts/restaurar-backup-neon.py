#!/usr/bin/env python3
"""Exercita dumps Neon privados em PostgreSQL local descartável sem rede."""
import importlib.util
import json
from pathlib import Path
import subprocess
import time
import uuid

ROOT = Path(__file__).resolve().parents[1]
spec = importlib.util.spec_from_file_location('cloud', ROOT / 'scripts/operar-cloud.py')
cloud = importlib.util.module_from_spec(spec)
spec.loader.exec_module(cloud)


def main():
    values = cloud.env_file(ROOT / '.env')
    private = cloud.env_file(ROOT / '.env.neon')
    uri = cloud.connection(values)
    name = 'atena-neon-restore-' + uuid.uuid4().hex[:10]
    report = {'postgres_client_major': 18, 'network': 'none', 'databases': []}

    def docker(*args, input=None):
        result = subprocess.run(['docker', *args], input=input, capture_output=True, timeout=120)
        if result.returncode:
            raise RuntimeError('Etapa Docker/restore falhou; diagnóstico sensível suprimido.')
        return result.stdout.decode().strip()

    def snapshot(database):
        return "SELECT json_build_object('tables', (SELECT count(*) FROM information_schema.tables WHERE table_schema='public'), 'columns', (SELECT count(*) FROM information_schema.columns WHERE table_schema='public'), 'rows', (SELECT json_object_agg(table_name, ((xpath('/row/total/text()',query_to_xml(format('SELECT count(*) AS total FROM %I.%I',table_schema,table_name),false,true,'')))[1])::text::bigint) FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'));"

    try:
        docker('run', '--rm', '-d', '--name', name, '--network', 'none', '-e', 'POSTGRES_HOST_AUTH_METHOD=trust', 'postgres:18-alpine')
        for _ in range(45):
            result = subprocess.run(['docker', 'exec', name, 'pg_isready', '-U', 'postgres'], capture_output=True, timeout=5)
            if result.returncode == 0:
                break
            time.sleep(1)
        else:
            raise RuntimeError('PostgreSQL de restore não iniciou.')
        for database in cloud.DATABASES:
            env = cloud.pg_env(uri, database, database, private[database.upper() + '_DB_PASSWORD'])
            dump = cloud.postgres(env, command='pg_dump', binary=True)
            directory = ROOT / '.atena-cloud-private'
            path = directory / (database + '-' + name + '.dump')
            if path.exists():
                raise RuntimeError('Snapshot existente preservado; use novo nome após revisão.')
            cloud.private_write(path, dump)
            docker('exec', name, 'createdb', '-U', 'postgres', database)
            docker('exec', '-i', name, 'pg_restore', '-U', 'postgres', '--no-owner', '--no-acl', '--exit-on-error', '-d', database, input=dump)
            query = snapshot(database)
            source = json.loads(cloud.postgres(env, query))
            restored = json.loads(docker('exec', name, 'psql', '-X', '-A', '-t', '-U', 'postgres', '-d', database, '-c', query))
            if source != restored:
                raise RuntimeError('Snapshot restaurado difere das contagens remotas.')
            report['databases'].append({'name': database, 'restore': True, **source})
        report['result'] = 'PASS'
        (ROOT / 'evidence/neon-restore.json').write_text(json.dumps(report, indent=2) + '\n')
        print(json.dumps(report, indent=2))
    finally:
        subprocess.run(['docker', 'rm', '-f', name], capture_output=True, timeout=30)


if __name__ == '__main__':
    try:
        main()
    except Exception as error:
        print('Restore interrompido: ' + (str(error) if isinstance(error, RuntimeError) else type(error).__name__))
        raise SystemExit(1)
