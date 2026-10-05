---
type: minerva-hld
project: Atena
date: 2026-10-04
status: vigente
tags: [atena, hld]
ai-first: true
---

### HLD: Atena Qualidade estrutural e produção

Versão: 1
Data: 2026-10-04
Responsável: Yoda

---

### Objetivo técnico
Substituir organização técnica global e fonte comprimida por responsabilidades coesas por recurso, eliminando entidades JPA nos contratos, regras em controllers e seleção genérica por reflexão/instanceof. Preservar contratos e fronteiras entregues; preparar publicação autorizada com arquivos duráveis sem nova infraestrutura.

Dependências com outros sistemas
- [PRD-002](../prds/prd-002-qualidade-producao.md), FDD-001/testes T-001 e FDD-002 de autoria implementador.
- MinervaFinancas remoto34fc98a: ADR003 e playbook backend A–C como referência seletiva, não cópia de stack ou API de outro domínio.
- Render e Neon MCP para operação remota; autenticação Neon é lacuna bloqueante da publicação.

---

### Arquitetura geral
Dois microsserviços e dois bancos/credenciais separados continuam conforme ADR001. Pacotes por recurso/função da ADR004, com domínio puro e portas tipadas. REST → Actor → {Service, Builder, DAO, Helper}. Actors coordenam transações/I/O; services de negócio e domínio recebem dados prontos e não importam Spring, HTTP/JPA ou driver. Adaptadores dao/jpa realizam persistência e mapeiam entidades próprias. DTOs explícitos traduzem os nomes/defaults do JSON existente; nenhum DAO acessado diretamente por controller. Configuração Spring e segurança HTTP ficam em comum/config ou comum/rest, sem concentrar regras específicas.

Ambiente de implantação
- Local Docker Compose preservado.
- Remoto Render/Neon solicitado; detalhes da [ADR005 proposta](../adrs/adr-005-producao-render-neon.md) condicionados a ferramentas/quota/custo gratuito verificados, sem publicação presumida.

Tecnologias principais
- Java 21/Spring Boot 3.5.16, Spring Data JPA/Validation/Flyway/Batch/Security/Cache/Actuator/Micrometer, PostgreSQL, React e Docker existentes.
- Sem nova dependência de runtime só para SOLID, factories ou formatação; inventário docs/lib.md continua fonte de versões/finalidade.

Padrões adotados
- Organização por recurso, portas/adaptadores e domínio puro; DTO/Builder explícitos.
- Factories de seleção para variantes reais de exportação/autorização; factories de criação só quando protegem invariantes do agregado.
- OC pragmático: coesão, baixa indentação, nomes e comportamento; sem limites arbitrários de campos/métodos, DTOs portadores de dados. A seção C do playbook Minerva canônico define disciplina, não dogma; prevalece sobre leitura literal de limites Financas, sem afirmar cumprimento mecânico das nove regras. Premissa/trade-off: coesão e invariantes prevalecem sobre wrappers/navegação decorativos; cada dispensa concreta deve ser identificada por classe/regra/justificativa na auditoria e revisada, não uma licença geral. DTOs têm exceção explícita8/9.

---

### Componentes e responsabilidades

| Componente | Responsabilidades | Dependências |
|---|---|---|
| recurso/rest e dto | Traduzir HTTP/JSON, validar forma e devolver DTO sem JPA | Actor e configuração de autenticação |
| recurso/actor | Coordenar caso de uso, transação e autorização tipada | Portas DAO, services e builders |
| recurso/dominio e service | Invariantes/cálculo puro; estados válidos e políticas sobre dados fornecidos | Tipos puros do domínio |
| recurso/builder | Mapeamento explícito domínio/DTO sem decisão/I/O; entidade JPA↔domínio fica exclusivamente em dao/jpa | Tipos nomeados |
| recurso/dao | Interfaces IRecursoDAO e adaptadores Jpa, consultas parametrizadas, erros tratados | Spring Data/JPA somente adaptador |
| Batch por contexto | Jobs/steps/readers/writers chamam casos de uso tipados, metadados e idempotência preservados | Actors/portas e Spring Batch |
| relatorio/exportação | Estratégias PDF/CSV/JSON selecionadas por Factory, snapshot persistido e DTO tipado | PDFBox apenas estratégia PDF |
| React por recurso | Componentes/forms/ações legíveis e estados existentes | REST preservado e CSS ADR003 |
| comum | Configuração framework, sessão, erros e apoio efetivamente compartilhado | Colaborações específicas justificadas |

