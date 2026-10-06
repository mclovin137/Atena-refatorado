---
type: minerva-adr
project: Atena
date: 2026-10-04
status: aceita
tags: [atena, adr]
ai-first: true
---

# ADR 004: Organização por recurso, domínio puro e factories concretas

## Para o futuro agente
Organização por recurso/função segue MinervaFinancas: REST com DTO, Actor coordenador, Service/domínio puros e DAO adaptador. Factories exigem variantes ou invariantes reais; contratos Atena permanecem. Consulte a Decisão para nomenclatura/OC e revise fronteiras antes alterar qualquer recurso.

## Contexto
Categoria: ADR. Pergunta: Como inverter dependências e remover regras/persistência dos contratos sem alterar comportamento? Documentos anteriores: [PRD-002](../prds/prd-002-qualidade-producao.md) e [HLD qualidade](../hlds/hld-qualidade-producao.md). Dono: Yoda; revisores independentes da redação: implementadores escolar/relatórios. RFC/LLD dispensados: requisito/decisão técnica e contrato FDD cobrem o escopo, sem alternativa de produto não resolvida.

O gatilho é mudança sistêmica da direção de dependência e responsabilidade REST/JPA→domínio puro/contratos tipados, não mera organização de arquivos. Usuário identificou código comprimido e difícil de ler, SOLID/factories/Object Calisthenics insuficientes e pediu organização/nomenclatura/playbooks reais de MinervaFinancas. Referência pinada https://github.com/Cris042/MinervaFinancas/tree/34fc98a6ac47f2ffc2bf23f055a3fd9ac15c7530; seções A–C e ADR003 consultadas. Não copiar stack SQLite/Boot4, OpenAPI/i18n/paginação ou novas regras de negócio de outro produto.

## Escopo
- Afetado: Substitui organização técnica global do HLD/FDD-001 para trabalho T-002; preserva ADR001/002/003 e schema/contratos salvo persistência de arquivos ADR005; PRD-002, HLD qualidade, FDD-002 e task futura.
- Fora de escopo: novos serviços, novas regras escolares, infraestrutura não autorizada e cobrança.

## Forças e critérios
- Preservar comportamento, contratos e segurança validados em T-001; distinguir decisão, implementação e execução.
- Atender requisito explícito sem abstração especulativa; dependências mínimas e custo financeiro zero.
- Autoria arquitetural Yoda; revisão independente necessária e nunca aprovação da própria implementação.

## Opções consideradas

### Opção 1: Estrutura por recurso da referência
- Status na análise: escolhida
- Benefícios: Agrupa mudança por domínio e torna direção de dependência visível.
- Custos e limitações: Migração ampla de nomes/mappers e testes.
- Riscos: DTO pode perder default/campo e autorização pode regredir.
- Trade-off: Aceita mais arquivos com responsabilidade real e preserva comportamento.
- Evidências: MinervaFinancas ADR003 e playbook A–C no remoto34fc98a; Atena services/escolar/web/{Access,Invariants,BoletimController,NotaController}.java e serviços relatorios/ReportController.java.

### Opção 2: Só expandir formatação do código atual
- Status na análise: descartada
- Benefícios: Menor diff.
- Custos e limitações: Mantém regras em controller/entidades nos contratos e políticas genéricas.
- Riscos: Legibilidade melhora superficialmente.
- Trade-off: Reduz esforço sem resolver pedido estrutural.
- Evidências: Arquivos Java citados com imports/métodos comprimidos, regras em controllers e instanceof em Access/Invariants; expandir formato não desloca responsabilidade.

### Opção 3: Framework genérico de CRUD e factories para tudo
- Status na análise: descartada
- Benefícios: Reduz repetição aparente.
- Custos e limitações: Reflexão e indireção escondem regras.
- Riscos: Mass assignment e fronteiras invisíveis.
- Trade-off: Menos linhas com maior acoplamento não atende SOLID.
- Evidências: Access/Invariants atuais mostram seleção central por tipo; Bean Validation e JPA explícitos já disponíveis, sem necessidade de framework CRUD.

## Decisão
Pacotes br.com.atena.<contexto>.<recurso>.{rest,actor,service,builder,dao,helper,dto,dominio}; comum só para colaboração efetiva. REST traduz DTO e delega Actor; Actor coordena caso de uso e portas; Service concentra regra pura; DAO adapta persistência/JPA ou integração, sem regras de negócio; Builder mapeia domínio/DTO sem I/O. Portas I<Recurso>DAO não importam Spring/JPA. JPA fica em dao/jpa; domínio puro protege invariantes. Interfaces prefixo I; DTO/Enum e testes IntegracaoTeste/Teste; nomes completos pt-BR por intenção. JSON/rotas/status existentes preservados com mapeamento explícito. Factory de estratégias só com duas implementações concretas (PDF/CSV/JSON, autorização por perfil); Factory de criação somente invariantes/agregados reais. OC exige baixa indentação, coesão, nomes e encapsulamento. DTOs têm exceção explícita às regras8/9 (campos/acessores) conforme referência. Premissa: o playbook Minerva canônico define disciplina não dogma; não declaramos cumprimento literal automático das nove regras Financas. Qualquer dispensa concreta, inclusive quantidade de campos/colaborações de agregado ou adapter framework, deve indicar classe/regra/motivo/trade-off na auditoria e passar review; sem justificativa, corrigir. Evitar wrappers/classes inúteis não dispensa coesão ou invariantes. Nenhum CRUD/reflection/instanceof genérico substitui fronteiras tipadas. Fonte multilinha com imports/métodos legíveis; formato verificado no review.

