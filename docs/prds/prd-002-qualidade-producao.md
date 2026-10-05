---
type: minerva-prd
project: Atena
date: 2026-10-04
status: aprovado
tags: [atena, prd]
ai-first: true
---

### PRD: Atena Qualidade estrutural e produção

Versão: 1
Data: 2026-10-04
Responsável: planejador/revisor

---

### Resumo
Usuário considera a implementação T-001 difícil de ler e abaixo da qualidade esperada. Requer organização/nomenclatura/backend/playbooks de MinervaFinancas, SOLID, factories e Object Calisthenics; também exige revisão dos ADRs contra roles e publicação automática Render/Neon via MCP. O resultado deve preservar os fluxos escolares já executados e informar honestamente limites. O pedido adicional autoriza arquivo de auditoria estruturado, sem estimar métricas externas.

---

### Contexto e problema

Público-alvo
- Secretaria, professores, alunos e responsáveis pela manutenção.

Cenários de uso chave
- Ler e alterar um caso de uso sem atravessar controllers/políticas genéricas de todos os recursos.
- Executar matrícula, ensino, notas, arquivos e relatórios com contratos T-001 preservados.
- Acessar produção HTTPS e conferir dados/arquivos após reinício.

Onde essa feature será implantada
- Sistema Atena existente; repositório privado Atena-refatorado. Produção Render e PostgreSQL Neon escolhidos pelo usuário, orquestrados via MCP quando ferramentas/autenticação permitirem.

Problemas priorizados
- Alta: código comprimido, regras/persistência misturadas, entidades nos contratos e ADRs sem análise exigida pela role.
- Alta: publicação remota ainda não executada; durabilidade de uploads e proteção de endpoints internos precisam atender ambiente público.

---

### Objetivos e métricas

| Objetivo | Métrica | Meta |
|---|---|---|
| Tornar a alteração por domínio legível | Revisão independente de responsabilidades, nomes, formato e factories | Nenhum achado bloqueante nas áreas modificadas; sem factory decorativa |
| Preservar funcionalidades T-001 | Suíte PostgreSQL/E2E existente e contratos comparados | Fluxos executados sem regressão não declarada |
| Corrigir ADRs | Molde/roles/alternativas/aceite e espelho Obsidian | Todos os ADRs existentes e novos aderentes e revisados |
| Publicar com dados duráveis | Deploy LIVE, HTTPS/login/E2E e reinício real | Dois serviços e dados/arquivos persistentes verificados; bloqueio externo declarado se ocorrer |
| Produzir auditoria verificável | AUDIT_REPORT.json versus diffs/evidências | JSON válido, todos os campos do formato solicitado e sem resultados inventados |

---

### Escopo

Incluso
- Refatoração de fonte backend e React para legibilidade e responsabilidade explícita.
- Revisão dos ADR001–003 e decisões novas rastreadas; documentação exigida e Obsidian.
- Preservação de REST/JSON/status/perfis, cálculo e jobs existentes; durabilidade de materiais/avatar necessária à publicação.
- Render/Neon via MCP, segurança pública, migrações necessárias, evidências operacionais e arquivo AUDIT_REPORT.json.

Fora de escopo
- Novas funções escolares, redesenho da interface, alteração de fórmulas ou importação inventada do dump ausente.
- Nova infraestrutura de cache/fila/armazenamento, contratação paga, substituição de Neon por outro banco ou mudança de fronteiras.
- OpenAPI/i18n/paginação/novos códigos HTTP copiados do produto Financas sem requisito Atena.
- Promessa de SLA, benchmark/carga, estimativa de tokens/custo/latência do modelo.

---

### Requisitos funcionais

#### RF-001 Manutenção por responsabilidade
O mantenedor deve localizar contrato, caso de uso, regra e persistência de um recurso sem reflexão ou árvores genéricas de tipos. Arquivos fonte precisam ser multilineares e legíveis; factories justificadas por variantes reais ou construção válida, sem multiplicar camadas vazias.

**Fluxo principal**
- Entrada REST traduz DTO explícito; caso de uso coordena portas e aplica regra de domínio; adaptador persiste; DTO de saída preserva o contrato existente.

