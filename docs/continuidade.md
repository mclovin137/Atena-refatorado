## Task ativa

T-003 em andamento: modelo acadêmico/importação privada e PHP legado real isolado. T-002 permanece parcial pela publicação cloud não executada.

## Estado atual

Branch feature/modelo-importacao-legado, base main 74886e38a6a3195a8984b821bee421d097fcc970. [PR draft #5](https://github.com/mclovin137/Atena-refatorado/pull/5) publicada; código/testes no SHA 7f0642899c5659b47821630319255d3207636486. Cadeia PRD/HLD/ADR006–007/FDD003 aprovada antes implementação, autoria distinta dos revisores.

Java/Spring/React: modelo e V6–V9 escritos; últimos resultados dos 11 casos escolares locais JDK21 estão verdes, combinando suíte anterior e reexecução focal de Escolar1+Modelo2. Incluem parser/AES-GCM, acadêmico/constraints/concorrência de associações, importação sintética/replay/restart/negativas. Essa limitação era local: o CI funcional no SHA 7f0642899c5659b47821630319255d3207636486 executou integralmente 15 JUnit (11 escolares e quatro de relatórios), sem falhas, erros ou skips, conferidos nos XML remotos. E2E React final passou contra Java/PostgreSQL reais: cadastro Faculdade→Curso→Grade→Requisito, associação de matéria e análise/aplicação sintética em desktop 1440 e mobile 390, sem overflow global ou erro React. O revisor leu os artefatos e visualizou a tela mobile. Restauração escolar isolada com nove migrações e snapshot AES-GCM íntegro foi registrada no resumo do autor e conferida; chave mantida fora do banco.

PHP final local passou: artefatos legado-fluxo.json e legado-navegador.json SUCCESS lidos, 146 arquivos PHP sem erro de sintaxe. Login três perfis/teclado, negativas globais de 46 rotas, 13 AJAX e 5 PDFs, seis famílias XSS, catálogo/matrícula, notas/presença, seis PDFs positivos, uploads/avatar privado/restart/pg_dump/restore/autorização/logout/SQL foram exercitados. Templates/atributos/textarea/PDF e AJAX identificados em lote foram corrigidos e conferidos. Desktop/mobile foram observados, com limite visual explícito abaixo.

## Decisões vigentes

Dois serviços Java, React, Spring Batch, PostgreSQL e Docker preservados. PHP é exceção adicional autorizada, base/role isoladas e sem sincronização automática. Faculdade desconhecida não é inventada; Curso legado só com nome continua aceito. Matrícula deriva Curso pela Turma, requisitos são administrativos sem motor de formação.

Origem por digest/tabela/ordinal, snapshot privado AES-GCM confirmado antes chunks e credenciais não ativadas. Cargos/estado/órfãos/grades ambíguas permanecem pendentes; empréstimo com pessoa resolvida e estado desconhecido continua ESTADO_NAO_MAPEADO. Migrações V1–V5 não alteradas; DDL/backfill/validação separados.

## Riscos e lacunas

Fonte original das 24 tabelas e 107 registros não foi encontrada na retomada; caminho/anexo solicitado. Análise privada histórica é evidência de parser, não importação do arquivo real. PHP 2024 e dump 2020 têm contratos diferentes; ponte sintética de catálogos não comprova tradução completa.

Mobile PHP preservado tem tabela/conteúdo cortados; autor mediu canvas de 1070 px em viewport de 390 px. Responsividade plena não foi aprovada. Faltam importação da fonte original, tradução completa para PHP, responsividade PHP plena, reversão nova completa e revisão/integração dos requisitos restantes. Autenticação/capacidade cloud continuam abertas. PR draft #5 aberta, sem ready, merge ou deploy.

Docker e gh estão acessíveis no ambiente atual; restrições antigas eram históricas. Base Atena exclusiva gravável,63 notas SQL, dependências/regras e cadeia/roadmap/task relidos; ledger sem obrigações abertas neste marco, prazos anteriores preservados no histórico. Neon não autenticado nem callable; capacidade/quota/TLS/canal Docker Render não comprovados e nenhuma produção publicada.

## Próximo passo

PR draft #5 publicada e os dois CI funcionais passaram no SHA 7f0642899c5659b47821630319255d3207636486: [revisão](https://github.com/mclovin137/Atena-refatorado/actions/runs/37462886067) e [testes](https://github.com/mclovin137/Atena-refatorado/actions/runs/37462885838). Os jobs integration/legado passaram e publicaram artefatos saneados; E2E React remoto também PASSOU. Este marco documental será revisado e publicado separadamente, com seus checks acompanhados antes de qualquer integração. Limites de dados, mobile, reversão e cloud mantêm a task em andamento, sem ready, merge ou deploy. Dados reais só após gates específicos e fonte autorizada disponível.

## Região gerada

<!-- minerva-continuity:generated:start -->
Sem automação de estado habilitada.
<!-- minerva-continuity:generated:end -->
