---
type: minerva-task
project: Atena
date: 2026-10-04
tags: [atena, task]
ai-first: true
---

# T-001 — Refatoração do Atena

## Para o futuro agente

Classificação: fluxo completo. Objetivo: entregar migração preservando fluxos do [PRD](../prds/prd-001-refatoracao.md), conforme [HLD](../hlds/hld-refatoracao.md). Dono implementação: implementador; revisão independente: planejador e operação. FDD deve ser escrito pelo implementador e revisado antes produção.

Escopo: dois serviços, frontend React, PostgreSQL/Flyway, Spring Batch, Docker, dois pipelines e evidências de integração. Exclusões: Redis/mensageria/search/Kubernetes, bibliotecas sem justificativa, novo produto, estimativa de tokens/custo/latência.

Matriz QA: fluxo completo feliz; login incorreto e acesso de perfil indevido; referências inexistentes e entrada inválida; recuperação/bimestre sem nota; matrícula duplicada; upload inválido e download autorizado; comunicação por turma; jobs vazios, repetidos e falha; relatório consistente com dados de origem. Todo endpoint exige pelo menos execução autorizada e cobertura de regra relevante por integração. Evidência de UI: screenshot em viewport móvel e desktop e fluxo de login com navegador real; screenshots complementam logs/builds, sem substituir testes.

Resumo: decisão dois contextos; evidência origem PHP lida; riscos schema original ausente e ambiguidades legacy; próximo passo FDD e implementação incremental. Conclusão só após registrar resultados observados; falhas não apagam evidência.

## Origem e responsabilidade

Categoria: task. Origem: prompt do experimento e legado Atena `ac48dc552b817233547104a5d0acba932d41ab91`; governança Minerva `76a991d7296cddba5e595a07e9b3b6f8da581b78`. Dono: planejador; revisor: implementação e operação. RFC e LLD dispensados: stack decidida pelo usuário e contratos concretos detalhados no FDD.

## Histórico

- 2026-10-04 — Criação para o experimento.

## Revisão independente — código e QA

Parecer em 2026-10-04: aprovado para operação, condicionado a evidências Compose/E2E e pipelines verdes antes merge. Cadeia de artefatos lida antes do código; dois contextos, domínio de cálculo puro, integrações por DTOs/HTTP, metadados Batch, SQL e sessões/perfis revisados. Testes locais observados no log real: SchoolFlowTest1 e ReportFlowTest2, zero falhas/erros, `mvn verify` BUILD SUCCESS 19:05:11; novos endpoints avatar/senha e V3 incluídos. Evidência de código não substitui execução do fluxo real entre serviços.

Limitações: importação de banco/dados reais não executada por ausência de dump; PDF nativo conserva exportação, acentos e campos institucionais, mas muda layout; snapshot inteiro em memória e consultas N+1 na origem permanecem risco de escala; troca de senha invalida a sessão atual, outras sessões permanecem até logout/expiração. Jobs testados com dados pequenos; sem benchmark ou carga. Sem achado bloqueante remanescente identificado no escopo inspecionado.

Revisão do patch cabeçalho em 2026-10-04: unidade/CNPJ/endereço/telefone, documento e matrícula restaurados no snapshot/PDF; CPF suprimido para professor e preservado titular/secretaria. V4 adiciona coluna opcional, sem novo contexto ou dependência. Código aprovado por revisão independente; teste5 ainda em execução no momento deste registro. Nenhuma declaração de validação dos novos campos antes do resultado observado.