**Fluxos alternativos e exceções**
- Apoio compartilhado só existe quando mais de um recurso o usa e a responsabilidade é coesa.

**Erros previstos**
- Dados inválidos e violações de perfil permanecem rejeitados nos status/formatos anteriores.

**Prioridade:** alta

---

#### RF-002 Operação escolar preservada
Preservar cadastros, matrícula/dependências/currículo, horários/aulas/presença, avaliações/notas/liberação/prazo, boletim, arquivos/avatar/senha, chat/calendário, relatórios e jobs nos contratos entregues T-001. Nenhuma aprovação dos bugs originais é reintroduzida.

**Fluxo principal**
- Secretaria cria estrutura e matrícula; professor permitido lança ensino; jobs inicializam notas e consolidam histórico; aluno autorizado consulta e baixa suas informações.

**Fluxos alternativos e exceções**
- Jobs repetidos não duplicam dados; falha/restart respeita snapshot persistido. Documento permanece omitido ao professor.

**Erros previstos**
- CSRF ausente, papel indevido, acesso a outro aluno, prazo fechado e upload inválido mantêm rejeições existentes.

**Prioridade:** alta

---

#### RF-003 Publicação e dados duráveis
Usuário acessa interface HTTPS Render e usa PostgreSQL Neon. Materiais/avatar e resultados de jobs devem continuar disponíveis após reinício; ferramentas indisponíveis impedem declarar publicação concluída.

**Fluxo principal**
- Recursos gratuitos autorizados são provisionados via MCP; serviços publicados passam health/login/fluxo entre serviços; reinício e nova consulta comprovam persistência.

**Fluxos alternativos e exceções**
- Hibernação free/cold start e sessão perdida exigem novo login; interface trata falhas sem sucesso falso.

**Erros previstos**
- MCP Neon sem autenticação, Docker não suportado pela ferramenta ou quota/custo não garantido bloqueiam publicação remota; operação local permanece utilizável.

**Prioridade:** alta

---

### Requisitos não funcionais

Performance
- Sem meta de latência inventada; preservar timeout/paginação/limites existentes e registrar cold start. Não declarar teste de carga ausente.

Disponibilidade
- Sem SLA prometido em free tier; health deve ser verificável e falhas devem permanecer explícitas.

Segurança e autorização
- HTTPS, cookies seguros, CSRF real, perfis/objeto, token interserviços e credenciais isoladas; sem PII/segredos nos logs/auditoria. Sem administrador de demonstração público.

Observabilidade
- Preservar Actuator/Micrometer e correlação W3C entre HTTP e Batch, com endpoints técnicos protegidos no ambiente público.

Confiabilidade e integridade de dados
- Migrações auditáveis, arquivos duráveis, snapshot fixo, idempotência dos jobs; backup/restore/rollback só declarados validados quando executados.

Compatibilidade e portabilidade
- Java/Spring Boot, React, PostgreSQL e Docker mantidos; contratos públicos e defaults preservados por mapeamento explícito. Docker Compose local continua funcional.

Compliance
- Dados de teste fictícios e limpeza por execução; nenhuma nova política legal/retentiva inventada.

Acessibilidade no frontend consumidor
- Contrato ADR003 mantido: labels/foco/teclado/estados e desktop/mobile verificáveis.

---

### Arquitetura e abordagem

Abordagem
- Contextos escolar/relatórios existentes conforme ADR001. Organização pedida na ADR004; estratégia remota proposta ADR005 e [HLD](../hlds/hld-qualidade-producao.md).

Componentes
- APIs, React, domínio escolar, jobs Batch, exportações e persistência existentes.

Integrações
- Escolar/relatórios HTTP, Render MCP e Neon MCP conforme solicitação; não criar alternativa silenciosa ao Neon.

### Decisões e trade-offs

#### Decisão: preservar arquitetura e stack
- **Justificativa:** requisitos originais vigentes; ADR001/002.
- **Trade-off:** refatoração ampla ocorre dentro dos mesmos serviços.

#### Decisão: organização e publicação pedidas
- **Justificativa:** pedido novo determina referência MinervaFinancas e Render/Neon; detalhes pertencem ADR004/005 e HLD.
- **Trade-off:** estrutura ganha arquivos coesos; operação gratuita possui limitações e lacunas reais.

