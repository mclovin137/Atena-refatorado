#!/usr/bin/env python3
"""Carga DEMO manual. Produção exige --aplicar e três origens HTTPS saudáveis."""
import argparse
import contextlib
import datetime
import fcntl
import hashlib
import http.cookiejar
import json
import os
from pathlib import Path
import secrets
import stat
import subprocess
import tempfile
import urllib.error
import urllib.parse
import urllib.request
import uuid

ROOT = Path(__file__).resolve().parents[1]
MARKER = 'atena-demo-v1'
PRIVATE = ROOT / '.atena-cloud-private' / 'demo'
ACCOUNTS = [('secretaria', 'SECRETARIA'), ('professor1', 'PROFESSOR'),
            ('professor2', 'PROFESSOR'), ('aluno1', 'ALUNO'),
            ('aluno2', 'ALUNO'), ('aluno3', 'ALUNO')]


def chaves_java():
    keys = {key for key, _ in ACCOUNTS} | {'curso'}
    for i in range(1, 3):
        keys.update(f'{kind}{i}' for kind in ('serie', 'materia', 'turma'))
        for subject in range(1, 3):
            keys.update((f'disciplina{i}-{subject}', f'aula{i}-{subject}'))
    for student in range(1, 4):
        keys.add(f'matricula{student}')
        for subject in range(1, 3):
            keys.update((f'nota{student}-{subject}', f'presenca{student}-{subject}'))
    return keys


class DemoErro(RuntimeError):
    """Diagnóstico deliberadamente sem dados privados de HTTP/SQL."""


class SemRedirect(urllib.request.HTTPRedirectHandler):
    def redirect_request(self, req, fp, code, msg, headers, newurl):
        raise DemoErro('Redirecionamento HTTP recusado')


class ClienteHTTP:
    def __init__(self, origin):
        self.origin = origin.rstrip('/')
        self.csrf = None
        self.opener = urllib.request.build_opener(
            SemRedirect(), urllib.request.HTTPCookieProcessor(http.cookiejar.CookieJar()))

    def request(self, path, method='GET', data=None, expected=200):
        if not path.startswith('/') or path.startswith('//'):
            raise DemoErro('Caminho HTTP inválido')
        headers = {'Accept': 'application/json'}
        body = None
        if data is not None:
            headers['Content-Type'] = 'application/json'
            body = json.dumps(data).encode()
        if method != 'GET' and self.csrf:
            headers['X-CSRF-TOKEN'] = self.csrf
        try:
            with self.opener.open(urllib.request.Request(
                    self.origin + path, data=body, method=method, headers=headers), timeout=30) as response:
                if response.status != expected:
                    raise DemoErro('Resposta HTTP inesperada')
                return json.load(response)
        except urllib.error.HTTPError as error:
            raise DemoErro(f'HTTP {error.code} em {method} {path.split("?")[0]}') from None
        except (OSError, ValueError):
            raise DemoErro('Falha HTTP; resposta de escrita pode ser ambígua') from None

    def login(self, email, senha):
        self.csrf = self.request('/api/session')['csrfToken']
        person = self.request('/api/session/login', 'POST', {'email': email, 'senha': senha})
        self.csrf = self.request('/api/session')['csrfToken']
        return person


def origem_publica(value):
    parsed = urllib.parse.urlsplit(value)
    if (parsed.scheme != 'https' or not parsed.hostname or parsed.username or parsed.password
            or parsed.path not in ('', '/') or parsed.query or parsed.fragment
            or parsed.hostname in ('localhost', '127.0.0.1', '::1')):
        raise DemoErro('Origem pública HTTPS inválida')
    return urllib.parse.urlunsplit(('https', parsed.netloc.lower(), '', '', ''))


def validar_saude(origins):
    for system, path in [('escolar', '/actuator/health'), ('relatorios', '/actuator/health'),
                         ('legado', '/health.php')]:
        value = ClienteHTTP(origins[system]).request(path)
        if value.get('status') != 'UP':
            raise DemoErro('Uma aplicação não está saudável; carga recusada')


def arquivo_privado(path):
    mode = path.lstat().st_mode
    if not stat.S_ISREG(mode) or stat.S_IMODE(mode) != 0o600:
        raise DemoErro('Arquivo privado deve ser regular e modo600')


