---
type: minerva-adr
project: Atena
date: 2026-10-04
status: aceita
tags: [atena, adr]
ai-first: true
---

# ADR 005: Implantação Render e PostgreSQL Neon sem perda de arquivos

## Para o futuro agente
Topologia aceita para a entrega incremental autorizada: três aplicações Docker Render free públicas HTTPS — escolar serve React, relatórios exige token de serviço e PHP legado usa base/role próprias conforme ADR007. Arquivos/avatar permanecem PostgreSQL BYTEA, sem depender de disco efêmero. Cookies Secure/HttpOnly/SameSite, CSRF, perfis/objeto e rotas técnicas protegidas continuam obrigatórios; nenhum administrador de demonstração fica público. Neon foi autenticado pela API e o projeto Atena existente, em plano free, recebeu bases/roles isoladas. PHP production executou bootstrap privado idempotente com TLS; os dois Java production conectaram por JDBC verify-full, aplicaram nove migrações escolares e duas de relatórios e responderam health UP. Esses processos locais usando Neon não são serviços publicados no Render. Render MCP respondeu autenticado, mas ainda faltam confirmação operacional do workspace, proteção contra excedentes cobrados e canal Docker de criação autenticado compatível; não há deploy LIVE ou URL publicada nesta etapa. Não criar recurso pago nem alterar os oito serviços preexistentes. Cold start/relogin e cotas compartilhadas são aceitos sem SLA; backup/restore novo só é validado após prova específica. Evidência antiga de restore/rollback T002 local não é substituída nem prova a nova reversão/cloud.

## Contexto
Categoria: ADR. Pergunta: Como publicar escolar, relatórios e o PHP adicional autorizado na plataforma efêmera escolhida mantendo arquivos duráveis e acesso seguro? Documentos anteriores: [PRD-002](../prds/prd-002-qualidade-producao.md) e [HLD qualidade](../hlds/hld-qualidade-producao.md). Dono: Yoda; revisores independentes da redação: implementadores escolar/relatórios. RFC/LLD dispensados: requisito/decisão técnica e contrato FDD cobrem o escopo, sem alternativa de produto não resolvida.

Implantação e durabilidade formam uma decisão coesa: a plataforma efêmera obriga tirar arquivos do disco para preservar a funcionalidade; não é escolha independente de armazenamento especulativo. Usuário solicita produção automática via MCP em Render e Neon. Na abertura de2026-10-04, Render MCP respondia autenticado e Neon não estava autenticado. Na retomada de2026-10-06, Neon passou a ser administrado pela API autorizada pela credencial local; ferramentas MCP Neon callable continuam ausentes. O desvio de canal é registrado, sem trocar provedor ou usar outro banco. Render free não recebe rede privada e tem filesystem efêmero. Estratégia aceita para preparação/publicação condicionada atende restrições sem broker/novo armazenamento, mas criação remota depende de ferramentas/conta/quota verificadas.

## Escopo
- Afetado: ADR002 trecho de deploy local é estendido, não apaga evidência T-001; PRD-002/HLD qualidade/FDD-002, V5 e objetos afetados no Obsidian, Compose/pipelines/README/lib e configuração produção.
- Fora de escopo: serviços além dos dois Java e do PHP autorizado especificamente por ADR007, novas regras escolares, infraestrutura não autorizada e cobrança.

## Forças e critérios
- Preservar comportamento, contratos e segurança validados em T-001; distinguir decisão, implementação e execução.
- Atender requisito explícito sem abstração especulativa; dependências mínimas e custo financeiro zero.
- Autoria arquitetural Yoda; revisão independente necessária e nunca aprovação da própria implementação.

## Opções consideradas

