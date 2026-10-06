---
type: minerva-hld
project: Atena
date: 2026-10-05
status: vigente
tags: [atena, hld]
ai-first: true
---

### HLD: Atena Modelo acadêmico, importação e legado

Versão: 1
Data: 2026-10-05
Responsável: Yoda planejador

### Objetivo técnico
Ampliar modelo por IDs e importar fonte MariaDB declarativamente sem executar SQL, mantendo APIs atuais e publicando PHP real de modo isolado e seguro. PRD003 origina escopo; ADR006 trata dados/importação e ADR007 runtime PHP. FDD independente detalhará casos de uso antes task pronta.

Dependências com outros sistemas
- Dois serviços Java/React/Batch e PostgreSQL existentes; PHP preservado em legacy; fonte dump privada.
- Render/Neon MCP ainda bloqueados; descobrir fonte PHP compatível é responsabilidade de operação, não prova presumida.

### Arquitetura geral
Proposta: manter dois serviços refatorados e adicionar somente container PHP legado separado, com credencial PostgreSQL própria e schema de compatibilidade isolado. Modelo normalizado continua propriedade do escolar; relatórios usa snapshots explícitos. Importação em Spring Batch no escolar, por grafo de dependências, e staging/reconciliação privados. Schema compatível não autoriza escrita concorrente bidirecional: cada implantação deve fixar modo de operação e fonte de verdade. Primeiro publicar PHP com sua base de compatibilidade separada, alimentada pelo mesmo dump auditado; sincronização automática fica fora do escopo. Nenhuma terceira base MariaDB, framework PHP novo, fila/cache externo ou microsserviço de biblioteca.

Ambiente de implantação
- Docker Compose local isolado primeiro; Render Docker/Neon depois de canais autenticados/gratuidade/TLS verificados.
Tecnologias principais
- Stack atual Java/Spring Batch/JPA/Flyway/PostgreSQL e React preservada; PHP/PDO pgsql somente como exceção explícita para executar legado real.
Padrões adotados
- Organização ADR004 por recurso/função, domínio puro, ports/adapters e DTOs explícitos; migrações aditivas/expand-contract.
- Parser de dump estritamente declarativo e allowlist, gravação preparada, origem por digest/tabela/PK e reconciliação sem PII em logs.

### Componentes e responsabilidades
| Componente | Responsabilidades | Dependências |
| --- | --- | --- |
| Escolar acadêmico | Faculdade, curso, grade, requisito, matrícula e associações por IDs; dados de biblioteca/eventos conservados no mesmo contexto, sem novos serviços/UI | Domínio puro/Actor/portas/DAO |
| Importação Batch | Ler somente DDL/INSERT permitidos, planejar seco, importar dependências, registrar origem/pendências/estado, replay | Arquivo privado e PostgreSQL próprio |
| Relatórios | Contrato snapshot atual preservado; novas informações só quando requisito e compatibilidade aprovados | HTTP explícito, sem importar JPA escolar |
| PHP legado | Páginas/rotas reais, sessão/CSRF/papel/objeto, adapter PDO PostgreSQL e tratamento de dialeto | Credencial/schema compat próprios, uploads protegidos |
| Operação | Versão PHP correspondente, canais autenticados, backup/restore, HTTPS, quota e publicação | Docker/Render/Neon, sem pagamento autorizado |

### Fluxo de requisições e de dados
**Fluxo de requisição**
- Java mantém transporte/Actor/política/service/domínio/DAO; novos contratos IDs explicitam vínculos. DTO antigo preserva defaults/status e não recebe campo obrigatório silenciosamente.
- PHP autoriza sessão/papel/objeto e CSRF antes qualquer query/arquivo. PDO não pode aceitar SQL arbitrário da request nem depender de rowCount SELECT.
**Fluxo de dados**
- Arquivo privado→parser allowlist→plano seco com 24 tabelas→staging→resolução única de referência→domínio ativo ou pendência privada→contagens reconciliadas.
- Faculdade ausente não é fabricada; vínculo pendente mantém cursos históricos sem invalidar API atual. Novos fluxos acadêmicos completos validam vínculo quando exigido; contrato legado POST Curso {nome} continua aceito como cadastro incompleto, sem obrigar Faculdade silenciosamente.
- Migração primeiro expande entidades/vínculos opcionais antigos; backfill determinístico; constraints novas aplicadas aos dados completos. Nenhum DROP nem alteração V1–V5.

