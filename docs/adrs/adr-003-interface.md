---
type: minerva-adr
project: Atena
date: 2026-10-04
status: aceita
tags: [atena, adr]
ai-first: true
---

# ADR 003: Design e responsividade existentes

## Para o futuro agente
Fundo claro, texto escuro, ação verde #176953, navegação #172d40; Segoe UI/Arial/sans-serif, Georgia no logotipo; base 8 px com 16/20/24 px; controle principal mínimo 44 px e foco visível. Até 767 px: coluna única e tabela com rolagem local; 768–1100 px: até duas colunas; acima: máximo 1500 px. Labels, teclado, mensagens e estados não dependem só de cor. Aplica a login/cadastros/ensino/arquivos/chat/calendário/relatórios.

## Contexto
Categoria: ADR. Pergunta: Qual contrato visual torna a interface escolar legível e responsiva? Documentos anteriores: [PRD-001](../prds/prd-001-refatoracao.md)/[HLD-001](../hlds/hld-refatoracao.md), agora revisados pelo PRD-002. Dono: Yoda; revisores independentes da redação: implementadores escolar/relatórios. RFC/LLD dispensados: requisito/decisão técnica e contrato FDD cobrem o escopo, sem alternativa de produto não resolvida.

ADR anterior descrevia contrato visual implementado, mas não apresentava opções/aceite conforme role. Nova redação mantém tokens e comportamentos observados; T-002 refatora legibilidade sem redesenhar produto.

## Escopo
- Afetado: ADR004 e PRD-002 preservam contrato; capturas desktop/mobile e fluxo de navegador; FDDs e frontend.
- Fora de escopo: novos serviços, novas regras escolares, infraestrutura não autorizada e cobrança.

## Forças e critérios
- Preservar comportamento, contratos e segurança validados em T-001; distinguir decisão, implementação e execução.
- Atender requisito explícito sem abstração especulativa; dependências mínimas e custo financeiro zero.
- Autoria arquitetural Yoda; revisão independente necessária e nunca aprovação da própria implementação.

## Opções consideradas

### Opção 1: Preservar CSS e componentes simples
- Status na análise: escolhida
- Benefícios: Mantém interface validada e dependências mínimas.
- Custos e limitações: Exige organização legível dos componentes.
- Riscos: Refatoração JSX pode alterar estados ou labels.
- Trade-off: Mantém identidade e limita escopo a qualidade.
- Evidências: frontend/src/style.css e App.jsx; evidence/secretaria-mobile.png, boletim-desktop.png e boletim-mobile.png conferidos no E2E T-001.

### Opção 2: Adicionar biblioteca de componentes/redesign
- Status na análise: descartada
- Benefícios: Componentes prontos.
- Custos e limitações: Dependência e mudança visual sem requisito.
- Riscos: Regressão em fluxo/teclado/layout.
- Trade-off: Facilidade inicial com custo de migração e escopo desnecessário.
- Evidências: Inventário docs/lib.md sem biblioteca UI; ADR003 e contrato visual já executado, ausência de pedido de redesign.

## Decisão
Fundo claro, texto escuro, ação verde #176953, navegação #172d40; Segoe UI/Arial/sans-serif, Georgia no logotipo; base 8 px com 16/20/24 px; controle principal mínimo 44 px e foco visível. Até 767 px: coluna única e tabela com rolagem local; 768–1100 px: até duas colunas; acima: máximo 1500 px. Labels, teclado, mensagens e estados não dependem só de cor. Aplica a login/cadastros/ensino/arquivos/chat/calendário/relatórios.

## Consequências

### Positivas
- Direção visual auditável.
- Sem dependência de UI adicional.

### Negativas
- CSS próprio exige cuidado em manutenção.

### Riscos residuais
- As capturas verificam fluxos executados, não certificação formal completa de acessibilidade.

## Impacto nos artefatos
- ADR004 e PRD-002 preservam contrato; capturas desktop/mobile e fluxo de navegador; FDDs e frontend.
- RFC e LLD dispensados: não há disputa de produto nem implementação alternativa não resolvida; HLD/ADR/FDD cobrem estrutura e contratos. Isto não dispensa lacunas remotas.

## Relações
- Origem original: [PRD-001](../prds/prd-001-refatoracao.md) e [HLD T-001](../hlds/hld-refatoracao.md). Revisão atual: [PRD-002](../prds/prd-002-qualidade-producao.md), pedido do experimento e auditoria T-002; [HLD qualidade](../hlds/hld-qualidade-producao.md).
- ADRs relacionadas: [fronteiras](adr-001-fronteiras.md), [stack](adr-002-stack.md), [organização](adr-004-organizacao-backend.md), [produção](adr-005-producao-render-neon.md).
- Substitui: nenhuma ADR; detalhes técnicos anteriores afetados são indicados acima.
- Substituída por: nenhuma.

## Critério para revisitar
Revisitar se houver nova tela, estado ou faixa de uso sem comportamento definido, ou falha real de acessibilidade.

## Aceite
- Responsável arquitetural: Yoda (agente planejador/revisor).
- Decisor: usuário nas escolhas explícitas; autonomia delegada no escopo remanescente. Decisão aceita no escopo autorizado, execução nova ainda não validada.
- Evidência do aceite: Autonomia expressa para implementar interface React funcional e regras Minerva de design/responsividade; direção foi registrada e conferida nas capturas T-001. Pedido novo exige qualidade do código, não redesenho.
- Revisão independente da redação: implementadores escolar/relatórios em 2026-10-04, com ajustes de origem/evidências/links aplicados. Isto não aprova sua implementação futura nem declara deploy executado.

## Histórico
- 2026-10-04: ADR existente corrigida contra role planejar/revisar e skill gerar-adr, preservando decisão anterior.
