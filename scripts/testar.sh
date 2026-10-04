#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
mkdir -p evidence
TEST_CONTAINER="atena-test-${RANDOM}-${RANDOM}"
TEST_PASSWORD="$(python3 -c 'import secrets; print(secrets.token_hex(24))')"
export DB_PASSWORD="$TEST_PASSWORD" SERVICE_TOKEN="$TEST_PASSWORD" ADMIN_EMAIL=secretaria@atena.local ADMIN_PASSWORD="$TEST_PASSWORD"
export SCHOOL_URL=http://localhost:8080 REPORTS_URL=http://localhost:8081
cleanup() { docker rm -f "$TEST_CONTAINER" >/dev/null 2>&1 || true; }
trap cleanup EXIT
# Contêiner exclusivo; nenhum volume ou banco de outra aplicação é alterado.
docker run -d --name "$TEST_CONTAINER" \
  -p "127.0.0.1:15432:5432" \
  -e POSTGRES_PASSWORD="$TEST_PASSWORD" \
  -e ESCOLAR_DB_PASSWORD="$TEST_PASSWORD" \
  -e RELATORIOS_DB_PASSWORD="$TEST_PASSWORD" \
  -v "$PWD/infra/postgres/init.sh:/docker-entrypoint-initdb.d/10-atena.sh:ro" \
  postgres:17-alpine >/dev/null
READY=false
for ((attempt=0; attempt<60; attempt++)); do
  if docker exec "$TEST_CONTAINER" pg_isready -h 127.0.0.1 -U postgres >/dev/null 2>&1 && \
     docker exec "$TEST_CONTAINER" psql -U postgres -d relatorios -tAc 'SELECT 1' >/dev/null 2>&1; then
    READY=true
    break
  fi
  sleep 1
done
if [[ "$READY" != true ]]; then
  docker logs "$TEST_CONTAINER" > evidence/postgres-falha.log 2>&1
  printf '%s\n' 'PostgreSQL de teste não ficou pronto.' >&2
  exit 1
fi
npm --prefix frontend ci
npm --prefix frontend run build
mkdir -p services/escolar/src/main/resources/static
cp -R frontend/dist/. services/escolar/src/main/resources/static/
# Cada módulo configura banco próprio nos testes na porta local 15432.
mvn -B -DfailIfNoTests=true verify 2>&1 | tee evidence/maven.log
