---
type: minerva-adr
project: Atena
date: 2026-10-04
tags: [atena, adr]
ai-first: true
---

# ADR-001 — Fronteiras escolar e relatórios

## Para o futuro agente

Status: aceita pela autonomia do experimento. Decisão: dois microsserviços conforme [HLD](../hlds/hld-refatoracao.md). Alternativas: monólito viola requisito; serviço por cadastro cria acoplamento distribuído sem valor. Benefício: processamento pesado isolado e núcleo escolar consistente. Custo: contrato HTTP e eventual diferença temporal dos relatórios. Metadados Batch e resultados próprios persistem no banco do contexto. Nenhum broker introduzido.

## Origem e responsabilidade

Categoria: adr. Origem: prompt do experimento e legado Atena `ac48dc552b817233547104a5d0acba932d41ab91`; governança Minerva `76a991d7296cddba5e595a07e9b3b6f8da581b78`. Dono: planejador; revisor: implementação e operação. RFC e LLD dispensados: stack decidida pelo usuário e contratos concretos detalhados no FDD.

## Histórico

- 2026-10-04 — Criação para o experimento.
