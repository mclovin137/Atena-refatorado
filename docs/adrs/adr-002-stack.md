---
type: minerva-adr
project: Atena
date: 2026-10-04
tags: [atena, adr]
ai-first: true
---

# ADR-002 — Stack e autenticação

## Para o futuro agente

Status: aceita pela stack mandatória. Java 21, Spring Boot, React, PostgreSQL e Docker Compose. Spring Web/Validation/JPA/Flyway/Security/Batch/Actuator e Micrometer atendem seus papéis antes de bibliotecas externas; Cache usa provedor em memória. Sessão HttpOnly e CSRF preservam perfis existentes sem dependência JWT. Biblioteca PDF só se justifica por exportações mPDF existentes e ausência equivalente no Spring; formatos substitutos precisam registrar perda. Dois pipelines (review e testes), sem infraestrutura além Docker/PostgreSQL. Deploy inicial local em loopback gratuito; publicação remota não será declarada se não executada. Dependências e versões: `docs/lib.md`.

## Origem e responsabilidade

Categoria: adr. Origem: prompt do experimento e legado Atena `ac48dc552b817233547104a5d0acba932d41ab91`; governança Minerva `76a991d7296cddba5e595a07e9b3b6f8da581b78`. Dono: planejador; revisor: implementação e operação. RFC e LLD dispensados: stack decidida pelo usuário e contratos concretos detalhados no FDD.

## Histórico

- 2026-10-04 — Criação para o experimento.
