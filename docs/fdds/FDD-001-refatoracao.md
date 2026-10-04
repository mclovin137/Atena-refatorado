---
id: FDD-001
type: minerva-fdd
project: Atena
date: 2026-10-04
tags: [atena, fdd]
ai-first: true
status: aprovado
---
# FDD-001 — Migração escolar

## Para o futuro agente

Este documento define os fluxos escolares e os lotes da migração. Implementador: Severino; revisor independente: planejador/revisor. Contratos REST sob `/api`; status aprovado significa contrato revisado, não execução funcional validada.

## Origem e responsabilidade

Categoria FDD; pergunta: como preservar e implementar os fluxos? Artefatos anteriores: [PRD](../prds/prd-001-refatoracao.md), [HLD](../hlds/hld-refatoracao.md) e ADRs-001/002/003. Origem executável: legado commit `ac48dc552b817233547104a5d0acba932d41ab91`. RFC dispensada pela stack mandatória e LLD dispensado porque contratos abaixo bastam à implementação. Autor de implementação mantém desenho; revisão editorial independente não transfere autoria.

## Contextos

`escolar-service` possui identidade (ALUNO, PROFESSOR, SECRETARIA), cadastros (pessoas, cursos, séries, matérias, unidade), currículo, turmas, disciplinas e professor responsável, matrículas/dependências, horários, planejamento/aulas, presença, avaliações/notas/recuperação, calendário, chat e materiais. O domínio de resultado escolar é puro Java; persistência JPA e transporte dependem dele. `relatorios-service` possui execuções Spring Batch e documentos derivados; recebe snapshot explícito por HTTP autenticado do escolar, usa banco próprio e não acessa tabelas escolares.

## Fluxos e autorização

Sessão Spring Security com senha BCrypt, cookie HttpOnly e proteção CSRF. Secretaria administra cadastros e vínculos; professor somente disciplinas atribuídas; aluno vê seus dados, aulas/materiais e envia mensagens. Bootstrap secretaria por variáveis obrigatórias de ambiente, sem credenciais no repositório. Dados cadastrais preservam CEP, pais, estado civil, sexo e contato de responsável; documento não vazio é único. Avatar pessoal POST/GET autoriza somente titular/secretaria, ImageIO verifica dimensões <=4096 e recodifica PNG. Mudança de senha exige senha atual, grava BCrypt e encerra a sessão, via PUT `/api/session/password`. Erros JSON pt-BR; validação Bean Validation e respostas 400/401/403/404/409.

Cadastros CRUD, vínculo turma/disciplina/matrícula e registro de avaliação, nota por avaliação, recuperação por bimestre, presença por aula. Resultado: escala 0..100; nota bimestral lançada diretamente, avaliações e suas notas mantidas separadas; avaliações qualitativas consolidadas por SOMA (teto100) ou MEDIA, mínimo duas avaliações, conforme `Models/ajax/EnviarAlaiacao.php`, sem apagar originais (alteração de auditabilidade registrada), recuperação limitada a 60 substitui nota inferior a 60 por valor maior; média anual considera quatro bimestres e parcial apenas lançados; aprovação exige média >=60 e frequência >=75%. Divergências 3300/3600 segundos no legado resolvidas com hora-aula de 60min, explicitamente registrada como mudança.

A secretaria controla prazo, liberação e reabertura por bimestre. Professor atualiza somente valor e recuperação de suas disciplinas quando a nota está liberada e o prazo vigente; não cria/exclui registro nem altera aluno/disciplina/bimestre/prazo/liberação. Aluno vê somente seus dados e notas de bimestre liberado. Grade curricular nomeada aplica suas matérias/carga às disciplinas da turma; não cria contexto adicional.

## Lote e relatórios

Job escolar `inicializarNotas`: pagina matrículas da turma e cria registros bimestrais faltantes por disciplina/aluno de modo idempotente. Transações em chunks, metadados Spring Batch persistidos PostgreSQL, consulta de execução e reexecução com parâmetros próprios. Origem: loops em MainMolde::cadastro_turmas/libera_notas.

Job relatório `consolidarHistorico`: lê snapshot completo autorizado, consolida cada matrícula/disciplina com as regras de notas/frequência já calculadas e persiste snapshot fixo e documento derivado versionado pela execução; reexecução usa o snapshot original. Consulta fonte usa transação REPEATABLE_READ. Snapshot completo permanece em memória: limite conhecido para grande volume. Exporta PDF nativo com PDFBox (Spring não oferece geração PDF), JSON e CSV. Origem: loops e consultas N+1 de Views/pdf/HistoricoCompleto.php. CSV neutraliza fórmulas. Preserva exportação PDF servidor.

## Infraestrutura e validação

Compose: PostgreSQL com dois bancos e usuários isolados, dois serviços Spring Boot e frontend React empacotado como recursos estáticos do escolar-service (sem servidor de infraestrutura adicional). Frontend acessível em loopback 18080, proxy para APIs e sessão na mesma origem. Volumes PostgreSQL e arquivos. Health/metrics Actuator, Spring Cache memória apenas catálogos públicos com invalidação em escrita. Sem broker, scheduler externo ou bibliotecas fora requisitos sem justificativa em docs/lib.md.

Integração de todos endpoints por Spring Boot Test com PostgreSQL real; fluxo completo matrícula → aula/presença → avaliação/notas/recuperação → lote → relatório. Dois pipelines separados review/testes com evidência. Teste navegador real de login e responsividade quando ambiente disponível.

## Histórico

- 2026-10-04 — Implementador escreveu desenho; revisor aprovou dois contextos e lotes. Revisão exigiu PDF nativo, paridade soma/média, prazos de notas e snapshot estável; contrato atualizado e espelho canônico conferido. Execução real registrada separadamente no log do experimento.
