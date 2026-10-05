---
type: minerva-fdd
project: Atena
date: 2026-10-04
status: aprovada
tags: [atena, fdd]
ai-first: true
---

### FDD: Qualidade estrutural e publicação durável do Atena

Versão: 1
Data: 2026-10-04
Responsável: implementador Severino

### Para o futuro agente
Documento de autoria do implementador, submetido ao planejador/revisor antes de produção. Deriva do PRD-002, ADR004/005 e HLD de qualidade/produção; proposta não prova implementação. Preservar contratos T-001 e regras escolares. Entrevista/pausa humana são sobrepostas pela autonomia expressa do prompt; ambiguidades seguem a decisão simples registrada. Nenhuma publicação será declarada com Neon MCP não autenticado.

### Origem
Pedido de qualidade e produção de 2026-10-04; [PRD-002](../prds/prd-002-qualidade-producao.md), [ADR004](../adrs/adr-004-organizacao-backend.md), [ADR005](../adrs/adr-005-producao-render-neon.md), [HLD](../hlds/hld-qualidade-producao.md), [Task T-002](../tasks/t-002-qualidade-producao.md). Referência MinervaFinancas ADR003, commit 34fc98a6ac47f2ffc2bf23f055a3fd9ac15c7530.

### 1. Contexto e motivação técnica
Baseline T-001 está integrado e tem evidências reais, mas controllers comprimidos expõem entidades JPA; Access/Invariants concentram múltiplos tipos com instanceof e HTTP/persistência. Exportação, snapshot e jobs misturam responsabilidades. T-002 reorganiza os dois serviços existentes, sem importar entidades entre contextos, além de preparar durabilidade de arquivos para Render/Neon. Usuários continuam SECRETARIA/PROFESSOR/ALUNO, com mesmos direitos e limites.

### 2. Objetivos técnicos
- REST só recebe/devolve DTO explícito e delega Actor; não acessa JPA, regras ou arquivos.
- Domínio e Service de regra pura não importam Spring, JPA, Servlet, Jackson ou transporte; Actor coordena portas e transações, DAO adapta persistência/HTTP.
- Pacotes por recurso `{rest,actor,service,builder,dao,helper,dto,dominio}`; entidades/repos JPA em `dao/jpa`; `comum` apenas para responsabilidade compartilhada concreta.
- Nomes pt-BR completos, interfaces prefixo I, DTO/Enum/IntegracaoTeste/Teste. Código multilinha, sem imports wildcard ou métodos comprimidos.
- Factories apenas para variantes reais: políticas de perfil, consolidação SOMA/MEDIA e exportadores PDF/CSV/JSON; construção válida de nota/resultado quando protege invariantes reais. Não criar factories CRUD, hierarquia genérica de persistência ou reflexão.
- Preservar rotas, status, campos JSON/defaults, permissões, cálculo, jobs e limites atuais, comprovados por execução após alterações.

### 3. Escopo e exclusões

**Incluído**
- Recursos escolar: pessoa/sessão/avatar, curso/série/matéria/unidade/currículo/turma/disciplina/matrícula, horário/aula/presença, avaliação/nota-avaliação/nota/consolidação, boletim, material, evento/mensagem, catálogo/listas internas/jobs/proxy relatórios.
- Relatórios: lançamento/consulta, snapshot HTTP persistido, reader/writer Batch, formatos de saída e metadados; arquivos/campos de domínio puro e DTOs explícitos.
- React/JS/CSS/manifestos fonte legíveis; componentes coesos de sessão, navegação, formulário de recurso, boletim, relatórios e perfil, sem redesenho nem biblioteca de UI.
- Persistência BYTEA de material e avatar, migração aditiva compatível, retropreenchimento controlado de arquivos locais e recuperação/exportação verificável.
- Perfil produção TLS/cookies/pools/forwarded headers, segurança pública e publicação por Jarvis quando ferramentas e plano gratuito permitem.
- AUDIT_REPORT.json atualizado por marco e gate mínimo de parsing/campos; EXPERIMENT_LOG cronológico, espelhos/base exigidos.

**Excluído**
- Nova função escolar, stack/serviço/fila/cache externo, importação de dump ausente, novo contrato de paginação/i18n/OpenAPI/status.
- Camadas sem comportamento, interfaces triviais sem fronteira, wrapper de todo primitivo, percentuais de cobertura inventados.
- Compra de plano, alteração dos serviços existentes de terceiros no Render, publicação sem autenticação Neon ou sucesso alegado sem teste.