### Opção 1: Serviços free HTTPS e arquivos PostgreSQL
- Status na análise: escolhida para preparação e publicação condicionada
- Benefícios: Atende serviços e persistência sem nova infraestrutura.
- Custos e limitações: Rotas técnicas públicas exigem token rigoroso; quota compartilhada/cold start.
- Riscos: Capacidade gratuita pode não bastar; bytea aumenta banco.
- Trade-off: Preserva domínio e dados com custo de latência/capacidade restrita.
- Evidências: Render free/private-network oficiais: free web não recebe rede privada, filesystem efêmero, suspensão/franquias; na abertura histórica de2026-10-04, descoberta Render MCP autenticado e Neon configurado sem autenticação; autenticação API Neon foi comprovada em2026-10-06.

### Opção 2: Private service/disco pago Render
- Status na análise: descartada
- Benefícios: Rede privada e disco persistente.
- Custos e limitações: Custo não autorizado e sem plano free correspondente.
- Riscos: Contratação violaria regra de custo zero.
- Trade-off: Operação mais simples com custo proibido.
- Evidências: Render free oficial: persistent disk e private service gratuitos não disponíveis para esse uso; prompt/governança proíbem contratar.

### Opção 3: Manter somente Docker local
- Status na análise: candidata
- Benefícios: Já validado e preserva dados locais.
- Custos e limitações: Não atende publicação remota solicitada.
- Riscos: Neon MCP pode bloquear a etapa nova.
- Trade-off: Serve contingência segura enquanto autenticação/capacidade faltarem.
- Evidências: compose.yaml, Dockerfile e evidence/fluxo-real.json T-001 mostram caminho local já executado; publicação remota ainda sem ferramentas autenticadas compatíveis.

## Decisão
Decisão aceita para preparar três serviços conforme ADR007 e publicar somente após os gates operacionais factuais. Provedores Render/Neon são escolha explícita do usuário; a autorização atual de todos os passos permite canais API/Blueprint disponíveis sem substituir tecnologia. Revisão da decisão não significa provisionamento Render ou deploy observado.

Topologia aceita para a entrega incremental autorizada: três aplicações Docker Render free públicas HTTPS — escolar serve React, relatórios exige token de serviço e PHP legado usa base/role próprias conforme ADR007. Arquivos/avatar permanecem PostgreSQL BYTEA, sem depender de disco efêmero. Cookies Secure/HttpOnly/SameSite, CSRF, perfis/objeto e rotas técnicas protegidas continuam obrigatórios; nenhum administrador de demonstração fica público. Neon foi autenticado pela API e o projeto Atena existente, em plano free, recebeu bases/roles isoladas. PHP production executou bootstrap privado idempotente com TLS; os dois Java production conectaram por JDBC verify-full, aplicaram nove migrações escolares e duas de relatórios e responderam health UP. Esses processos locais usando Neon não são serviços publicados no Render. Render MCP respondeu autenticado, mas ainda faltam confirmação operacional do workspace, proteção contra excedentes cobrados e canal Docker de criação autenticado compatível; não há deploy LIVE ou URL publicada nesta etapa. Não criar recurso pago nem alterar os oito serviços preexistentes. Cold start/relogin e cotas compartilhadas são aceitos sem SLA; backup/restore novo só é validado após prova específica. Evidência antiga de restore/rollback T002 local não é substituída nem prova a nova reversão/cloud.

## Consequências

### Positivas
- Permite publicação requerida sem novos tipos de infraestrutura.
- Arquivos tornam-se persistentes fora da instância.

### Negativas
- Free tier tem suspensão e franquias compartilhadas; sem promessa de SLA.
- BYTEA exige backup/capacidade e reverter app demanda compatibilidade.

### Riscos residuais
- Autenticação Neon/API, TLS e bootstrap são comprovados nesta retomada; ausência de ferramenta MCP Neon é limite de canal, não ausência de acesso ao banco. Render Docker/canal de criação, billing e confirmação do workspace ainda bloqueiam publicação; restore dos três bancos com contagens é comprovado nesta retomada; recuperação de blobs/snapshot AES-GCM e rollback de versão anterior permanecem sem prova específica.

