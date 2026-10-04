---
type: minerva-prd
project: Atena
date: 2026-10-04
tags: [atena, prd]
ai-first: true
---

# PRD-001 — Preservar o Atena

## Para o futuro agente

Pergunta: quais comportamentos preservar na migração? Perfis secretaria/professor/aluno, cadastros de pessoas/unidade/cursos/séries/turmas/disciplinas, grade/horário/matrícula/dependência, planejamento de aulas e conteúdo, avaliações, quatro notas bimestrais e recuperações, boletim/histórico/frequência, arquivos, chat e calendário. Livros/eventos descritos no README só viram paridade comprovada se houver implementação identificada; ausência precisa registro, não invenção.

Critérios de aceite verificáveis:
1. Login/logout e três perfis preservados; acessos proibidos recusados com teste de integração.
2. Fluxo secretaria cadastra estrutura e aluno, matricula; professor planeja aula, registra presença e notas; aluno consulta somente seus dados e notas liberadas.
3. Chat, calendário e arquivos têm criação e leitura autorizadas; upload/download preservam conteúdo e impedem acesso indevido.
4. Quatro bimestres (0..100), recuperações (0..60) substituem nota inferior a 60 se maiores; média anual divide por quatro e parcial por bimestres preenchidos. Alteração deliberada: hora-aula de 60 minutos e aprovação exige frequência >=75%, corrigindo precedência de aprovação do legado. Não alegar identidade absoluta do resultado legado.
5. Batch inicializa notas de turma sem duplicação; histórico consolida resultados e relata conclusão/falha observável. Relatórios de alunos/professores/turmas e histórico devem continuar disponíveis; mudança PDF precisa registro explícito.
6. React responsivo operável por teclado; validação em português e estados de erro visíveis.
7. Builds e testes realmente executados, cada endpoint coberto por integração idempotente; Docker Compose sobe apenas stack autorizada.

Origem: [épico](../epicos/epico-001-refatoracao.md). Estrutura: [HLD](../hlds/hld-refatoracao.md). Execução: [T-001](../tasks/t-001-refatoracao.md).

## Origem e responsabilidade

Categoria: prd. Origem: prompt do experimento e legado Atena `ac48dc552b817233547104a5d0acba932d41ab91`; governança Minerva `76a991d7296cddba5e595a07e9b3b6f8da581b78`. Dono: planejador; revisor: implementação e operação. RFC e LLD dispensados: stack decidida pelo usuário e contratos concretos detalhados no FDD.

## Histórico

- 2026-10-04 — Criação para o experimento.