### Modelo de dados (alto nível)
Entidades principais
- Faculdade explícita, distinta de Unidade escolar de cabeçalho existente; não assumir identidade entre ambas sem cadastro. Curso.faculdadeId; GradeCurricular.cursoId; RequisitoFormacao.gradeId e matéria por associação.
- Pessoa perfil ALUNO/PROFESSOR existente; Matricula.alunoId/turmaId, com Curso derivado por Turma.cursoId; não duplicar curso_id na matrícula evita dois cursos contraditórios e atende navegação Aluno→Matricula→Curso. DTO pode projetar cursoId quando necessário, preservando contratos T002.
- Associação matrícula_disciplina expressa Aluno–Matéria no contexto matrícula/oferta, derivando IDs sem colunas duplicadas; backfill preserva participação das matrículas atuais. Disciplina existente já é associação de Professor–Matéria/Turma; reusar professorId, sem criar professor_materia ou vigência não solicitada. RequisitoFormacao é entidade administrativa de GradeCurricular ligada N:N a Matéria por requisito_materia; não é pré-requisito nem motor de conclusão/elegibilidade.
- Livro/Empréstimo, Evento/Participação: conservação privada dos dados/relacionamentos, sem CRUD/UI ou motor de estoque/vagas além de campos do dump. Controle de importação com chave de origem. Tabelas vazias sem equivalência recebem destino privado declarado; nenhum domínio/camada/endpoints novos por completude de catálogo.
Relações
- Grade/requisito distinguem cabeçalho curricular de linha/exigência, preservando curriculo template atual até migração explícita; regra de conclusão de formação não pode ser inventada sem dados/requisito.
- Turma legado vinculada por idClass textual; nunca converter esse valor diretamente em PK. Pessoa relacionada por ID ausente permanece pendente.
Fonte de verdade
- Banco escolar normalizado para Java; schema/base de compatibilidade para PHP numa implantação adicional explicitamente isolada. Dump é fonte imutável privada; não é database runtime nem é executado. Não declarar sincronização entre interfaces.