def escrever_privado(path, data):
    fd, temporary = tempfile.mkstemp(dir=path.parent, prefix='.demo-')
    try:
        with os.fdopen(fd, 'w') as stream:
            json.dump(data, stream, ensure_ascii=False, indent=2)
            stream.flush()
            os.fsync(stream.fileno())
        os.replace(temporary, path)
        directory = os.open(path.parent, os.O_DIRECTORY)
        try:
            os.fsync(directory)
        finally:
            os.close(directory)
    finally:
        if os.path.exists(temporary):
            os.unlink(temporary)


@contextlib.contextmanager
def manifesto(destinations, directory=PRIVATE):
    for folder in [directory.parent, directory]:
        folder.mkdir(mode=0o700, exist_ok=True)
        if folder.is_symlink() or not folder.is_dir() or stat.S_IMODE(folder.stat().st_mode) != 0o700:
            raise DemoErro('Diretório privado inseguro')
    lock = directory / 'carga.lock'
    fd = os.open(lock, os.O_RDWR | os.O_CREAT | os.O_NOFOLLOW, 0o600)
    with os.fdopen(fd, 'r+') as stream:
        arquivo_privado(lock)
        fcntl.flock(stream, fcntl.LOCK_EX)
        path = directory / 'credenciais-demo.json'
        if path.exists() or path.is_symlink():
            arquivo_privado(path)
            try:
                value = json.loads(path.read_text())
            except (ValueError, OSError):
                raise DemoErro('Manifesto privado inválido; não assumir registros existentes') from None
            if value.get('marker') != MARKER or value.get('destinos') != destinations:
                raise DemoErro('Manifesto pertence a outros destinos ou versão')
        else:
            today = datetime.date.today()
            value = {'marker': MARKER, 'destinos': destinations, 'ano': today.year,
                     'data': today.isoformat(), 'ids': {'java': {}, 'php': {}},
                     'contas': {system: {key: {'senha': secrets.token_urlsafe(24)}
                                        for key, _ in ACCOUNTS} for system in ('java', 'php')}}
            escrever_privado(path, value)
        try:
            for system in ('java', 'php'):
                if not isinstance(value['ids'][system], dict):
                    raise ValueError()
                if set(value['contas'][system]) != {key for key, _ in ACCOUNTS}:
                    raise ValueError()
                expected = chaves_java() | ({'ano'} if system == 'php' else set())
                if set(value['ids'][system]) - expected:
                    raise ValueError()
                for key, locator in value['ids'][system].items():
                    if system == 'java':
                        if type(locator) is not int or locator <= 0:
                            raise ValueError()
                    elif key.startswith('nota'):
                        if not isinstance(locator, dict) or set(locator) != {
                                'cod_ano','cod_curso','cod_serie','cod_professo','cod_aluno','cod_diciplina'}:
                            raise ValueError()
                        if any(not isinstance(field, str) for field in locator.values()):
                            raise ValueError()
                    elif (not isinstance(locator, dict) or set(locator) != {'id'}
                          or type(locator['id']) is not int or locator['id'] <= 0):
                        raise ValueError()
                for key, _ in ACCOUNTS:
                    senha = value['contas'][system][key]['senha']
                    if not isinstance(senha, str) or not 12 <= len(senha.encode()) <= 72:
                        raise ValueError()
            datetime.date.fromisoformat(value['data'])
            if not 1900 <= value['ano'] <= 2200:
                raise ValueError()
        except (KeyError, TypeError, ValueError):
            raise DemoErro('Estrutura privada do manifesto inválida') from None
        yield value, lambda: escrever_privado(path, value)


def contas(value, system):
    return {key: {'nome': 'DEMO ' + key, 'email': key + '@demo.atena.example.test',
                  'matricula': 'DEMO-' + key, 'perfil': role,
                  'senha': value['contas'][system][key]['senha']}
            for key, role in ACCOUNTS}


