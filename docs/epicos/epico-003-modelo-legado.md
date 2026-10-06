---
type: minerva-epico
project: Atena
date: 2026-10-05
status: em-planejamento
tags: [atena, epico]
ai-first: true
---

# Épico 003: Dados acadêmicos reais e legado publicado

## Para o futuro agente
Pedido novo forneceu dump e cadeia Faculdade→Curso→GradeCurricular→RequisitoFormacao, com matrícula/associações por IDs, e publicação PHP real. O refatorado T002 integrado permanece preservado; publicação cloud ainda bloqueada. Duas entregas têm provas distintas: modelo/importação local e compatibilidade/publicação PHP; mismatch não impede avançar banco local após cadeia aprovada.

## Escopo e cadeia
[Roadmap](../roadmap.md) → [PRD003](../prds/prd-003-modelo-legado.md) → [HLD modelo-legado](../hlds/hld-modelo-legado.md)/[ADR006](../adrs/adr-006-modelo-importacao.md)/[ADR007](../adrs/adr-007-legado-postgresql.md) → FDD de autor independente → task após aprovação → implementação/review/CI → deploy somente quando efetivamente possível. RFC/LLD/refinamento separado dispensados: perguntas e critérios cabem nesta cadeia; não criar catálogo ou manual novo.

## Refinamento multiagente
Data: 2026-10-05. Estado: plano/FDD revisados independentemente, ajustes aplicados e T003 pronta. Produção local liberada somente nos escopos próprios; remoto bloqueado.

### Pareceres
| Agente real | Posição | Evidência e condição |
| --- | --- | --- |
| Planejador | Seguir com ressalvas | 24 tabelas/107 linhas mapeadas; IDs e constraints aditivos, fonte privada, nenhuma pessoa/instituição inventada |
| Implementador Java/importação | Análise independente concluída; plano revisado, autor FDD aprovado por revisores alheios | Cruzou labels/idClass; 21 docentes vazios são null, 40 empréstimos+1 participação têm usuário ausente; cargo0/4 desconhecido |
| Operação/revisor | Plano aprovado, sem código/deploy aprovado | Conferiu PRD/HLD/ADRs, dump/schema/history e superfície insegura PHP; FDD deve explicitar compatibilidade e segurança |

### Divergências e resolução
- Matricula.cursoId duplicado versus Curso derivado: derivar Matricula→Turma→Curso, autorizado como melhoria de integridade.
- Criar tabelas por palavras versus associações reais: Disciplina já representa docência; matrícula_disciplina tem contexto de oferta; requisito_materia é ligação administrativa sem motor de formação.
- Schema dump2020 versus código2024: compatibilidade explícita, sem alegar fonte inexistente ou biblioteca/inscrição PHP já implementadas.
- Autonomia versus entrevistas/aprovação humana: prompt prevalece, revisão independente por artefato mantida.
- Papel planejador versus produção: planejador só docs/review; autor Java escreve FDD/import/frontend; operação poderá exercer implementação PHP/infra, com revisores diferentes por escopo.

### Exigências por futura task
#### Modelo/importação e compatibilidade PHP T003
- Resultado: relações por IDs operacionais, importação107 registros contabilizada/replay, dados biblioteca/eventos do dump conservados com pendências privadas, sem CRUD/UI especulativo, PHP páginas/casos reais caracterizados sobre PG.
- PRD/HLD/ADR: propostas mínimas presentes e revisadas; aceitação final depende de revisão cruzada e FDD.
- FDD: contrato legado/refatorado, migrations novas V6+ sem alterar V1–V5, DDL separado backfill/import Batch, constraints/índices proporcionais e recuperação por restore/migração compensatória. Matriz permite órfãos/cargos sem privilégio automático.
- Testes: parser allowlist sem SQL bruto,107 contabilizados com replay/falha/restart, fixtures sintéticas, endpoint novo/alterado, papéis/objeto/CSRF/PDO/PDF/upload e E2E.
- Operação: primeiro Docker/PG isolado; Render/Neon só autenticados/gratuitos/TLS e após dois CI/revisão. Dump privado/segredos nunca artifacts públicos.
- Dependências: FDD independente aprovado; instituição/cargo/estado/arquivo ausentes ficam pendentes e não bloqueiam modelo local, mas impedem declarar importação ativa integral/login de papel desconhecido.

### Decisões e lacunas
- RequisitoFormacao administrativo Grade↔Matéria N:N; sem requisito prévio/elegibilidade/colação inferidos.
- Faculdade ausente (escolar0 linhas), sem cadastro fictício; Curso histórico nullable até reconciliação.
- PHP real isolado é exceção específica de runtime; não substitui Java/React nem introduz MariaDB/sincronização bidirecional.
- Conteúdo de arquivo ausente, estados/cargos desconhecidos e pessoas órfãs: preparação privada com motivo/replay.
- Neon auth/canal Docker Render/quota gratuita ainda bloqueiam produção.

## Responsabilidade e histórico
Dono planejamento: esta thread em papel Yoda. FDD/Java/import/frontend: implementador independente. Revisão do Java/FDD: planejador e operação; PHP/infra se assumidos pela operação terão revisão do planejador e implementador Java, sem autoaprovação/merge. Jarvis integra somente após revisão independente e dois pipelines verdes.
- 2026-10-05: épico/refinamento criado por pedido explícito e mudança de domínio/dados/runtime; análise privada e plano revisado operação, FDD aprovado e task pronta. Nenhum código T003 escrito.

- 2026-10-05: [FDD003](../fdds/fdd-003-modelo-importacao-legado.md) aprovado pelo planejador/PHP e operação Java/importação/segurança; [T003](../tasks/t-003-modelo-importacao-legado.md) pronta. Nenhuma aprovação do próprio artefato/implementação, nenhuma validação de código/deploy neste gate.
