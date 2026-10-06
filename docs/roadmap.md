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
| P0 | ADRs e continuidade conformes, auditoria JSON factual | Local integrado e revisão/CI observados; cloud permanece aberta | Role/skills canônicas, mirror Obsidian e revisão independente; sem autorevisão fictícia |
| P0 | T-002 qualidade por domínio e contratos tipados | Implementado localmente: relatórios 4 testes PG verdes, escolar 1 fluxo integrado ampliado verde, React build/formato verdes; E2E Docker real passou | Gate FDD/task cumprido antes produção; factory tem variantes/invariantes reais |
| P0 | Paridade de fluxos e legibilidade após refatoração | Cinco JUnit, E2E real cinco tipos, tracing, reinício/restore/rollback local e dois CI PR3 passaram | Builds/PG/E2E/segurança/jobs/tracing reais, review por recurso e artifacts |
| P1 | Durabilidade de arquivos/avatar PostgreSQL | V5/BYTEA/backfill e restauração individual passaram PG isolado; reinício/backup integral/E2E Docker real passou | SQL aditivo e compatibilidade/backfill/rollback no FDD; teste após reinício |
| P1 | Produção Render/Neon via MCP | NÃO EXECUTADA; Neon não autenticado, quota/compatibilidade pendentes | Dois serviços Docker gratuitos, TLS/token/cookies, bancos separados; health/login/E2E público e persistência observados |
| P1 | Encerramento auditável local T002 | Integrado PR4 74886e3; task/produção cloud permanecem parciais | AUDIT_REPORT.json completo e atualizado, revisão independente, dois CI verdes, merge/deploy factual e base sincronizada |

| P0 | T003 modelo acadêmico por IDs e importação107 registros auditada | Em implementação: últimos11casos escolares locais verdes por suíte+focal; importação sintética validada; E2E React acadêmico/restore escolar passaram; importação da fonte original de 107 registros pendente | FDD aprovado, migrações aditivas, replay/segurança e paridade contratos reais |
| P1 | T003 PHP legado real PostgreSQL | PHP/PG local, 146 lint, backend/PDF/bytes/restart/restore e navegador três perfis passaram; mobile tem limitação de conteúdo cortado | FDD/versão/dependências/rotas reais e correções mínimas, revisão/gates antes publicação |

Nenhuma data de entrega, SLA, custo/tokens/latência estimada. Hibernação e franquia free são limitações operacionais, não conclusão de publicação.

## Cadeia ativa
- [Épico003](epicos/epico-003-modelo-legado.md) → [PRD003](prds/prd-003-modelo-legado.md) → [HLD](hlds/hld-modelo-legado.md)/ADRs006–007 → [FDD003](fdds/fdd-003-modelo-importacao-legado.md) aprovado → [T003 em andamento](tasks/t-003-modelo-importacao-legado.md); produção local liberada por escopo após cadeia aprovada, remoto bloqueado.
- [Épico001](epicos/epico-001-refatoracao.md)/[T-001](tasks/t-001-refatoracao.md) preservam registro da entrega local.
- [Épico002](epicos/epico-002-qualidade-producao.md) → [PRD-002](prds/prd-002-qualidade-producao.md) → [HLD qualidade](hlds/hld-qualidade-producao.md) → [ADR004](adrs/adr-004-organizacao-backend.md)/[ADR005](adrs/adr-005-producao-render-neon.md) → [FDD-002 aprovado](fdds/fdd-002-qualidade-producao.md) → [T-002 pronta](tasks/t-002-qualidade-producao.md).
- [Continuidade](continuidade.md) contém somente estado ativo; [histórico](historico/2026-10.md) é append-only; [pendências](pendencias-obsidian.md) contêm obrigações abertas; docs/lib.md inventário. Auditoria estruturada solicitada: ../AUDIT_REPORT.json.

