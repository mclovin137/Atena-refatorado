# Inventário de dependências

Versões verificadas nos metadados Maven Central/npm e BOM Spring Boot. Status: adicionadas; execução registrada somente EXPERIMENT_LOG/evidência de testes. Stack obrigatória e recursos Spring priorizados. Dependências transitivas são geridas pelo BOM, não escolhas independentes.

| Dependência | Versão | Finalidade / justificativa |
|---|---|---|
| Java / Eclipse Temurin | 21 | Backend obrigatório; runtime e imagem de build open source |
| Spring Boot parent/plugin | 3.5.16 | APIs REST e gerenciamento coerente de dependências Spring |
| spring-boot-starter-web | 3.5.16 | REST, JSON/Jackson e React estático sem infraestrutura adicional |
| spring-boot-starter-data-jpa | 3.5.16 | Persistência Spring Data obrigatória, entidades por contexto |
| spring-boot-starter-validation | 3.5.16 | Bean Validation obrigatória em inputs |
| spring-boot-starter-security | 3.5.16 | Preserva login/perfis, sessões, BCrypt, autorização e CSRF (apenas escolar) |
| spring-boot-starter-batch | 3.5.16 (Batch 5.2.6) | Lotes pesados com chunks, transações e metadados persistentes |
| spring-boot-starter-actuator | 3.5.16 | Health e métricas Micrometer exigidas |
| spring-boot-starter-cache | 3.5.16 | Catálogos com Spring Cache memória e invalidação (apenas escolar) |
| micrometer-tracing-bridge-brave | 1.5.12 (BOM) | Tracing HTTP/correlação local sem coletor ou infraestrutura adicional |
| flyway-core / flyway-database-postgresql | 11.7.2 | Migrações reprodutíveis dos dois bancos e schema oficial Batch |
| PostgreSQL JDBC | 42.7.11 (BOM) | Driver para PostgreSQL obrigatório |
| PDFBox | 3.0.8 | Preserva PDFs legados; Spring não fornece gerador PDF, somente relatorios |
| spring-boot-starter-test | 3.5.16 | Integração e assertions, ferramenta padrão Spring exigida |
| spring-security-test | 6.5.11 (BOM) | Exercita CSRF, sessão e perfis reais nos testes de endpoints |
| React / react-dom | 19.2.4 | Frontend obrigatório, sem bibliotecas de componentes/estado adicionais |
| Vite / esbuild transitivo | 7.3.6 / 0.28.2 | Empacota JSX/React em assets estáticos; somente ferramenta de build |
| Node | 24 (imagem node:24-alpine) | Executa build frontend, ausente no runtime |
| Maven | 3.9 (imagem maven:3.9-eclipse-temurin-21) | Build Java, ausente no runtime |
| PostgreSQL | 17 (imagem postgres:17-alpine) | Única infraestrutura de dados permitida, dois bancos/users isolados |
| Docker / Compose | ferramentas locais | Infraestrutura obrigatória para orquestração local |
| Gitleaks | 8.30.1 | Gate de segredos CI/local exigido revisão de segurança, ferramenta externa |
| Playwright | 1.61.1 CI; 1.63.0 local PHP | Ferramenta temporária de evidência navegador real desktop/mobile; não dependência app |
| GitHub Actions checkout/setup-python/setup-java/setup-node/upload-artifact | v4/v5/v4/v4/v4 | Dois papéis pipelines Minerva e publicação evidência, runners padrão sujeitos à franquia gratuita da conta, sem contratação de excedentes |
| google-java-format | 1.25.2 | Ferramenta temporária de formatação/gate Java legível T-002; Spring não formata código. Alternativa: apenas formatação manual, descartada por falta de gate reprodutível. Nenhuma dependência runtime. |
| Prettier | 3.9.9 (MIT) | CLI temporária para formatar/verificar JSX, CSS, HTML e JSON T-002; Spring/google-java-format não cobrem essas fontes. Alternativa manual sem gate descartada; não instalada no runtime nem manifesto da aplicação. |
| Python | 3 | Ferramenta de preparação/env/gates locais já disponível, ausente runtime |

Não há Redis, Kafka, brokers, Elasticsearch, Kubernetes, H2, Testcontainers ou bibliotecas de UI. Na entrega T001/T002, bibliotecas PHP preservadas em legacy eram somente referência. T003 autoriza runtime PHP adicional separado; dependências propostas/efetivas abaixo não são adotadas pela stack Java.

