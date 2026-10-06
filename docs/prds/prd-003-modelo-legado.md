---
type: minerva-prd
project: Atena
date: 2026-10-05
status: aprovado
tags: [atena, prd]
ai-first: true
---

### PRD: Atena Modelo acadêmico e publicação do legado real

Versão: 1
Data: 2026-10-05
Responsável: planejador/revisor

---

### Resumo
O usuário forneceu dump MariaDB e exige banco novo com Faculdade→Curso→GradeCurricular→RequisitosFormacao, matrícula do aluno ligada ao curso pela turma e associações de matéria por IDs. Também pede publicar o PHP legado real, preservando o sistema refatorado e autorizando melhorias. São entregas relacionadas, mas importação no Java e execução PHP precisam de critérios separados: o dump não corresponde ao schema do checkout PHP atual.

### Contexto e problema
Público-alvo
- Secretaria, docentes, alunos e mantenedores autorizados.

Cenários de uso chave
- Consultar relações acadêmicas explícitas sem joins por nomes.
- Importar registros válidos e resolver pendências sem inventar dados.
- Usar a interface PHP efetiva com autenticação e dados reais protegidos em produção.

Onde essa feature será implantada
- Atena existente, PostgreSQL obrigatório, Render/Neon solicitados. PHP é pedido específico adicional, não substituição dos dois serviços Java/React.

Problemas priorizados
- Alta: modelo atual não explicita Faculdade/Grade/Requisito nem Curso na matrícula; dump inclui biblioteca e participação em eventos não representadas.
- Alta: schema dump sistema diverge do PHP checkout; execução pública exige identificar versão compatível ou traduzir casos de uso explicitamente.
- Alta: dump tem pessoas ausentes e credenciais históricas; exposição direta é insegura e importação sem reconciliação perde integridade.

### Objetivos e métricas
| Objetivo | Métrica | Meta |
| --- | --- | --- |
| Cobrir dados fornecidos | Tabelas com destino ou pendência explícita | 24 de 24, sem descarte silencioso |
| Manter rastreabilidade | Registros lidos = importados + pendentes + rejeitados auditados | Todos os 107 registros atuais, digest/contagens sem PII pública |
| Garantir vínculos | Relações persistidas com IDs e constraints verificadas | Nenhum órfão ativo ou pessoa/instituição inventada |
| Preservar sistema existente | Contratos/E2E T002 e migração aditiva | Nenhuma regressão não declarada nos fluxos executados |
| Publicar legado efetivo | URL HTTPS, revisão segurança, login/fluxos reais | Deploy e evidências observados, ou requisito explicitamente não atendido |

### Escopo
Incluso
- Modelo pedido, associação aluno/matéria e professor/matéria/requisito, conservação dos dados de biblioteca/eventos e demais tabelas do dump com destinos rastreados, sem exigir nova interface de gestão.
- Importação privada auditável, validação/reexecução sem duplicação, tratamento de vínculos textuais/órfãos/estados desconhecidos.
- Compatibilidade incremental do refatorado; PHP efetivo, adaptação ao PostgreSQL e correções de segurança necessárias antes publicar.
- Governança, auditoria/log/estado e documentação mínima disparada por mudança real.

Fora de escopo
- Inventar faculdade, alunos/docentes ausentes, significado de cargos/estados ou conteúdos de arquivos não fornecidos.
- Executar dump bruto, versionar PII/credenciais, pagar infraestrutura, acrescentar MariaDB de runtime.
- Novo microsserviço biblioteca, framework PHP novo, sincronização bidirecional genérica, redefinição de fórmulas acadêmicas sem requisito.

