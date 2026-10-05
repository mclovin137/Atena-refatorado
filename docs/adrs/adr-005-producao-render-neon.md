---
type: minerva-adr
project: Atena
date: 2026-10-04
status: proposta
tags: [atena, adr]
ai-first: true
---

# ADR 005: Implantação Render e PostgreSQL Neon sem perda de arquivos

## Para o futuro agente
Hospedagem Render e banco PostgreSQL Neon foram pedidos explicitamente; detalhes permanecem proposta para revisão independente. Duas aplicações Docker Render free públicas HTTPS, escolar serve React; relatórios e endpoints internos exigem token secreto mesmo em rede pública, sem métricas/dados internos anônimos. Cookies Secure/HttpOnly/SameSite e CSRF real; headers forwarded só confiáveis na plataforma. PostgreSQL TLS com credenciais/bancos isolados; pool pequeno. Uploads/avatar preservados em BYTEA por migração aditiva V5 e leitura compatível/retropreenchimento de arquivos locais, não em disco efêmero. Não criar recurso pago nem alterar oito serviços existentes. Cold start/relogin admitidos sem SLA de disponibilidade; jobs e exportações persistem. Provisionamento Neon e limitação do MCP Render para Docker são lacunas bloqueantes da etapa remota. Backup/restore integral T002 e rollback T001 após exportar bytes a UUIDs foram executados com sucesso em ambientes isolados, mantendo V5 sem DOWN; isso não valida publicação/TLS/custo remotos.

## Contexto
Categoria: ADR. Pergunta: Como publicar dois serviços na plataforma efêmera escolhida mantendo arquivos duráveis e acesso seguro? Documentos anteriores: [PRD-002](../prds/prd-002-qualidade-producao.md) e [HLD qualidade](../hlds/hld-qualidade-producao.md). Dono: Yoda; revisores independentes da redação: implementadores escolar/relatórios. RFC/LLD dispensados: requisito/decisão técnica e contrato FDD cobrem o escopo, sem alternativa de produto não resolvida.

Implantação e durabilidade formam uma decisão coesa: a plataforma efêmera obriga tirar arquivos do disco para preservar a funcionalidade; não é escolha independente de armazenamento especulativo. Usuário solicita produção automática via MCP em Render e Neon. Render MCP autenticado; Neon MCP foi configurado no Codex, mas não autenticado, sem ferramentas callable nesta sessão; credencial necessária não disponível. Render free não recebe rede privada e tem filesystem efêmero. Estratégia proposta atende restrições sem broker/novo armazenamento, mas criação remota depende de ferramentas/conta/quota verificadas.

## Escopo
- Afetado: ADR002 trecho de deploy local é estendido, não apaga evidência T-001; PRD-002/HLD qualidade/FDD-002, V5 e objetos afetados no Obsidian, Compose/pipelines/README/lib e configuração produção.
- Fora de escopo: novos serviços, novas regras escolares, infraestrutura não autorizada e cobrança.

## Forças e critérios
- Preservar comportamento, contratos e segurança validados em T-001; distinguir decisão, implementação e execução.
- Atender requisito explícito sem abstração especulativa; dependências mínimas e custo financeiro zero.
- Autoria arquitetural Yoda; revisão independente necessária e nunca aprovação da própria implementação.

## Opções consideradas

### Opção 1: Dois serviços free HTTPS e arquivos PostgreSQL
- Status na análise: hipótese
- Benefícios: Atende serviços e persistência sem nova infraestrutura.
- Custos e limitações: Rotas técnicas públicas exigem token rigoroso; quota compartilhada/cold start.
- Riscos: Capacidade gratuita pode não bastar; bytea aumenta banco.
- Trade-off: Preserva domínio e dados com custo de latência/capacidade restrita.
- Evidências: Render free/private-network oficiais: free web não recebe rede privada, filesystem efêmero, suspensão/franquias; descoberta Render MCP autenticado e Neon configurado sem autenticação.

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
TBD: detalhes operacionais permanecem proposta; revisão documental não significa capacidade de provisionamento ou deploy. Provedores Render/Neon são escolha explícita do usuário.

