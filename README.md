# Atena

Sistema escolar refatorado em dois serviços Java 21/Spring Boot: escolar (identidade, cadastros e ensino) e relatórios (Spring Batch e PDFs). Frontend React servido pelo serviço escolar; PostgreSQL com bancos separados e Docker Compose. Fontes PHP originais ficam em `legacy/`, fora da execução nova.

Para executar localmente, instale Docker/Compose e Python 3 e rode na raiz:

```bash
./scripts/preparar-ambiente.sh
docker compose up --build -d
```

Abra http://127.0.0.1:18080. O primeiro acesso usa `ADMIN_EMAIL` e `ADMIN_PASSWORD` do `.env` local, gerado com credenciais aleatórias e ignorado pelo Git. Consulte esse arquivo no seu ambiente; nunca o publique. Para encerrar mantendo dados, use `docker compose down`.

Os volumes preservam PostgreSQL e materiais enviados. Não execute `down -v` se quiser manter esses dados. O ambiente publicado fica restrito ao próprio computador; nenhum deploy público é presumido.

Validação local: `./scripts/revisar.sh` e `./scripts/testar.sh`; testes exigem Java 21+, Maven e Node 24 além de Docker. Os dois pipelines no GitHub publicam evidências de revisão e testes/navegador. Decisões e critérios estão em [docs/roadmap.md](docs/roadmap.md), [docs/prds/prd-001-refatoracao.md](docs/prds/prd-001-refatoracao.md) e [docs/hlds/hld-refatoracao.md](docs/hlds/hld-refatoracao.md); resultados efetivamente executados estão em [EXPERIMENT_LOG.md](EXPERIMENT_LOG.md).

O repositório original não contém dump de banco: importação de dados reais não foi validada. O layout PDF foi simplificado; a regra de frequência/hora-aula corrige bugs do PHP conforme registrado no experimento. Snapshot completo em memória e consultas na origem limitam escala; não houve teste de carga.