### Requisitos funcionais
#### RF-001 Relações acadêmicas explícitas
Faculdade tem cursos; curso tem grades; grade tem requisitos de formação. Aluno tem matrículas ligadas a turma e navegáveis ao curso por ID da turma, com matéria associável a aluno/professor/requisito por IDs.
**Fluxo principal**
- Secretaria cria instituição/curso/grade/requisito/matéria e matrícula válidos; sistema rejeita vínculo turma/curso incoerente.
**Fluxos alternativos e exceções**
- Modelo existente sem instituição permanece consultável como legado até reconciliação; novos vínculos acadêmicos completos exigem IDs válidos quando usados pelo fluxo novo; POST Curso {nome} legado permanece aceito como cadastro incompleto com Faculdade não informada. RequisitoFormacao é cadastro administrativo da Grade ligado N:N a Matéria, sem cálculo de colação ou pré-requisito. Matricula→Turma→Curso atende a cadeia sem duplicar cursoId.
**Erros previstos**
- Referência ausente, associação duplicada, exclusão de recurso utilizado e mudança de curso incompatível.
**Prioridade:** alta

#### RF-002 Importação completa e auditável
Cobrir 24 tabelas e 107 registros do dump autorizado. Separar dados válidos de pendentes/rejeitados e preservar origem/IDs sem publicar conteúdo sensível.
**Fluxo principal**
- Operador fornece arquivo privado; análise declarativa produz plano seco; confirmação do plano pela cadeia autorizada permite gravar dados parametrizados e contabilizar destino/pendência por origem.
**Fluxos alternativos e exceções**
- Replay do mesmo digest não duplica; resolução posterior de vínculo reprocessa somente pendências. Erro interrompe etapa com estado factual e permite restart.
**Erros previstos**
- SQL fora da gramática permitida, duplicação/colisão, relacionamento ambíguo, estado/cargo não interpretado e arquivo referenciado ausente.
**Prioridade:** alta

#### RF-003 Conservação de biblioteca, eventos e tabelas restantes
Livros/estoque/empréstimos e eventos/vagas/participações são dados a conservar com relações auditáveis; calendário não substitui seus campos. Não há requisito de CRUD/UI ou motor de estoque/vagas. As14 tabelas vazias recebem destino declarado de preparação privada quando não há equivalência comprovada, sem criar camadas/endpoints vazios.
**Fluxo principal**
- Importador preserva dados e origem em tabelas de conservação privadas ou entidades já equivalentes; empréstimo/participação sem pessoa/estado válido continua pendente. Não publicar endpoints/UI novos apenas para completar o dump.
**Fluxos alternativos e exceções**
- Registros de pessoa ausente ficam pendentes privados e auditáveis; nenhuma conta sintética é criada.
**Erros previstos**
- Referência inexistente/ambígua, estado desconhecido e estrutura não suportada; rejeição mantém fonte e contagem, sem perda silenciosa.
**Prioridade:** alta

#### RF-004 Publicação do PHP legado efetivo
Publicar interface e casos de uso PHP reais, não uma página demonstrativa. Identificar versão compatível com dump ou demonstrar tradução de contrato antes disponibilizar dados reais.
**Fluxo principal**
- Versão/base/rotas são fixadas; autenticação, autorização, CSRF, SQL parametrizado e arquivos protegidos são verificados; publicação HTTPS passa login por papéis e fluxos reais.
**Fluxos alternativos e exceções**
- Cloud indisponível mantém entrega local e requisito remoto parcial; passwords legadas exigem tratamento seguro privado, sem credencial de demonstração pública.
**Erros previstos**
- Schema incompatível, versão ausente, papel desconhecido, dependência PHP incompatível, TLS/canal Docker/quota não comprovados.
**Prioridade:** alta

### Requisitos não funcionais
Performance
- Sem meta inventada; importação em lote limitado com restart, leitura de metadados sem carregar blobs desnecessariamente.
Disponibilidade
- Sem SLA prometido; health e comportamento de cold start observados antes declarar produção.
Segurança e autorização
- Dados privados fora do Git/artifacts públicos; HTTPS, cookies/CSRF, perfil/objeto, queries parametrizadas, uploads fora webroot e download autorizado. MD5 não é credencial nova segura.
Observabilidade
- Logs de importação por digest/tabela/contagens/estado sem linhas, PII ou hashes de senha; métricas/autorização existentes preservadas.
Confiabilidade e integridade de dados
- Constraints acadêmicas, contabilização total e replay idempotente, origem preservada, backup/restore e rollback executados em isolamento.
Compatibilidade e portabilidade
- Contratos JSON/status e snapshots refatorados preservados ou alteração explicitamente caracterizada; PHP PostgreSQL em Docker é exceção específica, não MariaDB.
Compliance
- Acesso restrito a dump e staging, segredo nunca versionado, mínimo de dados em evidências. Retenção não presumida.
Acessibilidade no frontend consumidor
- React conforme ADR003; páginas PHP reais verificadas desktop/mobile/teclado com limitações históricas declaradas.