## Consequências

### Positivas
- Regras e contratos ganham nomes e responsabilidade explícitos.
- Domain services e factories podem ser testados sem framework.

### Negativas
- Mudança estrutural ampla pode esconder regressão de campos/defaults.

### Riscos residuais
- Mapeamento explícito e fronteiras requerem testes de caracterização existentes e inspeção independente.

## Impacto nos artefatos
- Substitui organização técnica global do HLD/FDD-001 para trabalho T-002; preserva ADR001/002/003 e schema/contratos salvo persistência de arquivos ADR005; PRD-002, HLD qualidade, FDD-002 e task futura.
- RFC e LLD dispensados: não há disputa de produto nem implementação alternativa não resolvida; HLD/ADR/FDD cobrem estrutura e contratos. Isto não dispensa lacunas remotas.

## Relações
- Origem: [PRD-002](../prds/prd-002-qualidade-producao.md), pedido do experimento e auditoria T-002; [HLD qualidade](../hlds/hld-qualidade-producao.md).
- ADRs relacionadas: [fronteiras](adr-001-fronteiras.md), [stack](adr-002-stack.md), [interface](adr-003-interface.md), [produção](adr-005-producao-render-neon.md).
- Substitui: nenhuma ADR; detalhes técnicos anteriores afetados são indicados acima.
- Substituída por: nenhuma.

## Critério para revisitar
Revisitar se ciclos por recurso, duplicação ou factories decorativas aparecerem; review deve simplificar em vez de legitimar abstração.

## Aceite
- Responsável arquitetural: Yoda (agente planejador/revisor).
- Decisor: usuário nas escolhas explícitas; autonomia delegada no escopo remanescente. Decisão aceita no escopo autorizado, execução nova ainda não validada.
- Evidência do aceite: Pedido novo do usuário determina MinervaFinancas como referência e factories/SOLID/Object Calisthenics; decisões menores simples dentro da autonomia original. Não há afirmação de revisão independente da implementação ainda inexistente.
- Revisão independente da redação: implementadores escolar/relatórios em 2026-10-04, com ajustes de origem/evidências/links aplicados. Isto não aprova sua implementação futura nem declara deploy executado.

## Histórico
- 2026-10-04: ADR criada para o pedido T-002.

## Adendo: dispensa técnica específica de calistenia — 2026-10-06

Object Calisthenics permanece aplicado parcialmente; esta decisão não certifica as nove regras, não transforma SOLID/formatação em calistenia e não autoriza dispensas gerais para domínio, frontend ou PHP. A pergunta atual do usuário não solicita reescrever toda a aplicação para cumprimento literal. Desvios existentes do parser, ator de relatórios, factory, tipos primitivos e coleções do domínio permanecem explícitos na auditoria, com o estado factual de implantação separado.

Classe afetada: `br.com.atena.escolar.sessao.rest.LimitarLoginFiltro`, incluindo seu estado técnico privado `Janela`. Dispensa contextual das regras3/4/5/8: IP/tempo/contador/status HTTP são valores do adaptador Servlet, não conceitos do domínio escolar; o mapa de janelas é privado, limitado por capacidade e expirado, com atualização sincronizada; relógio e capacidade permitem política de janela e prova determinística, formando três campos necessários ao mesmo mecanismo. A cadeia `response.getWriter().write(...)` escreve a resposta pela API Servlet e não navega um agregado de domínio. O filtro não expõe o mapa nem seus contadores.

Motivo/trade-off: preservar mecanismo técnico pequeno e verificável sem criar objetos de domínio para IP/HTTP, wrappers de coleção/configuração ou objetos intermediários sem comportamento próprio apenas para reduzir contagem de campos/pontos. Aceita-se a violação literal desses quatro exercícios no adaptador; não se dispensa limite de memória, concorrência, CSRF, encapsulamento ou revisão de comportamento. Reinício perde contadores; proxy pode agrupar clientes e ataques distribuídos permanecem possíveis, riscos já registrados.

Responsável decisório: revisor_deploy/Yoda, distinto do implementador. A auditoria identifica classe/regras/motivo/trade-off e deve receber revisão documental independente pela operação. A integração continua condicionada aos dois CI verdes no HEAD correto; falha funcional do HEAD2a80f8d não é suprimida por esta dispensa. Nenhuma mudança de produção foi feita por esta decisão.
