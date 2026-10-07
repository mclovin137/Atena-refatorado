#!/usr/bin/env python3
"""Prepara plano Render revisável; jamais provisiona nem publica serviços."""
import base64
import importlib.util
import json
from pathlib import Path
import secrets
import urllib.request

ROOT = Path(__file__).resolve().parents[1]
spec = importlib.util.spec_from_file_location('cloud', ROOT / 'scripts/operar-cloud.py')
cloud = importlib.util.module_from_spec(spec)
spec.loader.exec_module(cloud)


def main():
    import yaml
    blueprint = yaml.safe_load((ROOT / 'render.yaml').read_text())
    local = cloud.env_file(ROOT / '.env')
    neon = cloud.env_file(ROOT / '.env.neon')
    production = ROOT / '.env.producao'
    if not production.exists():
        values = {'SERVICE_TOKEN': secrets.token_urlsafe(48),
                  'IMPORT_ENCRYPTION_KEY': base64.b64encode(secrets.token_bytes(32)).decode(),
                  'ADMIN_EMAIL': local['ADMIN_EMAIL'], 'ADMIN_PASSWORD': secrets.token_urlsafe(36),
                  'LEGADO_ADMIN_EMAIL': local['LEGADO_ADMIN_EMAIL'], 'LEGADO_ADMIN_PASSWORD': secrets.token_urlsafe(36)}
        cloud.private_write(production, ''.join(f'{key}={value}\n' for key, value in values.items()).encode())
    values = cloud.env_file(production)
    plans = []
    for service in blueprint['services']:
        if service['plan'] != 'free' or service['runtime'] != 'docker' or service.get('autoDeployTrigger') != 'off':
            raise RuntimeError('Blueprint fora do plano gratuito Docker manual revisado.')
        part = service['name'].split('-')[-1]
        env = {}
        pending = []
        for entry in service['envVars']:
            key = entry['key']
            if 'value' in entry:
                env[key] = entry['value']
            elif key in ('DB_URL', 'DB_USER', 'DB_PASSWORD'):
                env[key] = neon[part.upper() + '_' + key]
            elif key.startswith('LEGADO_DB_'):
                env[key] = neon[key]
            elif key in values:
                env[key] = values[key]
            elif key in ('REPORTS_URL', 'SCHOOL_URL', 'LEGADO_BASE_URL'):
                pending.append(key)
            else:
                raise RuntimeError('Variável sem origem: ' + key)
        envpath = ROOT / ('.env.render.' + part)
        if not envpath.exists():
            cloud.private_write(envpath, ''.join(f'{key}={value}\n' for key, value in env.items()).encode())
        plans.append({'name': service['name'], 'plan': 'free', 'runtime': 'docker',
                      'branch': service['branch'], 'dockerfilePath': service['dockerfilePath'],
                      'dockerContext': service['dockerContext'], 'dockerCommand': service.get('dockerCommand', ''),
                      'env_keys': list(env), 'pending_public_urls': pending})
    report = {'services': plans, 'render_api_key_present': bool(local.get('RENDER_API_KEY')),
              'workspace_confirmed': local.get('RENDER_WORKSPACE_CONFIRMED') == 'true',
              'no_payment_method_confirmed': local.get('RENDER_NO_PAYMENT_METHOD_CONFIRMED') == 'true',
              'cloud_created': False}
    print(json.dumps(report, indent=2))
    if local.get('RENDER_API_KEY') and local.get('RENDER_WORKSPACE_CONFIRMED') == 'true':
        # Validação somente; sem criação. Multipart com blueprint sem segredos.
        workspace = local['RENDER_WORKSPACE_ID']
        boundary = 'atena-' + secrets.token_hex(12)
        data = (f'--{boundary}\r\nContent-Disposition: form-data; name="ownerId"\r\n\r\n{workspace}\r\n'
                f'--{boundary}\r\nContent-Disposition: form-data; name="file"; filename="render.yaml"\r\n'
                'Content-Type: application/yaml\r\n\r\n').encode() + (ROOT / 'render.yaml').read_bytes() + f'\r\n--{boundary}--\r\n'.encode()
        request = urllib.request.Request('https://api.render.com/v1/blueprints/validate', data=data,
                                         headers={'Authorization': 'Bearer ' + local['RENDER_API_KEY'],
                                                  'Content-Type': 'multipart/form-data; boundary=' + boundary})
        with urllib.request.urlopen(request, timeout=45) as response:
            result = json.load(response)
        print(json.dumps({'render_blueprint_valid': result.get('valid'), 'planned_actions': result.get('plan', {}).get('totalActions')}, indent=2))


if __name__ == '__main__':
    try:
        main()
    except Exception as error:
        print('Preparação interrompida: ' + (str(error) if isinstance(error, RuntimeError) else type(error).__name__))
        raise SystemExit(1)
