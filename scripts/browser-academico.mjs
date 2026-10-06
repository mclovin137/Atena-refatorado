import { mkdir, writeFile, readFile } from "node:fs/promises";
import { createRequire } from "node:module";
import { randomUUID } from "node:crypto";
import assert from "node:assert/strict";
const require = createRequire(import.meta.url);
const { chromium } = require(process.env.PLAYWRIGHT_MODULE || "playwright");
let configuracao = {};
if (!process.env.ADMIN_EMAIL || !process.env.ADMIN_PASSWORD) {
  const texto = await readFile(".env", "utf8");
  configuracao = Object.fromEntries(
    texto
      .split("\n")
      .filter((linha) => linha.includes("="))
      .map((linha) => {
        const indice = linha.indexOf("=");
        return [linha.slice(0, indice), linha.slice(indice + 1)];
      }),
  );
}
const base =
  process.env.ATENA_BROWSER_URL ||
  `http://127.0.0.1:${process.env.ATENA_PORT || configuracao.ATENA_PORT || "18080"}`;
const email = process.env.ADMIN_EMAIL || configuracao.ADMIN_EMAIL;
const senha = process.env.ADMIN_PASSWORD || configuracao.ADMIN_PASSWORD;
assert.ok(
  email && senha,
  "Defina credenciais sintéticas privadas para o navegador",
);
const navegador = await chromium.launch({ headless: true });
const pagina = await navegador.newPage({
  viewport: { width: 1440, height: 900 },
});
const criados = [],
  provas = [],
  erros = [];
