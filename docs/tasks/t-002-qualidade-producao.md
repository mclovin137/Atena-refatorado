---
type: minerva-task
project: Atena
date: 2026-10-04
status: em-execucao
tags: [atena, task]
ai-first: true
---

# T-002: Qualidade por domínio e publicação verificável

## Para o futuro agente
Entregar fonte legível/DDD/SOLID/factories concretas, ADRs/roadmap/estado conformes e produção Render/Neon autorizada, preservando fluxos e contratos T-001. Implementação local e validação integradas concluídas; CI/PR/merge pendentes; publicação remota bloqueada por autenticação/capacidade das ferramentas, não presumida concluída.

## Origem
- Caminho: fluxo completo; muda estrutura, persistência de arquivos e superfície operacional pública.
- PRD: [PRD-002](../prds/prd-002-qualidade-producao.md), todos critérios de aceite.
- FDD aprovado: [FDD-002](../fdds/fdd-002-qualidade-producao.md), autoria implementador e revisão independente planejador em 2026-10-04 antes produção.
- HLD: [qualidade/produção](../hlds/hld-qualidade-producao.md).
- ADRs aplicáveis: [001](../adrs/adr-001-fronteiras.md), [002](../adrs/adr-002-stack.md), [003](../adrs/adr-003-interface.md), [004](../adrs/adr-004-organizacao-backend.md), [005 proposta](../adrs/adr-005-producao-render-neon.md). Provedores escolhidos pelo usuário; execução/detalhes remotos ainda condicionados.

## Resumo decisório mínimo
- Objetivo: manutenção legível e publicação factual, sem alterar funções escolares.
- Decisão: organização por recurso/função, domínio puro, DTOs, políticas/factories reais; bytes duráveis implementados e validados em PostgreSQL local, nenhuma infra nova.
- Evidências: fonte atual/refêrencia MinervaFinancas pinada, role/skills Minerva locais semeadas, revisão cruzada dos artefatos e FDD.
- Riscos e lacunas: refatoração amplia diff; migração/backup/restore/rollback isolados executados, sem prova de capacidade cloud; Neon não autenticado e MCP Render sem Docker, quota não comprovada.
- Próximo passo: revisão final factual da auditoria/estado e commit/PR; duas pipelines remotas antes de merge por operação independente.

## Responsáveis
- Planejamento: Yoda/planejador.
- Implementação: agente escolar implementou escolar, configuração/infra/gates/auditoria; agente relatórios implementou relatórios/React e testes escolares delegados. Operação executou validação e discovery, sem autoria do código de produção T002.
- Auditoria: planejador aprovou FDD inicial antes de produção. Após limite de threads impedir sua reativação, operação assumiu revisão independente de estrutura/QA/segurança sem autoria de produção; implementadores revisaram docs do planejador. Infra/config do implementador escolar foram revisadas pela operação.

## Resultado esperado
Dois serviços e React com código legível e contratos preservados, artefatos de decisão corretos, evidências de paridade e publicação remota real quando ferramentas permitirem. Ausência de produção deve aparecer no audit/report e no aceite não cumprido, não virar sucesso por aprovação local.

## Escopo

### Incluído
- Backend por recurso/{rest,actor,service,builder,dao,helper,dto,dominio}; fonte multilinear, nomes completos, DTOs explícitos, domínio puro, policies coesas, factories para variantes/invariantes reais.
- Extração de Batch/exports/snapshot, React por responsabilidade e ADRs/roadmap/estado/base conformes.
- V5 bytes material/avatar aditiva, compatibilidade/retropreenchimento e validação de integridade/reinício; segurança/configuração/provisionamento Render/Neon via ferramentas autorizadas.
- AUDIT_REPORT.json válido completo e atualizado por marco, revisado contra evidências; EXPERIMENT_LOG factual.

### Excluído
- Novas funções, fórmulas, rota/status/JSON/paginação/i18n/OpenAPI copiados de outro produto, novas fronteiras/infra, compras ou alteração de terceiros.
- Factory genérica CRUD, interface de única implementação sem fronteira real, builder/DTO duplicado sem responsabilidade, wrappers triviais e testes espelho de renomeação.
- Migração de dump ausente, carga/benchmark ou promessa de SLA/métricas estimadas.

## Dependências e bloqueios
- FDD aprovado e cadeia vigente liberam implementação LOCAL; não há bloqueante técnico do contrato local.
- Neon MCP configurado mas não autenticado/sem callabletools; dono operação; bloqueia provisionamento remoto.
- Render create_web_service não suporta Docker na descrição callable; outra via API/CLI/Blueprint exigiria credencial/capacidade real e registro de desvio do MCP solicitado. Custo gratuito/quota ainda precisa prova; dono operação.
- ADR005 proposta será revisada antes execução de configuração/provisionamento remoto; nenhuma solução paga ou fallback de banco autorizado.

## Passos de implementação
1. Caracterizar campos/defaults/rotas/status e separar DTO, domínio e DAO por recurso; manter assertions de comportamento existentes.
2. Extrair Actor/Service puro, policies e factories justificadas; reorganizar Batch/exportação/snapshot e frontend com legibilidade real.
3. Implementar bytes aditivos e compatibilidade com dados locais, testar ida/integridade/restart e plano reversível sem DOWN destrutivo automático.
4. Executar builds/testes PG/E2E e review independente por domínio; corrigir regressões, atualizar audit/experimento/dependências/base.
5. Operação provisiona/deploya apenas com ferramentas autenticadas/plano gratuito confirmado e gates verdes; comprova HTTPS/health/login/fluxo/reinício ou registra bloqueio.

