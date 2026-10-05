---
type: minerva-hld
project: Atena
date: 2026-10-04
tags: [atena, hld]
ai-first: true
---

# HLD — Dois serviços do Atena

## Para o futuro agente

Pergunta: como separar responsabilidades sem fragmentar o domínio? `escolar-service` é autoridade transacional de identidade/perfis, estrutura escolar, matrícula, ensino, comunicação e arquivos. Seus vínculos e autorização são densos; separá-los aumentaria transações distribuídas sem requisito. `relatorios-service` executa consolidação/exportações em Spring Batch e guarda resultados e metadados de execução; leitura exclusivamente por contrato HTTP explícito do serviço escolar, sem compartilhar entidades JPA ou consultar seu banco.

Um PostgreSQL Docker pode hospedar bancos separados e credenciais distintas por serviço; compartilhamento físico não autoriza compartilhamento de tabelas. React usa proxy no mesmo host para cookies/CSRF. Serviços internos não exigem broker; jobs usam metadados persistidos Spring Batch.

Processamentos pesados identificados: `MainMolde::cadastro_turmas`, `cadastro_diciplina` e `libera_notas` percorrem alunos/matérias para inicializar notas; `Views/pdf/HistoricoCompleto.php` e relatórios percorrem matrículas/anos/disciplinas/notas com consultas N+1. Escolha: job escolar `inicializarNotas` e job relatórios `consolidarHistorico`, com identidade de execução e resultado consultável. Ações pequenas permanecem síncronas.

Domínio mantém invariantes separado de transporte; application coordena, infrastructure implementa persistência; contracts são DTOs/IDs. Sem abstração genérica de CRUD ou entidades entre contextos.

Decisões: [fronteiras](../adrs/adr-001-fronteiras.md), [stack](../adrs/adr-002-stack.md), [visual](../adrs/adr-003-interface.md). Requisitos: [PRD](../prds/prd-001-refatoracao.md).

## Origem e responsabilidade

Categoria: hld. Origem: prompt do experimento e legado Atena `ac48dc552b817233547104a5d0acba932d41ab91`; governança Minerva `76a991d7296cddba5e595a07e9b3b6f8da581b78`. Dono: planejador; revisor: implementação e operação. RFC e LLD dispensados: stack decidida pelo usuário e contratos concretos detalhados no FDD.

## Histórico

- 2026-10-04 — Criação para o experimento.