Mapeamento completo da fonte observada (destino proposto, detalhamento de campos no FDD)
| Tabela origem | Linhas | Destino/decisão e pendência |
| --- | ---: | --- |
| arquivos | 0 | Material durável e vínculo oferta/turma/autor; referência de disco privada, conteúdo ausente não fabricado |
| atividades | 0 | Destino preparação privada; estrutura declarada, sem criar Atividade/Entrega/UI; futuras linhas não suportadas ficam rejeitadas/preservadas com origem |
| calendario | 0 | Evento calendário existente por data/título, sem confundir inscrição/evento público |
| chat | 0 | Mensagem existente e campo visualização a preservar; origem sender/destiny validada |
| curso | 5 | Curso por ID de origem; faculdade nula historicamente até cadastro explícito |
| diario | 0 | Destino preparação privada se sem equivalência comprovada; nenhuma camada administrativa criada sem dados |
| diariocontroler | 0 | Destino preparação privada e estrutura declarada, sem CRUD/controle de diário inventado |
| emprestimolivros | 40 | Conservação privada Empréstimo com referência Livro válida; todos40 usuários ausentes ficam pendentes privados, nunca pessoa fictícia |
| escolar | 0 | Unidade/cabeçalho escolar existente com campos extras explícitos; não inferir Faculdade |
| evento | 7 | Dados Evento com local/vagas/duração conservados em tabela privada ou extensão equivalente, sem interface/motor de vagas |
| gradecurricular | 21 | Oferta Disciplina e relação à Grade/itens; idTurma referencia turmas.idClass textual21/21, materia por nome21/21; idProf vazio21/21→null, não órfão |
| grades | 15 | Cabeçalho GradeCurricular agrupado por nome e itens por matéria; matéria resolve15/15, vínculo Curso é contextual pelas Turmas, quando nome de grade associa múltiplos cursos, vínculo pendente até resolução explícita, nunca escolher o primeiro |
| horaio | 0 | Horário existente; período textual/dia/data exigem mapeamento explícito, nunca duração suposta |
| livros | 5 | Dados Livro/estoque conservados privadamente, data lançamento literal validada |
| materia | 5 | Matéria existente por origem, sem criar disciplinas automaticamente sem oferta |
| matriculados | 0 | Matricula aluno/turma/estado; Curso derivado da turma |
| notas | 0 | Notas quatro bimestres/recuperações por oferta identificada; idTurma sozinho não identifica matéria, ambiguidade futura fica pendente |
| participarevento | 2 | Dados Participação conservados; evento resolve2/2, usuário ausente1/2; cargo/ativação da pessoa restante ainda precisa definição |
| perguntas | 0 | Destino preparação privada; nenhum banco de questões/quiz/endpoints novo; conteúdo futuro não suportado preservado/rejeitado explicitamente |
| presence | 0 | Aula/Presença existente após resolver turma/professor/matéria/aluno e período; fonte sem PK/AUTO exige chave estável de origem quando não vazia |
| tarefas | 0 | Destino preparação privada; reaproveitar avaliação somente com equivalência provada, sem camadas/tarefas novas por tabela vazia |
| tracking | 0 | Destino preparação privada, IP/localização/telefone restritos; nenhum tracking novo ou cópia para logs públicos |
| turmas | 5 | Turma por chave origem e idClass; curso/nome5/5 e grade/nome5/5, quatro nomes grade distintos; ano/período/complemento preservados sem criar Série fictícia; periodo representa Série literal5/5 (dois valores acadêmicos distintos), então Série pode ser derivada por agrupamento exato desses valores de origem com mapa rastreável, preservando serie_id NOT NULL. Período futuro ambíguo fica pendente, não cria Série arbitrária |
| user | 2 | Pessoa e subtipos ALUNO/PROFESSOR somente quando cargo comprovado; cargo0/4 não definido na fonte atual, não importar como SECRETARIA por default; senha/hast privados e não reexpostos |

Fonte SHA256 `100f85215fb1a2cd203c8f9df3a598eef625fb871f927cfa5a1f7151e4706d2a`, 24386bytes, 24CREATE/23PK/23AUTO e nenhumaFK. Presence é tabela sem PK/AUTO; grades possui ambas. Contagens lidas por parser literal privado, nenhuma execução SQL. Total107 registros,14tabelas vazias. Pessoas/dados de origem conservados somente na preparação privada; entidades/aluno/professor podem compartilhar PK=FK Pessoa para preservar IDs consumidos por APIs, sem duplicar identidades. Criar domínio de tabelas vazias somente quando sua semântica estiver definida no FDD; preservar estrutura/rejeitar conteúdo não suportado explicitamente é preferível a scaffold genérico que inventa regra.

### Interfaces públicas
| Nome | Tipo | Protocolo | Exposição | SLAs/Limites |
| --- | --- | --- | --- | --- |
| APIs/React escolar existentes | API/UI | REST/HTTP local, HTTPS remoto | Perfis existentes | Status/defaults preservados |
| Acadêmico novo | API | REST | Perfil/objeto autorizado | Endpoints necessários ao modelo definidos no FDD, sem CRUD biblioteca/eventos especulativo |
| PHP páginas/ajax/PDF | UI/API | HTTP local/HTTPS remoto | Sessão/papel/objeto/CSRF | Rotas de versão fixada e upload limitado |
| Importação | Operação/Batch | Execução autenticada privada | Operador autorizado | Tamanho/gramática/chunk/timeout definidos e testados no FDD |

