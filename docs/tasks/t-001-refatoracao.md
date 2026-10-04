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
