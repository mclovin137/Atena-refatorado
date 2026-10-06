---
type: minerva-adr
project: Atena
date: 2026-10-05
status: aceita
tags: [atena, adr]
ai-first: true
---

# ADR 006: Modelo acadêmico por IDs e importação privada

## Para o futuro agente
Proposta originada pelo pedido T003. Não autoriza produção antes revisão da cadeia/FDD nem declara deployment; autonomia substitui consultas humanas previstas no skill, não a revisão independente.

## Contexto
Schema atual não possui cadeia Faculdade/Curso/Grade/Requisito e curso da matrícula é hoje derivável pela turma. Dump sistema tem 24 tabelas/107 linhas, sem FK e dados órfãos. Necessário ampliar sem invalidar contratos anteriores ou executar SQL bruto.

## Escopo
- Afetado: PRD003/HLD T003 e implementação futura.
- Fora de escopo: infraestrutura paga, MariaDB runtime, reescrita dos dois serviços ou descarte silencioso do dump.

## Forças e critérios
- Pedido explícito atendido, vínculos/segurança comprovados e fonte privada preservada; custos zero, replay/rollback auditáveis e contratos anteriores caracterizados.

## Opções consideradas
### Opção 1: executar dump MariaDB e conectar aplicação
- Status na análise: descartada.
- Benefícios: restauração aparente rápida.
- Custos e limitações: runtime MariaDB viola infraestrutura e fonte SQL não é confiável executável.
- Riscos: órfãos permanecem, diferença de versão e execução de comandos perigosos.
- Trade-off: velocidade aparente troca integridade/segurança.
- Evidências: dump privado e PDO checkout usam schemas distintos.

### Opção 2: ampliar PostgreSQL aditivamente e importar declarativamente
- Status na análise: candidata.
- Benefícios: IDs/constraints, preservação fonte/pendências e replay idempotente; mesmo contexto escolar.
- Custos e limitações: parser/mapeamento explícito e reconciliação necessários; zero instituição/pessoa sintética.
- Riscos: associação textual ambígua e cargo desconhecido não permitem ativação.
- Trade-off: mais trabalho específico, integridade verificável e dados pendentes preservados.
- Evidências: todas21grade.idTurma resolvem turmas.idClass;40empréstimos não resolvem user; escolar vazio.

### Opção 3: manter modelo atual sem importar
- Status na análise: descartada para atender pedido.
- Benefícios: preserva entrega T002 sem risco novo.
- Custos e limitações: cadeia acadêmica e biblioteca continuam ausentes.
- Riscos: requisito não atendido.
- Trade-off: menor mudança, nenhuma evolução solicitada.

## Decisão
Proposta opção2: manter Curso da matrícula derivado pela Turma para evitar redundância; reusar Disciplina para associação Professor–Matéria, criar matrícula_disciplina somente pela seleção explícita contextual e requisito_materia para cadastro administrativo de formação, sem motor de elegibilidade. Modelo aditivo com IDs e staging/reconciliação privados; Spring Batch já existente processa fonte allowlist sem executar SQL. Mapeamento de toda tabela no HLD, FDD define DDL/casos; tabelas vazias também têm destino declarado privado, sem criar camadas/endpoints sem comportamento requerido. Faculdade ausente não é criada por suposição.

## Consequências
### Positivas
- Relacionamentos ficam auditáveis; sistema existente permanece consultável e importação não apaga registros inválidos silenciosamente.
### Negativas
- Dados sem instituição/pessoa/cargo confiável ficam pendentes; não é importação integral ativa por força de dados ausentes.
### Riscos residuais
- Estado/cargo/órfãos e autenticação cloud desconhecidos impedem declaração de conclusão; análise/FDD/gates tratam cada risco sem preencher dados inventados.

## Impacto nos artefatos
- PRD003, HLD T003, FDD futuro, roadmap/épico/task após aprovação, docs/lib quando dependência mudar e notas SQL após DDL real.

## Relações
- Origem: [PRD003](../prds/prd-003-modelo-legado.md).
- ADRs relacionadas: [ADR002](adr-002-stack.md), [ADR004](adr-004-organizacao-backend.md), [ADR005](adr-005-producao-render-neon.md).
- Substitui: nenhuma; exceção delimitada quando aceita.
- Substituída por: nenhuma.

## Critério para revisitar
Inviabilidade gratuita/segura comprovada, fonte correspondente ausente ou perda de contrato/dado observada exige revisão explícita, sem trocar tecnologia silenciosamente.

## Aceite
- Responsável arquitetural: Yoda planejador nesta thread.
- Decisor: pedido do usuário fixa resultado e autonomia; solução revisada independentemente por operação e implementador, com aceite arquitetural do planejador dentro da autonomia do pedido.
- Evidência do aceite: novo pedido T003 forneceu modelo/dump e publicação PHP; detalhes estruturais revisados e aceitos para etapa local nesta data; nenhuma migração/deploy executado.

## Histórico
- 2026-10-05: proposta criada; nenhuma implementação/migração/deploy T003 executada.

- 2026-10-05: decisão aceita para implementação local após revisão cruzada independente do plano/FDD e autonomia explícita do pedido. Não é aprovação de deployment; limites/condições de cloud permanecem.
