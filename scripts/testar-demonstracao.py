#!/usr/bin/env python3
"""QA DEMO em serviços reais descartáveis. Sem leitura dos dotenv/prod/Neon."""
import copy
import hashlib
import http.cookiejar
import importlib.util
import json
import os
from pathlib import Path
import re
import secrets
import subprocess
import tempfile
import time
import urllib.error
import urllib.parse
import urllib.request
import uuid

ROOT = Path(__file__).resolve().parents[1]
spec = importlib.util.spec_from_file_location('demo', ROOT / 'scripts/povoar-demonstracao.py')
demo = importlib.util.module_from_spec(spec)
spec.loader.exec_module(demo)
RUN = 'atena-demo-qa-' + uuid.uuid4().hex[:12]
PASSWORD = secrets.token_urlsafe(24)
IMAGE = os.environ.get('DEMO_QA_PHP_IMAGE')
BUILT_IMAGE = False
OWN = []
EVIDENCE = ROOT / 'evidence' / 'demo-qa.json'
EVIDENCE.parent.mkdir(exist_ok=True)
EVIDENCE.write_text(json.dumps({'status': 'RUNNING', 'cloud_escritas': 0}) + '\n')
PRIVATE = Path(tempfile.mkdtemp(prefix=RUN + '-'))
PRIVATE.chmod(0o700)
ENV = {**os.environ, 'POSTGRES_PASSWORD': PASSWORD, 'DB_PASSWORD': PASSWORD,
       'ADMIN_PASSWORD': PASSWORD, 'SERVICE_TOKEN': secrets.token_urlsafe(24),
       'LEGADO_DB_PASSWORD': PASSWORD, 'LEGADO_ADMIN_PASSWORD': PASSWORD}


def docker(*args, body=None):
    result = subprocess.run(['docker', *args], input=body, stdout=subprocess.PIPE,
                            stderr=subprocess.PIPE, env=ENV, timeout=600)
    if result.returncode:
        raise RuntimeError('Operação QA Docker falhou; payload privado omitido')
    return result.stdout.decode().strip()


def start(name, args):
    OWN.append(name)
    return docker('run', '-d', '--name', name, *args)


def port(name):
    return docker('port', name, '8080/tcp').split(':')[-1]


def sql(db, statement):
    return docker('exec', '-i', RUN + '-pg', 'psql', '-U', 'postgres', '-d', db,
                  '-tA', '-v', 'ON_ERROR_STOP=1', body=statement.encode())


def snapshot(db, tables):
    # Hash de linhas reais protege senhas/IDs e comprova preservação inclusive das credenciais.
    parts = [sql(db, f'SELECT row_to_json(t)::text FROM (SELECT * FROM "{table}" ORDER BY 1) t;')
             for table in tables]
    return hashlib.sha256('\n'.join(parts).encode()).hexdigest()


def wait(fn):
    deadline = time.monotonic() + 150
    while time.monotonic() < deadline:
        try:
            if fn():
                return
        except (Exception,):
            pass
        time.sleep(1)
    raise RuntimeError('Serviço QA não ficou pronto')


class RedirectPHPControlado(urllib.request.HTTPRedirectHandler):
    def redirect_request(self, req, fp, code, msg, headers, newurl):
        return None  # Expõe303 à assertion, nunca segue nem reenvia credenciais.


class PHPCliente:
    def __init__(self, origin):
        self.origin, self.csrf = origin, None
        self.opener = urllib.request.build_opener(RedirectPHPControlado(),
                          urllib.request.HTTPCookieProcessor(http.cookiejar.CookieJar()))

    def request(self, path, data=None, status=200):
        headers = {'User-Agent': 'Atena-DEMO-QA'}
        if data is not None:
            headers['Content-Type'] = 'application/x-www-form-urlencoded'
            headers['X-CSRF-TOKEN'] = self.csrf
        request = urllib.request.Request(self.origin + '/' + path,
                    data=urllib.parse.urlencode(data).encode() if data else None, headers=headers)
        try:
            response = self.opener.open(request, timeout=15)
        except urllib.error.HTTPError as error:
            response = error
        with response:
            assert response.status == status, f'QA PHP HTTP recebido{response.status}, esperado{status}'
            body = response.read()
        token = re.search(rb'<meta name="atena-csrf" content="([a-f0-9]+)"', body)
        if token:
            self.csrf = token[1].decode()
        return body

    def login(self, account):
        self.request('')
        self.request('', {'acao': 'Enviar', 'nome': account['email'],
                          'matricula': account['matricula'], 'senha': account['senha']}, 303)
        assert b'Sair' in self.request('main/Home/')