---

### Dependências

#### Técnica: implementação e evidências T-001
Contratos, testes reais e capturas servem de caracterização; implementador escreve FDD e produção, revisor independente verifica.

#### Externa: Render/Neon e MCP
Operação confirma ferramentas, autenticação, Docker, TLS e franquia gratuita antes provisionar; ausência é bloqueio externo da publicação.

---

### Riscos e mitigação

#### Reorganização altera contrato/autorização
- **Probabilidade:** alta
- **Impacto:** regressão funcional ou vazamento de dados.
- **Mitigação:** DTO allowlist com nomes/defaults explícitos; política tipada; testes existentes e review independente.
- **Plano de contingência:** interromper publicação, corrigir ou reverter código preservando dados.

#### Infraestrutura gratuita não comporta publicação solicitada
- **Probabilidade:** alta
- **Impacto:** deploy bloqueado, hibernação ou perda de arquivos.
- **Mitigação:** confirmar ferramenta/quota; persistir arquivos no PostgreSQL e validar após reinício; somente plano gratuito.
- **Plano de contingência:** manter operação Docker local e informar bloqueio, sem contratar ou substituir tecnologia.

---

### Critérios de aceitação
Checklist objetivo que define se a feature está pronta.

- [ ] Fonte legível, organização por recurso, domínio puro, controllers finos, DTOs explícitos e nenhuma árvore genérica por reflexão/typecode.
- [ ] Factories e demais abstrações possuem responsabilidade concreta registrada; não há serviço/porta/builders vazios ou wrappers sem invariante.
- [ ] Todos os ADRs seguem molde/roles, opções reais, decisões rastreáveis e revisão independente; base Obsidian confere com espelhos.
- [ ] Suíte PostgreSQL e navegador preservam campos/defaults/rotas/status/perfis, cálculos, arquivos e jobs; evidências publicadas.
- [ ] Render/Neon via MCP executados e health/HTTPS/login/E2E/reinício persistente comprovados, ou publicação marcada não atendida com bloqueio factual.
- [ ] AUDIT_REPORT.json válido e completo no formato solicitado, auditado contra diff/dependências/evidências; IDs não fornecidos não inventados e métricas externas não estimadas.

---

### Testes e validação

Tipos de teste obrigatórios
- Caracterização por fluxos PostgreSQL reais e integração de todo endpoint, preservando cobertura T-001.
- Casos de invariantes de domínio/factory somente quando a extração introduzir comportamento a proteger.
- Segurança cookie/CSRF real, papéis/objeto/secret token, arquivo/autorização; Batch repetição/falha/restart.
- Navegador desktop/mobile, tracing e durabilidade após reinício em produção quando disponível.

Estratégia de validação
- Reutilizar suíte ampla e E2E existente, sem testes espelho nem centenas de testes triviais. Dados próprios e limpeza idempotente; evidência como artifacts e imagens no PR. Validado significa executado/observado. Revisão de legibilidade e auditoria JSON independentes do implementador; duas pipelines verdes antes merge/deploy. Origem: pedido novo em 2026-10-04 e MinervaFinancas remoto34fc98a. RFC/LLD dispensados pelo escopo definido e contratos detalhados em ADR/HLD/FDD; entrevistas incompatíveis com autonomia expressa, registrada como conflito do prompt.

Revisão independente do contrato em 2026-10-04: agentes implementadores escolar/relatórios conferiram requisitos/aceite/limites e não identificaram bloqueante local após ajustes; pedido explícito e autonomia fundamentam escopo, sem afirmar aceite humano inventado ou resultados novos.

Categoria: PRD. Pergunta: que resultado de qualidade/paridade/publicação o usuário exige e como verificar? Origem [épico002](../epicos/epico-002-qualidade-producao.md) e T001; documento posterior [HLD qualidade](../hlds/hld-qualidade-producao.md)/FDD002/T002. Dono planejador; revisores implementadores escolar/relatórios. RFC/LLD dispensados: escopo explícito e FDD suficiente, sem decisão de negócio aberta.
