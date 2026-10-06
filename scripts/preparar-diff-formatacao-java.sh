#!/usr/bin/env bash
# Evidência auxiliar de formatação no CI; não altera o checkout nem enfraquece o dry-run.
set -euo pipefail
cd "$(dirname "$0")/.."
mkdir -p evidence
formatter_cache="${TMPDIR:-/tmp}/atena-google-java-format-1.25.2.jar"
if [[ ! -f "$formatter_cache" ]]; then
  curl --fail --silent --show-error --location \
    https://repo.maven.apache.org/maven2/com/google/googlejavaformat/google-java-format/1.25.2/google-java-format-1.25.2-all-deps.jar \
    --output "$formatter_cache"
fi
format_worktree="$(mktemp -d "${TMPDIR:-/tmp}/atena-format-XXXXXX")"
cleanup() {
  git worktree remove --force "$format_worktree" >/dev/null 2>&1 || rmdir "$format_worktree"
}
trap cleanup EXIT
git worktree add --detach "$format_worktree" HEAD >/dev/null
docker run --rm -v "$format_worktree:/repo" -v "$formatter_cache:/formatter.jar:ro" -w /repo \
  maven:3.9-eclipse-temurin-21 bash -c \
  'find services -path "*/src/*" -name "*.java" -print0 | xargs -0 java -jar /formatter.jar --aosp --replace'
git -C "$format_worktree" diff --binary -- services > evidence/java-format.patch
git rev-parse HEAD > evidence/java-format-source.txt