let csrf, falha;
pagina.on("pageerror", (erro) => erros.push(erro.message));
await mkdir("evidence", { recursive: true });
async function api(caminho, method = "GET", data, esperado = 200) {
  const resposta = await pagina.request.fetch(`${base}/api/${caminho}`, {
    method,
    data,
    headers: method === "GET" ? {} : { "X-CSRF-TOKEN": csrf },
  });
  assert.equal(
    resposta.status(),
    esperado,
    `Falha ${method} ${caminho}: status ${resposta.status()}`,
  );
  return esperado === 204 ? null : resposta.json();
}
async function criar(caminho, corpo) {
  const resposta = await api(caminho, "POST", corpo, 201);
  criados.push(`${caminho}/${resposta.id}`);
  return resposta;
}
async function navegar(rotulo) {
  await pagina
    .getByRole("navigation")
    .getByRole("button", { name: rotulo, exact: true })
    .click();
  await pagina
    .getByText("Atualizando…", { exact: true })
    .waitFor({ state: "hidden" });
  assert.equal(
    await pagina.locator("main > .error").count(),
    0,
    "Navegação deve carregar referências",
  );
}
async function criarNaTela(rotulo, caminho, nome, preencher = async () => {}) {
  await navegar(rotulo);
  await pagina
    .getByRole("button", { name: "+ Novo registro", exact: true })
    .click();
  const formulario = pagina.locator(".record-form");
  await formulario.getByLabel("Nome", { exact: true }).fill(nome);
  await preencher(formulario);
  const resposta = pagina.waitForResponse(
    (resposta) =>
      resposta.url().endsWith(`/api/${caminho}`) &&
      resposta.request().method() === "POST",
  );
  await formulario
    .getByRole("button", { name: "Salvar registro", exact: true })
    .click();
  const recebido = await resposta;
  assert.equal(recebido.status(), 201, `Cadastro visual ${caminho}`);
  const registro = await recebido.json();
  criados.push(`${caminho}/${registro.id}`);
  await formulario.waitFor({ state: "hidden" });
  return registro;
}
async function captura(nome) {
  const arquivo = `evidence/${nome}.png`;
  await pagina.screenshot({ path: arquivo, fullPage: true });
  assert.ok(
    await pagina.evaluate(
      () => document.documentElement.scrollWidth <= window.innerWidth,
    ),
    "Viewport deve conter a página; tabelas usam rolagem local",
  );
  provas.push(arquivo);
}
try {
  await pagina.goto(base, { waitUntil: "networkidle" });
  await pagina.locator("input[type=email]").fill(email);
  await pagina.locator("input[type=password]").fill(senha);
  await pagina.getByRole("button", { name: "Entrar →", exact: true }).click();
  await pagina.getByRole("navigation").waitFor();
  csrf = (await api("session")).csrfToken;
  const marca = randomUUID();
  const materia = await criar("materias", { nome: `Matéria E2E ${marca}` });
  const faculdade = await criarNaTela(
    "Faculdades",
    "faculdades",
    `Faculdade E2E ${marca}`,
  );
  const curso = await criarNaTela(
    "Cursos",
    "cursos",
    `Curso E2E ${marca}`,
    (formulario) =>
      formulario
        .getByLabel("Faculdade (opcional no cadastro histórico)")
        .selectOption(String(faculdade.id)),
  );
  assert.equal(curso.faculdadeId, faculdade.id);
  const grade = await criarNaTela(
    "Grades por curso",
    "grades-curriculares",
    `Grade E2E ${marca}`,
    (formulario) =>
      formulario.getByLabel("Curso").selectOption(String(curso.id)),
  );
  const requisito = await criarNaTela(
    "Requisitos de formação",
    "requisitos-formacao",
    `Requisito E2E ${marca}`,
    (formulario) =>
      formulario.getByLabel("Grade curricular").selectOption(String(grade.id)),
  );
  const associacoes = pagina
    .locator("section.panel")
    .filter({
      has: pagina.getByRole("heading", { name: "Matérias do requisito" }),
    });
  await associacoes.getByLabel("Requisito").selectOption(String(requisito.id));
  await associacoes.getByLabel("Matéria").selectOption(String(materia.id));
  await associacoes.getByRole("button", { name: "Associar matéria" }).click();
  await associacoes
    .getByRole("button", { name: "Remover associação" })
    .waitFor();
  assert.equal(
    (await api(`requisitos-formacao/${requisito.id}/materias`)).length,
    1,
  );
  await captura("t003-requisito-desktop");
  await pagina.setViewportSize({ width: 390, height: 844 });
  await captura("t003-requisito-mobile");
  await associacoes.getByRole("button", { name: "Remover associação" }).click();
  await associacoes
    .getByRole("button", { name: "Remover associação" })
    .waitFor({ state: "hidden" });
  assert.equal(
    (await api(`requisitos-formacao/${requisito.id}/materias`)).length,
    0,
  );
  await navegar("Importação do legado");
  const nomeImportado = `Importada E2E ${marca}`;
  await pagina
    .getByLabel("Arquivo SQL (até 10 MiB)")
    .setInputFiles({
      name: "fixture.sql",
      mimeType: "application/sql",
      buffer: Buffer.from(
        `INSERT INTO \`materia\` (\`id\`,\`nome\`) VALUES (1,'${nomeImportado}');`,
      ),
    });
  const analise = pagina.waitForResponse(
    (resposta) =>
      resposta.url().endsWith("/api/importacoes/analisar") &&
      resposta.request().method() === "POST",
  );
  await pagina.getByRole("button", { name: "Analisar arquivo" }).click();
  const resultado = await analise;
  assert.equal(resultado.status(), 202);
  const importacao = await resultado.json();
  await pagina.getByRole("button", { name: "Aplicar plano" }).waitFor();
  const plano = await api(`importacoes/${importacao.id}`);
  assert.equal(plano.tabelas.length, 24);
  await pagina.getByRole("button", { name: "Aplicar plano" }).click();
  await pagina.getByText("Concluída", { exact: true }).waitFor();
  const importada = (await api("materias")).find(
    (item) => item.nome === nomeImportado,
  );
  assert.ok(importada, "Plano aplicado deve persistir matéria real");
  criados.push(`materias/${importada.id}`);
  await captura("t003-importacao-mobile");
  await pagina.setViewportSize({ width: 1440, height: 900 });
  await captura("t003-importacao-desktop");
  assert.deepEqual(erros, [], "React não deve emitir erros de execução");
} catch (erro) {
  falha = erro;
} finally {
  for (const recurso of criados.reverse()) {
    try {
      await api(recurso, "DELETE", undefined, 204);
    } catch (erro) {
      falha ||= erro;
    }
  }
  await navegador.close();
  const resumo = {
    status: falha ? "FALHOU" : "PASSOU",
    fluxos: [
      "cadastro visual Faculdade→Curso→Grade→Requisito",
      "associação/remover requisito-matéria",
      "analisar e aplicar importação sintética",
      "desktop1440/mobile390 sem overflow global",
    ],
    capturas: provas,
    errosReact: erros.length,
  };
  await writeFile(
    "evidence/t003-browser-react.json",
    JSON.stringify(resumo, null, 2) + "\n",
  );
}
if (falha) {
  const mensagem = falha.message
    .replaceAll(senha, "[REDACTED]")
    .replaceAll(email, "[EMAIL_SINTETICO]");
  throw new Error(mensagem);
}
console.log(
  "React T003: cadastros acadêmicos, associação e importação reais passaram em desktop e mobile.",
);
