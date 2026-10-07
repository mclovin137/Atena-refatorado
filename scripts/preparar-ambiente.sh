#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
if [[ "${1:-}" != "" && "${1:-}" != --completar ]]; then
  printf '%s\n' 'Uso: preparar-ambiente.sh [--completar]' >&2
  exit 2
fi
if [[ -e .env && "${1:-}" != --completar ]]; then
  printf '%s\n' 'Ambiente existente preservado; credenciais não foram substituídas.'
  exit 0
fi
umask 077
python3 - <<'PY'
from pathlib import Path
import base64
import os
import secrets
p = Path('.env')
existing = p.read_text() if p.exists() else ''
known = {line.split('=', 1)[0] for line in existing.splitlines() if '=' in line and not line.startswith('#')}
keys = ['POSTGRES_PASSWORD', 'ESCOLAR_DB_PASSWORD', 'RELATORIOS_DB_PASSWORD', 'SERVICE_TOKEN', 'ADMIN_PASSWORD', 'LEGADO_DB_PASSWORD', 'LEGADO_ADMIN_PASSWORD']
with p.open('a' if p.exists() else 'x') as target:
    if existing and not existing.endswith('\n'):
        target.write('\n')
    for key in keys:
        if key not in known:
            target.write(f'{key}={secrets.token_hex(24)}\n')
    if 'IMPORT_ENCRYPTION_KEY' not in known:
        target.write('IMPORT_ENCRYPTION_KEY=' + base64.b64encode(secrets.token_bytes(32)).decode() + '\n')
    for key, value in [('ADMIN_EMAIL', 'secretaria@atena.local'), ('ATENA_PORT', '18080'), ('LEGADO_PORT', '18082'), ('LEGADO_ADMIN_EMAIL', 'secretaria@legado.atena.local')]:
        if key not in known:
            target.write(f'{key}={value}\n')
os.chmod(p, 0o600)
PY
printf '%s\n' 'Ambiente privado preparado; valores existentes preservados. Chave de importação deve ser guardada separadamente do backup.'
