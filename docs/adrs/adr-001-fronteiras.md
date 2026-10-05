---
type: minerva-adr
project: Atena
date: 2026-10-04
status: aceita
tags: [atena, adr]
ai-first: true
---

# ADR 001: Fronteiras escolar e relatórios

## Para o futuro agente
Dois microsserviços com contratos HTTP explícitos, dados e credenciais separados; nenhum serviço por cadastro nem importação de entidade entre contextos.

## Contexto
Categoria: ADR. Pergunta: Onde termina a consistência escolar e começa o processamento histórico? Documentos anteriores: [PRD-001](../prds/prd-001-refatoracao.md)/[HLD-001](../hlds/hld-refatoracao.md), agora revisados pelo PRD-002. Dono: Yoda; revisores independentes da redação: implementadores escolar/relatórios. RFC/LLD dispensados: requisito/decisão técnica e contrato FDD cobrem o escopo, sem alternativa de produto não resolvida.

O núcleo escolar mantém consistência de identidade, ensino e notas; relatórios isolam processamento pesado. A correção desta ADR decorre do pedido de revisão contra roles, preservando a decisão T-001.

## Escopo
- Afetado: PRD-001 e PRD-002; HLD refatoração e HLD qualidade; FDD-001 e FDD-002; contratos de snapshot e Batch.
- Fora de escopo: novos serviços, novas regras escolares, infraestrutura não autorizada e cobrança.

## Forças e critérios
- Preservar comportamento, contratos e segurança validados em T-001; distinguir decisão, implementação e execução.
- Atender requisito explícito sem abstração especulativa; dependências mínimas e custo financeiro zero.
- Autoria arquitetural Yoda; revisão independente necessária e nunca aprovação da própria implementação.

## Opções consideradas

### Opção 1: Dois serviços por contexto
- Status na análise: escolhida
- Benefícios: Isola lotes e mantém transações escolares locais.
- Custos e limitações: Exige contrato HTTP e autenticação entre serviços.
- Riscos: Indisponibilidade da origem impede snapshot.
- Trade-off: Aceita coordenação distribuída mínima para isolar histórico.
- Evidências: legacy/Models/MainMolde.php (cadastros/notas), legacy/Views/pdf/HistoricoCompleto.php (loop histórico), serviços escolar/relatorios e endpoints snapshot T-001.

### Opção 2: Manter monólito PHP
- Status na análise: descartada
- Benefícios: Menor mudança operacional imediata.
- Custos e limitações: Não atende stack nem microsserviços exigidos.
- Riscos: Acoplamento existente persiste.
- Trade-off: Ganhar estabilidade imediata perderia requisito mandatário.
- Evidências: Árvore legacy/Controller e legacy/Models, acoplamento PHP/SQL observado na análise T-001 e stack mandatória do prompt.

### Opção 3: Um serviço por cadastro
- Status na análise: descartada
- Benefícios: Deploy separado por cadastro.
- Custos e limitações: Muitas transações e chamadas distribuídas.
- Riscos: Acoplamento de matrículas/notas/turmas.
- Trade-off: Ganhar granularidade sem autonomia real do domínio.
- Evidências: Relações turma/matrícula/disciplina/nota em V1 escolar e chamadas snapshot; nenhum cadastro autônomo demonstrado.

## Decisão
Dois microsserviços com contratos HTTP explícitos, dados e credenciais separados; nenhum serviço por cadastro nem importação de entidade entre contextos.

## Consequências

### Positivas
- Núcleo escolar permanece fonte de verdade.
- Processamento histórico isolado.

### Negativas
- Falha HTTP precisa ser tratada e exportação é assíncrona.

### Riscos residuais
- Snapshot inteiro e consultas N+1 continuam sem validação de carga.

## Impacto nos artefatos
- PRD-001 e PRD-002; HLD refatoração e HLD qualidade; FDD-001 e FDD-002; contratos de snapshot e Batch.
- RFC e LLD dispensados: não há disputa de produto nem implementação alternativa não resolvida; HLD/ADR/FDD cobrem estrutura e contratos. Isto não dispensa lacunas remotas.

## Relações
- Origem original: [PRD-001](../prds/prd-001-refatoracao.md) e [HLD T-001](../hlds/hld-refatoracao.md). Revisão atual: [PRD-002](../prds/prd-002-qualidade-producao.md), pedido do experimento e auditoria T-002; [HLD qualidade](../hlds/hld-qualidade-producao.md).
- ADRs relacionadas: [stack](adr-002-stack.md), [interface](adr-003-interface.md), [organização](adr-004-organizacao-backend.md), [produção](adr-005-producao-render-neon.md).
- Substitui: nenhuma ADR; detalhes técnicos anteriores afetados são indicados acima.
- Substituída por: nenhuma.

## Critério para revisitar
001 não muda fronteiras na T-002. Revisitar somente se uma responsabilidade demonstrar autonomia transacional nova ou acoplamento impedir evolução.

## Aceite
- Responsável arquitetural: Yoda (agente planejador/revisor).
- Decisor: usuário nas escolhas explícitas; autonomia delegada no escopo remanescente. Decisão aceita no escopo autorizado, execução nova ainda não validada.
- Evidência do aceite: Arquitetura mandatória no prompt original: microsserviços com responsabilidades claras; autonomia expressa para escolher poucas fronteiras baseadas no código.
- Revisão independente da redação: implementadores escolar/relatórios em 2026-10-04, com ajustes de origem/evidências/links aplicados. Isto não aprova sua implementação futura nem declara deploy executado.

## Histórico
- 2026-10-04: ADR existente corrigida contra role planejar/revisar e skill gerar-adr, preservando decisão anterior.