---

### Fluxo de requisições e de dados

**Fluxo de requisição**
- Sessão HTTP/CSRF valida transporte; usuário puro é entregue ao Actor por adaptador autenticado.
- DTO allowlist preserva nomes e defaults; Actor consulta portas, aplica política por perfil/objeto e regras puras, persiste via DAO e devolve DTO por Builder.
- Exportação escolar confirma acesso, chama relatórios com token/tracing; job usa snapshot fixo autorizado e estratégias de formato, sem reconsultar dados mutáveis na exportação.
- Batch escolar mantém parâmetros/nome/idempotência e cria apenas notas ausentes por matrícula; falhas propagam estado factual.

**Fluxo de dados**
- JPA não cruza REST nem contextos. Identificadores/DTO snapshot são contratos explícitos.
- Materiais/avatar atuais: proposta V5 aditiva guarda bytes no PostgreSQL e conserva metadados/formato/magic/limites; novos uploads usam armazenamento durável; leitura de legado em disco e retropreenchimento controlado preservam compatibilidade local.
- Migração não remove a coluna/referência antiga imediatamente; rollback não pode perder novos bytes. Backup/exportação e compatibilidade de app anterior devem ser resolvidos no FDD antes afirmar reversão validada.

---

### Modelo de dados (alto nível)

Entidades principais
- Pessoa/Unidade/Curso/Série/Currículo/Turma/Disciplina/Matrícula, ensino/notas/presença, material/avatar e comunicação; relatórios/linhas e metadados Batch próprios.

Relações
- Schema V1–V4 e chaves existentes preservados; domínio depende de portas para consultar relações necessárias às invariantes.
- V5 de conteúdo durável é proposta necessária por filesystem efêmero, não novo banco/serviço; objetos afetados devem ser documentados na base após SQL real.

Fonte de verdade
- Banco escolar para dados escolares/arquivos e banco relatórios para snapshot/resultado. Cache memória é derivado e invalidado como antes.

---

### Interfaces públicas

| Nome | Tipo | Protocolo | Exposição | SLAs/Limites |
|---|---|---|---|---|
| Escolar /api e React | API/interface | REST/HTTPS no remoto, HTTP loopback local | Usuários autenticados, frontend público | Rotas/status/JSON preservados; sem SLA gratuito inventado |
| Relatórios/internal snapshot/listas | API | HTTP local privado; proposta HTTPS remoto | Token interserviço obrigatório, nunca anônimo | Timeout/connect/read e payload limites existentes |
| Actuator health/métricas | API | HTTP/HTTPS | Health mínimo público; detalhes/métricas restritos | Informação sem segredo/PII |
| Render/Neon MCP | Ferramenta operação | Contrato do provedor | Operação autenticada | Neon ainda não conectado; não fallback silencioso |

---

### Considerações de escalabilidade e disponibilidade

Abordagem geral
- Poucos serviços definidos pelo domínio, sem autoscaling/replica/mensageria adicionais. Pool PostgreSQL pequeno na configuração remota deve respeitar quota confirmada.

Técnicas aplicadas
- Chunk/paginação/idempotência dos jobs, snapshot persistido, cache em memória e timeout existentes; nenhum framework genérico de CRUD.
- Render free não recebe private network; proposta exige HTTPS/token mesmo em rotas técnicas públicas. Hibernação após inatividade, franquia 750 h/workspace compartilhada e filesystem efêmero não sustentam promessa de disponibilidade contínua.

Meta de disponibilidade
- Nenhuma meta numérica contratada. Health/login/fluxos devem passar após deploy e reinício/cold start; falha é registrada e não convertida em sucesso por retry ilimitado.

---

### Segurança

Autenticação
- BCrypt e sessão existentes; Secure/HttpOnly/SameSite e header CSRF X-CSRF-TOKEN corretos para HTTPS. Reinício pode exigir relogin; não adicionar Spring Session sem requisito justificado.