T-002 usa a skill Render Blueprints e as referências Docker/env-vars para preparar `render.yaml`, sem criar infraestrutura. A estrutura foi verificada localmente com o schema oficial https://render.com/schema/render.yaml.json e PyYAML/jsonschema já disponíveis no ambiente; isso não valida credenciais, TLS, orçamento nem implantação. CLI/API de validação Render não estão autenticadas/disponíveis. `SERVICE` é argumento de build não secreto admitido pelo Dockerfile; credenciais não são argumentos consumidos durante o build. O perfil `prod` usa JDBC `verify-full` e `org.postgresql.ssl.DefaultJavaSSLFactory` para verificar certificado e hostname com o truststore Java, conforme https://jdbc.postgresql.org/documentation/ssl/ . Nenhum handshake Neon foi executado.

## T003: PHP legado adicional

Status em 2026-10-06: lock/audit Composer conferidos; imagem PHP construída e runner real PostgreSQL executou login três perfis, PDFs positivos, uploads/avatar BYTEA, reinício e pg_dump/restore com bytes/autorização. Artefatos legado-fluxo.json e legado-navegador.json finais SUCCESS foram conferidos, assim como 146 lint sem erro. XSS em lote corrigido e navegador em Docker passou com três perfis/teclado/desktop/mobile; tabela mobile cortada permanece limite explícito. Falhas anteriores de ambiente/seletor foram corrigidas e preservadas no log. CI/cloud continuam pendentes. A falha anterior de download é histórica.

| Dependência | Versão proposta | Finalidade e alternativa considerada |
| --- | --- | --- |
| PHP/Apache imagem oficial | php:8.4-apache | Executa páginas PHP reais em runtime mantido; [ciclo oficial](https://www.php.net/supported-versions.php) prevê suporte de segurança8.4 até2028. Alternativa PHP8.0 descartada por fim de suporte; Java/React não executam fonte PHP. Exceção ADR007, sem framework novo. |
| PDO pgsql | extensão da versão PHP adotada | Conecta PHP ao PostgreSQL obrigatório com statements preparados; PDO MySQL não conecta Neon e MariaDB runtime não autorizado. |
| gd/mbstring/calendar | extensões da versão PHP adotada | [mPDF exige ambas](https://mpdf.github.io/about-mpdf/requirements-v7.html); GD também valida/reencoda avatar; calendar atende cal_days_in_month já chamado pelo legado. Usar extensões nativas, não nova biblioteca de imagem. |
| mPDF | 8.3.1, lock resolvido | Atualização da dependência já existente8.0.0 para PDFs reais em PHP suportado; PDFBox/Spring não executam templates PHP. Lock/transitivas resolvidos abaixo; templatesPDF/runtime ainda precisam teste. |
| Composer | 2, somente build; imagem/versionamento exato pendentes | Resolve lock e instala dependências PHP existentes no build, ausente do runtime. Alternativa vendor antigo preservado não atende PHP8.4; nenhum pacote novo por arquitetura. |

AES-GCM/digest da importação usam JDK já inventariado, sem nova dependência; nonce/tag/AAD/chave privada/backup são contrato FDD003, não framework criptográfico próprio. PHP Compose será opt-in com PostgreSQL/role próprios, porta local18082 loopback; configuração escrita não equivale a runtime/deploy validado.

Lock PHP T003 resolvido: mPDF8.3.1; transitivas já existentes atualizadas myclabs/deep-copy1.14.0, paragonie/random_compat9.99.100, psr/log3.0.2, setasign/fpdi2.6.8. Transitivas adicionais requeridas pelo mPDF: mpdf/psr-http-message-shim2.0.1, mpdf/psr-log-aware-trait3.0.0 e psr/http-message2.0. Justificativa: abstrações PSR usadas internamente pela biblioteca de PDF já existente; não introduzem framework/fila/backend novo nem são escolhas independentes do produto. Alternativa manter mPDF8.0.0 não suporta runtimePHP8.4 escolhido; reescrever templates no PDFBox não executa PHP legado. Autor observou Composer update/audit exit0 com no advisories; ignorou extensões somente na resolução, build deve comprovar extensões reais. Nenhuma prova PDF/build derivada do lock.

- 2026-10-06: navegador PHP local usa módulo temporário Playwright1.63.0 e imagem existente mcr.microsoft.com/playwright:v1.63.0-noble para resolver libasound ausente no host sem modificá-lo. CI mantém Playwright1.61.1 pinado e instala dependências no runner. Nenhuma dependência runtime da aplicação foi adicionada; resultados das execuções são registrados separadamente.

- 2026-10-06: E2E React acadêmico também executado com Playwright temporário 1.63.0 em Docker, contra Java 21 real. Sanitizador de evidências usa somente Python stdlib já inventariado; nenhuma dependência runtime nova.