### 4. Fluxos detalhados e diagramas
**Fluxo principal**
1. REST valida forma do DTO; Actor recebe requisição/identificadores, obtém usuário puro via adaptador `IUsuarioAtual`, aplica política tipada e consulta portas específicas.
2. Builder traduz DTO em domínio; domínio protege invariantes por construção. Service calcula regra pura com dados prontos (prazo, recuperação, presença, soma/média), sem carregar banco.
3. Actor executa operação transacional quando necessário. DAO JPA mapeia domínio/entidade e traduz falhas de infraestrutura em erro de aplicação seguro; Builder prepara resposta DTO, preservando nomes/defaults.
4. Policies por perfil expõem operações tipadas (pessoa, turma, disciplina, aluno, nota etc.) com responsabilidades claras. Factory seleciona perfil uma vez; não existe dispatch Object/instanceof nem código de tipo por recurso. Consultas relacionadas pertencem portas específicas, com ids entre recursos. A seleção por perfil não cria interface gigante para todos os recursos: colaboração de capacidades básicas por perfil e políticas específicas de nota/ensino/pessoa protegem responsabilidades coesas.
5. BoletimActor carrega dados por portas; PrepararBoletimService aplica política de visibilidade e cálculo em projeção de domínio pura, e BoletimBuilder somente converte a projeção pronta para snapshot DTO. Fonte continua REPEATABLE_READ; professor só suas disciplinas e documento vazio, aluno só próprio/liberadas.
6. Escola Batch usa leitor matrícula paginado e writer que chama caso de uso de preparação de quatro notas ausentes; jobs/names/chunk50/idempotência preservados. Report Batch usa snapshot imutável persistido e upsert por report/numero; snapshot é gravado em transação REQUIRES_NEW antes das linhas do chunk, porque RED de T-002 demonstrou que o rollback do writer em T-001 anulava o snapshot e reconsultava origem mutável. reader/writer extraídos de configuração para unidades coesas.
7. Report REST delega lançamento/consulta/download a actors. ExportacaoFactory seleciona PDF/CSV/JSON concretos; metadados e dados persistidos não dependem do formato. PDF preserva nomes/acentos/CNPJ/CPF/matrícula/autorização atuais.
8. Upload valida tamanho/extensão/assinatura; avatar é reencodificado antes de persistir. Conteúdo BYTEA é fonte durável; nome UUID e metadados permanecem, download attachment com autorização. Leitura compatível de arquivo local existe para registros antigos sem bytes; retropreenchimento idempotente migra sem excluir arquivo original.

9. React separa componentes concretos, cliente HTTP e área de dados por geração de autenticação. Logout/troca de conta desmontam a área, descartando resultados, cadastros, jobs, referências e callbacks antigos; mudança de senha encerra sessão e retorna ao login. Cliente HTTP trata corpo vazio 200 como null e rejeita JSON inválido não vazio. Dois problemas do baseline foram demonstrados/corrigidos; validação isolada do cliente e navegador real de logout/troca de conta/senha passaram.

**Fluxos alternativos e exceções**
- Professor modifica só valor/recuperação de nota existente dentro da liberação/prazo; metadados protegidos, secretaria controla período e aluno só lê publicado.
- Factory SOMA/MEDIA recebe ao menos duas avaliações; cap100 soma, média pelo número de avaliações. Avaliações originais permanecem.
- Perfil/senha/avatar passam DTO específico, sem campos de credencial no output nem mass assignment. Default ativo=true e avatarPresente read-only mantidos em GET/session, login e Pessoa, com as mesmas formas/valores atuais.
- Job report falho reinicia usando snapshot salvo; nenhuma mudança na origem afeta retomada. Jobs recusam excesso com limites de pool/fila atuais; não criar retry não idempotente.
- Render/Neon bloqueados deixam código local utilizável e auditoria partial, sem criar alternativa de fornecedor nem contratar pagamento.

