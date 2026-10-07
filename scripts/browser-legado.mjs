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
    for (const width of [320, 390, 768, 1024, 1440]) {
      await page.setViewportSize({width, height: 900});
      if (await page.evaluate(() => Math.max(document.documentElement.scrollWidth, document.body.scrollWidth) > innerWidth + 1)) throw new Error(`Login excede viewport ${width}`);
      const input = await page.locator('input[name=senha]').boundingBox();
      if (input.x < 0 || input.x + input.width > width + 1) throw new Error('Campo de login cortado');
      if (role === 'secretaria') {
        const capture = await page.screenshot({path: `evidence/legado-login-${width}.png`, fullPage: true});
        if (capture.readUInt32BE(16) !== width) throw new Error(`Captura login excede viewport ${width}`);
      }
    }
    await page.setViewportSize({width: role === 'secretaria' ? 1366 : 390, height: 900});
    if (process.env.LEGADO_LOGIN_ONLY === '1') {
      results.push({ tela: 'login', faixas: [320, 390, 768, 1024, 1440], medidas: 'html, body, campo de senha e largura PNG dentro do viewport' });
      await context.close();
      break;
    }
    await page.locator('input[name=nome]').fill(email);
    await page.locator('input[name=matricula]').fill(enrollment);
    await page.locator('input[name=senha]').fill(password);
    await Promise.all([
      page.waitForURL('**/main/Home/**', { waitUntil: 'domcontentloaded' }),
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
    for (const width of [320, 390, 768, 1024, 1440]) {
      await page.setViewportSize({ width, height: 900 });
      await page.goto(new URL('main/Home/', base).href);
      const dimensions = await page.evaluate(() => ({ width: innerWidth, document: document.documentElement.scrollWidth }));
      if (dimensions.width !== width || dimensions.document > width + 1) throw new Error(`Layout global excede viewport ${width}: ${JSON.stringify(dimensions)}`);
      const canvas = page.locator('canvas').first();
      if (await canvas.count()) {
        const box = await canvas.boundingBox();
        if (box.x < 0 || box.x + box.width > width + 1) throw new Error(`Gráfico cortado em ${width}`);
        const chart = await page.evaluate(() => {
          const instance = Object.values(window.Chart.instances).find(item => (item.canvas || item.chart?.canvas)?.id === 'myChart');
          return instance && { width: instance.width || instance.chart.width, values: instance.data.datasets[0].data.length, labels: instance.data.labels.length, legend: instance.legend.legendItems.length };
        });
        if (!chart || chart.width > width + 1 || chart.values !== 4 || chart.labels !== 4 || !chart.legend) throw new Error('Gráfico não se adaptou ou perdeu dados/legenda');

      }
      if (width < 1024) {
        const toggle = page.getByRole('button', { name: 'Menu de navegação' });
        await toggle.focus();
        await page.keyboard.press('Enter');
        if (await toggle.getAttribute('aria-expanded') !== 'true') throw new Error('Menu não abre por teclado');
        await page.keyboard.press('Escape');
        if (await toggle.getAttribute('aria-expanded') !== 'false' || !(await toggle.evaluate(el => el === document.activeElement))) throw new Error('Menu não fecha/devolve foco');
      }
      await page.screenshot({path: `evidence/legado-${role}-home-${width}.png`, fullPage: true});
      if (role === 'aluno') {
        await page.goto(new URL('main/boletim?ano=2026', base).href);
        const table = page.locator('.atena-tabela').last();
        await table.focus();
        const geometry = await table.evaluate(el => ({right: el.getBoundingClientRect().right, columns: el.querySelectorAll('tr:last-child td').length}));
        if (geometry.right > width + 1 || !geometry.columns) throw new Error('Tabela sem colunas ou fora da tela');
        if (await table.evaluate(el => el.scrollWidth > el.clientWidth)) {
          await page.keyboard.press('ArrowRight');
          await page.waitForFunction(() => document.activeElement.scrollLeft > 0);
        }
        const scroll = await table.evaluate(el => { el.scrollLeft = el.scrollWidth; return { left: el.scrollLeft, available: el.scrollWidth > el.clientWidth }; });
        if (scroll.available && !scroll.left) throw new Error('Colunas finais da tabela inacessíveis');
        if (await page.evaluate(() => Math.max(document.documentElement.scrollWidth, document.body.scrollWidth) > innerWidth + 1)) throw new Error('Boletim excede viewport');
      }
      if (role === 'secretaria') {
        for (const route of ['cadastrar-alunos', 'listagem-alunos']) {
          const response = await page.goto(new URL(`main/${route}`, base).href);
          if (response.status() !== 200) throw new Error('Formulário/listagem não respondeu200');
          if (await page.evaluate(() => Math.max(document.documentElement.scrollWidth, document.body.scrollWidth) > innerWidth + 1)) throw new Error(`${route} excede viewport ${width}`);
          const fields = await page.locator('form input:not([type=hidden]), form select').evaluateAll(nodes => nodes.filter(el => el.getClientRects().length).map(el => el.getBoundingClientRect().right));
          if (fields.some(right => right > width + 1)) throw new Error('Campo do formulário cortado');
          await page.screenshot({path: `evidence/legado-${route}-${width}.png`, fullPage: true});
        }
      }
      await page.screenshot({path: `evidence/legado-${role}-${width}.png`, fullPage: true});
    }
    if (failures.length) throw new Error(failures[0]);
    results.push({ faixas: [320, 390, 768, 1024, 1440], responsividade: 'sem overflow global, menu por teclado, tabelas completas', perfil: role, login: 'validado', home: 'HTTP 200', teclado: 'login por Enter', capturas: ['desktop', 'mobile'] });
    await context.close();
  }
  await writeFile(process.env.LEGADO_LOGIN_ONLY === '1' ? 'evidence/legado-login-navegador.json' : 'evidence/legado-navegador.json', JSON.stringify({ status: 'success', resultados: results }, null, 2));
} finally {
  await browser.close();
}