### Considerações de escalabilidade e disponibilidade
Abordagem geral
- Importação paginada/chunks com restart e reconciliação; uploads duráveis PG já existentes; PHP pode reutilizar armazenamento compatível privado sem webroot.
Técnicas aplicadas
- Limites de payload/carga e pools justificados pelo ambiente real; sem scaling/componente novo por conveniência.
Meta de disponibilidade
- Sem SLA inventado/free quota presumida; health e falha/cold start observados.

### Segurança
Autenticação
- Tratamento de cargos0/4 e passwords históricas depende de fonte comprovada; contas desconhecidas não viram secretaria por default. Nenhuma credencial pública/demo derivada do dump.
Autorização
- Gate global e por objeto nos AJAX/PDF/uploads, CSRF em mutação, perfil permitido explicitamente; negação por padrão para papel não interpretado.
Proteção de dados
- Dump chmod0600 e fora Git; staging privado por schema/role; arquivos servidos attachment depois autorização, nunca executáveis no webroot; nomes/CPF/email/senhas não aparecem em artifact.
Gestão de segredos
- Env privado/provedor, rotação/redefinição privada se necessária, logs redigidos. Neon TLS verify-full conforme ADR005, handshake ainda não executado.

### Observabilidade
Logs
- Digest, tabela, contagens, estado/chave de execução; sem valores de linhas/PII/credenciais.
Métricas
- Batch lidos/importados/pendentes/rejeitados e estado; métricas atuais autenticadas preservadas.
Tracing
- Java W3C atual preservado; não prometer tracing PHP sem implementação observada.
Dashboards e alertas
- Nenhum coletor novo; logs/Actuator existentes e health PHP mínimo sem detalhes sensíveis.

### Riscos arquiteturais e mitigação
#### Fonte PHP e dump incompatíveis
- **Probabilidade:** alta, divergência observada.
- **Impacto:** login/query falha ou vínculo errado.
- **Mitigação:** localizar versão correspondente, caracterizar SQL/rotas e fixar commit; adaptar explicitamente quando impossível.
- **Plano de contingência:** bloquear publicação até demonstração de fluxo real, não entregar mock como legado.
#### Órfãos e atributos sem semântica comprovada
- **Probabilidade:** alta, dump sem FK e referências ausentes.
- **Impacto:** corrupção ou autorização indevida.
- **Mitigação:** pendência privada/replay, nunca associação aproximada por nome ou pessoa sintética.
- **Plano de contingência:** importação parcial auditada, origem integral preservada.
#### Interfaces divergentes escrevem dados distintos
- **Probabilidade:** média.
- **Impacto:** usuários supõem sincronização inexistente.
- **Mitigação:** bases/roles/modos claros, não publicar ambas como fonte única sincronizada.
- **Plano de contingência:** selecionar implantação ativa por público, conservar refatorado intacto.

### ADRs e próximos passos
ADRs associados
- ADR006 modelo/importação; ADR007 PHP isolado; ADR001/002/004/005 continuam vigentes fora da exceção específica.
Decisões pendentes
- Versão PHP real, semântica cargos/estados, instituição ausente, canal gratuito autenticado Render/Neon. PHP compat base não garante resolver dump órfão.
Próximos passos
- Revisores conferem proposta; implementador escreve FDD/matriz/schema/contratos; planejador aprova FDD independente e cria task somente após cadeias prontas. RFC/LLD dispensados pelo recorte; domínio biblioteca não exige novo serviço.

Refinamento pré-FDD2026-10-05: novos testes necessários incluem colisão de IDs de origem com bootstrap existente, digest/replay107 contabilizados, cabeçalho Grade agrupado sem duplicação e sem escolher Curso ambíguo, duas Séries derivadas dos períodos literais5/5, mismatch schema/PDO e prepared statements com valores adversários. Campos/cargos/desconhecidos não bloqueiam modelagem local quando mantidos pendentes sem privilégios.
