---
type: minerva-adr
project: Atena
date: 2026-10-04
status: aceita
tags: [atena, adr]
ai-first: true
---

# ADR 002: Stack Spring e autenticação por sessão

## Para o futuro agente
Java 21/Spring Boot, React, PostgreSQL, Docker; Spring Data JPA, Validation, Flyway, Batch, Cache em memória, Actuator/Micrometer, Security sessão BCrypt/CSRF; PDFBox apenas exportações existentes. Versões/finalidades no inventário. Publicação local original continua válida; implantação remota autorizada no pedido novo é detalhada na ADR005.

## Contexto
Categoria: ADR. Pergunta: Qual stack/segurança preserva o produto sob os requisitos mandatórios? Documentos anteriores: [PRD-001](../prds/prd-001-refatoracao.md)/[HLD-001](../hlds/hld-refatoracao.md), agora revisados pelo PRD-002. Dono: Yoda; revisores independentes da redação: implementadores escolar/relatórios. RFC/LLD dispensados: requisito/decisão técnica e contrato FDD cobrem o escopo, sem alternativa de produto não resolvida.

Stack definida no prompt; segurança de sessões preserva perfis do legado. A organização técnica T-001 foi insuficiente e é corrigida pela ADR004; esta ADR nunca autoriza expor entidades JPA ou políticas genéricas por reflexão.

## Escopo
- Afetado: Inventário docs/lib.md; ADR004 organização; ADR005 implantação; HLDs; FDDs; pipelines review e testes.
- Fora de escopo: novos serviços, novas regras escolares, infraestrutura não autorizada e cobrança.

## Forças e critérios
- Preservar comportamento, contratos e segurança validados em T-001; distinguir decisão, implementação e execução.
- Atender requisito explícito sem abstração especulativa; dependências mínimas e custo financeiro zero.
- Autoria arquitetural Yoda; revisão independente necessária e nunca aprovação da própria implementação.

## Opções consideradas

### Opção 1: Spring e sessão
- Status na análise: escolhida
- Benefícios: Atende stack e fornece validação/persistência/segurança padronizadas.
- Custos e limitações: Exige cookie/CSRF correto e configuração TLS pública.
- Riscos: Sessões locais se perdem no restart.
- Trade-off: Aceita relogin sem introduzir infraestrutura de sessão.
- Evidências: pom.xml raiz, services/*/pom.xml, SecurityConfig.java e SchoolFlowTest T-001; Spring Cache/Batch/Validation/JPA e CSRF real executados.

### Opção 2: Manter PHP/MySQL
- Status na análise: descartada
- Benefícios: Menor migração inicial.
- Custos e limitações: Viola stack obrigatória.
- Riscos: Mantém SQL e segurança legados.
- Trade-off: Menor mudança não compensa requisitos descumpridos.
- Evidências: legacy/composer.json mPDF, fontes PHP e persistência SQL; incompatibilidade com stack explícita.

### Opção 3: JWT e infraestrutura adicional
- Status na análise: descartada
- Benefícios: Pode suportar sessão entre instâncias.
- Custos e limitações: Revogação/rotação e bibliotecas não justificadas.
- Riscos: Complexidade de autorização adicional.
- Trade-off: Escala hipotética sem demanda não justifica abstração.
- Evidências: Requisito de sessão/perfis original e inventário docs/lib.md; nenhuma demanda de escala/token externo fornecida.

## Decisão
Java 21/Spring Boot, React, PostgreSQL, Docker; Spring Data JPA, Validation, Flyway, Batch, Cache em memória, Actuator/Micrometer, Security sessão BCrypt/CSRF; PDFBox apenas exportações existentes. Versões/finalidades no inventário. Publicação local original continua válida; implantação remota autorizada no pedido novo é detalhada na ADR005.

## Consequências

### Positivas
- Recursos Spring atendem capacidades obrigatórias.
- Sem broker/cache externo.

### Negativas
- Sessões atuais são locais à instância e reinício requer login.

### Riscos residuais
- Troca de senha invalida sessão atual, outras sessões continuam até expiração; limitação T-001 mantida.

## Impacto nos artefatos
- Inventário docs/lib.md; ADR004 organização; ADR005 implantação; HLDs; FDDs; pipelines review e testes.
- RFC e LLD dispensados: não há disputa de produto nem implementação alternativa não resolvida; HLD/ADR/FDD cobrem estrutura e contratos. Isto não dispensa lacunas remotas.

## Relações
- Origem original: [PRD-001](../prds/prd-001-refatoracao.md) e [HLD T-001](../hlds/hld-refatoracao.md). Revisão atual: [PRD-002](../prds/prd-002-qualidade-producao.md), pedido do experimento e auditoria T-002; [HLD qualidade](../hlds/hld-qualidade-producao.md).
- ADRs relacionadas: [fronteiras](adr-001-fronteiras.md), [interface](adr-003-interface.md), [organização](adr-004-organizacao-backend.md), [produção](adr-005-producao-render-neon.md).
- Substitui: nenhuma ADR; detalhes técnicos anteriores afetados são indicados acima.
- Substituída por: nenhuma.

## Critério para revisitar
Revisitar apenas se requisito comprovado de múltiplas instâncias/sessões persistentes exigir novo contrato e justificativa.

## Aceite
- Responsável arquitetural: Yoda (agente planejador/revisor).
- Decisor: usuário nas escolhas explícitas; autonomia delegada no escopo remanescente. Decisão aceita no escopo autorizado, execução nova ainda não validada.
- Evidência do aceite: Prompt original fixa Java/Spring Boot, React, PostgreSQL e Docker; exige Spring primeiro e dependências mínimas. Segurança por sessão é decisão autorizada pela autonomia, sem aprovação humana inventada.
- Revisão independente da redação: implementadores escolar/relatórios em 2026-10-04, com ajustes de origem/evidências/links aplicados. Isto não aprova sua implementação futura nem declara deploy executado.

## Histórico
- 2026-10-04: ADR existente corrigida contra role planejar/revisar e skill gerar-adr, preservando decisão anterior.
