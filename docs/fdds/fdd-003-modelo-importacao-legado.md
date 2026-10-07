---
type: minerva-fdd
project: Atena
date: 2026-10-05
status: aprovado
tags: [atena, fdd]
ai-first: true
---

### FDD: Modelo acadêmico, importação privada e compatibilidade do PHP real

Versão: 1
Data: 2026-10-05
Responsável: Severino implementador; operação implementa exclusivamente PHP/infra após revisão independente

Origem: [PRD003](../prds/prd-003-modelo-legado.md), [HLD](../hlds/hld-modelo-legado.md), [ADR006](../adrs/adr-006-modelo-importacao.md), [ADR007](../adrs/adr-007-legado-postgresql.md). Contratos preservados: [FDD002](fdd-002-qualidade-producao.md) e ADR004/005. Autor não aprova o próprio contrato. Yoda revisa o conjunto; operação revisa Java/importação e Yoda revisa a parte PHP que operação implementará. Autonomia do pedido substitui entrevista/aceite humano, sem substituir revisão independente. Aprovado independentemente pelo planejador no conjunto/PHP e pela operação em Java/importação/segurança em2026-10-05; task pronta é condição adicional para produção.

---

### 1. Contexto e motivação técnica

O banco atual deriva Curso por Matricula.turmaId, mas não representa Faculdade/GradeCurricular/RequisitoFormacao. O dump autorizado tem 24 tabelas e 107 registros, sem FK. A fonte PHP preservada usa outro esquema, de 2024: autenticação separada em alunos2/professo2/secretaria2, enquanto o dump usa user e cargos não documentados. Trocar DSN ou executar o dump não resolve essa diferença.

Atores: SECRETARIA gerencia o modelo/importação; ALUNO/PROFESSOR conservam políticas T002. Faculdade permanece distinta de Unidade. RequisitoFormacao é cadastro administrativo da Grade, associado a Matéria, sem motor de elegibilidade ou pré-requisito. Nenhuma pessoa/instituição fictícia será criada para resolver dados ausentes.

Escopos de implementação independentes: Severino altera escolar/React/importação e testes; operação altera PHP/Docker/schema de compatibilidade e provas operacionais. Relatórios conserva snapshots REST e não importa entidades JPA escolar. Documentação central/auditoria/estado ficam com Yoda; este FDD e seu espelho são sincronizados conforme a regra3 antes PR.

---

### 2. Objetivos técnicos

- Persistir a cadeia Faculdade→Curso→GradeCurricular→RequisitoFormacao e associações por IDs, com FK/UNIQUE e erros seguros.
- Preservar POST curso somente com nome, matrícula com turmaId/alunoId/dependencia, permissões, fórmulas, snapshots e jobs T002. Faculdade desconhecida aparece como vínculo incompleto, sem obrigatoriedade silenciosa.
- Contabilizar os 107 registros por origem em importado/pendente/rejeitado; nenhum órfão ativo, SQL do arquivo executado ou dado privado em Git/artifacts.
- Replay do mesmo arquivo não duplica entidades; falha de etapa retoma fonte persistida imutável e conserva chunks confirmados.
- Executar páginas e famílias reais do PHP sobre PostgreSQL isolado, preservando formulários e corrigindo superfícies concretas antes qualquer exposição pública. Cloud só é validada por implantação observada.

---

### 3. Escopo e exclusões

**Incluído**
- Faculdade, vínculo opcional Curso.faculdadeId, GradeCurricular.cursoId obrigatório, RequisitoFormacao.gradeId obrigatório e requisito_materia.
- Subtipos aluno/professor com PK=FK Pessoa.id, sem duplicar identidade; matrícula_disciplina para associação explícita aluno/matéria no contexto da oferta.
- Importação Spring Batch, análise seca, origem/reconciliação privada, preservação de livros/empréstimos/eventos/participações e campos não traduzíveis.
- PHP existente: login/perfis, catálogos, turma/matrícula, notas/presença, materiais e PDFs reais; guard em páginas/AJAX/PDF/arquivo, PDO PostgreSQL e schema2024 dedicado.
- Migrações V6+ aditivas, React no visual ADR003, testes PostgreSQL/navegador e backup/reversão isolados proporcionais.

