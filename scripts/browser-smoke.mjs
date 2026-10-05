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
  return response.json();
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
  const suffix = randomUUID();
  const professor = await create('pessoas', { pessoa: { nome: 'Professor de teste', email: `professor-${suffix}@example.test`, matricula: `p-${suffix}`, perfil: 'PROFESSOR', ativo: true }, senha: env.ADMIN_PASSWORD });
  const aluno = await create('pessoas', { pessoa: { nome: 'Aluno de teste', email: `aluno-${suffix}@example.test`, matricula: `a-${suffix}`, perfil: 'ALUNO', ativo: true, documento: '000.000.000-00' }, senha: env.ADMIN_PASSWORD });
  const unidade = await create('unidades', { nome: `Escola E2E ${suffix}`, endereco: 'Rua de teste', telefone: '123', cnpj: '12.345.678/0001-90' });
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
  for (const grade of grades) {
    created.push(`notas/${grade.id}`);
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
  const outsider = await create('pessoas', { pessoa: { nome: 'Outro aluno de teste', email: `outro-${suffix}@example.test`, matricula: `o-${suffix}`, perfil: 'ALUNO', ativo: true }, senha: env.ADMIN_PASSWORD });
  for (const [person, enrolled] of [[aluno, true], [outsider, false]]) {
    const context = await browser.newContext(); contexts.push(context);
    const session = await context.request.get(`${baseURL}/api/session`);
    const initialToken = (await session.json()).csrfToken;
    const login = await context.request.post(`${baseURL}/api/session/login`, { headers: { 'X-CSRF-TOKEN': initialToken }, data: { email: person.email, senha: env.ADMIN_PASSWORD } });
    assert.equal(login.status(), 200);
    const studentToken = (await (await context.request.get(`${baseURL}/api/session`)).json()).csrfToken;
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
