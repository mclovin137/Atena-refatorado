#!/bin/sh
set -eu
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname postgres \
  --set=escolar_password="$ESCOLAR_DB_PASSWORD" \
  --set=relatorios_password="$RELATORIOS_DB_PASSWORD" <<'SQL'
CREATE ROLE escolar LOGIN PASSWORD :'escolar_password';
CREATE ROLE relatorios LOGIN PASSWORD :'relatorios_password';
CREATE DATABASE escolar OWNER escolar;
CREATE DATABASE relatorios OWNER relatorios;
REVOKE CONNECT ON DATABASE escolar FROM PUBLIC;
REVOKE CONNECT ON DATABASE relatorios FROM PUBLIC;
GRANT CONNECT ON DATABASE escolar TO escolar;
GRANT CONNECT ON DATABASE relatorios TO relatorios;
SQL
