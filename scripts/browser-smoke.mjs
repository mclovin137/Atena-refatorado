import { readFile, mkdir, writeFile } from 'node:fs/promises';
import { createRequire } from 'node:module';
import { execFileSync } from 'node:child_process';
import { randomUUID } from 'node:crypto';
import assert from 'node:assert/strict';
const require = createRequire(import.meta.url);
const { chromium } = require(process.env.PLAYWRIGHT_MODULE || 'playwright');
const env = Object.fromEntries((await readFile('.env', 'utf8')).split('\n').filter(line => line.includes('=')).map(line => { const at = line.indexOf('='); return [line.slice(0, at), line.slice(at + 1)]; }));
const baseURL = `http://127.0.0.1:${env.ATENA_PORT || '18080'}`;
function redact(text) {
  for (const [key, value] of Object.entries(env)) if (/PASSWORD|TOKEN/.test(key) && value) text = text.replaceAll(value, '[REDACTED]');
  return text;
}
await mkdir('evidence', { recursive: true });
let healthy = false;
for (let attempt = 0; attempt < 60; attempt++) {
  try {
    const response = await fetch(`${baseURL}/actuator/health`, { signal: AbortSignal.timeout(3000) });
    if (response.ok && (await response.json()).status === 'UP') { healthy = true; break; }
  } catch { /* O processo pode estar iniciando; o próximo teste continua limitado à saúde local. */ }
  await new Promise(resolve => setTimeout(resolve, 1000));
}
assert.ok(healthy, 'Aplicação não apresentou saúde UP no limite de inicialização');
const browser = await chromium.launch({ headless: true });
const created = [], reportIds = [], evidence = [];
let page, csrf, mainFailure;
const contexts = [];
async function request(path, method = 'GET', data, expected = 200, extraHeaders = {}) {
  const response = await page.request.fetch(`${baseURL}/api/${path}`, { method, data, headers: { ...(method === 'GET' ? {} : { 'X-CSRF-TOKEN': csrf }), ...extraHeaders } });
  assert.equal(response.status(), expected, `${method} ${path}: esperado ${expected}, recebido ${response.status()}`);
  if (expected === 204) return null;
  const text = await response.text();
  return text ? JSON.parse(text) : null;
}
async function create(path, data) {
  const value = await request(path, 'POST', data, 201);
  created.push(`${path}/${value.id}`);
  return value;
}
async function awaitCompletion(path) {
  const deadline = Date.now() + 30000;
  while (Date.now() < deadline) {
    const value = await request(path);
    if (value.status === 'COMPLETED') return value;
    assert.ok(!['FAILED', 'STOPPED', 'ABANDONED'].includes(value.status), `Execução ${path} terminou em ${value.status}`);
    await new Promise(resolve => setTimeout(resolve, 150));
  }
  throw new Error(`Execução ${path} não concluiu no limite de30s`);
}
try {
  page = await browser.newPage({ viewport: { width: 1440, height: 900 } });
  const errors = [];
  page.on('pageerror', error => errors.push(error.message));
  await page.goto(baseURL, { waitUntil: 'networkidle' });
  await page.screenshot({ path: 'evidence/login-desktop.png', fullPage: true });
  assert.equal(await page.locator('input[type="password"]').count(), 1, 'Login deve ter campo de senha');
  await page.locator('input[type="email"]').fill(env.ADMIN_EMAIL);
  await page.locator('input[type="password"]').fill(env.ADMIN_PASSWORD);
  await page.locator('button[type="submit"]').click();
  await page.waitForFunction(() => !document.querySelector('input[type="password"]'));
  csrf = (await request('session')).csrfToken;
  await page.screenshot({ path: 'evidence/secretaria-desktop.png', fullPage: true });
  await page.setViewportSize({ width: 390, height: 844 });
  await page.screenshot({ path: 'evidence/secretaria-mobile.png', fullPage: true });
  assert.equal(await page.evaluate(() => document.documentElement.scrollWidth <= window.innerWidth), true, 'Tela mobile deve caber na viewport');
  assert.equal((await page.request.get(`${baseURL}/actuator/metrics`)).status(), 200, 'Secretaria deve consultar métricas autenticadas');
  const suffix = randomUUID();
  const professor = await create('pessoas', { pessoa: { nome: 'Professor de teste', email: `professor-${suffix}@example.test`, matricula: `p-${suffix}`, perfil: 'PROFESSOR', ativo: true }, senha: env.ADMIN_PASSWORD });
  const aluno = await create('pessoas', { pessoa: { nome: 'Aluno de teste', email: `aluno-${suffix}@example.test`, matricula: `a-${suffix}`, perfil: 'ALUNO', ativo: true, documento: '000.000.000-00' }, senha: env.ADMIN_PASSWORD });
  const curso = await create('cursos', { nome: `Curso E2E ${suffix}` });
  const serie = await create('series', { nome: `Série E2E ${suffix}` });
  const materia = await create('materias', { nome: `Matemática E2E ${suffix}` });
  const turma = await create('turmas', { nome: `Turma E2E ${suffix}`, cursoId: curso.id, serieId: serie.id, ano: 2026 });
  const disciplina = await create('disciplinas', { turmaId: turma.id, materiaId: materia.id, professorId: professor.id, cargaHoraria: 20 });
  await create('matriculas', { turmaId: turma.id, alunoId: aluno.id, dependencia: false });
  const aula = await create('aulas', { disciplinaId: disciplina.id, data: '2026-10-04', conteudo: 'Frações', planejamento: 'Exercícios', horas: 2 });
  await create('presencas', { aulaId: aula.id, alunoId: aluno.id, faltas: 1 });
  const job = await request('jobs/inicializar-notas', 'POST', { turmaId: turma.id, requestId: suffix }, 202);
  const finished = await awaitCompletion(`jobs/${job.id}`);
  const grades = (await request('notas')).filter(n => n.disciplinaId === disciplina.id && n.alunoId === aluno.id);
  assert.equal(grades.length, 4, 'Batch deve criar quatro bimestres');
  for (const grade of grades) created.push(`notas/${grade.id}`);
  const vazio = await request(`boletins/${aluno.id}`);
  assert.deepEqual(vazio.unidade, {}, 'Snapshot sem unidade deve preservar objeto vazio');
  assert.deepEqual(vazio.linhas[0].notas, [null, null, null, null]);
  assert.deepEqual(vazio.linhas[0].recuperacoes, [null, null, null, null]);
  const boletimVazio = await request('relatorios', 'POST', { alunoId: aluno.id, tipo: 'BOLETIM', requestId: `${suffix}-boletim-vazio` }, 202);
  reportIds.push(boletimVazio.id);
  await awaitCompletion(`relatorios/${boletimVazio.id}`);
  const boletimJson = await request(`relatorios/${boletimVazio.id}/download?format=json`);
  assert.ok(Number.isSafeInteger(boletimVazio.id));
  const snapshotPersistido = JSON.parse(execFileSync('docker', ['compose', 'exec', '-T', 'postgres', 'psql', '-U', 'postgres', '-d', 'relatorios', '-tA', '-v', 'ON_ERROR_STOP=1', '-c', `SELECT snapshot FROM relatorio WHERE id=${boletimVazio.id};`], { encoding: 'utf8' }).trim());
  assert.deepEqual(snapshotPersistido.unidade, {}, 'Snapshot BOLETIM recebido por HTTP e persistido deve preservar unidade vazia');
  assert.equal(boletimJson.nome, aluno.nome);
  assert.equal(boletimJson.tipo, 'BOLETIM');
  assert.deepEqual(boletimJson.linhas[0].notas, [null, null, null, null]);
  assert.deepEqual(boletimJson.linhas[0].recuperacoes, [null, null, null, null]);
  for (const formato of ['pdf', 'csv']) {
    const arquivo = await page.request.get(`${baseURL}/api/relatorios/${boletimVazio.id}/download?format=${formato}`);
    assert.equal(arquivo.status(), 200);
    const bytes = await arquivo.body();
    if (formato === 'pdf') assert.equal(bytes.subarray(0, 5).toString(), '%PDF-');
    else assert.ok(bytes.toString().includes('registro;conteudo'));
  }
  evidence.push({ tipo: 'BOLETIM', contratoEntreServicos: 'unidade vazia e quatro notas/recuperações null preservadas', formatos: ['json', 'pdf', 'csv'] });
  const unidade = await create('unidades', { nome: `Escola E2E ${suffix}`, endereco: 'Rua de teste', telefone: '123', cnpj: '12.345.678/0001-90' });
  for (const grade of grades) {
    await request(`notas/${grade.id}`, 'PUT', { ...grade, valor: 75, recuperacao: null, liberada: true, prazo: '2200-01-01' });
  }
  const boletim = await request(`boletins/${aluno.id}`);
  assert.equal(boletim.unidade.nome, unidade.nome, 'Boletim deve incluir unidade escolar do snapshot');
  assert.equal(boletim.unidade.cnpj, unidade.cnpj, 'Snapshot deve preservar CNPJ da unidade');
  assert.equal(boletim.linhas[0].resultado.media, 75);
  assert.equal(boletim.linhas[0].resultado.frequencia, 95);
  assert.equal(boletim.linhas[0].resultado.situacao, 'APROVADO');
  const traceId = randomUUID().replaceAll('-', '');
  const report = await request('relatorios', 'POST', { alunoId: aluno.id, tipo: 'HISTORICO', requestId: suffix }, 202, { traceparent: `00-${traceId}-1111111111111111-01` });
  reportIds.push(report.id);
  const reportFinished = await awaitCompletion(`relatorios/${report.id}`);
  const logs = execFileSync('docker', ['compose', 'logs', '--no-color'], { encoding: 'utf8', maxBuffer: 10 * 1024 * 1024 });
  await writeFile('evidence/compose.log', logs);
  const traced = logs.split('\n').filter(line => line.includes(traceId) && line.includes('atena.http'));
  assert.ok(traced.some(line => line.includes('escolar-1') && line.includes('HTTP POST /api/relatorios')), 'Serviço escolar deve registrar traceId recebido');
  assert.ok(traced.some(line => line.includes('relatorios-1') && line.includes('HTTP POST /api/relatorios')), 'HTTP entre serviços deve preservar traceId');
  assert.ok(traced.some(line => line.includes('/api/internal/snapshot/{alunoId}')), 'Batch deve preservar contexto ao buscar snapshot');
  evidence.push({ tracing: 'W3C preservado entre serviços e processamento Batch', traceId, registros: traced.length });
  const reportJson = await request(`relatorios/${report.id}/download?format=json`);
  assert.equal(reportJson.linhas.length, 1, 'Relatório deve conter a disciplina do aluno');
  assert.equal(reportJson.linhas[0].resultado.media, 75, 'Relatório deve refletir o snapshot real do serviço escolar');
  for (const [tipo, fixture] of [['LISTA_ALUNOS', aluno], ['LISTA_PROFESSORES', professor], ['LISTA_TURMAS', turma]]) {
    const lista = await request('relatorios', 'POST', { tipo, requestId: `${suffix}-${tipo}` }, 202);
    reportIds.push(lista.id);
    await awaitCompletion(`relatorios/${lista.id}`);
    const conteudo = await request(`relatorios/${lista.id}/download?format=json`);
    const linha = conteudo.linhas.find(item => item.id === fixture.id);
    assert.ok(linha, 'Relatório de lista deve consumir fixture real do serviço escolar');
    assert.equal(linha.nome, fixture.nome);
    if (tipo === 'LISTA_TURMAS') assert.equal(linha.ano, fixture.ano);
    else assert.equal(linha.matricula, fixture.matricula);
    for (const formato of ['pdf', 'csv']) {
      const arquivo = await page.request.get(`${baseURL}/api/relatorios/${lista.id}/download?format=${formato}`);
      assert.equal(arquivo.status(), 200);
      const bytes = await arquivo.body();
      if (formato === 'pdf') assert.equal(bytes.subarray(0, 5).toString(), '%PDF-');
      else assert.ok(bytes.toString().includes('registro;conteudo'));
    }
    evidence.push({ lista: tipo, contratoEntreServicos: 'campos reais preservados', formatos: ['json', 'pdf', 'csv'] });
  }
  let exportedPdf;
  for (const format of ['pdf', 'csv']) {
    const response = await page.request.get(`${baseURL}/api/relatorios/${report.id}/download?format=${format}`);
    assert.equal(response.status(), 200);
    const data = await response.body();
    if (format === 'pdf') { assert.equal(data.subarray(0, 5).toString(), '%PDF-'); exportedPdf = data; }
    else assert.ok(data.toString().includes('registro;conteudo'));
    evidence.push({ formato: format, bytes: data.length, status: response.status() });
  }
  const materialResponse = await page.request.post(`${baseURL}/api/materiais`, { headers: { 'X-CSRF-TOKEN': csrf }, multipart: { disciplinaId: String(disciplina.id), nome: 'Histórico de teste', file: { name: 'historico.pdf', mimeType: 'application/pdf', buffer: exportedPdf } } });
  assert.equal(materialResponse.status(), 201);
  const material = await materialResponse.json();
  created.push(`materiais/${material.id}`);
  const png = await page.screenshot({ type: 'png', clip: { x: 0, y: 0, width: 1, height: 1 } });
  const avatarUpload = await page.request.post(`${baseURL}/api/pessoas/${aluno.id}/avatar`, { headers: { 'X-CSRF-TOKEN': csrf }, multipart: { file: { name: 'avatar.png', mimeType: 'image/png', buffer: png } } });
  assert.equal(avatarUpload.status(), 200);
  const avatarAntes = await (await page.request.get(`${baseURL}/api/pessoas/${aluno.id}/avatar`)).body();
  execFileSync('docker', ['compose', 'restart', 'escolar', 'relatorios'], { stdio: 'pipe' });
  let reiniciou = false;
  for (let tentativa = 0; tentativa < 90; tentativa++) {
    try { const saude = await fetch(`${baseURL}/actuator/health`); if (saude.ok && (await saude.json()).status === 'UP') { reiniciou = true; break; } } catch {}
    await new Promise(resolve => setTimeout(resolve, 1000));
  }
  assert.ok(reiniciou, 'Runtime deve reiniciar com BYTEA persistido');
  csrf = (await request('session')).csrfToken;
  await request('session/login', 'POST', { email: env.ADMIN_EMAIL, senha: env.ADMIN_PASSWORD });
  csrf = (await request('session')).csrfToken;
  assert.deepEqual(await (await page.request.get(`${baseURL}/api/materiais/${material.id}/download`)).body(), exportedPdf);
  assert.deepEqual(await (await page.request.get(`${baseURL}/api/pessoas/${aluno.id}/avatar`)).body(), avatarAntes);
  evidence.push({ persistencia: 'material e avatar BYTEA preservados após reinício dos dois serviços' });
  const outsider = await create('pessoas', { pessoa: { nome: 'Outro aluno de teste', email: `outro-${suffix}@example.test`, matricula: `o-${suffix}`, perfil: 'ALUNO', ativo: true }, senha: env.ADMIN_PASSWORD });
  for (const [person, enrolled] of [[aluno, true], [outsider, false]]) {
    const context = await browser.newContext(); contexts.push(context);
    const session = await context.request.get(`${baseURL}/api/session`);
    const initialToken = (await session.json()).csrfToken;
    const login = await context.request.post(`${baseURL}/api/session/login`, { headers: { 'X-CSRF-TOKEN': initialToken }, data: { email: person.email, senha: env.ADMIN_PASSWORD } });
    assert.equal(login.status(), 200);
    const studentToken = (await (await context.request.get(`${baseURL}/api/session`)).json()).csrfToken;
    assert.equal((await context.request.get(`${baseURL}/actuator/metrics`)).status(), 403, 'Aluno não consulta métricas operacionais');
    const download = await context.request.get(`${baseURL}/api/materiais/${material.id}/download`);
    assert.equal(download.status(), enrolled ? 200 : 403, 'Arquivo só pode ser baixado por aluno da disciplina');
    if (enrolled) {
      assert.deepEqual(await download.body(), exportedPdf, 'Download deve preservar exatamente os bytes enviados');
      assert.equal((await (await context.request.get(`${baseURL}/api/boletins/${aluno.id}`)).json()).documento, aluno.documento, 'Aluno deve ler apenas seu próprio documento no boletim');
    }
    const otherPerson = await context.request.get(`${baseURL}/api/pessoas/${professor.id}`);
    assert.equal(otherPerson.status(), 403, 'Aluno não consulta cadastro privado do professor');
    const writePerson = await context.request.put(`${baseURL}/api/pessoas/${professor.id}`, { headers: { 'X-CSRF-TOKEN': studentToken }, data: { pessoa: professor } });
    assert.equal(writePerson.status(), 403, 'Aluno não altera conta de professor');
    const writeGrade = await context.request.post(`${baseURL}/api/notas`, { headers: { 'X-CSRF-TOKEN': studentToken }, data: { disciplinaId: disciplina.id, alunoId: aluno.id, bimestre: 1, valor: 100, liberada: true } });
    assert.equal(writeGrade.status(), 403, 'Aluno não cadastra notas');
    await context.close();
  }
  const teacherContext = await browser.newContext(); contexts.push(teacherContext);
  const teacherToken = (await (await teacherContext.request.get(`${baseURL}/api/session`)).json()).csrfToken;
  assert.equal((await teacherContext.request.post(`${baseURL}/api/session/login`, { headers: { 'X-CSRF-TOKEN': teacherToken }, data: { email: professor.email, senha: env.ADMIN_PASSWORD } })).status(), 200);
  const teacherBoletim = await teacherContext.request.get(`${baseURL}/api/boletins/${aluno.id}`);
  assert.equal(teacherBoletim.status(), 200);
  assert.equal((await teacherBoletim.json()).documento, '', 'Professor não pode ler documento privado do aluno');
  await teacherContext.close();
  const anonymous = await browser.newContext(); contexts.push(anonymous);
  assert.equal((await anonymous.request.get(`${baseURL}/actuator/metrics`)).status(), 401, 'Métricas exigem autenticação');
  assert.equal((await anonymous.request.get(`${baseURL}/api/materiais/${material.id}/download`)).status(), 401, 'Arquivo exige autenticação');
  await anonymous.close();
  evidence.push({ autorizacao: 'Aluno não cadastra nota/altera docente; arquivo exige autenticação e vínculo de matrícula; bytes preservados' });
  await page.setViewportSize({ width: 1440, height: 900 });
  await page.getByRole('button', { name: 'Boletim e histórico', exact: true }).click();
  await page.locator('select[name="aluno"]').first().selectOption(String(aluno.id));
  await page.getByRole('button', { name: 'Consultar resultados', exact: true }).click();
  await page.getByRole('cell', { name: 'APROVADO', exact: true }).waitFor();
  await page.screenshot({ path: 'evidence/boletim-desktop.png', fullPage: true });
  await page.setViewportSize({ width: 390, height: 844 });
  await page.screenshot({ path: 'evidence/boletim-mobile.png', fullPage: true });
  assert.equal(await page.evaluate(() => document.documentElement.scrollWidth <= window.innerWidth), true, 'Boletim mobile deve caber na viewport');
  const privacidade = await browser.newContext(); contexts.push(privacidade);
  const paginaAluno = await privacidade.newPage();
  paginaAluno.on('pageerror', error => errors.push(error.message));
  async function entrarPessoa(pessoa, senha) {
    await paginaAluno.locator('main.login input[name="email"]').fill(pessoa.email);
    await paginaAluno.locator('main.login input[name="senha"]').fill(senha);
    await paginaAluno.locator('main.login button[type="submit"]').click();
    await paginaAluno.locator('main.login').waitFor({ state: 'detached' });
  }
  await paginaAluno.goto(baseURL, { waitUntil: 'networkidle' });
  await entrarPessoa(aluno, env.ADMIN_PASSWORD);
  await paginaAluno.getByRole('button', { name: 'Boletim e histórico', exact: true }).click();
  await paginaAluno.getByRole('button', { name: 'Consultar resultados', exact: true }).click();
  await paginaAluno.getByRole('cell', { name: 'APROVADO', exact: true }).waitFor();
  await paginaAluno.getByRole('button', { name: 'Sair da conta', exact: true }).click();
  await paginaAluno.locator('main.login input[name="senha"]').waitFor();
  await entrarPessoa(outsider, env.ADMIN_PASSWORD);
  await paginaAluno.getByRole('button', { name: 'Boletim e histórico', exact: true }).click();
  assert.equal(await paginaAluno.getByRole('cell', { name: 'APROVADO', exact: true }).count(), 0, 'Troca de conta deve descartar boletim anterior');
  await paginaAluno.getByRole('button', { name: 'Meu perfil', exact: true }).click();
  await paginaAluno.locator('input[name="atual"]').fill(env.ADMIN_PASSWORD);
  const novaSenha = `${randomUUID()}Aa1!`;
  await paginaAluno.locator('input[name="nova"]').fill(novaSenha);
  await paginaAluno.getByRole('button', { name: 'Alterar senha e sair', exact: true }).click();
  await paginaAluno.locator('main.login input[name="email"]').waitFor();
  await entrarPessoa(outsider, novaSenha);
  await privacidade.close();
  evidence.push({ sessaoReact: 'logout e mudança de conta limpam boletim; alteração de senha retorna ao login e novo acesso funciona sem erro de corpo vazio' });
  assert.deepEqual(errors, [], 'Navegador não deve apresentar erro JavaScript');
  evidence.push({ fluxo: 'matrícula→aula/presença→Spring Batch→boletim→HTTP entre serviços→Spring Batch→PDF/CSV/JSON', job: finished, relatorio: reportFinished, media: 75, frequencia: 95 });
  await writeFile('evidence/fluxo-real.json', JSON.stringify(evidence, null, 2));
  console.log('Fluxo real entre serviços, Spring Batch, PDF/CSV/JSON, login e desktop/mobile validados.');
} catch (error) {
  mainFailure = error;
  error.message = redact(error.message || String(error));
  if (error.stack) error.stack = redact(error.stack);
  await writeFile('evidence/erro-e2e.json', JSON.stringify({ erro: error.message }, null, 2));
  if (page && !page.isClosed()) {
    try { await page.screenshot({ path: 'evidence/browser-falha.png', fullPage: true }); } catch { /* Preserva o erro original quando a página já não pode ser capturada. */ }
  }
  throw error;
} finally {
  const cleanupErrors = [];
  // Limpa somente os fixtures desta execução. Metadados de execução Batch
  // permanecem como trilha; requestId único torna a repetição independente.
  for (const id of reportIds) {
    try {
      assert.ok(Number.isSafeInteger(id));
      execFileSync('docker', ['compose', 'exec', '-T', 'postgres', 'psql', '-U', 'postgres', '-d', 'relatorios', '-v', 'ON_ERROR_STOP=1', '-c', `DELETE FROM linha WHERE relatorio_id=${id}; DELETE FROM relatorio WHERE id=${id};`], { stdio: 'pipe' });
    } catch (error) { cleanupErrors.push(redact(`Relatório ${id}: ${error.message}`)); }
  }
  if (page && csrf) for (const path of created.reverse()) {
    try { await request(path, 'DELETE', undefined, 204); }
    catch (error) { cleanupErrors.push(redact(`${path}: ${error.message}`)); }
  }
  await browser.close();
  if (cleanupErrors.length) {
    await writeFile('evidence/falhas-limpeza.json', JSON.stringify(cleanupErrors, null, 2));
    if (!mainFailure) throw new Error('Falha ao limpar fixtures; consulte evidence/falhas-limpeza.json');
    console.error('Limpeza também falhou; erro original preservado. Consulte evidence/falhas-limpeza.json');
  }
}