### Arquitetura e abordagem
Abordagem
- Contexto existente ADR001/002/004; decisões novas propostas ADR006/007 e HLD T003, sem topologia escolhida pelo PRD.
Componentes
- Gestão escolar/refatorada, relatórios, PHP efetivo, banco e importação necessários.
Integrações
- Fonte dump privado, dados acadêmicos e Render/Neon conforme pedido.

### Decisões e trade-offs
#### Decisão: preservar refatorado e publicar legado adicional
- **Justificativa:** pedido novo explícito delimita PHP real e banco novo.
- **Trade-off:** compatibilidade de versão/dados precisa de provas separadas, proposta no HLD/ADRs.

### Dependências
#### Técnica: dump privado e fonte correspondente
Dump extraído do pedido atual por orquestração; histórico disponível não contém a versão correspondente. Mapeamento de cargo/estado depende de fonte ou permanece pendente.
#### Externa: canais Render/Neon gratuitos autenticados
Bloqueios T002 ainda não resolvidos; nenhuma publicação presumida.

### Riscos e mitigação
#### Dados órfãos e relação textual ambígua
- **Probabilidade:** alta, ocorrências observadas.
- **Impacto:** importação errada, pessoa indevida ou perda de dados.
- **Mitigação:** análise declarativa, chave de origem, pendência privada e matching único por label apenas com prova.
- **Plano de contingência:** não ativar registro e manter fonte/replay rastreável.
#### Legado público vulnerável ou incompatível
- **Probabilidade:** alta, PDO MySQL e endpoints frágeis observados.
- **Impacto:** vazamento, alteração indevida ou falha dos fluxos reais.
- **Mitigação:** revisão independente, caracterização por rota, correções mínimas e dados sintéticos primeiro.
- **Plano de contingência:** bloquear exposição pública sem fingir deploy concluído.

### Critérios de aceitação
- [ ] 24 tabelas mapeadas; 107 registros contabilizados por digest/origem/destino ou pendência; dump nunca executado bruto nem publicado.
- [ ] Modelo pedido com IDs/constraints e teste de incoerência turma/curso; grade/requisito e associações funcionais sem wrappers decorativos.
- [ ] Livros/empréstimos/eventos/participações e outros dados têm conservação/importação comprovada, sem CRUD/UI especulativo; nenhuma pessoa/faculdade inventada para sanar órfão.
- [ ] Contratos refatorados e Batch mantidos com testes reais; importação replay/restart e isolamento comprovados.
- [ ] PHP versão/base fixados, fluxo real PostgreSQL/login/perfis/arquivos/relatórios observado e superfícies inseguras corrigidas.
- [ ] Deploy HTTPS Render/Neon efetivamente verificado ou status parcial com bloqueio factual; dois gates/review independentes antes integração.

### Testes e validação
Tipos de teste obrigatórios
- Caracterização dos contratos atuais; integração PostgreSQL por endpoint novo/alterado, jobs e importação com fixture sintética.
- Dump malicioso/rejeitado, parser literal, IDs colidentes, órfãos, cargos/desconhecidos, idempotência e restart.
- Segurança PHP por rota/perfil/objeto/CSRF/SQL/upload/arquivo e navegador real; backup/restore/reversão isolados.
Estratégia de validação
- FDD autor independente detalha matriz enxuta; revisor confere plano e provas antes produção. Evidência privada contém somente contagens/digest e dados sintéticos; arquivo bruto nunca entra em logs. Categoria PRD, dono planejador, revisão operação/implementador. RFC/LLD dispensados: pedido delimitado e HLD/FDD suficientes; entrevistas humanas dispensadas por autonomia explícita, sem converter desconhecido em fato.