def plano_java(value, ids):
    """Referências ausentes deixam os filhos pendentes; nenhum ID é adivinhado."""
    rows = []
    def add(key, table, data, fields, dependencies=()):
        if all(dep in ids for dep in dependencies):
            rows.append((key, table, data, {field: data[field] for field in fields}))
    for key, account in contas(value, 'java').items():
        person = {field: account[field] for field in ('nome', 'email', 'matricula', 'perfil')}
        person['ativo'] = True
        rows.append((key, 'pessoas', {'pessoa': person, 'senha': account['senha']},
                     {'email': person['email'], 'matricula': person['matricula'], 'nome': person['nome']}))
    add('curso', 'cursos', {'nome': 'DEMO Curso'}, ['nome'])
    for i in range(1, 3):
        add(f'serie{i}', 'series', {'nome': f'DEMO Série {i}'}, ['nome'])
        add(f'materia{i}', 'materias', {'nome': f'DEMO Matéria {i}'}, ['nome'])
        deps = ('curso', f'serie{i}')
        add(f'turma{i}', 'turmas', {'nome': f'DEMO Turma {i}', 'ano': value['ano'],
                                  'cursoId': ids.get('curso'), 'serieId': ids.get(f'serie{i}')},
            ['nome'], deps)
        for subject in range(1, 3):
            key = f'disciplina{i}-{subject}'
            deps = (f'turma{i}', f'materia{subject}', f'professor{subject}')
            add(key, 'disciplinas', {'turmaId': ids.get(deps[0]), 'materiaId': ids.get(deps[1]),
                                    'professorId': ids.get(deps[2]), 'cargaHoraria': 20},
                ['turmaId', 'materiaId'], deps)
            add(f'aula{i}-{subject}', 'aulas', {'disciplinaId': ids.get(key), 'data': value['data'],
                                               'conteudo': f'DEMO Aula {i}/{subject}', 'horas': 2},
                ['disciplinaId', 'data', 'conteudo'], (key,))
    for student in range(1, 4):
        group = 2 if student == 3 else 1
        deps = (f'turma{group}', f'aluno{student}')
        add(f'matricula{student}', 'matriculas', {'turmaId': ids.get(deps[0]),
                                                'alunoId': ids.get(deps[1]), 'dependencia': False},
            ['turmaId', 'alunoId'], deps)
        for subject in range(1, 3):
            deps = (f'disciplina{group}-{subject}', f'aluno{student}', f'matricula{student}')
            add(f'nota{student}-{subject}', 'notas', {'disciplinaId': ids.get(deps[0]),
                 'alunoId': ids.get(deps[1]), 'bimestre': 1, 'valor': 70 + student * 5, 'liberada': True},
                ['disciplinaId', 'alunoId', 'bimestre'], deps)
            deps = (f'aula{group}-{subject}', f'aluno{student}', f'matricula{student}')
            add(f'presenca{student}-{subject}', 'presencas', {'aulaId': ids.get(deps[0]),
                 'alunoId': ids.get(deps[1]), 'faltas': student % 2}, ['aulaId', 'alunoId'], deps)
    return rows


class CargaJava:
    def __init__(self, client, value, save):
        self.client, self.value, self.save = client, value, save
        self.catalogs = {}

    def verificar(self):
        self.catalogs = {table: self.client.request('/api/' + table) for table in
                         ('pessoas', 'cursos', 'series', 'materias', 'turmas', 'disciplinas',
                          'matriculas', 'aulas', 'notas', 'presencas')}
        self.conferir_marcadores()
        self.conferir(plano_java(self.value, self.value['ids']['java']))

    def conferir_marcadores(self):
        # Nome/conteúdo reservado pode existir antes de qualquer pai confirmado.
        # Pré-verificação não depende de IDs para reconhecer essas colisões.
        placeholders = {key: 1 for key in chaves_java()}
        ids = self.value['ids']['java']
        for key, table, payload, _ in plano_java(self.value, placeholders):
            data = payload.get('pessoa', payload)
            markers = {field: data[field] for field in ('nome', 'conteudo')
                       if isinstance(data.get(field), str) and data[field].startswith('DEMO ')}
            if not markers:
                continue
            for row in self.catalogs[table]:
                if (all(row.get(field) == expected for field, expected in markers.items())
                        and row['id'] != ids.get(key)):
                    raise DemoErro('Colisão com marcador DEMO sem propriedade confirmada')

    def conferir(self, rows):
        ids = self.value['ids']['java']
        for key, table, _, identity in rows:
            catalog = self.catalogs[table]
            if key in ids:
                matches = [row for row in catalog if row['id'] == ids[key]]
                if len(matches) != 1 or any(matches[0].get(field) != expected
                                           for field, expected in identity.items()):
                    raise DemoErro('Identidade DEMO confirmada foi removida ou substituída')
            else:
                # Pessoas podem colidir por qualquer uma das duas identidades únicas.
                fields = ('email', 'matricula') if table == 'pessoas' else tuple(identity)
                if any((any(row.get(f) == identity[f] for f in fields) if table == 'pessoas'
                        else all(row.get(f) == identity[f] for f in fields)) for row in catalog):
                    raise DemoErro('Colisão com identidade reservada sem propriedade confirmada')

    def aplicar(self):
        while True:
            rows = plano_java(self.value, self.value['ids']['java'])
            self.conferir(rows)
            pending = [row for row in rows if row[0] not in self.value['ids']['java']]
            if not pending:
                return len(self.value['ids']['java'])
            key, table, payload, _ = pending[0]
            created = self.client.request('/api/' + table, 'POST', payload, expected=201)
            if not isinstance(created.get('id'), int) or created['id'] <= 0:
                raise DemoErro('Criação sem identidade confirmada; exige reconciliação')
            self.value['ids']['java'][key] = created['id']
            self.save()
            self.catalogs[table].append(created)