Hospedagem Render e banco PostgreSQL Neon foram pedidos explicitamente; detalhes permanecem proposta para revisão independente. Duas aplicações Docker Render free públicas HTTPS, escolar serve React; relatórios e endpoints internos exigem token secreto mesmo em rede pública, sem métricas/dados internos anônimos. Cookies Secure/HttpOnly/SameSite e CSRF real; headers forwarded só confiáveis na plataforma. PostgreSQL TLS com credenciais/bancos isolados; pool pequeno. Uploads/avatar preservados em BYTEA por migração aditiva V5 e leitura compatível/retropreenchimento de arquivos locais, não em disco efêmero. Não criar recurso pago nem alterar oito serviços existentes. Cold start/relogin admitidos sem SLA de disponibilidade; jobs e exportações persistem. Provisionamento Neon e limitação do MCP Render para Docker são lacunas bloqueantes da etapa remota. Backup/restore integral T002 e rollback T001 após exportar bytes a UUIDs foram executados com sucesso em ambientes isolados, mantendo V5 sem DOWN; isso não valida publicação/TLS/custo remotos.

## Consequências

### Positivas
- Permite publicação requerida sem novos tipos de infraestrutura.
- Arquivos tornam-se persistentes fora da instância.

### Negativas
- Free tier tem suspensão e franquias compartilhadas; sem promessa de SLA.
- BYTEA exige backup/capacidade e reverter app demanda compatibilidade.

### Riscos residuais
- Neon MCP configurado mas não autenticado e superfície pública bloqueiam conclusão remota até validação; billing/spend guard e recuperação ainda a verificar.

## Impacto nos artefatos
- ADR002 trecho de deploy local é estendido, não apaga evidência T-001; PRD-002/HLD qualidade/FDD-002, V5 e objetos afetados no Obsidian, Compose/pipelines/README/lib e configuração produção.
- RFC e LLD dispensados: não há disputa de produto nem implementação alternativa não resolvida; HLD/ADR/FDD cobrem estrutura e contratos. Isto não dispensa lacunas remotas.

## Relações
- Origem: [PRD-002](../prds/prd-002-qualidade-producao.md), pedido do experimento e auditoria T-002; [HLD qualidade](../hlds/hld-qualidade-producao.md).
- ADRs relacionadas: [fronteiras](adr-001-fronteiras.md), [stack](adr-002-stack.md), [interface](adr-003-interface.md), [organização](adr-004-organizacao-backend.md).
- Substitui: nenhuma ADR; detalhes técnicos anteriores afetados são indicados acima.
- Substituída por: nenhuma.

## Critério para revisitar
Revisitar caso quota free/capacidade/MCP impeça dois serviços Docker ou durabilidade PostgreSQL; registrar bloqueio real, sem substituir provedor ou contratar pagamento silenciosamente.

## Aceite
- Responsável arquitetural: Yoda (agente planejador/revisor).
- Decisor: usuário nas escolhas explícitas; autonomia delegada no escopo remanescente. Detalhes propostos ainda pendentes de revisão independente.
- Evidência do aceite: Pedido novo escolhe Render e Neon via MCP; a escolha de provedor é explícita. Detalhes operacionais desta proposta não têm aceite independente ainda e nenhuma publicação é declarada.
- Revisão independente da redação: implementadores escolar/relatórios em 2026-10-04, com ajustes de origem/evidências/links aplicados. Isto não aprova sua implementação futura nem declara deploy executado.

## Histórico
- 2026-10-04: ADR criada para o pedido T-002.
- Fontes operacionais: [Render free](https://render.com/docs/free), [rede privada](https://render.com/docs/private-network); descoberta MCP observada por operação, sem criação Neon. A descrição callable de create_web_service aceita somente subset de runtimes sem Docker/container registry; o Docker requerido não está disponível nesse contrato. Automação via API/CLI/Blueprint requer credencial real e registraria desvio do canal MCP pedido; native Java não é substituto autorizado para contornar a stack.