### 5. Contratos públicos (assinaturas, endpoints, headers, exemplos)
**Recursos REST**
- Tipo: endpoint; rotas `/api/{pessoas,cursos,series,materias,unidades,curriculos,turmas,disciplinas,matriculas,horarios,aulas,presencas,avaliacoes,notas-avaliacoes,notas,eventos,mensagens,materiais}` mantêm métodos e campos T-001. POST201, GET/PUT200, DELETE204; person Input continua `{pessoa:{...},senha:...}` e id não permitido na criação.
- Unknown properties rejeitadas; campos opcionais/defaults mapeados explicitamente. Beans de validação ficam DTO, invariantes repetidas no domínio somente para caminhos internos distintos, não duplicação ritual.

**Sessão e segurança**
- GET `/api/session` retorna `{csrfToken,user?}`; login POST `/api/session/login` `{email,senha}` retorna PessoaRespostaDTO compatível. Header `X-CSRF-TOKEN` real e cookie de token preservados; logout/password encerram sessão atual. Produção usa Secure/HttpOnly/SameSite e TLS; localhost mantém comportamento local.
- `X-Service-Token` exigido em todas rotas internas de ambas apps, comparação constante e segredo externo de pelo menos32 caracteres; endpoints públicos Render nunca usam rede como autorização.

**Snapshot entre contextos**
- GET `/api/internal/snapshot/{alunoId}?releasedOnly=false&professorId=...`; DTO externo não importa domínio de outra app.
- Resposta: `{alunoId,nome,matricula,documento,unidade:{nome,endereco,telefone,cnpj},linhas:[{turma,ano,disciplina,disciplinaId,cargaHoraria,dependencia,notas:[null,null,null,null],recuperacoes:[null,null,null,null],faltas,resultado:{media,parcial,frequencia,situacao}}]}`. Sem unidade mantém exatamente `{}`, não null nem campos preenchidos com null; quatro posições de notas/recuperações preservam nulls. Professor documento vazio. Listas `/api/internal/listas/{tipo}` preservam payload atual e tipos LISTA_ALUNOS/LISTA_PROFESSORES/LISTA_TURMAS.

**Jobs e downloads**
- Job `inicializarNotas`, `consolidarHistorico`, parâmetros e endpoints T-001 mantidos; reportrequestId idempotente por owner, GETstatus e download `?format=pdf|csv|json` preservados.
- PDF/CSV/JSON retornam bytes content-type e attachment existentes; relatório não concluído409, formato400, inexistente404. Joblaunch retorna202; nenhuma mudança de estado observável por reorganização.
- Materiais máximo10MB tipos PDF/DOC/ZIP/RAR/PNG/JPEG; avatar máximo5MB e4096px. Bytes/decode/signedcontent mesmos critérios T-001, sem descompactar/executar e sem antivírus prometido.

### 6. Erros, exceções e fallback
- ErroAplicacao puro carrega categoria/status seguro; adaptador REST mapeia contrato atual de ApiErrors sem expor query, stack, caminho ou dado de outro usuário. DAO traduz constraint/conflito e ausência quando cabível, preservando causa interna.
- 400 formato/validação;401 ausente/inativo;403 perfil/objeto;404 recurso;409 prazo/conflito/reportpendente;500 mensagem genérica; downstream proxy mantém status seguro.
- HTTP interserviços connect5s/read30s, sem retry não idempotente. Não adicionar breaker/fila/circuito sem requisito e decisão nova.
- Migração V5 aditiva BYTEA não remove colunas nem arquivos legados. UP testado com bytes existentes/novos; restauração/exportação anterior ao DOWN assegura cópia recuperável dos bytes, e rollback de app deve continuar lendo conteúdo durável. Não rodar DOWN destrutivo em produção. Contagens/hash e consulta autenticada verificam integridade. Se reversão exata não for provada, registrar rollback não validado e impedir declaração de segurança de reversão.

### 7. Observabilidade
**Métricas**
- Actuator/Micrometer/Batch existentes; health público sanitizado, métricas autenticadas/secretaria ou serviço conforme app.
**Logs**
- Método/rota template/status/categoria/trace sem payload, query, PII ou segredo; não imprimir connection strings/credenciais na auditoria.
**Tracing**
- Bridge Brave/W3C preservado; RestClient.Builder gerenciado e executor contexto propagado. Validar mesmo trace id escola→report→snapshot após refatoração.
**Dashboards e alertas**
- Nenhum novo backend/coletor/painel externo; evidências de health/logs/metrics do ambiente realmente executado.

