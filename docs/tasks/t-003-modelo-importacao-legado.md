---
type: minerva-task
project: Atena
date: 2026-10-05
status: em-andamento
tags: [atena, task]
ai-first: true
---

# T-003: Importar dados acadêmicos por IDs e executar PHP real com segurança

## Para o futuro agente
Entrega local: modelo acadêmico operável e importação rastreável das 24 tabelas e 107 registros, além de PHP preservado adaptado ao PostgreSQL. Entrega remota: publicar PHP real em Render/Neon somente com ferramentas/gratuidade/autenticação e gates comprovados. Limites de cargos/instituição/grade/órfãos são pendências privadas explícitas, não dados fictícios. Task pode permanecer parcial por cloud/dados irrecuperáveis, sem declarar conclusão integral.

## Origem
- Caminho: fluxo-completo; muda dados, contratos, regras, importação e runtime PHP com risco de segurança.
- PRD: [PRD003](../prds/prd-003-modelo-legado.md), RF001–004.
- FDD aprovado: [FDD003](../fdds/fdd-003-modelo-importacao-legado.md); autor implementador, revisão independente integral planejador/PHP e operação Java/importação/segurança em 2026-10-05.
- HLD: [modelo-legado](../hlds/hld-modelo-legado.md).
- ADRs aplicáveis: [ADR006](../adrs/adr-006-modelo-importacao.md)/[ADR007](../adrs/adr-007-legado-postgresql.md), ADR001/002/004/005 fora da exceção específica PHP.

## Resumo decisório mínimo
- Objetivo: operacionalizar cadeia Faculdade/Curso/Grade/Requisito e relações contextuais, importar fonte privada sem SQL executável e exercer páginas PHP reais.
- Decisão: aditivo por IDs, Curso derivado da Turma, associação administrativa matrícula_disciplina, requisitos administrativos sem motor de formação; PHP separado PG sem sincronização prometida.
- Evidências: dump digest, 24 tabelas e 107 linhas analisados declarativamente; revisões plano/FDD independentes e espelhos conferidos. Dois testes puros de parser/criptografia passaram no Maven offline e a QA independente verificou os 107 registros e casos sintéticos. Na retomada, os últimos resultados dos11casos Java locais estão verdes por suíte+focal, sem comando integral único final. PHP runner/navegador finais SUCCESS e146lint foram conferidos; backend/PDF/bytes/restart/restore/segurança exercitados. Mobile apresenta conteúdo cortado e fonte107 original não disponível; E2E React final e resumo de restauração escolar foram conferidos; CI funcional integral no SHA 7f0642899c5659b47821630319255d3207636486 passou: 15 JUnit, E2E React e PHP, com artefatos publicados.
- Riscos e lacunas: 40 empréstimos e uma participação órfãos; cargos 0/4 desconhecidos; Faculdade ausente; Grade ambígua não resolvível nesta entrega; versão 2020 PHP ausente; cloud bloqueado.
- Próximo passo: autores implementam seus escopos incrementalmente; planejador/operação revisam alterações de autoria alheia, testes reais e dois CI precedem integração/deploy.

## Responsáveis
- Planejamento/documentação central/audit/estado: esta thread em papel Yoda, sem produção.
- Implementação Java/modelo/DDL/importação Batch/React/testes: autor FDD independente, escopo services/escolar e frontend; reports só se correção de contrato inevitável e revisada, nunca compartilhando JPA.
- Implementação PHP/infra A3: operação em papel explícito Severino, legacy e Docker/Compose/blueprint/scripts aplicáveis, dependências PHP justificadas. Não aprova nem integra o próprio escopo.
- Auditoria Java/importação: planejador e operação, distintos do autor; auditoria PHP/infra: planejador e implementador Java, distintos do autor.
- Integração/deploy: responsável Jarvis designado pelo orquestrador, sem autoaprovação/merge da própria implementação; precedido por parecer independente e dois pipelines verdes.

## Resultado esperado
APIs acadêmicas e importação operacionais com rastreio por origem/digest; fonte com 107 registros contabilizada em importado/pendente/rejeitado, sem perda silenciosa. PHP páginas/famílias reais funcionam em PG isolado com controles corretos; publicação remota somente se efetivamente observada.

