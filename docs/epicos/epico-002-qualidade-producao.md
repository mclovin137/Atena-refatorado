---
type: minerva-epico
project: Atena
date: 2026-10-04
status: em-planejamento
tags: [atena, epico]
ai-first: true
---

# Épico 002: Qualidade verificável e publicação

## Para o futuro agente
Origem: revisão do usuário sobre legibilidade/SOLID/factories/OC, ADRs fora das roles, roadmap/estado insuficientes e produção Render/Neon requerida. Resultado: melhorar manutenção sem perder fluxos e publicar somente com evidência real. T-001 foi entrega de código integrada/validada localmente, não deploy Render/Neon.

## Escopo e cadeia
[Roadmap](../roadmap.md) → este épico → [PRD-002](../prds/prd-002-qualidade-producao.md) → [HLD](../hlds/hld-qualidade-producao.md)/ADRs004–005 → [FDD-002 aprovado](../fdds/fdd-002-qualidade-producao.md) → [T-002 pronta](../tasks/t-002-qualidade-producao.md) → PR/review/testes → merge/deploy/auditoria. Sem RFC/LLD extra: requisitos e referência definidos, detalhes de implementação cabem no FDD. Arquivo AUDIT_REPORT.json é requisito explicitamente novo, não documentação especulativa.

## Refinamento multiagente

Data: 2026-10-04
Situação: vigente, com lacunas operacionais
Escopo analisado: organização, contratos, segurança, durabilidade e publicação.

### Pareceres

| Agente real | Posição | Pontos-chave | Evidências |
|---|---|---|---|
| Planejador/Yoda | SEGUIR COM RESSALVAS | Dois contextos mantidos; estrutura por recurso e domínio puro; cortar factories/camadas decorativas; corrigir todos ADRs | ADR003 e playbook A–C MinervaFinancas remoto34fc98a; código Atena |
| Implementador/Severino | SEGUIR COM RESSALVAS | DTO defaults/nome preservados; Actor coordena, Service puro, JPA mapeado; arquivo bytea aditivo com compatibilidade | Fonte e contratos existentes, leitura readonly da referência |
| Operação com recortes QA/Neo/Jarvis | SEGUIR COM RESSALVAS | Caracterização PG/E2E existente, CSRF real/papel/objeto/jobs/tracing; TLS/token/bytes duráveis; Neon não autenticado | E2E T-001, Render free/private-network oficiais e descoberta MCP |

Foram três agentes independentes reais; não houve cinco instâncias separadas para todos os nomes canônicos. Limitação registrada, sem simular consenso de vozes fictícias. Parecer de QA/segurança/operação não transfere autoria nem dispensa revisão independente do próprio código/infra.

### Divergências e resolução
1. Aplicar cada item Financas versus preservar Atena: consulta seletiva organização/SOLID/OC; não importar stack, contratos ou funcionalidades de outro produto.
2. Rede privada/disk Render free versus dois serviços/arquivos: recurso pago descartado; proposta HTTPS com token e bytes em PostgreSQL. Implantação segue bloqueada até MCP/quota/configuração gratuita verificadas.
3. Entrevista/aceite humano dos skills versus autonomia: pedido original manda não perguntar; decisões simples registradas sem inventar confirmação humana. Publicação/risco não é declarada validada sem execução.

### Exigências por futura task
#### T-002 criada após FDD aprovado
- Resultado: fonte legível/DDD/SOLID/factories concretas, contratos preservados, ADRs conformes, roadmap/estado/auditoria atuais e publicação comprovada quando possível.
- PRD: aceite verificável de legibilidade, DTO/defaults/autorização, evidências e relatório JSON.
- HLD/ADRs: estrutura/refêrencia exatas e segurança/durabilidade remota; sem novas fronteiras/infra.
- FDD: mapa de recursos, portas/DTO/estratégias, Actor/Service puro, Batch e V5; critérios/provas de compatibilidade.
- Testes: suíte PG e E2E existente, autorização/CSRF/falha/restart; durabilidade pós-restart e artifacts/screenshots.
- Operação: Render/Neon via MCP somente gratuitos, logs sem PII, backups/restore e reversão funcional executados em ambientes isolados; produção/TLS/quota cloud ainda bloqueados.
- Dependências: FDD revisado precede produção; Neon/autorização/quota são bloqueio da etapa remota.

### Decisões e lacunas
- Confirmado: referência MinervaFinancas e Render/Neon escolhidos pelo usuário; stack/fronteiras Atena mantidas.
- ❓ LACUNA: Neon MCP configurado mas não autenticado; dono operação, bloqueia provisionamento/deploy remoto.
- ❓ LACUNA: Render MCP Docker/quota/cobrança ainda sem prova completa; dono operação, bloqueia publicação até verificação.
- Dados legados/carga não validados seguem limites T-001, sem trabalho adicional fictício.

## Responsabilidade e histórico
Dono planejamento: Yoda; FDD/produção: implementador; revisão independente e operação: agentes distintos por superfície.
- 2026-10-04: épico criado com refinamento proporcional real; sem task antecipada antes FDD aprovado.

- 2026-10-04: cadeia revisada por implementadores escolar/relatórios; FDD aprovado independentemente pelo planejador, T002 criada pronta para etapa local. Publicação continua bloqueada.

Categoria: épico. Pergunta: qual sequência de capacidades/dependências atende revisão do usuário? Anterior roadmap; posterior PRD002/HLD/FDD/T002. Dono planejador; revisores implementadores/operação. Refinamento registrado neste artefato, sem criar análise extra; RFC/LLD dispensados pelo escopo definido.

- 2026-10-05: implementação local refatorada e cinco testes PG isolados passaram; E2E Docker/React/tracing/reinício e backup/restore/rollback isolados passaram. CI/PR/merge ainda pendentes; publicação remota continua bloqueada.