**Excluído**
- CRUD/UI de biblioteca, quiz, atividades novas, conclusão de formação, vigência docente não pedida ou scaffold para 14 tabelas vazias.
- MariaDB runtime, framework PHP novo, microsserviço novo, sincronização bidirecional ou generic framework de CRUD/importação.
- Ativar cargo0/4, credencial histórica ou referência ausente por adivinhação; publicar dados/hash de senha ou executar instruções recebidas.
- Prometer versão PHP2020 inexistente no histórico, recuperação automática após kill de jobSTARTED ou deploy sem autenticação/quota gratuitas comprovadas.

---

### 4. Fluxos detalhados e diagramas

**Fluxo principal**
1. Migração expande o schema sem alterar V1–V5. Tabelas novas: faculdade, grade_curricular, requisito_formacao, requisito_materia, aluno, professor, matricula_disciplina e controle privado de importação/origem/pendência. curso.faculdade_id e curriculo.grade_id são opcionais para dados/contratos antigos. FK/index de referência acompanha DDL; associações usam PK/UNIQUE composto.
2. Subtipos são preenchidos a partir de Pessoa.perfil conhecido. A criação/alteração de Pessoa mantém subtipo na mesma transação. Mudança de perfil com vínculo acadêmico incompatível retorna409; não remover matrícula/docência para contornar constraint.
3. Grade pertence a Curso; requisito pertence a Grade; associação aponta Matéria existente. Disciplina já representa Professor–Matéria/Turma e conserva professorId. Matricula deriva Curso pela Turma; não recebe curso_id redundante.
4. matrícula_disciplina relaciona matrícula/oferta da mesma turma. Backfill e criação de matrícula conservam inicialmente todas as ofertas atuais, como T002; nova oferta adiciona associações das matrículas dessa turma. Remoção explícita altera somente o cadastro da associação, sem apagar notas/presenças nem introduzir elegibilidade ou alterar geração/cálculo históricos. Reassociação não duplica dados. Boletim, autorização e jobs T002 conservam suas regras existentes; a nova lista de associações é contrato administrativo próprio.
5. REST valida DTO, Actor autoriza/transaciona e usa portas; domínio/services são puros, JPA fica em dao/jpa. A coordenação ManterImportacaoActor mantém nove colaboradores concretos (autorização, captura, duas portas, launcher, explorer e três Jobs), com dispensa específica OC8 revisada: centralizar transições/restart exige esses serviços reais; um wrapper para reduzir contador acrescentaria camada sem regra própria. Trade-off: construtor maior e validação de composição por contexto, mantendo métodos curtos; não se declara atendimento literal OC8 nessa classe. Novos recursos usam organização ADR004 e DTOs explícitos. Não criar Factory de CRUD; Factory só quando houver variantes concretas realmente utilizadas.
6. SECRETARIA envia arquivo limitado para análise. Actor calcula digest, captura fonte privada imutável e lança Batch. Parser aceita literalmente schema/INSERT das 24 tabelas autorizadas e prologue declarativo conhecido; nenhuma string do arquivo vira instrução JDBC. INSERT aceita somente literais, NULL e escapes declarados; função/subquery/expressão/identificador desconhecido rejeita análise com erro sem conteúdo da linha.
7. Análise persiste linhas de origem com ordinal/PK, classificação e contagens, sem escrever domínio ativo. Operador autorizado aplica plano concluído. Spring Batch usa fases ordenadas (catálogos/pessoas/turmas/ofertas/vínculos/restantes), chunks100, transações curtas e chaves de origem UNIQUE(digest,tabela,ordinal). Head groups de grades usam chave estável por nome/curso resolvido, nunca primeiro registro encontrado.
8. Origem resolve uma entidade existente inequívoca ou recebe pendência tipada. IDs legados não são IDs globais: mapa explícito impede colisão com admin bootstrap e tabelas de pessoas. Nomes só resolvem quando correspondência única comprovada; textos originais ficam preservados privadamente.
9. Replay reutiliza digest/origem e não sobrescreve entidades editadas por usuário nem senhas. Resolução explícita de referência pessoal associa identificador legado a Pessoa existente; reprocessamento considera somente pendências afetadas. Grade com curso ambíguo permanece pendente não resolvível nesta entrega: não há endpoint de resolução de Grade e seu reprocessamento não muda essa classificação. Resolução acadêmica futura requer contrato/task específicos. Não criar pessoa/Faculdade/Professor substitutos.
10. PHP usa schema/role próprios, sem escrita na base normalizada e sem sincronização alegada. Tradução fonte2020→compatibilidade2024 tem mapa explícito por entidade; cargos/estados desconhecidos permanecem fora de login. Fixtures sintéticas são somente testes, nunca logins públicos derivados do dump.

