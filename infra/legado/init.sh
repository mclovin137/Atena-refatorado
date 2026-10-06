#!/bin/sh
set -eu
: "${LEGADO_DB_PASSWORD:?Credencial legado obrigatória}"
psql --host postgres --username postgres --dbname postgres -v ON_ERROR_STOP=1 <<'SQL'
\getenv legado_password LEGADO_DB_PASSWORD
SELECT format('CREATE ROLE legado LOGIN PASSWORD %L', :'legado_password') WHERE NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname='legado') \gexec
SELECT 'CREATE DATABASE legado OWNER legado' WHERE NOT EXISTS (SELECT 1 FROM pg_database WHERE datname='legado') \gexec
REVOKE CONNECT ON DATABASE legado FROM PUBLIC;
GRANT CONNECT ON DATABASE legado TO legado;
SQL
PGPASSWORD="$LEGADO_DB_PASSWORD" psql --host postgres --username legado --dbname legado -v ON_ERROR_STOP=1 --file /infra/schema.sql
