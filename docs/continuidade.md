## Task ativa

T-003 em andamento: preparação manual de dados DEMO autorizados somente após ambos os sistemas online; modelo acadêmico/importação privada e PHP legado real já integrados incrementalmente. Entrega incremental funcional autorizada; RF002/RF003 históricos continuam parciais. T-002 permanece parcial pela publicação Render não executada.

## Estado atual

Branch feature/dados-demonstracao, criada de experiment/main ef4c8737178b97ebcaccdadf8b29cb1ec413a9e1. PR5 integrada por revisor distinto; CI a093 revisão37520983193/testes37520983328 SUCCESS com artifacts conferidos17JUnit/PHP10fluxos/3perfis×5faixas/React4fluxos. Dois CI pós-merge37553610660/37553610546 também SUCCESS. Falha2a80 permanece histórica. Carga DEMO manual implementada e QA isolado SUCCESS:36recursosJava/37PHP, seis logins por aplicação, rerun sem alteração de dados/hashes, colisãoPHP zeroescritaJava, Aluno com notas e Professor409 por prazo fechado preservado. Oito testes focais de preflight/retomada/gate verdes. Nenhuma escrita DEMO no Neon e nenhum serviço Render publicado; reviewfinal favorável e novo CI ainda pendente.

Java/Spring/React: modelo acadêmico e V6–V9 escritos. CI anterior executou15JUnit (11 escolares/quatro relatórios), sem falhas/erros/skips; React acadêmico local/remoto cobriu cadeia Faculdade→Curso→Grade→Requisito, associação de matéria e importação sintética desktop1440/mobile390 sem overflow global ou pageerror. Nova proteção login20POST/min/endereço foi implementada com capacidade/expurgo limitados e teste de janela; integração CSRF válida+nova sessão comprovou429/Retry-After após20tentativas e Maven package final passou. A falha remota veio de contaminação do repositório CSRF pelo teste novo; teste do limite agora possui contexto separado e CSRF real. Suíte Java local final:17JUnit/0falhas/0erros/0skips. Correção validada também nos CI a093 e pós-merge ef4.

PHP: prova final local legado-fluxo.json SUCCESS com10fluxos e legado-navegador.json SUCCESS com três perfis em320/390/768/1024/1440. Artefatos reportaram ausência de overflow do elemento raiz, menu teclado/Escape/foco e tabelas completas com scroll local; login, catálogo/matrícula, notas/presença/PDF, XSS/CSRF/objeto, uploads/avatarBYTEA/restart/restore e limite login429 foram exercitados. Revisão visual independente encontrou login320/body500 não coberto pela assertion anterior; CSS corrigida e prova focal nova apenas de login nas cinco larguras passou, body/html e PNGIHDR exatos320/390/768/1024/1440. Revisor visualizou PNG320 final e fechou o achado. A suíte10fluxos/3perfis é anterior a essa última correçãoCSS; não foi repetida integralmente. PHP148arquivos lint sem erro. CI2a80 também falhou por contador compartilhado no teste do limite; focal corrigido usa instância UUID isolada, passou20negativas200/21novaSessão429 e preservou contador anterior8, sem mudar produção.

Neon API autenticada: projeto Atena blue-bonus-15349315, branch production, organização free e PostgreSQL18.6. RolesSQL comuns escolar/relatorios/legado e bases próprias criadas após parecer independente; nenhuma pertença neonsuperuser/readall/writeall ou flagadmin, seis negativas de privilégio CONNECT cruzado. Neondb existente preservada. Java prod local usando Neon confirmou JDBCverify-full, healthUP e Flyway escolar9/relatorios2; admins privados escolar1/PHP1, bootstrapPHP2x idempotente/hashverificado. Restore dos três dumps em PostgreSQL18.6 localnetworknone passou comparação de todasROWcounts e schema35/192,9/68,26/202. Artefatos JSON seguros evidence/neon-*.json; dumps/logs .atena-cloud-private700, dotenv600 ignorados pelo Git e fora globCI.

## Decisões vigentes

Dois serviços Java, React, Spring Batch, PostgreSQL e Docker preservados. PHP é exceção ADR007 com base/role isoladas, sem sincronização automática. Faculdade desconhecida não inventada; Curso legado só com nome aceito; matrícula deriva Curso pela Turma, requisitos administrativos sem motor de formação.

Origem digest/tabela/ordinal, snapshot privado AES-GCM antes chunks e credenciais históricas não ativadas. Cargos/estado/órfãos/grades ambíguas permanecem pendentes; empréstimo resolvido com estado desconhecido mantém ESTADO_NAO_MAPEADO. MigraçõesV1–V5 preservadas; DDL/backfill/validação separados. ADR005 aceita para preparação/publicação condicionada de três serviços; marco task autoriza integração incremental após revisão independente e dois CI verdes, sem declarar T003 integralmente aceita. Object Calisthenics parcial: ADR004 dispensa especificamente regras3/4/5/8 do adaptador LimitarLoginFiltro/Janela; parser, ator de relatório, factory e tipos/projeções de domínio mantêm desvios explícitos, sem conformidade literal às nove regras.

## Riscos e lacunas

Fonte original24tabelas/107registros ausente; análise privada histórica não é importação real. PHP2024/dump2020 diferem, ponte sintética não comprova tradução completa. Dados históricos RF002/RF003 continuam não atendidos; aplicações publicáveis usam base vazia e bootstrap privado. Achado loginPHP320 foi fechado por correçãoCSS/prova focal nova com body/documento/PNGIHDR, separada da suíte funcional anterior.

Render MCP autenticado, mas workspace operacional ainda não confirmado pelo usuário; RENDER_API_KEY ausente e MCP createDocker é limitado/incompatível com contexto/Dockerfile. OpenAPI permite Docker completo; custo zero depende de confirmar ausência de cartão/controle equivalente e franquia, pois planfree/spendlimitbuild0 não impede cobrança de banda havendo cartão. Nenhum serviço Render criado, nenhuma URL/LIVE/publicação comprovados. URLs recíprocas e teste HTTPS/login/PDF/upload pós-publicação aguardam serviços reais.

Restore novo valida schema/dadosbootstrap/metadados, sem owner/ACL automático. Produção vazia não contém material/avatar/fonteAES: bytes/snapshotAES e rollback de versão anterior/cloud não são provados por esse restore. Provas locais antigas permanecem limitadas ao seu ambiente. Limite login local perde contadores ao reiniciar e proxy pode agrupar clientes; ataque distribuído não eliminado. Base Atena exclusiva gravável e dependências/ADRs/task sincronizados e relidos; sem obrigação documental vencida.

## Próximo passo

Revisão independente final dos scripts/runner/workflow DEMO e snapshot documental favorável; publicar novo PR após gates proporcionais, aguardando dois CI do novo HEAD. Task/FDD DEMO e Base revisados antes código; evidence/demo-qa.json registra prova isolada, não produção. Preservar credenciais/dados existentes, manifesto privado600 no diretório .atena-cloud-private/demo e colisão failclosed. Carga real somente depois de URLs HTTPS/health escolar, relatórios e PHP comprovados e Render API/workspace/billing confirmados. Nenhum insert Neon antecipado. Benchmarks/PDF do usuário ficam fora do stage. Não registrar SHA próprio em followup documental e reiniciar CI.

## Região gerada

<!-- minerva-continuity:generated:start -->
Sem automação de estado habilitada.
<!-- minerva-continuity:generated:end -->