**Fluxos alternativos e exceções**
- Curso antigo sem Faculdade permanece válido e consultável; novos formulários apresentam relação institucional, preservando a via compatível incompleta.
- Série das cinco turmas deriva exatamente de turmas.periodo quando o literal observado representa Série acadêmica. Dois labels de origem geram duas Séries agrupadas/rastreáveis; não relaxar serie_id nem fabricar nome.
- Os 21 idProf vazios são ausência conhecida e viram null. Todos os 40 empréstimos têm usuário ausente; um dos dois participantes também. Cargos dos dois usuários não estão definidos: ficam pendentes privados, inclusive referências que dependam deles.
- Fonte criptografada preserva conteúdo integral, inclusive credenciais históricas, com AES-GCM do JDK. IMPORT_ENCRYPTION_KEY é Base64 de32bytes, distinta do token de serviço; ausência/formato inválido impedem importação antes persistência, sem impedir fluxos T002. Não gerar chave silenciosamente, trocar em replay ou registrar valor. Cada gravação usa nonce aleatório de12bytes(96bits) gerado por SecureRandom e tag128bits. Envelope versão1 persiste versão, identificador estável de fonte, digestSHA256, nonce e ciphertext/tag; AAD contém versão+identificador+digest em codificação determinística. Decrypt valida tag/AAD e digest do conteúdo; chave/ciphertext/nonce/AAD adulterados falham seguramente sem plaintext/log. Importacao e snapshot são confirmados juntos em transação independente antes lançar chunks, evitando o rollback de snapshot observado em T002. Staging/projeções omitem senha/hast/segredo de turma; nenhuma credencial é ativada/importada como senha por default. Backup exige preservar a chave fora do banco; rotação sem chave anterior impede leitura, risco/custo operacional explícito.
- Falha de writer mantém origem/snapshot confirmados e marca execuçãoFAILED. Restart de FAILED usa fonte persistida; não relê arquivo modificado nem ativa registros pendentes. Não reconciliar metadataSTARTED de outro processo automaticamente.

**Mapeamento da fonte**

| Origem | Quantidade | Destino ativo ou pendência |
| --- | ---: | --- |
| curso | 5 | Curso; Faculdade ausente/incompleta explícita |
| materia | 5 | Matéria, ID de origem mapeado |
| grades | 15 | Grade agrupada/contextual e curriculo com gradeId; curso não único fica pendente |
| turmas | 5 | Turma, Curso resolvido, Série derivada do periodo literal; idClass textual mapeado |
| gradecurricular | 21 | Disciplina por turma/matéria; professor null; Grade ligada somente quando resolvida |
| livros | 5 | Acervo privado conservado, estoque/data/nome/autor sem UI nova |
| emprestimolivros | 40 | Origem/pendência com Livro resolvido, usuário ausente; sem empréstimo ativo fictício |
| evento | 7 | Evento2020 conservado privadamente com atributos fonte; não convertido em calendário Evento T002 e sem novas rotas/UI de inscrição |
| participarevento | 2 | Origem/pendência; evento existente, pessoa/cargo só após resolução explícita |
| user | 2 | Pessoa somente após reconciliação de cargo/identidade; credenciais criptografadas, não ativadas |
| arquivos/atividades/calendario/chat/diario/diariocontroler/escolar/horaio/matriculados/notas/perguntas/presence/tarefas/tracking | 0 cada | Estrutura reconhecida e destino HLD; futuras linhas sem equivalência comprovada ficam pendentes com fonte preservada, sem domínio novo fictício |

