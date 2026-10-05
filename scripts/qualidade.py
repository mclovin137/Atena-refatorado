#!/usr/bin/env python3
"""Verifica contratos de auditoria e fronteiras explícitas do código Java próprio."""
import json
import re
from pathlib import Path

SECOES = {
    "execution": {"status", "attempt", "needs_rework", "rework_reason"},
    "requirements": {"requirements_received", "requirements_addressed", "requirements_not_addressed", "possible_regressions"},
    "architecture": {"followed", "violations_detected", "architectural_decisions"},
    "implementation": {"files_created", "files_modified", "files_deleted", "dependencies_added", "abstractions_created"},
    "overengineering": {"possible_overengineering", "items", "justifications"},
    "security": {"security_concerns_found", "security_changes_made", "new_security_risks_possible"},
    "validation": {"tests_created", "tests_executed", "tests_passed", "tests_failed", "static_analysis_executed", "validation_not_performed"},
    "quality": {"known_technical_debt_added", "known_technical_debt_removed", "assumptions", "open_questions"},
}
auditoria = json.loads(Path("AUDIT_REPORT.json").read_text())
assert set(auditoria) == set(SECOES) | {"experiment_id", "task_id", "model_role", "decisions"}, "Chaves raiz da auditoria divergentes"
for secao, campos in SECOES.items():
    assert set(auditoria[secao]) == campos, f"Campos divergentes na auditoria: {secao}"
assert auditoria["execution"]["status"] in {"success", "partial", "failed"}
assert auditoria["execution"]["attempt"] is None or isinstance(auditoria["execution"]["attempt"], int)
assert isinstance(auditoria["execution"]["needs_rework"], bool)
for decisao in auditoria["decisions"]:
    assert set(decisao) == {"decision", "reason", "requirement", "trade_off"}
for dependencia in auditoria["implementation"]["dependencies_added"]:
    assert set(dependencia) == {"name", "reason", "alternative_considered", "necessary"}
for abstracao in auditoria["implementation"]["abstractions_created"]:
    assert set(abstracao) == {"type", "name", "reason", "required_by"}

falhas = []
for arquivo in Path("services").glob("*/src/main/java/**/*.java"):
    fonte = arquivo.read_text()
    imports = re.findall(r"^import\s+(?:static\s+)?([^;]+);", fonte, re.MULTILINE)
    for nome_interface in re.findall(r"\bpublic\s+interface\s+(\w+)", fonte):
        if not nome_interface.startswith("I"):
            falhas.append(f"{arquivo}: interface sem prefixo I: {nome_interface}")
    pacote = re.search(r"^package\s+([^;]+);", fonte, re.MULTILINE).group(1)
    for dependencia in imports:
        if "*" in dependencia:
            falhas.append(f"{arquivo}: import wildcard {dependencia}")
        if ".dominio" in pacote and not dependencia.startswith(("java.", "br.com.atena.")):
            falhas.append(f"{arquivo}: domínio importa framework {dependencia}")
        if ".dominio" in pacote and any(parte in dependencia for parte in (".dto.", ".dao.", ".actor.", ".rest.", ".jpa.")):
            falhas.append(f"{arquivo}: domínio acessa adaptador {dependencia}")
        if ".service" in pacote and any(parte in dependencia for parte in ("org.springframework.", "jakarta.", ".dao.", ".rest.", ".dto.")):
            falhas.append(f"{arquivo}: Service puro acessa I/O/transporte {dependencia}")
        if ".builder" in pacote and any(parte in dependencia for parte in ("org.springframework.", "jakarta.persistence.", ".dao.", ".actor.")):
            falhas.append(f"{arquivo}: Builder acessa I/O {dependencia}")
        if ".rest" in pacote and dependencia.startswith("br.com.atena.") and (".dao." in dependencia or ".jpa." in dependencia):
            falhas.append(f"{arquivo}: REST acessa persistência {dependencia}")
    # Esta inspeção é lexical e delimitada; não substitui revisão SOLID/OC nem análise AST.
    codigo = re.sub(r'"(?:\\.|[^"\\])*"|//[^\n]*|/\*[\s\S]*?\*/', '', fonte)
    if "java.lang.reflect" in codigo or "instanceof" in codigo and ".autorizacao." in pacote:
        falhas.append(f"{arquivo}: reflexão/typecode na autorização")
if falhas:
    raise SystemExit("\n".join(falhas))
print("PASS auditoria JSON e fronteiras lexicais Java; revisão semântica permanece necessária.")
