## Task ativa
T-001 — Refatoração incremental do Atena; fluxo completo.

## Estado atual
Planejamento e FDD aprovados por revisão independente. Código dos dois serviços e frontend implementado e congelado para revisão. Integração PostgreSQL real executada: três testes abrangentes sem falhas, build Maven verify e frontend concluídos. Infra Docker/Compose e CI escritas e revisadas; PostgreSQL iniciado e isolamento de acesso entre bancos verificado pela operação. Revisão independente código/QA aprovada; Compose/E2E com navegador e pipelines ainda pendentes.

## Decisões vigentes
Objetivo: preservar o sistema escolar na stack exigida. Decisão: dois contextos escolar e relatórios; processamento inicialização de notas e histórico em Spring Batch; PDF nativo PDFBox preserva exportação existente. Domínio Resultado puro Java; JPA na infraestrutura. Evidências: origem PHP raw, planejamento com links existentes, Obsidian planejamento conferido, FDD revisado; operação executou bash/Compose, PostgreSQL e gitleaks. Donos: implementação e operação; revisão independente: planejador.

## Riscos e lacunas
Legado integral clonado e preservado em `legacy/` no commit ac48dc552b817233547104a5d0acba932d41ab91; dump de schema/dados original ausente. Hora-aula de 60min e aprovação com frequência>=75% alteram bugs legados explicitamente. Controles prazo/liberação/objeto, upload, snapshot fixo e campos pessoais/avatar foram corrigidos e cobertos pela suíte observada. Snapshot memória/N+1 e layout PDF permanecem limites; importação real não validada. Aprovações humanas conflitantes são substituídas pela autonomia explícita do experimento. CI remoto e deploy completo ainda sem evidência.

## Próximo passo
Operação termina build Compose e E2E real com screenshots/tracing; revisor confere evidências e atualiza task/Obsidian. Publicação/PR e merge dependem dos gates reais, sem presumir CI remoto executado.

## Região gerada
<!-- minerva-continuity:generated:start -->
Sem automação de estado habilitada.
<!-- minerva-continuity:generated:end -->
