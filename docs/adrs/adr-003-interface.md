---
type: minerva-adr
project: Atena
date: 2026-10-04
tags: [atena, adr]
ai-first: true
---

# ADR-003 — Design e responsividade

## Para o futuro agente

Status: aceita pela autonomia. Direção: aplicação escolar legível, fundo claro, texto escuro, verde #176953 para ação principal e azul escuro #172d40 para navegação; erros usam texto além de cor. Fontes Segoe UI/Arial/sans-serif e Georgia no logotipo; espaçamento-base 8px com campos 16/20/24px; controle principal mínimo 44px; foco visível. Cada campo tem label e erros associados; navegação e dialogs por teclado. Até 767px: coluna única, navegação compacta e tabelas com scroll localizado; 768–1100px: formulários até duas colunas; acima: conteúdo limitado a 1500px e navegação lateral/cabeçalho. Sem animação necessária. Tabelas mantêm cabeçalhos e nomes; estados carregando/vazio/erro/sucesso explícitos. Contrato aplica a login, cadastros, aulas, avaliações, notas, arquivos, chat, calendário e relatórios. Não inserir biblioteca de componentes para esse requisito.

## Origem e responsabilidade

Categoria: adr. Origem: prompt do experimento e legado Atena `ac48dc552b817233547104a5d0acba932d41ab91`; governança Minerva `76a991d7296cddba5e595a07e9b3b6f8da581b78`. Dono: planejador; revisor: implementação e operação. RFC e LLD dispensados: stack decidida pelo usuário e contratos concretos detalhados no FDD.

## Histórico

- 2026-10-04 — Criação para o experimento; direção visual refinada durante implementação para refletir tokens e faixas efetivas.
