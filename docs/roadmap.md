---
type: minerva-roadmap
project: Atena
date: 2026-10-04
status: vigente
tags: [atena, roadmap]
ai-first: true
---

# Roadmap Atena

## Para o futuro agente
Produto escolar para secretaria, professores e alunos. Prioridade: preservar funcionalidades e segurança, tornar fonte/decisões legíveis, depois publicar dois serviços Render com PostgreSQL Neon quando operação autenticada/gratuita estiver comprovada. Este é o roadmap canônico de produto no caminho docs/roadmap.md e base Atena; não criar aliases STATE/ROADMAP na raiz.

## Etapas, prioridades e dependências

| Prioridade | Entrega | Estado observado | Dependências e condição de conclusão |
|---|---|---|---|
| P0 | T-001 migração PHP para Spring/React, dois contextos, PG/Docker e Batch | Código integrado e validado LOCALMENTE; PR1/PR2 integrados | Testes PG/E2E, relatórios e autorização executados; limites históricos registrados |
| P0 | ADRs e continuidade conformes, auditoria JSON factual | Em andamento no épico002 | Role/skills canônicas, mirror Obsidian e revisão independente; sem autorevisão fictícia |
| P0 | T-002 qualidade por domínio e contratos tipados | Implementado localmente: relatórios 4 testes PG verdes, escolar 1 fluxo integrado ampliado verde, React build/formato verdes; E2E Docker real passou | Gate FDD/task cumprido antes produção; factory tem variantes/invariantes reais |
| P0 | Paridade de fluxos e legibilidade após refatoração | Relatórios: quatro testes/verify verdes; escola, integração, E2E e tracing novos ainda pendentes | Builds/PG/E2E/segurança/jobs/tracing reais, review por recurso e artifacts |
| P1 | Durabilidade de arquivos/avatar PostgreSQL | V5/BYTEA/backfill e restauração individual passaram PG isolado; reinício/backup integral/E2E Docker real passous | SQL aditivo e compatibilidade/backfill/rollback no FDD; teste após reinício |
| P1 | Produção Render/Neon via MCP | NÃO EXECUTADA; Neon não autenticado, quota/compatibilidade pendentes | Dois serviços Docker gratuitos, TLS/token/cookies, bancos separados; health/login/E2E público e persistência observados |
| P1 | Encerramento auditável da entrega nova | Não concluído | AUDIT_REPORT.json completo e atualizado, revisão independente, dois CI verdes, merge/deploy factual e base sincronizada |

Nenhuma data de entrega, SLA, custo/tokens/latência estimada. Hibernação e franquia free são limitações operacionais, não conclusão de publicação.

## Cadeia ativa
- [Épico001](epicos/epico-001-refatoracao.md)/[T-001](tasks/t-001-refatoracao.md) preservam registro da entrega local.
- [Épico002](epicos/epico-002-qualidade-producao.md) → [PRD-002](prds/prd-002-qualidade-producao.md) → [HLD qualidade](hlds/hld-qualidade-producao.md) → [ADR004](adrs/adr-004-organizacao-backend.md)/[ADR005](adrs/adr-005-producao-render-neon.md) → [FDD-002 aprovado](fdds/fdd-002-qualidade-producao.md) → [T-002 pronta](tasks/t-002-qualidade-producao.md).
- [Continuidade](continuidade.md) contém somente estado ativo; [histórico](historico/2026-10.md) é append-only; [pendências](pendencias-obsidian.md) contêm obrigações abertas; docs/lib.md inventário. Auditoria estruturada solicitada: ../AUDIT_REPORT.json.

## Riscos e limites
Contratos/autorização podem regredir durante extração; factories/ports sem responsabilidade são overengineering. Neon MCP não autenticado e configuração/quota Render gratuita bloqueiam remota; não contratar, usar credenciais indevidas ou substituir provedor silenciosamente. Dump ausente e ausência de carga permanecem limites. Não confundir código de produção integrado com deploy de produção: T-001 validou ambiente local; Render/Neon ainda não publicados.

## Origem e responsabilidade
Dono: planejador/Yoda; implementador e operação fazem entregas distintas e revisão independente. Governança Minerva remota76a991d e referência seletiva MinervaFinancas remoto34fc98a; prompt/autonomia e pedidos adicionais prevalecem sobre entrevistas ou restrição original de docs quando autorizam AUDIT_REPORT.json.

## Histórico
- 2026-10-04: roadmap inicial de migração T-001.
- 2026-10-04: T-001 local integrado PR1 a29342ac3c99be5a733d6ccb6e32c8d85465dbc2; fechamento PR2 4ee1baf8b9d4e1ab54467504d3cbebb4ba838ad6, CI/evidências reais no histórico/task.
- 2026-10-04: roteiro de produto atualizado para qualidade, ADRs/estado/auditoria e produção remota ainda pendente conforme pedidos novos.