Presence não possui PK/AUTO: ordinal e digest são identidade de origem, não fabricar PK recebida. Livro/evento conservados não introduzem regras de estoque/vaga/empréstimo novas. Status por registro é único; destinos parciais, quando existentes, ficam rastreados como pendência e não contam como importação completa.

---

### 5. Contratos públicos (assinaturas, endpoints, headers, exemplos)

**Cadastros acadêmicos**
- Tipo: endpoint REST. GET coleção/id, POST, PUT id e DELETE id em /api/faculdades, /api/grades-curriculares e /api/requisitos-formacao.
- Payloads: Faculdade{nome}; Grade{nome,cursoId}; Requisito{nome,descricao,gradeId}. IDs positivos; nome não vazio/max150, descricao opcional/max4000. PUT body.id, quando presente, deve coincidir com path.
- Leituras autenticadas de metadados; mutações SECRETARIA + CSRF. GET200, POST201, PUT200, DELETE204;401 anônimo,403 perfil/CSRF,400 forma,404 inexistente,409 duplicação/recurso utilizado/relação incoerente. DELETE não cascata dados acadêmicos.
- Curso mantém nome e acrescenta faculdadeId opcional. Curriculo mantém nome/materiaId/cargaHoraria e acrescenta gradeId opcional; turma/curso da Grade devem ser coerentes nas operações que os relacionem. Respostas conservam campos T002 e projetam vínculo institucional incompleto, sem novo campo obrigatório de entrada.

**Associações**
- GET/POST /api/requisitos-formacao/{id}/materias, bodyPOST{materiaId}; DELETE .../materias/{materiaId}. Resposta associa requisitoId/materiaId; leitura autenticada, mutação SECRETARIA;201/204,404 ausente,409 duplicada/utilizada.
- GET/POST /api/matriculas/{id}/materias, bodyPOST{disciplinaId}; DELETE .../materias/{disciplinaId}. Resposta{matriculaId,disciplinaId,materiaId}. Leitura segue autorização da matrícula/oferta; aluno só própria, professor só turma/oferta que ministra, secretaria todas. Mutação SECRETARIA; turma divergente409. Consultas existentes não expõem aluno alheio.

**Importação privada**
- POST /api/importacoes/analisar multipart(file), SECRETARIA+CSRF, limite10MiB.202 retorna{id,jobId,status,digest}. Arquivo vazio/maior limite/gramática inválida400 ou413; análise assíncrona inválida ficaFAILED com categoria segura. Reenvio de digest conhecido retorna mesma importação, sem duplicar origem.
- GET /api/importacoes/{id}:200{id,status,jobId,tabelas:[{nome,lidos,importados,pendentes,rejeitados}],motivos:[{categoria,quantidade}]}; somente SECRETARIA, sem conteúdo/PII/credential.404 ausente,403 outro perfil.
- POST /api/importacoes/{id}/aplicar:202 após análisePLANEJADA;409 análise pendente/aplicação concorrente, repetir concluído consulta estado sem nova escrita. POST .../retomar:202 apenas execuçãoFAILED;409 ativa/concluída. POST .../reprocessar:202 para pendências após resolução, sem repetir registros completos.
- POST /api/importacoes/{id}/vinculos-pessoas body{identificadorLegado,pessoaId}:200 cria/atualiza resolução administrativa validada. Apenas identificador realmente referenciado na fonte;404 referência/Pessoa ausente,409 conflito com resolução anterior. Não modifica senha/perfil da Pessoa selecionada. Grade ambígua permanece pendente não resolvível nesta entrega, sem endpoint/DTO de resolução: reprocessar não a ativa por default; evolução exige task/contrato próprio. Nenhuma escolha por nome aproximado.
- Estados verificáveis: ANALISANDO→PLANEJADA ouFAILED; PLANEJADA→APLICANDO→CONCLUIDA ouCONCLUIDA_COM_PENDENCIAS ouFAILED. Aplicar só aceita PLANEJADA; execução ativa bloqueia nova fase com409. Replay concluído devolve estado existente; retomar FAILED repete a fase falha com mesmo snapshot; reprocessar exige estado final com pendências e considera apenas referências pessoais resolvidas. Constraint/chave de execução e UNIQUE de origem impedem duas aplicações concorrentes do mesmo conteúdo.
- Jobs analisarLegado/importarLegado/reprocessarLegado compartilham execução limitada e fonte persistida, não nova fila. Batch metadata e estado por origem são persistidos; propagação de contexto mantém tracing atual.