### 8. Dependências e compatibilidade
| Componente | Versão mínima | Observações |
| --- | --- | --- |
| Stack T-001 Java/Spring/PostgreSQL/React/Batch/PDFBox | versões docs/lib.md | Sem upgrade copiado de MinervaFinancas; usar soluções Spring antes de biblioteca nova. |
| Render/Neon MCP | capacidades efetivamente descobertas | Autenticação/quota/TLS ainda bloqueantes da execução remota. |
| Ferramenta de formatação/quality | definir em docs/lib antes adoção | Preferir ferramenta externa temporária sem dependência runtime; gate deve ser verificável, não estética alegada. |
**Garantias de compatibilidade**
- Mesmo schema V1V4 e contratos de fonte T-001; V5 apenas expansão bytes. Sessões podem exigir novo login após deploy/restart como já ocorre; senhas BCrypt nunca expostas/migradas para formato inseguro.
- Testes existentes adaptam nomes/pacotes sem alterar assertions de campos/defaults/status/comportamento; não afrouxar a suíte para aceitar regressões; nomes Teste/IntegracaoTeste requerem include Surefire explícito para não falsoverde.

### 9. Critérios de aceite técnicos
- Revisor lê recurso inteiro por diretório, domínio puro e controller fino; nenhum dispatch instanceof/reflection por recurso e nenhuma entidade JPA em contrato REST.
- Inputs/outputs explicit DTO preservam campos/defaults/rotas/status/perfis; factory cobre variantes concretas reais e não construtores triviais.
- Escola e relatório builds/testes reais PostgreSQL, domínio/factories relevantes, todo endpoint e CSRF wire real, jobs idempotentes/falha/restart passam após refatoração.
- V5 conteúdos legados/novos persistidos e leitura após reinício são verificados; reversão só declarada com restore efetivo.
- React build e navegador desktop/mobile com errosJS/overflow/fluxo completo/capturas conferidas, tracing entre serviços aprovado.
- Auditoria JSON schema/campos/inventário/evidências atualizados por marco; nenhuma métrica externa inventada. Pipelinesreview/testes verdes e revisão independente antes merge por Jarvis.
- Publicação HTTPS Render/Neon, login/fluxo/reinício/arquivos verificados ou requisito marcado não atendido com bloqueio de ferramenta/auth/quota real.

### 10. Riscos e mitigação
### Mapeamento DTO/segurança
- **Probabilidade:** alta
- **Impacto:** perda de campos/defaults ou exposição de informação.
- **Mitigação:** caracterização T-001, mapeamento explícito, negativas de perfil/id/CSRF, reviewer independente.
- **Plano de contingência:** bloquear publicação, corrigir contrato ou reverter versão sem remover dados.
### Migração e limite gratuito
- **Probabilidade:** alta
- **Impacto:** perda de arquivos, aumento do PostgreSQL, publicação bloqueada.
- **Mitigação:** V5 aditiva/backup/hash/retropreenchimento, pools limitados, zero contratação, readonly discovery.
- **Plano de contingência:** Docker local continua disponível e auditoria registra partial; não substituir Neon nem inventar execução.
### Abstrações demais
- **Probabilidade:** média
- **Impacto:** custo de navegação e regra escondida.
- **Mitigação:** factory>=2variantes, ports de fronteira declarados, service apenas quando regra pura existe, helper comum apenas reutilização efetiva.
- **Plano de contingência:** remover abstração sem responsabilidade e simplificar antes gate final.

### Revisão independente do contrato
2026-10-04: planejador/revisor leu FDD, origem/contratos T-001, PRD/HLD e ADR004/005. Aprovado para implementação incremental local: DTO/defaults/unidade{} e arraysnull preservados, Actor I/O/Service puro, JPA sóadaptador, factories concretas, Batch/snapshot/bytes e provas adequadas. Autoria do implementador mantida. Aprovação do contrato não declara implementação/testes/produção validados; Neon configurado não autenticado e criação Docker via MCP Render não disponível bloqueiam publicação remota. Redações de planejamento passaram revisão independente de implementador/report; ajustes precisão aplicados.

Categoria: FDD. Pergunta: quais contratos, variantes, defaults, invariantes e migração implementam PRD/HLD aprovados? Anteriores PRD002/HLD/ADR004–005; posterior T002. Dono implementador; revisor planejador independente. RFC/LLD dispensados: contratos concretos aqui detalhados e não há camada de desenho adicional necessária.
