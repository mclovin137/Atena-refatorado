#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
for file in scripts/*.sh infra/postgres/*.sh; do bash -n "$file"; done
python3 - <<'PY'
from pathlib import Path
required = ['docs/rules.md', 'docs/continuidade.md', 'docs/lib.md', 'docs/pendencias-obsidian.md', 'docs/prds/prd-001-refatoracao.md', 'docs/hlds/hld-refatoracao.md', 'docs/fdds/FDD-001-refatoracao.md', 'docs/tasks/t-001-refatoracao.md', 'EXPERIMENT_LOG.md']
missing = [file for file in required if not Path(file).is_file()]
if missing:
    raise SystemExit('Documentos obrigatórios ausentes: ' + ', '.join(missing))
expected = ['Task ativa', 'Estado atual', 'Decisões vigentes', 'Riscos e lacunas', 'Próximo passo', 'Região gerada']
headings = [line[3:].strip() for line in Path('docs/continuidade.md').read_text().splitlines() if line.startswith('## ')]
if len(headings) != len(expected) or set(headings) != set(expected):
    raise SystemExit('Continuidade precisa conter uma única seção de cada título declarado.')
if Path('docs/continuidade.md').stat().st_size > 8192:
    raise SystemExit('Continuidade excede 8 KB.')
PY
if [[ -n "$(git ls-files -- .env '.env.*' | sed '/^\.env\.example$/d')" ]]; then
  printf '%s\n' 'Arquivo de credenciais está versionado.' >&2
  exit 1
fi
./scripts/preparar-ambiente.sh
docker compose config --quiet
if command -v gitleaks >/dev/null 2>&1; then
  gitleaks detect --source . --no-git --redact --log-level warn
else
  docker run --rm -v "$PWD:/repo:ro" ghcr.io/gitleaks/gitleaks:v8.30.1 \
    detect --source /repo --no-git --redact --log-level warn
fi