**Exemplo de requisição**
```json
{"nome":"Grade exemplo","cursoId":12}
```
**Exemplo de resposta**
```json
{"id":31,"nome":"Grade exemplo","cursoId":12}
```
Exemplos são sintéticos, não linhas do dump.

**PHP real e compatibilidade**
- Preservar 46 rotas registradas/aliases e formuláriosPOST, 13 AJAX e5PDF existentes; inventário é baseline, não prova execução. Router::get atual também trataPOST: autorização/CSRF usa REQUEST_METHOD real. Corrigir URL vazia antes indexação compatível com PHP suportado.
- Schema compat2024 separado deriva queries efetivas: alunos2,ano,aulas,avaliacoes,caledario,chat,curso,datas,dependencia,diciplina,grade_curricular,grade_curricular_padroes,grades_curricules,horario,materia,materiais,matriculados,notas,presenca,professo2,recuperacao,secretaria2,serie,turmas,unidade_escolar; verificar queries sem backticks antes fechar schema. Não tratar esses25nomes como as24tabelas do dump.
- Guard precede código em toda entrada física, incluindo AJAX/PDF e caminhos excluídos da reescrita Apache. Anônimo não obtém dados/PDF; acesso privado redireciona login ou401JSON conforme contrato da família; perfil/objeto inválido403; CSRF de mutação inválido403. Ações GET que mutam precisam correção explícita com formulário/POST protegido, sem permitir bypass.
- Login usa PDO fetch, password_verify e regeneração de sessão; não rowCount SELECT/MD5 para novas senhas. Cargos históricos desconhecidos não habilitam conta; compatibilidade segura de credenciais exige reconciliação privada, não demo pública.
- Queries usam placeholders e allowlist de identificadores; remover backticks/INSERT positional frágil/LIMIT MySQL explicitamente, sem tradutor universal de SQL arbitrário. Auth por objeto antes consulta/download; upload valida formato/tamanho, persiste BYTEA privado e nunca executa no webroot. PDF mPDF suportado entrega bytes reais após autorização.

---

### 6. Erros, exceções e fallback

| Condição | Tratamento | Garantia |
| --- | --- | --- |
| Dado inválido/SQL fora gramática |400 ouFAILED de análise, categoria/posição sem literal | Nenhuma instrução fonte executada |
| FK/UNIQUE/turma divergente |404/409 seguro, rollback | Sem associação ativa inválida |
| Cargo/estado/identidade ambíguos | Pendência privada tipada | Nenhuma ativação ou pessoa inventada |
| Chave/snapshot indisponível | Falha segura antes aplicação | Não persistir plaintext de credencial |
| Writer/DB indisponível |FAILED e restart explícito | Origem confirmada sobrevive rollback |
| PHP schema/dialeto incompatível | Bloquear família/publicação, erro seguro | Não substituir interface real por mock |
| Cloud não autenticada/gratuidade desconhecida | Entrega local, remoto não atendido | Nenhuma compra/deploy presumido |