## Impacto nos artefatos
- ADR002 trecho de deploy local é estendido, não apaga evidência T-001; PRD-002/HLD qualidade/FDD-002, V5 e objetos afetados no Obsidian, Compose/pipelines/README/lib e configuração produção.
- RFC e LLD dispensados: não há disputa de produto nem implementação alternativa não resolvida; HLD/ADR/FDD cobrem estrutura e contratos. Isto não dispensa lacunas remotas.

## Relações
- Origem: [PRD-002](../prds/prd-002-qualidade-producao.md), pedido do experimento e auditoria T-002; [HLD qualidade](../hlds/hld-qualidade-producao.md).
- ADRs relacionadas: [fronteiras](adr-001-fronteiras.md), [stack](adr-002-stack.md), [interface](adr-003-interface.md), [organização](adr-004-organizacao-backend.md).
- Substitui: nenhuma ADR; detalhes técnicos anteriores afetados são indicados acima.
- Substituída por: nenhuma.

## Critério para revisitar
Revisitar caso quota free/capacidade/MCP impeça os três serviços Docker autorizados ou durabilidade PostgreSQL; registrar bloqueio real, sem substituir provedor ou contratar pagamento silenciosamente.

## Aceite
- Responsável arquitetural: Yoda (agente planejador/revisor).
- Decisor: usuário nas escolhas explícitas e na autorização atual de todos os passos; autonomia delegada no escopo remanescente. Arquitetura aceita condicionalmente pelo planejador, com revisão independente da operação antes integrar a atualização documental.
- Evidência do aceite: pedidos T002/T003 escolhem Render/Neon e PHP adicional; autorização atual cobre preparação e publicação. Plano Neon de roles comuns sem privilégios administrativos, bases próprias, TLS e bootstrap foi revisado independentemente antes executar; fatos observados constam em evidence/neon-java-prod.json e evidence/neon-php-bootstrap.json. Publicação Render permanece não executada. Atualização desta ADR depende da revisão documental distinta da autoria.
- Revisão independente da redação: implementadores escolar/relatórios em 2026-10-04, com ajustes de origem/evidências/links aplicados. Isto não aprova sua implementação futura nem declara deploy executado.

## Histórico
- 2026-10-04: ADR criada para o pedido T-002.
- Fontes operacionais: [Render free](https://render.com/docs/free), [rede privada](https://render.com/docs/private-network); descoberta MCP observada por operação, sem criação Neon. A descrição callable de create_web_service aceita somente subset de runtimes sem Docker/container registry; o Docker requerido não está disponível nesse contrato. Automação via API/CLI/Blueprint requer credencial real e registraria desvio do canal MCP pedido; native Java não é substituto autorizado para contornar a stack.

- 2026-10-06: adendo decisório substitui o estado corrente sem apagar os fatos históricos da abertura. Plano Neon free autenticado, três roles comuns sem neonsuperuser/privilégios globais e negativas de CONNECT cruzado foram conferidos pela operação; bootstrap privado PHP e Java production usando Neon passaram. Revisor leu os JSON de contagens/health/migrações/TLS sem PII. Backups/dados privados ficam em .atena-cloud-private fora do glob de artefatos. Source original/tradução histórica continuam parciais. Render requer canal Docker, workspace e prova de custo zero antes criação; plan free e spend limit de build isolados não garantem ausência de cobrança de bandwidth quando há cartão, conforme documentação oficial atual.

- 2026-10-06: restauração nova dos três dumps Neon executada em PostgreSQL18 local descartável sem rede e comparada com a origem por contagem de todas as tabelas/colunas/linhas: escolar35tabelas/192colunas, nove migrações e uma pessoa; relatórios9tabelas/68colunas, duas migrações; legado26tabelas/202colunas e uma secretaria. evidence/neon-restore.json PASS foi lido independentemente. As bases de materiais/importação estão vazias: essa execução não prova recuperação de blobs/snapshot AES-GCM nem rollback de versão anterior da aplicação. Dumps sem owner/ACL preservam dados/schema, não restauram automaticamente roles/grants; roles restritas e seis negativas CONNECT cruzadas constam em evidence/neon-isolamento.json, sem privilégios administrativos ou globais.
