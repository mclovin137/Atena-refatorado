---
type: minerva-adr
project: Atena
date: 2026-10-05
status: aceita
tags: [atena, adr]
ai-first: true
---

# ADR 007: Runtime PHP legado isolado sobre PostgreSQL

## Para o futuro agente
Proposta originada pelo pedido T003. Não autoriza produção antes revisão da cadeia/FDD nem declara deployment; autonomia substitui consultas humanas previstas no skill, não a revisão independente.

## Contexto
Usuário pede publicar sistema legado real além do refatorado. Isso excepciona backend Java somente para PHP legado; não autoriza nova infraestrutura MariaDB. Código preservado tem PDO MySQL/dialeto/SQL interpolado/PDF e upload frágeis, e usa outro schema que o dump. Trocar DSN não produz compatibilidade.

## Escopo
- Afetado: PRD003/HLD T003 e implementação futura.
- Fora de escopo: infraestrutura paga, MariaDB runtime, reescrita dos dois serviços ou descarte silencioso do dump.

## Forças e critérios
- Pedido explícito atendido, vínculos/segurança comprovados e fonte privada preservada; custos zero, replay/rollback auditáveis e contratos anteriores caracterizados.

## Opções consideradas
### Opção 1: publicar PHP preservado sem alterações
- Status na análise: descartada.
- Benefícios: interface original imediatamente preservada.
- Custos e limitações: DSN/schema/dialeto incompatíveis com Neon; falhas de segurança observadas.
- Riscos: vazamento/execução de upload/SQL inject e login quebrado.
- Trade-off: aparência rápida sem funcionamento seguro.
- Evidências: legacy/Models/MySql.php, HomeMolde.php, MainMolde.php e endpoints ajax/PDF.

### Opção 2: Docker PHP isolado com PDO PostgreSQL e compatibilidade explícita
- Status na análise: candidata.
- Benefícios: executa páginas/casos reais, mantém PostgreSQL e Java existentes; versão fonte fixada.
- Custos e limitações: schema dedicado/role limitada e correções por rota; busca de fonte correspondente e caracterização precedem publicação.
- Riscos: preservação de hábitos inseguros e sincronização ilusória se bases distintas forem ocultadas.
- Trade-off: legado real possível, custo de compatibilidade e duas implantações claramente independentes sem sincronização prometida.
- Evidências: pedido novo autoriza PHP; infraestrutura Docker/PG já obrigatória e descrição Render MCP anuncia limitação Docker, mas schema runtime contradiz e capacidade não foi confirmada.

### Opção 3: publicar somente React e chamar isso de legado
- Status na análise: descartada.
- Benefícios: usa runtime já validado.
- Custos e limitações: não entrega interface PHP pedida.
- Riscos: falsa declaração de requisito atendido.
- Trade-off: simplicidade troca escopo solicitado.

## Decisão
Proposta opção2, com exceção explícita restrita à publicação PHP real. Operação analisou histórico disponível: dez commits2024, primeiro5306ac2 já usa alunos2/professo2/secretaria2; código2020 correspondente não encontrado. FDD deve documentar tradução explícita e mapear domínios da outra versão sem alegar paridade de rotas inexistentes. mPDF8.0.0 incompatível com PHP suportado exige atualização justificada da dependência existente e teste PDF real. Antes exposição pública corrigir auth/papel/objeto/CSRF/queries parametrizadas/uploads/PDF e versão dependências. Schema/role PHP separado sem escrita concorrente na base normalizada; mesma fonte privada pode alimentar ambos por importação auditada. Deploy só com canal autenticado/custo gratuito comprovados.

## Consequências
### Positivas
- Interface legada é efetivamente exercitada e Java/React preservados; não introduz MariaDB ou framework PHP novo.
### Negativas
- PHP é exceção de runtime registrada; duas bases/modos exigem transparência e não sincronizam automaticamente. Não há prova cloud atual.
### Riscos residuais
- Estado/cargo/órfãos e autenticação cloud desconhecidos impedem declaração de conclusão; análise/FDD/gates tratam cada risco sem preencher dados inventados.

## Impacto nos artefatos
- PRD003, HLD T003, FDD futuro, roadmap/épico/task após aprovação, docs/lib quando dependência mudar e notas SQL após DDL real.

## Relações
- Origem: [PRD003](../prds/prd-003-modelo-legado.md).
- ADRs relacionadas: [ADR002](adr-002-stack.md), [ADR004](adr-004-organizacao-backend.md), [ADR005](adr-005-producao-render-neon.md).
- Substitui: nenhuma; exceção delimitada quando aceita.
- Substituída por: nenhuma.