try:
    if not IMAGE:
        IMAGE = RUN + '-php-image'
        docker('build', '-t', IMAGE, str(ROOT / 'legacy'))
        BUILT_IMAGE = True
    subprocess.run(['python3', '-m', 'unittest', 'discover', '-s', 'scripts/tests', '-v'],
                   cwd=ROOT, check=True, timeout=60)
    start(RUN + '-pg', ['--memory=192m', '--cpus=1', '-e', 'POSTGRES_PASSWORD', 'postgres:17-alpine'])
    wait(lambda: docker('exec', RUN + '-pg', 'pg_isready', '-U', 'postgres') != '')
    ip = docker('inspect', '-f', '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}', RUN + '-pg')
    sql('postgres', 'CREATE DATABASE escolar; CREATE DATABASE legado;')
    sql('legado', (ROOT / 'infra/legado/schema.sql').read_text())
    legacy = {'LEGADO_DB_DSN': f'pgsql:host={ip};port=5432;dbname=legado', 'LEGADO_DB_USER': 'postgres',
              'LEGADO_DB_PASSWORD': PASSWORD, 'LEGADO_ENV': 'development',
              'LEGADO_ADMIN_EMAIL': 'bootstrap@demo.qa.invalid', 'LEGADO_ADMIN_PASSWORD': PASSWORD}
    ENV.update(legacy, DB_URL=f'jdbc:postgresql://{ip}:5432/escolar', DB_USER='postgres',
               ADMIN_EMAIL='bootstrap@demo.qa.invalid', JAVA_TOOL_OPTIONS='-Xmx320m -XX:MaxMetaspaceSize=160m')
    start(RUN + '-java', ['--memory=560m', '--cpus=1', '-p', '127.0.0.1::8080',
          *sum((['-e', key] for key in ('DB_URL','DB_USER','DB_PASSWORD','ADMIN_EMAIL','ADMIN_PASSWORD',
                                      'SERVICE_TOKEN','JAVA_TOOL_OPTIONS')), []),
          '-v', str(ROOT / 'services/escolar/target/escolar-service-1.0.0.jar') + ':/app.jar:ro',
          'eclipse-temurin:21-jre', 'java', '-jar', '/app.jar'])
    school = 'http://127.0.0.1:' + port(RUN + '-java')
    client = demo.ClienteHTTP(school)
    wait(lambda: client.request('/actuator/health')['status'] == 'UP')
    # PHP usa a mesma fonte existente, sem rebuild de bibliotecas nem fixture de produto.
    docker('run', '--rm', '-v', str(ROOT / 'legacy') + ':/demo:ro',
           *sum((['-e', key] for key in legacy), []), IMAGE, 'php', '/demo/bin/inicializar.php')
    start(RUN + '-php', ['--memory=128m', '--cpus=1', '-p', '127.0.0.1::8080',
          '-v', str(ROOT / 'legacy') + ':/var/www/html:ro',
          *sum((['-e', key] for key in legacy), []), IMAGE])
    php_origin = 'http://127.0.0.1:' + port(RUN + '-php')
    wait(lambda: demo.ClienteHTTP(php_origin).request('/health.php')['status'] == 'UP')
    client.login(ENV['ADMIN_EMAIL'], PASSWORD)
    php = demo.CargaPHP(IMAGE, legacy)
    external_course = client.request('/api/cursos', 'POST', {'nome': 'Curso externo QA'}, expected=201)
    external_series = client.request('/api/series', 'POST', {'nome': 'Série externa QA'}, expected=201)
    orphan = client.request('/api/turmas', 'POST', {'nome': 'DEMO Turma 1', 'ano': 2026,
        'cursoId': external_course['id'], 'serieId': external_series['id']}, expected=201)
    # Pré-verificação real PHP recusa colisão e deixa Java completamente vazio.
    sql('legado', "INSERT INTO curso(nome) VALUES ('DEMO Curso');")
    destinations = {'escolar': school, 'legado': php_origin, 'base_legado': RUN}
    with demo.manifesto(destinations, PRIVATE / 'privado' / 'demo') as (value, save):
        before = snapshot('escolar', ['pessoa','curso','serie','turma'])
        try:
            demo.carregar(client, php, value, save)
            raise AssertionError('Turma órfã deveria recusar antes dos pais')
        except demo.DemoErro:
            pass
        assert snapshot('escolar', ['pessoa','curso','serie','turma']) == before
        sql('escolar', 'DELETE FROM turma WHERE id=' + str(orphan['id']) + ';')
        preserved_java = sql('escolar', "SELECT row_to_json(t)::text FROM pessoa t WHERE email='bootstrap@demo.qa.invalid';")
        preserved_php = sql('legado', "SELECT row_to_json(t)::text FROM secretaria2 t WHERE email='bootstrap@demo.qa.invalid';")
        before = snapshot('escolar', ['pessoa','curso'])
        try:
            demo.carregar(client, php, value, save)
            raise AssertionError('Colisão deveria recusar carga')
        except demo.DemoErro:
            pass
        assert snapshot('escolar', ['pessoa','curso']) == before
        sql('legado', "DELETE FROM curso WHERE nome='DEMO Curso';")
        counts = demo.carregar(client, php, value, save)
        assert counts == {'java': 36, 'php': 37}, counts
        tables_java = ['pessoa','curso','serie','materia','turma','disciplina','matricula','aula','nota','presenca']
        tables_php = ['secretaria2','professo2','alunos2','curso','serie','materia','ano','turmas','diciplina','matriculados','notas','aulas','presenca','datas','unidade_escolar']
        # Alteração legítima posterior não é sobrescrita no rerun.
        sql('legado', "UPDATE notas SET n1='99' WHERE cod_aluno='DEMO-aluno1';")
        first = (snapshot('escolar', tables_java), snapshot('legado', tables_php))
        # A releitura durável usa o mesmo arquivo, sem reacquirir o lock em uso.
        reloaded = json.loads((PRIVATE / 'privado' / 'demo' / 'credenciais-demo.json').read_text())
        assert demo.carregar(client, php, reloaded, lambda: None) == counts
        assert first == (snapshot('escolar', tables_java), snapshot('legado', tables_php))
        assert preserved_java == sql('escolar', "SELECT row_to_json(t)::text FROM pessoa t WHERE email='bootstrap@demo.qa.invalid';")
        assert preserved_php == sql('legado', "SELECT row_to_json(t)::text FROM secretaria2 t WHERE email='bootstrap@demo.qa.invalid';")
        assert client.request('/api/cursos/' + str(external_course['id']))['nome'] == 'Curso externo QA'
        for key, account in demo.contas(value, 'java').items():
            profile = demo.ClienteHTTP(school)
            assert profile.login(account['email'], account['senha'])['perfil'] == account['perfil']
            if key == 'aluno1':
                grades = profile.request('/api/boletins/' + str(value['ids']['java'][key]))
                assert any(75 in row['notas'] for row in grades['linhas'])
                try:
                    profile.request('/api/cursos', 'POST', {'nome': 'NEGATIVO'}, expected=201)
                    raise AssertionError('Aluno não pode criar catálogo')
                except demo.DemoErro as error:
                    assert '403' in str(error)
            if key == 'professor1':
                note_id = value['ids']['java']['nota1-1']
                grade = client.request('/api/notas/' + str(note_id))
                payload = {f: grade[f] for f in ('disciplinaId','alunoId','bimestre','valor','liberada')}
                try:
                    profile.request('/api/notas/' + str(note_id), 'PUT', payload)
                    raise AssertionError('Prazo ausente deve continuar fechado')
                except demo.DemoErro as error:
                    assert '409' in str(error)
        for key, account in demo.contas(value, 'php').items():
            profile = PHPCliente(php_origin)
            profile.login(account)
            if key == 'aluno1':
                assert b'99' in profile.request('main/boletim?ano=' + str(value['ano']))
                profile.request('main/cadastrar-curso', status=403)
            if key == 'professor1':
                profile.request('main/cadastrar-curso', status=403)
    output = {'status': 'SUCCESS', 'contas_autenticadas': {'java': 6, 'php': 6}, 'recursos': counts,
              'rerun_sem_alteracao': True, 'colisao_php_sem_escrita_java': True,
              'turma_orfa_bloqueada_sem_criar_pais': True, 'bootstrap_preservado': True,
              'aluno_notas_visiveis': True, 'professor_prazo_fechado_409': True,
              'autorizacao_negativa': True, 'cloud_escritas': 0}
    EVIDENCE.write_text(json.dumps(output, indent=2) + '\n')
    print(json.dumps(output))
finally:
    if json.loads(EVIDENCE.read_text()).get('status') != 'SUCCESS':
        EVIDENCE.write_text(json.dumps({'status': 'FAILED', 'cloud_escritas': 0}) + '\n')
    for name in reversed(OWN):
        subprocess.run(['docker', 'rm', '-f', name], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
    if BUILT_IMAGE:
        subprocess.run(['docker', 'image', 'rm', IMAGE], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
    import shutil
    shutil.rmtree(PRIVATE)
