import { createRequire } from 'node:module';
import { mkdir, writeFile } from 'node:fs/promises';
const require = createRequire(import.meta.url);
const { chromium } = require(process.env.PLAYWRIGHT_MODULE);
const base = process.env.LEGADO_TEST_URL;
const password = process.env.LEGADO_TEST_PASSWORD;
if (!base || !password) throw new Error('Ambiente sintético PHP não configurado');
await mkdir('evidence', { recursive: true });
const browser = await chromium.launch({ headless: true });
const results = [];
try {
  for (const [role, email, enrollment] of [
    ['secretaria', 'secretaria@legado.qa.invalid', '000000'],
    ['professor', 'docente@qa.invalid', '010001'],
    ['aluno', 'aluno@qa.invalid', '010002'],
  ]) {
    const context = await browser.newContext({ viewport: { width: 1366, height: 900 } });
    const page = await context.newPage();
    const failures = [];
    page.on('pageerror', () => failures.push('Erro JavaScript da página'));
    await page.goto(base);
    await page.locator('input[name=nome]').fill(email);
    await page.locator('input[name=matricula]').fill(enrollment);
    await page.locator('input[name=senha]').fill(password);
    await Promise.all([
      page.waitForURL('**/main/Home/**'),
      (async () => {
        await page.locator('input[name=acao]').focus();
        await page.keyboard.press('Enter');
      })(),
    ]);
    await page.locator('button[type=submit][name=loggout]').first().waitFor();
    const home = await page.request.get(new URL('main/Home/', base).href);
    if (home.status() !== 200) throw new Error('Home PHP não respondeu 200');
    await page.screenshot({ path: `evidence/legado-${role}-desktop.png`, fullPage: true });
    await page.setViewportSize({ width: 390, height: 844 });
    await page.screenshot({ path: `evidence/legado-${role}-mobile.png`, fullPage: true });
    if (role === 'aluno') {
      const report = await page.goto(new URL('main/boletim?ano=2026', base).href);
      if (report.status() !== 200) throw new Error('Boletim PHP não respondeu 200');
      await page.screenshot({ path: 'evidence/legado-aluno-boletim-mobile.png', fullPage: true });
      await page.locator('h2').filter({ hasText: 'Boletim' }).waitFor();
      await page.setViewportSize({ width: 390, height: 844 });
      await page.screenshot({ path: 'evidence/legado-aluno-boletim-mobile.png', fullPage: true });
    }
    if (failures.length) throw new Error(failures[0]);
    results.push({ perfil: role, login: 'validado', home: 'HTTP 200', teclado: 'login por Enter', capturas: ['desktop', 'mobile'] });
    await context.close();
  }
  await writeFile('evidence/legado-navegador.json', JSON.stringify({ status: 'success', resultados: results }, null, 2));
} finally {
  await browser.close();
}