def plano_php(value):
    rows = []
    def add(key, table, data, fields=None):
        rows.append({'key': key, 'table': table, 'data': data,
                     'identity': {f: data[f] for f in (fields or data)}})
    for key, account in contas(value, 'php').items():
        table = {'SECRETARIA': 'secretaria2', 'PROFESSOR': 'professo2', 'ALUNO': 'alunos2'}[account['perfil']]
        data = {f: account[f] for f in ('nome', 'email', 'matricula')}
        data.update(estado=1, imagem='user.jpg', senha=account['senha'])
        if table == 'secretaria2':
            data['diretor'] = '0'
        add(key, table, data, ['nome', 'email', 'matricula'])
    add('curso', 'curso', {'nome': 'DEMO Curso'})
    add('ano', 'ano', {'nome': str(value['ano'])})
    for i in range(1, 3):
        add(f'serie{i}', 'serie', {'nome': f'DEMO Série {i}'})
        add(f'materia{i}', 'materia', {'nome': f'DEMO Matéria {i}'})
        add(f'turma{i}', 'turmas', {'ano': str(value['ano']), 'curso': 'DEMO Curso', 'serie': f'DEMO Série {i}'})
        for subject in range(1, 3):
            common = {'cod_ano': str(value['ano']), 'cod_curso': 'DEMO Curso',
                      'cod_serie': f'DEMO Série {i}', 'cod_professo': f'DEMO-professor{subject}'}
            add(f'disciplina{i}-{subject}', 'diciplina', dict(common, nome=f'DEMO Matéria {subject}', carga_horaria='20'), [*common, 'nome'])
            add(f'aula{i}-{subject}', 'aulas', dict(common, cod_materia=f'DEMO Matéria {subject}',
                data=value['data'], conteudo=f'DEMO Aula {i}/{subject}', aulas='2'), [*common, 'cod_materia', 'data', 'conteudo'])
    for student in range(1, 4):
        group = 2 if student == 3 else 1
        common = {'cod_ano': str(value['ano']), 'cod_curso': 'DEMO Curso',
                  'cod_serie': f'DEMO Série {group}', 'cod_aluno': f'DEMO-aluno{student}'}
        add(f'matricula{student}', 'matriculados', dict(common, nome=f'DEMO aluno{student}', dependencia=0))
        for subject in range(1, 3):
            links = dict(common, cod_professo=f'DEMO-professor{subject}')
            fields = dict(links, cod_diciplina=f'DEMO Matéria {subject}')
            add(f'nota{student}-{subject}', 'notas', dict(fields, n1=str(70 + student * 5)), fields)
            fields = dict(links, cod_materia=f'DEMO Matéria {subject}', data=value['data'], bimestre='1')
            add(f'presenca{student}-{subject}', 'presenca', dict(fields, faltas=str(student % 2)), fields)
    return rows