## Riscos e limites
Contratos/autorização podem regredir durante extração; factories/ports sem responsabilidade são overengineering. Neon MCP não autenticado e configuração/quota Render gratuita bloqueiam remota; não contratar, usar credenciais indevidas ou substituir provedor silenciosamente. Dump T003 fornecido/privado e analisado; dados órfãos e schema diferente impedem importação automática sem reconciliação. Carga de produção não testada. Não confundir código de produção integrado com deploy de produção: T-001 validou ambiente local; Render/Neon ainda não publicados.

## Origem e responsabilidade
Dono: planejador/Yoda; implementador e operação fazem entregas distintas e revisão independente. Governança Minerva remota76a991d e referência seletiva MinervaFinancas remoto34fc98a; prompt/autonomia e pedidos adicionais prevalecem sobre entrevistas ou restrição original de docs quando autorizam AUDIT_REPORT.json.

## Histórico
- 2026-10-04: roadmap inicial de migração T-001.
- 2026-10-04: T-001 local integrado PR1 a29342ac3c99be5a733d6ccb6e32c8d85465dbc2; fechamento PR2 4ee1baf8b9d4e1ab54467504d3cbebb4ba838ad6, CI/evidências reais no histórico/task.
- 2026-10-04: roteiro de produto atualizado para qualidade, ADRs/estado/auditoria e produção remota ainda pendente conforme pedidos novos.

- 2026-10-05: etapa local T002 integrada pela [PR3](https://github.com/mclovin137/Atena-refatorado/pull/3) em be532c8c04d8c6baddef6c6eb6916014f92c0105, após [review CI](https://github.com/mclovin137/Atena-refatorado/actions/runs/37310715833) e [testes CI](https://github.com/mclovin137/Atena-refatorado/actions/runs/37310716099) verdes no HEAD1650028. T002/deploy permanecem parciais por bloqueio remoto; fechamento documental tem revisão/gates próprios.

- 2026-10-05: novo pedido T003 com dump24tabelas/107linhas, modelo/associações e publicação PHP. Plano conservador conserva Java/React/PG, instituição/cargos/órfãos não inventados e runtime PHP como exceção específica proposta; FDD/task precedem implementação.

- 2026-10-06: retomada autorizada com Docker acessível e Base Atena gravável; 63 notas de objetos SQL escritas e relidas sem presumir validação. Fonte privada original ausente no ambiente atual; análise histórica dos 107 registros não prova importação. Testes sintéticos/runtime continuam em execução, remoto não publicado.

- 2026-10-06: PHP runner e navegador finais SUCCESS conferidos, 146 lint verdes; limite mobile mantém aceite responsividade aberto. Parecer permite draft parcial sem merge/deploy. Java com 11 últimos resultados verdes por suíte+focal; E2E React e restore escolar final conferidos. Fonte original de 107 registros ausente e Dois CI funcionais passaram no SHA 7f0642899c5659b47821630319255d3207636486; fechamento documental tem checks próprios.

- 2026-10-06: revisão independente leu os resumos Java/E2E e visualizou requisito mobile utilizável. E2E corrigiu defeito real de keys duplicadas entre componentes irmãos; chaves distintas e execução final passaram. Sanitização de artefatos protege credenciais sem apagar falhas/status; publicação prevista somente como draft parcial, com CI e requisitos restantes explícitos.

- 2026-10-06: [PR draft #5](https://github.com/mclovin137/Atena-refatorado/pull/5) publicada com código/testes no SHA `7f0642899c5659b47821630319255d3207636486`; [revisão](https://github.com/mclovin137/Atena-refatorado/actions/runs/37462886067) e [testes](https://github.com/mclovin137/Atena-refatorado/actions/runs/37462885838) SUCCESS. CI integral conferido: 15 JUnit sem falhas/erros/skips, PHP/React/fluxos reais sintéticos e artefatos publicados. Task mantém escopo parcial por fonte original, tradução PHP, mobile PHP, reversão e cloud; nenhum ready/merge/deploy.