Autorização
- Políticas tipadas Aluno/Professor/Secretaria substituem árvores genéricas; preservam papel e propriedade/matrícula, prazo/liberação e omissão de documento ao professor.
- SERVICE_TOKEN secreto obrigatório em relatórios e endpoints internos; constant-time/sem bypass. Métricas e logs não expõem dados internos.

Proteção de dados
- TLS verificado na conexão Neon, HTTPS Render, limites/magic e reencode de avatar preservados. Fixtures fictícios com limpeza. Nenhuma sessão ou payload pessoal em logs/auditoria.
- Armazenamento em repouso/backup depende de capacidade comprovada do provedor; não presumir certificação ou restore.

Gestão de segredos
- Ambiente/MCP autenticado, sem commits ou output de credenciais; ADMIN secreto aleatório exclusivo, sem conta demo pública. Forwarded headers somente sob proxy/plataforma confiável, configuração distinta do local.

---

### Observabilidade

Logs
- Preservar correlação estruturada sem PII/segredos e distinguir erro de input, acesso, integração e falha Batch.

Métricas
- Health, latência/erro HTTP e métricas de jobs pela stack existente; sem coletor novo.

Tracing
- W3C traceparent escolar→relatórios→snapshot acionado pelo Batch; mesma evidência deve sobreviver à extração de responsabilidades.

Dashboards e alertas
- Nenhum serviço contratado; consoles dos provedores e Actuator protegido. Sem promessa de alertas automatizados ainda não implementados.

---

### Riscos arquiteturais e mitigação

#### Refatoração ampla esconde regressões de política e contrato
- **Probabilidade:** alta
- **Impacto:** perda funcional ou acesso indevido.
- **Mitigação:** DTO allowlist/defaults explícitos, serviços puros, testes existentes em PG real/E2E e review por domínio; nenhuma aprovação só por contagem de arquivos/classes.
- **Plano de contingência:** corrigir/reverter aplicação antes deploy mantendo schema aditivo e dados.

#### Publicação free/MCP insuficiente
- **Probabilidade:** alta
- **Impacto:** bloqueio ou perda de dados em instância efêmera.
- **Mitigação:** Neon autenticado e Docker/quota gratuita confirmados; armazenamento de bytes, smoke/restart e custos guardados; não tocar serviços alheios.
- **Plano de contingência:** manter local funcional e registrar remota não atendida. Backup/restore/rollback não validados até execução.

---

### ADRs e próximos passos

ADRs associados
- [ADR001](../adrs/adr-001-fronteiras.md), [ADR002](../adrs/adr-002-stack.md), [ADR003](../adrs/adr-003-interface.md) corrigidas contra role/skill.
- [ADR004](../adrs/adr-004-organizacao-backend.md) organiza backend; [ADR005](../adrs/adr-005-producao-render-neon.md) propõe detalhes de implantação.

Decisões pendentes
- Redações/planejamento revisados independentemente por implementadores escolar/relatórios; FDD aprovado pelo planejador e task pronta para implementação local. Nenhum código novo foi aprovado como validado.
- Neon MCP configurado, mas não autenticado; create_web_service do MCP Render não aceita Docker/container registry na descrição callable atual; outra via automatizada exigiria credencial real e registro de desvio do canal MCP solicitado. Guard de quota/cobrança também precisa evidência. Dono operação; bloqueia etapa remota, não refatoração local.

Próximos passos
- Implementador escreve FDD com inventário de módulos/DTOs/portas/Factories e plano V5; revisor aprova antes task pronta.
- Implementação incremental; testes e audits reais; duas pipelines, merge/deploy por operação e auditoria AUDIT_REPORT.json independente. Documentação adicional só exigida pelo fluxo; RFC/LLD não necessários neste escopo.

Categoria: HLD. Pergunta: como organizar dependências/componentes e implantação para suportar PRD002? Anteriores PRD002 e ADRs vigentes; posterior [FDD002](../fdds/fdd-002-qualidade-producao.md)/[T002](../tasks/t-002-qualidade-producao.md). Dono Yoda; revisores implementadores escolar/relatórios. RFC/LLD dispensados: não há desenho independente além do detalhamento FDD.
