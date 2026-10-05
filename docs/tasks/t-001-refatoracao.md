---
type: minerva-task
project: Atena
date: 2026-10-04
tags: [atena, task]
ai-first: true
---

# T-001 — Refatoração do Atena

## Para o futuro agente

Classificação: fluxo completo. Objetivo: entregar migração preservando fluxos do [PRD](../prds/prd-001-refatoracao.md), conforme [HLD](../hlds/hld-refatoracao.md). Dono implementação: implementador; revisão independente: planejador e operação. FDD deve ser escrito pelo implementador e revisado antes produção.

Escopo: dois serviços, frontend React, PostgreSQL/Flyway, Spring Batch, Docker, dois pipelines e evidências de integração. Exclusões: Redis/mensageria/search/Kubernetes, bibliotecas sem justificativa, novo produto, estimativa de tokens/custo/latência.

Matriz QA: fluxo completo feliz; login incorreto e acesso de perfil indevido; referências inexistentes e entrada inválida; recuperação/bimestre sem nota; matrícula duplicada; upload inválido e download autorizado; comunicação por turma; jobs vazios, repetidos e falha; relatório consistente com dados de origem. Todo endpoint exige pelo menos execução autorizada e cobertura de regra relevante por integração. Evidência de UI: screenshot em viewport móvel e desktop e fluxo de login com navegador real; screenshots complementam logs/builds, sem substituir testes.

Resumo inicial: dois contextos; origem PHP lida; riscos de schema original ausente e ambiguidades do legado; migração incremental conforme FDD. Conclusão só após registrar resultados observados; falhas não apagam evidência.

## Origem e responsabilidade

Categoria: task. Origem: prompt do experimento e legado Atena `ac48dc552b817233547104a5d0acba932d41ab91`; governança Minerva `76a991d7296cddba5e595a07e9b3b6f8da581b78`. Dono: planejador; revisor: implementação e operação. RFC e LLD dispensados: stack decidida pelo usuário e contratos concretos detalhados no FDD.

## Histórico

- 2026-10-04 — Criação para o experimento.

## Revisão independente — código e QA

Parecer em 2026-10-04: aprovado para operação, condicionado a evidências Compose/E2E e pipelines verdes antes merge. Cadeia de artefatos lida antes do código; dois contextos, domínio de cálculo puro, integrações por DTOs/HTTP, metadados Batch, SQL e sessões/perfis revisados. Testes locais observados no log real: SchoolFlowTest (1 teste) e ReportFlowTest (2 testes), zero falhas/erros, `mvn verify` BUILD SUCCESS 19:05:11; novos endpoints avatar/senha e V3 incluídos. Evidência de código não substitui execução do fluxo real entre serviços.

Limitações: importação de banco/dados reais não executada por ausência de dump; PDF nativo conserva exportação, acentos e campos institucionais, mas muda layout; snapshot inteiro em memória e consultas N+1 na origem permanecem risco de escala; troca de senha invalida a sessão atual, outras sessões permanecem até logout/expiração. Jobs testados com dados pequenos; sem benchmark ou carga. Sem achado bloqueante remanescente identificado no escopo inspecionado.

Revisão do patch cabeçalho em 2026-10-04: unidade/CNPJ/endereço/telefone, documento e matrícula restaurados no snapshot/PDF; CPF suprimido para professor e preservado titular/secretaria. V4 adiciona coluna opcional, sem novo contexto ou dependência. Código aprovado por revisão independente; teste5 executado e observado: `./scripts/testar.sh` BUILD SUCCESS em 2026-10-04 22:40:49, SchoolFlowTest (1 teste)/ReportFlowTest (2 testes) com zero falhas/erros. Asserts escola/CNPJ/CPF/matrícula e CPF vazio para professor passaram. Docker build dos dois serviços concluído; E2E/browser inicialmente falhou no launch por libasound.so.2 ausente, sem prova visual do produto nessa rodada. Operação corrige ambiente da ferramenta e reexecuta.

Achado de E2E real após teste5: login React retornou 403 porque cliente envia `X-CSRF-TOKEN` e CookieCsrfTokenRepository esperava `X-XSRF-TOKEN`. A suíte anterior injetava CSRF via helper e não provava esse contrato HTTP: verde parcial, não prova do fluxo de login. Correção mínima define o header explicitamente e o teste de login extrai cookie/token de GET /session e envia o header real, sem helper. Revisão independente aprova o diff; execução escolar focada e nova execução E2E permanecem necessárias. Scanner fallback Docker corrigido para carregar configuração explícita e eliminar falso positivo do env local sem dispensar gate de env versionado.

## Fechamento da validação local

Em 2026-10-04, teste escolar focado após correção CSRF executado com PostgreSQL real: BUILD SUCCESS às 22:49:04, um teste sem falhas/erros. A prova de login passa por cookie e header reais antes dos helpers de domínio. Suíte completa anterior teste5 permanece evidência dos dois serviços e PDFs; código relatório não mudou nesta correção.

Docker build e runtime dos dois serviços executados. E2E real `scripts/browser-smoke.mjs` passou: login React, matrícula→aula/presença→job escolar→boletim→HTTP entre serviços→job histórico→PDF/CSV/JSON, média 75/frequência 95/APROVADO, arquivos com bytes preservados e autorização por usuário/matrícula. Tracing W3C conservou o mesmo traceId em três registros: POST escolar, POST relatórios e snapshot escolar acionado pelo Batch. Evidências: `evidence/fluxo-real.json`, `evidence/browser-smoke.log`, `evidence/compose.log`, cinco PNGs de login/secretaria/boletim desktop/mobile. Revisor abriu as capturas secretaria-mobile e boletim desktop/mobile e conferiu legibilidade/responsividade; tabela mobile tem rolagem local.

Parecer independente local: aprovado, sem achado bloqueante remanescente identificado. Falhas anteriores (biblioteca de navegador ausente, login 403 por CSRF, teste misturando helper e cookies, tentativa antes runtime pronto) são preservadas no log do experimento. Aprovação local não declara CI remoto ou merge executados: esses gates ainda são condição de liberação.

## Conclusão observada

Entrega de produção concluída em 2026-10-04 22:59:45 America/Sao_Paulo (2026-10-05T01:59:45Z): [PR #1](https://github.com/mclovin137/Atena-refatorado/pull/1) integrado, merge `a29342ac3c99be5a733d6ccb6e32c8d85465dbc2`. Ambos os pipelines do head revisado `9da093395a1f93e376389e4d044d216676ead596` terminaram com sucesso: [revisão](https://github.com/mclovin137/Atena-refatorado/actions/runs/37253333394) e [testes/operação](https://github.com/mclovin137/Atena-refatorado/actions/runs/37253333431). Artifacts de integração e navegador foram publicados. Revisão independente aprovada após código, testes reais, fluxos entre serviços e capturas desktop/mobile.

Conclusão cobre a implementação e os fluxos executados; importação de dados legados, carga e paridade integral de layout permanecem limites registrados. Fechamento dos registros é uma alteração posterior somente de documentação, sem nova produção; sua revisão/CI seguirá o fluxo normal. Esta nota referencia a entrega PR #1 e não afirma merge futuro de documentação.