class CargaPHP:
    def __init__(self, image, env):
        self.image, self.env = image, env

    def executar(self, operation, value):
        name = 'atena-demo-cli-' + uuid.uuid4().hex[:12]
        command = ['docker', 'run', '--rm', '--name', name, '-i', '--memory=128m', '--cpus=1',
                   '-v', str(ROOT / 'legacy') + ':/demo:ro']
        for key in ('LEGADO_DB_DSN', 'LEGADO_DB_USER', 'LEGADO_DB_PASSWORD', 'LEGADO_ENV'):
            command.extend(['-e', key])
        command.extend([self.image, 'php', '/demo/bin/povoar-demonstracao.php'])
        request = {'operacao': operation, 'rows': plano_php(value), 'ids': value['ids']['php']}
        try:
            result = subprocess.run(command, input=json.dumps(request).encode(), stdout=subprocess.PIPE,
                                    stderr=subprocess.PIPE, env={**os.environ, **self.env}, timeout=120)
        finally:
            subprocess.run(['docker', 'rm', '-f', name], stdout=subprocess.DEVNULL,
                           stderr=subprocess.DEVNULL, timeout=15)
        if result.returncode:
            raise DemoErro('Operação PHP recusada; nenhuma mensagem SQL privada é emitida')
        try:
            return json.loads(result.stdout)
        except ValueError:
            raise DemoErro('Resposta PHP ambígua; exige reconciliação antes de retomar') from None


def ler_env(path):
    arquivo_privado(path)
    values = {}
    for line in path.read_text().splitlines():
        if line and not line.startswith('#') and '=' in line:
            key, value = line.split('=', 1)
            values[key] = value
    return values


def carregar(client, php, value, save):
    java = CargaJava(client, value, save)
    java.verificar()
    php.executar('verificar', value)
    java.aplicar()
    result = php.executar('aplicar', value)
    value['ids']['php'] = result['ids']
    save()
    return {'java': len(value['ids']['java']), 'php': len(value['ids']['php'])}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--aplicar', action='store_true')
    parser.add_argument('--escolar')
    parser.add_argument('--relatorios')
    parser.add_argument('--legado')
    parser.add_argument('--imagem-legado')
    parser.add_argument('--env', type=Path, default=ROOT / '.env.producao')
    args = parser.parse_args()
    if not args.aplicar:
        print('Plano DEMO: por sistema, 6 contas e 2 turmas, vínculos, notas e presenças. Nenhuma escrita.')
        return
    try:
        if not all([args.escolar, args.relatorios, args.legado, args.imagem_legado]):
            raise DemoErro('Informar as três origens e a imagem PHP revisada')
        origins = {key: origem_publica(getattr(args, key)) for key in ('escolar', 'relatorios', 'legado')}
        validar_saude(origins)
        env = ler_env(args.env)
        for key in ('ADMIN_EMAIL', 'ADMIN_PASSWORD', 'LEGADO_DB_DSN', 'LEGADO_DB_USER', 'LEGADO_DB_PASSWORD'):
            if not env.get(key):
                raise DemoErro('Configuração privada incompleta')
        env['LEGADO_ENV'] = 'production'
        db = dict(part.split('=', 1) for part in env['LEGADO_DB_DSN'].removeprefix('pgsql:').split(';') if '=' in part)
        identity = '|'.join(db.get(key, '') for key in ('host', 'port', 'dbname'))
        destinations = dict(origins, base_legado=hashlib.sha256(identity.encode()).hexdigest())
        client = ClienteHTTP(origins['escolar'])
        if client.login(env['ADMIN_EMAIL'], env['ADMIN_PASSWORD'])['perfil'] != 'SECRETARIA':
            raise DemoErro('Bootstrap administrativo inválido')
        with manifesto(destinations) as (value, save):
            result = carregar(client, CargaPHP(args.imagem_legado, env), value, save)
        print(json.dumps({'status': 'SUCCESS', 'recursos': result, 'credenciais': 'arquivo privado local'}))
    except (DemoErro, subprocess.SubprocessError, OSError):
        print('Carga DEMO recusada ou interrompida; preserve o manifesto para revisão/retomada segura.')
        raise SystemExit(1)


if __name__ == '__main__':
    main()