## Escopo
### Incluído
- Faculdade/Curso/Grade/Requisito/Matéria, subtipos Pessoa compartilhando ID e associações contextuais; migrações V6+ aditivas, backfill separado, contratos T002 preservados.
- Parser literal24origens, AES-GCM fonte privada JDK conformeFDD, análise/aplicação/replay/restart e vínculos pessoais administrativos; sem rawSQL.
- Conservação livros/empréstimos/eventos/participações e dados não traduzíveis;14tabelas vazias têm destino privado declarado, não camadas/UI.
- PHP fonte2024 caracterizada e compatibilidade explícita; PDO preparado/fetch, auth/papel/objeto/CSRF em46rotas/13AJAX/5PDF/entradas físicas; uploads privados duráveis e mPDF/PHP suportados.
- Regras/gates existentes, audit/log/base e provas proporcionais de migração/backup/reversão/produção quando possível.
### Excluído
- Instituição/pessoa/cargo/senha novos por suposição, motor formação, elegibilidade por associação, vigência docente ou cursos redundantes na matrícula.
- CRUD biblioteca/quizzes/UI não solicitado, nova infraestrutura/MariaDB/frameworkPHP, sincronização bidirecional ou tradutor universal SQL.
- Endpoint de resolução Gradeambígua nesta entrega; sua pendência continua declarada e não bloqueia modelo local.
- Alegação de deploy/handshake/quota/versãoPHP2020 sem execução/prova; custo pago não autorizado.

## Dependências e bloqueios
- Cadeia/FDD e task pronta liberam produção LOCAL nos escopos definidos.
- Dados/cargos/Faculdade/arquivo ausentes: não ativar/conceder privilégio, preservar origem e pendência; dono implementador/importação.
- Chave importação32bytes Base64 necessária somente à função importação, privada e backup separado; ausência não quebra fluxos T002; dono infra/operação.
- Neon não autenticado/callable, descrição Render anuncia limitação Docker, schema contraditório e capacidade não confirmada/canal alternativo sem credencial e gratuidade não confirmada; bloqueia remoto, não implementação local; dono operação.

## Passos de implementação
1. Expandir schema/domínio/DTO/Actors/DAOs e formular cadastro novo com visual/contratos compatíveis; backfill idempotente subtipos/associações.
2. Implementar fonte privada autenticada, parser allowlist/estado Batch e origem; análise seca107classificada, aplicação por dependências e replay/restart/falha.
3. Exercitar dump privado localmente com digest/contagens apenas; não publicar linhas/PII/hash. Conferir agrupamentos Grade/Série/origem e pendências reais.
4. Adaptar PHP/PG e dependênciaPDF, guard global+objeto+CSRF, upload/download privado e queries reais; caracterizar famílias positivas/negativas e não entregar mock.
5. Rodar testes PostgreSQL/Java/React/PHP/E2E/gates proporcionais, backup/restore/reversão isolados; revisão independente, dois CI e integração.
6. Publicar PHP real Render/Neon somente com canal gratuito autenticado/TLS/gates; observar HTTPS/login/fluxos/dados persistentes ou declarar remoto não atendido.

## Critérios de aceite
- [ ] RF001: cadeia acadêmica CRUD e associaçõesIDs funcionam, FK/UNIQUE/curso-oferta coerentes; contrato Curso {nome} e regras T002 preservados.
- [ ] RF002:24origens/107registros contabilizados por digest, sem SQL bruto/PII pública; fonte confirmada anteschunk e replay/restart sem duplicação/edição sobrescrita.
- [ ] RF002: colisãoIDs/bootstrap, Grade agrupada/ambígua, cargo/usuário inexistente, Série literal e constraints testados sem pessoa/instituição fictícia.
- [ ] RF003: dados biblioteca/eventos/participações conservados privadamente sem CRUD/UI/motor não requerido; vazios/rejeições explicitamente rastreados.
- [ ] RF004: PHP páginas/rotas/famílias reais PG funcionam; acesso direto/owner/CSRF/SQL/upload/PDF protegidos e versão/deps justificadas; health/login sozinho não é aceite.
- [ ] RF004 remoto: URL HTTPS Render/Neon e persistência/fluxos reais observados, ou requisito remoto parcial com bloqueio factual.

## Testes obrigatórios
- [ ] Integração positiva/400/401/403/404/409/CSRF por endpoint novo/alterado, fixtures idempotentes e limpeza; suítes/E2E T002 intactas.
- [ ] Parser adversarial/função/subquery/DDL desconhecido/escapes/limite rejeitados sem efeito colateral; prepared statements não recebem SQL da fonte.
- [ ] AES-GCM nonce/tag/AAD/digest/chave adulterados falham sem plaintext; snapshot fixo persiste apesar rollback e fonte externa removida/alterada; replay/concorrência estados corretos.
- [ ] Importação privada107: contagens/fases/motivos/origem reconciliados e sem segredo/artifact; colisão/órfãos/grade/perfil explicitamente pendentes.
- [ ] PHP com três perfis sintéticos, cadastros/matrícula/notas/presença/material/PDF reais; matriz de 46 rotas, 13 AJAX, cinco PDFs e entradas diretas com ownership/CSRF/SQL e upload sem execução/GET público.
- [ ] Backup/restore/reversão isolados do novo estado/bytes/snapshot+chave privada; cloud/TLS somente se executados.