## Critério para revisitar
Inviabilidade gratuita/segura comprovada, fonte correspondente ausente ou perda de contrato/dado observada exige revisão explícita, sem trocar tecnologia silenciosamente.

## Aceite
- Responsável arquitetural: Yoda planejador nesta thread.
- Decisor: pedido do usuário fixa resultado e autonomia; solução revisada independentemente por operação e implementador, com aceite arquitetural do planejador dentro da autonomia do pedido.
- Evidência do aceite: novo pedido T003 forneceu modelo/dump e publicação PHP; detalhes estruturais revisados e aceitos para etapa local nesta data; nenhuma migração/deploy executado.

## Histórico
- 2026-10-05: proposta criada; nenhuma implementação/migração/deploy T003 executada.

- 2026-10-05: decisão aceita para implementação local após revisão cruzada independente do plano/FDD e autonomia explícita do pedido. Não é aprovação de deployment; limites/condições de cloud permanecem.

## Adendo: retomada autorizada da publicação e responsividade PHP — 2026-10-06

O usuário autorizou executar os passos de deploy de ambos. A fonte histórica ausente impede concluir importação/reconciliação RF002–003 e disponibilizar dados históricos; não impede publicar aplicações reais em bases inicialmente vazias com bootstrap administrativo privado. Fixtures sintéticas pertencem à validação, são identificadas e limpas; nenhuma credencial de demonstração ou histórica fica ativa na publicação. Escolar, relatórios e PHP permanecem isolados, sem sincronização automática. A autorização do usuário não dispensa revisão independente, dois CI verdes, canal autenticado, plano gratuito e proteção contra excedente cobrado.

Contrato responsivo específico do PHP, preservando sua identidade visual e fluxos, sem aplicar automaticamente tokens React: desktop >=1024 px mantém sidebar/layout; 768–1023 px usa drawer existente com conteúdo ocupando largura útil; 320–767 px inicia drawer fechado, organiza formulários/ações em coluna e mantém labels/conteúdo. Drawer deve operar por teclado, fechar com Escape e devolver foco ao controle de abertura, sem overflow do documento nem conteúdo inacessível. Tabelas mantêm todas as colunas/ações com rolagem horizontal LOCAL em contêiner nomeado e focável; não esconder dados para satisfazer largura. Canvas adapta dimensões à área útil mantendo dados, eixos e legenda legíveis, sem corte ou escala ilegível. Meta viewport correta permite zoom do usuário; proibir zoom e usar zoom CSS não são estratégias responsivas. Verificar 320/390/768/1024/1440 px, login e os três perfis, navegação teclado, formulário, tabela, PDF/upload; nenhuma rolagem horizontal global.

Responsável decisório: revisor_deploy/Yoda. Revisão independente do contrato: operacao_deploy/Jarvis confirmou as faixas, preservação das colunas/ações, Escape/restituição de foco e integridade dos gráficos nesta rodada, antes implementação. Aplicação implementa e revisores distintos conferem código/provas; este aceite é de desenho, não de execução nem publicação.

CSP Java já está configurada; CSP PHP não deve romper scripts legados nem ser declarada efetiva sem navegador. Proteção de tentativas de login deve ser contextual e sem nova infraestrutura: janela limitada, chave não burlável por trocar apenas a sessão, erros sem identificação de conta e limites de memória/expurgo. Risco residual é registrado se mecanismo não for comprovado; headers isolados não substituem escaping/CSRF/autorização. Rollback preserva DDL aditivo e exige prova compatível de aplicação/restore antes declarar reversão validada; nenhum DOWN destrutivo é autorizado.

Proteção mínima aprovada para login público: no máximo20 POST login/minuto por REMOTE_ADDR; Java usa mapa em memória com cardinalidade/expurgo limitados; PHP usa diretório temporário privado700, arquivo600 contendo mapa limitado por digest do endereço, flock e janela/expurgo, compartilhados pelos workers da única instância. A chave não depende de cookie/sessão nem de X-Forwarded-For controlável. Excesso recebe429 genérico e Retry-After antes BCrypt; teste com CSRF válido ultrapassa limite e troca cookie sem resetar; janela é verificada por relógio controlável sem espera longa. Conta existente e ausente recebem mesmo comportamento; nenhuma nova infraestrutura/DDL/lib é adotada. Trade-offs: proxy pode agrupar clientes e causar limite compartilhado, reinício perde contadores e ataques distribuídos continuam possíveis. Erro de arquivo privado/inseguro falha seguramente; não seguir symlink nem expurgar arquivos de outro aplicativo. Autor aplicação revisou proposta e operação revisa decisão/texto independentemente antes fechamento.
