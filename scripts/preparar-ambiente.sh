#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
if [[ -e .env ]]; then
  printf '%s\n' 'Ambiente existente preservado; credenciais não foram substituídas.'
  exit 0
fi
umask 077
python3 - <<'PY'
from pathlib import Path
import secrets
keys = ['POSTGRES_PASSWORD', 'ESCOLAR_DB_PASSWORD', 'RELATORIOS_DB_PASSWORD', 'SERVICE_TOKEN', 'ADMIN_PASSWORD']
with Path('.env').open('x') as target:
    for key in keys:
        target.write(f'{key}={secrets.token_hex(24)}\n')
    target.write('ADMIN_EMAIL=secretaria@atena.local\nATENA_PORT=18080\n')
PY
printf '%s\n' 'Arquivo .env criado com acesso privado. Consulte-o localmente para o login da secretaria.'