## Critérios de aceite
- [x] RF001: código legível/por recurso, controllers sem regra/SQL, domínio/Service sem Spring/JPA/HTTP, DTOs sem entidade JPA/reflexão/typecode; mapping JPA restrito ao DAO.
- [x] RF001: abstrações/factories têm responsabilidade objetiva; ausência de camadas vazias/wrappers mecânicos e política global gigante.
- [x] RF002: contratos/defaults/cálculo/autorização/jobs preservados com testes existentes reais após migração; unidade vazia{} e arraysnull não mudam.
- [x] RF003: arquivos/avatar persistem após reinício, hashes/bytes e autorização iguais; reversão/restore só declarados quando executados.
- [x] PRD: ADR001–005 aderentes role/molde/aceite/alternativas e revisão independente, roadmap/estado/base atualizados.
- [ ] RF003: Render/Neon publicado e verificado via MCP/canal autorizado ou requisito explicitamente não atendido e status parcial por bloqueio real.
- [x] PRD: AUDIT_REPORT.json completo, parsing/gate significativo e revisão independente, IDs externos não inventados e métricas não estimadas.

## Testes obrigatórios
- [x] Suíte PG real cobrindo integração idempotente de todo endpoint e fluxos T-001; apenas adaptar pacotes/nomes, não relaxar assertions.
- [x] Login cookie/header X-CSRF-TOKEN real e ausência rejeitada; negativa papel/objeto, prazo/liberação, documentos mascarados e arquivos/avatar.
- [x] Matrícula→presença→notas→boletim média75/frequência95/APROVADO; dois Batch COMPLETED, repetição/falha/restart e snapshot fixo.
- [x] PDF/CSV/JSON e metadata, bytes material/avatar após restart e backfill; migração ida/volta/export/restore só onde novo comportamento exige, sem confundir plano com prova.
- [x] Fluxo navegador React desktop/mobile, tracing HTTP/Batch e novas factories/invariantes quando relevantes. RED/GREEN/REFACTOR para regra/endpoint novo, não cada renomeação.

## Evidências obrigatórias
- [ ] Builds/testes/scanner/quality review observados e artifacts CI ligados ao PR; relatórios JUnit não vazios após renomear testes.
- [ ] Imagem e/ou vídeo dos testes de endpoint publicado pelo pipeline; screenshots reais desktop/mobile e JSON de fluxo.
- [ ] Publicação LIVE SHA, HTTPS/health/login/E2E/restart/bytes no remoto quando executada; se impossível, evidência específica do bloqueio.

## Documentação e Obsidian
- Arquivos .md: cadeia T002, ADR001–005, rules/continuidade/lib/pendências/histórico, definições canônicas pertinentes e experimento; AUDIT_REPORT.json solicitado.
- Notas: Bases/Atena/roadmap.md, epicos/epico-002-qualidade-producao.md, prds/prd-002-qualidade-producao.md, hlds/hld-qualidade-producao.md, fdds/fdd-002-qualidade-producao.md, tasks/t-002-qualidade-producao.md, adrs, dependências/skills/agentes/roles e objetos SQL afetados depois migração real.

## Riscos e lacunas
- Ampliação de diff e security mapping: mitigar por etapas e paridade executada; dono implementadores/revisor.
- Arquivos em banco/quota/restore: testar limites e persistência, não garantir rollback sem restauração; dono implementação/operação.
- ❓ LACUNA: Neon autenticação e capacidade Docker/custo gratuito no canal autorizado, dono operação; bloqueia remota, não implementação local.

## Definição de pronto
- [x] Implementação respeita contratos e todos critérios ou registra não atendidos sem status sucesso fictício.
- [ ] Testes/evidências obrigatórias produzidos e observados.
- [x] Documentação/base e auditoria factual atualizadas.
- [ ] PR auditado por agente diferente do implementador de cada superfície.
- [ ] Pipelines review/testes verdes antes merge e deploy.

## Histórico
- 2026-10-04: task criada pronta após FDD aprovado independentemente; etapa remota bloqueada, execução local liberada. Revisões de redação: implementador escolar e relatório, ajustes de evidências/origem/mapping aplicados; aprovação local não declara código novo validado.

Categoria: task. Pergunta: quais passos, donos e provas tornam o contrato aprovado executável? Anteriores PRD/HLD/ADR/FDD002; posterior PR/review/CI e deploy quando desbloqueado. Dono planejador; revisores implementação/operação; auditor da produção permanece distinto de cada implementador. Sem QA/análise/RFC/LLD adicional: critérios e evidências pertencem à própria task.

- 2026-10-05: implementação local e validações isoladas/integradas concluídas: relatórios4 e escolar1 testes JUnit verdes; React build/check/cliente isolado verdes; E2E real, tracing, reinício BYTEA, backup/restore/rollback UUID isolados passaram. Revisão independente de operação substitui planejador indisponível por limite de threads. Artefatos locais ignorados preservam falhas/retrabalhos; publicação CI e deploy ainda não executados.

- 2026-10-05: E2E adicional BOLETIM via HTTP/snapshot persistido comprovou unidade{} e arraysnull, mantendo export JSON nome/tipo/linhas; cinco tipos reais e três formatos passaram. Etapa local revisada por operação independente; PR/CI/publicação ainda pendentes.

- 2026-10-05: PR3 aberta https://github.com/mclovin137/Atena-refatorado/pull/3 com produção4b16077; workflows/revisão final/merge pendentes no HEAD documental atualizado. T002 parcial e deploy bloqueado mantidos, sem gate humano.