Resiliência: sem retry infinito/circuit breaker/dependência nova. Batch retry só falha transitória tipada, máximo3; invalidez semântica é pendência/rejeição, não retry. Timeouts/pools existentes preservados; transações não incluem rede. API não entrega stack/driver/query/linha recebida. Erro de autenticação/autorização não vira fallback permissivo.

---

### 7. Observabilidade

**Métricas**
- Actuator/Micrometer/Batch existentes: execução, linhas classificadas, estado e tempo observado; cardinalidade por tabela/fase, nunca pessoa/filename livre.
**Logs**
- Método/rota template/status, jobId, digest, fase/tabela/contagens/categoria. Nunca SQL bruto, payload, querystring privada, nome/email/CPF/senha/hash/chave.
**Tracing**
- W3C e propagação contexto Java atuais; importação associa execução ao trace sem logs de linha. Tracing PHP não prometido sem implementação verificada.
**Dashboards e alertas**
- Nenhum backend/coletor novo. Health PHP mínimo sem detalhes; métricas Java continuam protegidas. Auditoria distingue implementado/validado e guarda só contagens/evidências sintéticas.

---

### 8. Dependências e compatibilidade

| Componente | Versão mínima | Observações |
| --- | --- | --- |
| Java/Spring Batch/JPA/Flyway/PostgreSQL/React | docs/lib.md vigente | Nenhuma biblioteca runtime nova para modelo/parser/importação; JDK fornece AES-GCM/digest |
| PHP/PDO pgsql | PHP8.4 candidato a verificar por operação | Exceção ADR007, container separado; extensões necessárias inventariadas antes adoção |
| mPDF existente |>=8.2.5 candidato verificado antes instalação | Atualizar dependência existente8.0.0 incompatível; Spring/PDFBox não executam PHP legado |
| CLI/gates existentes | docs/lib.md | Formatter/Prettier/Gitleaks/Playwright temporários, sem runtime adicional |

**Garantias de compatibilidade**
- Não editar migrações existentes nem excluir colunas/tabelas históricas; expandir→preencher→validar, contração fora desta entrega.
- Contratos T002 e avaliações/boletim/calendário/materiais/CSRF preservados; teste anterior não afrouxado. O gráfico novo não inventa Curso redundante na matrícula.
- Reversão exige backup/restore real e verificação de bytes/origem; DDL novo pode permanecer após rollback da aplicação, como V5. Não alegar reversão completa por DROP sem recuperar dados.
- Fonte privada guarda versão/digest/origem e não é publicada. Snapshot criptografado exige chave preservada para restore/restart; papéis desconhecidos e senhas não são ativados por importação.
- PHP é base independente explicitamente identificada; nenhuma sincronização automática com React/Java prometida.

---

### 9. Critérios de aceite técnicos

