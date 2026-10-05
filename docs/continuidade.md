## Task ativa
T-001 — Refatoração do Atena; fluxo completo. Validação local concluída, liberação remota pendente.

## Estado atual
Código de dois serviços Java/Spring Boot e frontend React implementado. PostgreSQL/Flyway e Spring Batch executados; suíte completa teste5: três testes abrangentes sem falhas/erros. Após falha de login real com HTTP 403, contrato CSRF corrigido e teste escolar focado verde às 22:49:04. Docker build/runtime e E2E navegador real passaram; screenshots desktop/mobile conferidos por revisor independente. PR draft aberto; pipelines remotos e merge ainda aguardam confirmação.

## Decisões vigentes
Objetivo: preservar escola no domínio e stack exigidos. Dois contextos: escolar e relatórios; jobs inicializarNotas/consolidarHistorico. Snapshot fixo e fonte REPEATABLE_READ; PDF nativo preserva escola/CNPJ/CPF/matrícula, documento omitido professor. Spring Cache memória, Actuator/Micrometer e tracing W3C sem coletor adicional. Evidências observadas: testes PG, Docker, scanner local/Docker, E2E com jobs COMPLETED/exportações/arquivos/autorização, três registros traceId idêntico, capturas reais. Parecer revisor: aprovado localmente. Donos: implementador e operação; revisor independente: planejador.

## Riscos e lacunas
Legado integral preservado em legacy/ no commit ac48dc552b817233547104a5d0acba932d41ab91; dump original ausente, importação real não validada. Hora-aula de 60 min e frequência na aprovação corrigem bugs e alteram resultado legado. Layout PDF simplificado. Snapshot memória/N+1 na origem limitam escala; sem benchmark/carga. Troca senha invalida só sessão atual. Aprovações humanas conflitantes seguem autonomia expressa do prompt. Falhas e retrabalhos reais ficam no EXPERIMENT_LOG; não confundir suítes anteriores com prova de login React.

## Próximo passo
Operação acompanha pipelines remotos e anexa evidências no PR; revisor confirma resultados/escopo, conclui task/Obsidian e operação executa merge somente com gates verdes. Orquestrador entrega JSON factual sem estimar métricas.

## Região gerada
<!-- minerva-continuity:generated:start -->
Sem automação de estado habilitada.
<!-- minerva-continuity:generated:end -->
