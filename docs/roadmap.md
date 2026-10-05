---
type: minerva-roadmap
project: Atena
date: 2026-10-04
tags: [atena, roadmap]
ai-first: true
---

# Roadmap Atena

## Para o futuro agente

Preservar a operação escolar na stack obrigatória. Ordem: (1) caracterizar legado e contratos; (2) governança e fundação Docker/PostgreSQL; (3) identidade, cadastros e matrículas; (4) aulas, avaliações, notas, frequência, arquivos, chat e calendário; (5) jobs e relatórios; (6) builds, testes de integração e operação. Épico: [Migração escolar](epicos/epico-001-refatoracao.md).

## Origem e responsabilidade

Categoria: roadmap. Origem: prompt do experimento e legado Atena `ac48dc552b817233547104a5d0acba932d41ab91`; governança Minerva `76a991d7296cddba5e595a07e9b3b6f8da581b78`. Dono: planejador; revisor: implementação e operação. RFC e LLD dispensados: stack decidida pelo usuário e contratos concretos detalhados no FDD.

## Histórico

- 2026-10-04 — Criação para o experimento.

## Conclusão observada

Entrega de produção concluída em 2026-10-04 22:59:45 America/Sao_Paulo (2026-10-05T01:59:45Z): [PR #1](https://github.com/mclovin137/Atena-refatorado/pull/1) integrado, merge `a29342ac3c99be5a733d6ccb6e32c8d85465dbc2`. Ambos os pipelines do head revisado `9da093395a1f93e376389e4d044d216676ead596` terminaram com sucesso: [revisão](https://github.com/mclovin137/Atena-refatorado/actions/runs/37253333394) e [testes/operação](https://github.com/mclovin137/Atena-refatorado/actions/runs/37253333431). Artifacts de integração e navegador foram publicados. Revisão independente aprovada após código, testes reais, fluxos entre serviços e capturas desktop/mobile.

Conclusão cobre a implementação e os fluxos executados; importação de dados legados, carga e paridade integral de layout permanecem limites registrados. Fechamento dos registros é uma alteração posterior somente de documentação, sem nova produção; sua revisão/CI seguirá o fluxo normal. Esta nota referencia a entrega PR #1 e não afirma merge futuro de documentação.