## Evidências obrigatórias
- [ ] Logs/JUnit não vazios/builds/gates reais e contagens saneadas publicados pelo pipeline e ligados ao PR; jamais dump/PII/credenciais.
- [ ] Imagem/vídeo de testes de endpoint e navegador desktop/mobile com login/fluxo real, revisados independentemente.
- [ ] Relatório de digest/24 tabelas e 107 classificados/replay e limites explícitos, preservando falhas/retrabalho sem falsas conclusões.
- [ ] Publicação URL/SHA/health/HTTPS/dados persistentes quando efetiva, ou bloqueio factual parcial.

## Documentação e Obsidian
- Arquivos existentes de governança/roadmap/épico/PRD/HLD/ADRs/FDD/task/lib/continuidade/pendências; AUDIT_REPORT.json e EXPERIMENT_LOG.md obrigatórios.
- Base Atena: espelhos da cadeia e uma nota por objeto SQL real, dependências quando manifestos mudarem, regras de negócio quando alteradas. Nenhum manual/QA/checklist/análise extra por conveniência.

## Riscos e lacunas
- Cargo/identidade/estado/arquivo/instituição desconhecidos: staging privado/replay e negação por default, nunca privilégios aproximados; dono importador.
- PHP schema/segurança: compatibilidade por query/caso e controles em entrada física, não apenas router; dono autor PHP, revisores independentes.
- Gestão de chave/restart: backup fora do banco e falha segura, sem fallback/framework criptográfico próprio; dono autor importação/infra.
- ❓ LACUNA: autenticação/capacidade/gratuidade cloud; dono operação, bloqueia somente publicação remota.

## Definição de pronto
- [ ] Código cumpre cadeia e critérios ou declara não atendidos/status parcial sem sucesso fictício.
- [ ] Testes/provas executados e evidências observadas; código escrito claramente separado de validado.
- [ ] Audit/log/estado/Base atualizados e inventário factual de abstrações/dependências, sem IDs/métricas externas inventados.
- [ ] PR auditado por agente diferente de cada autor e duas pipelines verdes no HEAD integrado.
- [ ] Deploy real comprovado ou requisito explicitamente aberto/parcial, sem outra tecnologia/pagamento não autorizado.

## Histórico
- 2026-10-05: task criada pronta somente após plano/FDD independentemente revisados e ajustes aplicados. Nenhuma implementação/build/importação/deploy T003 validado nesta abertura; fonte privada analisada e provas documentais conferidas.

- 2026-10-05: autores confirmaram início incremental nos escopos separados; faculdades/grades/requisitos escritos em transição, sem compilação/teste T003 observados. Dependências PHP propostas registradas antes adoção; reviewer permanece ativo e não escreve produção.

- 2026-10-05: marco atual de dois testes puros Maven e QA independente do parser/envelope executados. Compilação inicial falhou por import ausente; corrigida antes dos testes verdes. Build PHP falhou download; Docker local negado e Base externa readonly permanecem limitações. As provas completas e os critérios de pronto continuam abertos.

- 2026-10-06: retomada com Docker/Base acessíveis; 63 notas SQL, dependências, duas regras e cadeia conferidas. Runner PHP backend passou etapas sintéticas de autenticação/perfis/PDF/materiais/avatar/restart/restore, antes falha ambiental navegador; XSS em lote e suíte Java ainda em validação. Sem novos CI, integração, deploy ou importação da fonte107 original.

- 2026-10-06: parecer independente PHP favorável somente a draft parcial após fonte/loteXSS e evidências finais SUCCESS;146lint e3perfis/teclado/desktopmobile conferidos. Tabela mobile cortada/medição1070vs390 impede declaração responsiva plena. Java tem 11 últimos resultados verdes por suíte+focal, E2E React e resumo de restore final conferidos. Nenhum aceite integral, merge, deploy ou importação original107 presumido.

- 2026-10-06: E2E React final passou em Java 21/PostgreSQL reais, cobrindo cadastro visual da cadeia acadêmica, associação/remover matéria e importação sintética, desktop 1440/mobile 390 sem overflow global. Restauração escolar isolada com nove migrações e snapshot AES-GCM confirmado no resumo do autor, chave externa. Revisão independente favorece draft parcial; fonte original, tradução PHP completa, responsividade PHP plena, reversão nova integral, fechamento documental/integração e produção continuam abertos.

- 2026-10-06: [PR draft #5](https://github.com/mclovin137/Atena-refatorado/pull/5) publicada em `7f0642899c5659b47821630319255d3207636486`; [review CI](https://github.com/mclovin137/Atena-refatorado/actions/runs/37462886067) e [testes CI](https://github.com/mclovin137/Atena-refatorado/actions/runs/37462885838) SUCCESS no mesmo SHA. Revisor conferiu XML remotos: 15 casos (11 escolares e quatro de relatórios), zero falhas/erros/skips; E2E React remoto PASSOU com quatro capturas e pipeline PHP passou. Resultado local por duas execuções não é confundido com a execução remota integral. Parecer continua restrito a draft parcial; nenhuma integração/produção ou conclusão dos critérios de dados/mobile/cloud inferida.
