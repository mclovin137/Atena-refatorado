## Task ativa
T-001 — Refatoração incremental do Atena; fluxo completo.

## Estado atual
Planejamento e FDD aprovados por revisão independente. Backend escolar em implementação: entidades JPA, migração, sessão/perfis, CRUD e resultados/jobs; relatórios e frontend em andamento. Infra Docker/Compose e CI escritas e revisadas; PostgreSQL iniciado e isolamento de acesso entre bancos verificado pela operação. A aplicação completa ainda não foi validada.

## Decisões vigentes
Objetivo: preservar o sistema escolar na stack exigida. Decisão: dois contextos escolar e relatórios; processamento inicialização de notas e histórico em Spring Batch; PDF nativo PDFBox preserva exportação existente. Domínio Resultado puro Java; JPA na infraestrutura. Evidências: origem PHP raw, planejamento com links existentes, Obsidian planejamento conferido, FDD revisado; operação executou bash/Compose, PostgreSQL e gitleaks. Donos: implementação e operação; revisão independente: planejador.

## Riscos e lacunas
Fetch legado ainda incompleto; schema/data original ausente. Hora-aula de 60min e aprovação com frequência>=75% alteram bugs legados explicitamente. Revisão inicial detectou controle de prazo/liberação de notas ausente; implementador corrigirá antes validação. Aprovações humanas conflitantes são substituídas pela autonomia explícita do experimento. CI remoto e deploy completo ainda sem evidência.

## Próximo passo
Implementador termina contratos e correções, frontend e integração idempotente por endpoint; operação executa builds/Compose e screenshot de UI; planejador revisa diff e evidências, sem declarar paridade além do observado.

## Região gerada
<!-- minerva-continuity:generated:start -->
Sem automação de estado habilitada.
<!-- minerva-continuity:generated:end -->
