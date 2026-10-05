#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
formatter_cache="${TMPDIR:-/tmp}/atena-google-java-format-1.25.2.jar"
if [[ ! -f "$formatter_cache" ]]; then
  curl --fail --silent --show-error --location \
    https://repo.maven.apache.org/maven2/com/google/googlejavaformat/google-java-format/1.25.2/google-java-format-1.25.2-all-deps.jar \
    --output "$formatter_cache"
fi
# JDK21 completo evita a incompatibilidade observada com javac25 e a falta de javac no JRE.
docker run --rm -v "$PWD:/repo:ro" -v "$formatter_cache:/formatter.jar:ro" -w /repo \
  maven:3.9-eclipse-temurin-21 bash -c \
  'find services -path "*/src/*" -name "*.java" -print0 | xargs -0 java -jar /formatter.jar --aosp --dry-run --set-exit-if-changed'
