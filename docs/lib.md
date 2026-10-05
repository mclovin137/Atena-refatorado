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
| Playwright | 1.61.1 | Ferramenta temporária de evidência navegador real desktop/mobile; não dependência app |
| GitHub Actions checkout/setup-python/setup-java/setup-node/upload-artifact | v4/v5/v4/v4/v4 | Dois papéis pipelines Minerva e publicação evidência, runners padrão sujeitos à franquia gratuita da conta, sem contratação de excedentes |
| google-java-format | 1.25.2 | Ferramenta temporária de formatação/gate Java legível T-002; Spring não formata código. Alternativa: apenas formatação manual, descartada por falta de gate reprodutível. Nenhuma dependência runtime. |
| Prettier | 3.9.9 (MIT) | CLI temporária para formatar/verificar JSX, CSS, HTML e JSON T-002; Spring/google-java-format não cobrem essas fontes. Alternativa manual sem gate descartada; não instalada no runtime nem manifesto da aplicação. |
| Python | 3 | Ferramenta de preparação/env/gates locais já disponível, ausente runtime |

Não há Redis, Kafka, brokers, Elasticsearch, Kubernetes, H2, Testcontainers ou bibliotecas de UI. Bibliotecas PHP originais ficam em legacy/ somente referência; não são instaladas nem executadas pela stack nova.

T-002 usa a skill Render Blueprints e as referências Docker/env-vars para preparar `render.yaml`, sem criar infraestrutura. A estrutura foi verificada localmente com o schema oficial https://render.com/schema/render.yaml.json e PyYAML/jsonschema já disponíveis no ambiente; isso não valida credenciais, TLS, orçamento nem implantação. CLI/API de validação Render não estão autenticadas/disponíveis. `SERVICE` é argumento de build não secreto admitido pelo Dockerfile; credenciais não são argumentos consumidos durante o build. O perfil `prod` usa JDBC `verify-full` e `org.postgresql.ssl.DefaultJavaSSLFactory` para verificar certificado e hostname com o truststore Java, conforme https://jdbc.postgresql.org/documentation/ssl/ . Nenhum handshake Neon foi executado.