| Fluxo/prova | Validação necessária |
| --- | --- |
| Acadêmico completo | Faculdade→Curso→Grade→Requisito→Matéria, matrícula/oferta, professor existente; CRUD positivo e404/400/409/401/403/CSRF por endpoint, FK/UNIQUE e turma divergente |
| Compatibilidade T002 | Escola/relatórios suites existentes + E2E cinco tipos, perfil/senha/logout/privacidade e bytes/tracing após migração |
| Importação sintética | Colisão com admin, matching único/ambíguo, usuário ausente/cargo desconhecido, head grouping, PK inexistente,107classificados e replay sem duplicar; fixture pública exclusivamente fictícia |
| Fonte maliciosa | Função/subquery/DDL executável/tabela desconhecida/escape/limite rejeitados sem side effect; driver recebe sóSQL constante preparado |
| Autenticidade snapshot | Nonce12bytes aleatório entre gravações, tag128, AAD/envelope determinísticos; alterar ciphertext/nonce/AAD/chave falha sem plaintext/log; snapshot confirmado antes writer e mesma chave/fonte funciona após restart |
| Falha/restart | Writer falha após chunk, fonte persistida permanece fixa mesmo arquivo externo alterado/removido; FAILEDretoma sem duplicar; não equivaler a killSTARTED |
| Reconciliação | Resolver referência para Pessoa real autorizada e reprocessar sópendências; sem alterar perfil/senha e sem sobrescrever edição de usuário |
| Dump privado autorizado | Execução local privada contabiliza24tabelas/107registros por status/motivo; relatório contém sócontagens/digest, nunca dump/PII/hash |
| PHP real | Navegador login por3perfis sintéticos, catálogos/matrícula/notas/presença/material/PDF emPG; negações globais nas46rotas/13AJAX/5PDF e objeto/CSRF por famílias, sem esconder rota como mock |
| Operação | Compose, backup/restore separado Java/PHP e reversão isolada observados; HTTPS/Render/Neon apenas quando canais/quota autenticados permitem; limites explícitos |
| Governança | Builds reais, imports/formatação/gates, dois CI verdes e revisão independente; inventário de abstrações/dependências atualizado no audit central |

Cobertura por fluxo completo; não criar testes espelho de mapper nem percentual arbitrário. Casos focais do lexer/criptografia são necessários ao tratamento de entrada privada e replay, não decoração. Nenhum check verde ou arquivo escrito basta para declarar comportamento validado.

---

### 10. Riscos e mitigação

### Identidade/grade/estado desconhecido
- **Probabilidade:** alta, observada na fonte.
- **Impacto:** vínculos errados, privilégios indevidos ou perda de registros.
- **Mitigação:** matching único/mapeamento por origem, cargo desconhecido negado, head group contextual, pendência privada e contabilização por linha.
- **Plano de contingência:** importar somente parte comprovada e manter origem criptografada, sem preencher dados fictícios.

### Fonte PHP incompatível/insegura
- **Probabilidade:** alta, observada.
- **Impacto:** login/query/PDF quebrados ou exposição de dados.
- **Mitigação:** schema2024 por queries reais, guard global/objeto/CSRF, queries preparadas, versão PHP/mPDF suportada e prova por família.
- **Plano de contingência:** bloquear exposição/família sem tradução comprovada e registrar requisito parcial; nunca chamar React de PHP real.

### Migração/associações e replay
- **Probabilidade:** média.
- **Impacto:** regressão do histórico, duplicação ou lock prolongado.
- **Mitigação:** adição compatível, backfill idempotente, FK/UNIQUE/indexes, chunk limitado, fonte fixa/origem e teste de rollback/restart.
- **Plano de contingência:** rollback da aplicação com DDL aditivo conservado e restore isolado validado; corrigir sem apagar histórico.

### Segredo de importação e cloud
- **Probabilidade:** média para perda de chave; alta para bloqueio cloud atual.
- **Impacto:** snapshot não legível ou publicação impossível.
- **Mitigação:** chave dedicada foraDB/artifact, permissões privadas, backup com gestão de segredo separada, nenhuma credencial pública/compra por default.
- **Plano de contingência:** preservar fonte original privada enquanto chave/restore não forem verificados; ambiente local e remoto explicitamente parcial.

Categoria FDD: contratos e migração concretos que implementam PRD/HLD revisados. RFC/LLD dispensados pelo recorte e pelo plano; nenhuma documentação adicional criada. Dependências propostas e novas abstrações concretas serão inventariadas antes adoção; aprovação independente e task pronta precedem código.

Revisão independente2026-10-05: planejador conferiu integralmente estrutura/PHP e ajustes; operação conferiu Java/importação/segurança. Nenhuma validação de código/deploy inferida; autor FDD não aprovou o próprio trabalho.

Revisão documental 2026-10-06: diferença Base/espelho conferida; dispensa OC8 de ManterImportacaoActor incorporada como atualização explícita já revisada, preservando contratos e decisões da Base. Runtime e critérios de aceite dependem de evidências separadas.
