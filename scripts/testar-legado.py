#!/usr/bin/env python3
"""Fluxo PHP real em projeto Docker descartável. Nunca lê o dump nem a stack local."""
import struct
import zlib
import hashlib
import http.cookiejar
import json
import os
from pathlib import Path
import re
import secrets
import socket
import subprocess
import sys
import tempfile
import time
import urllib.error
import urllib.parse
import urllib.request
import uuid

ROOT = Path(__file__).resolve().parents[1]
EVIDENCE = ROOT / 'evidence'
EVIDENCE.mkdir(exist_ok=True)
run_id = 'atena-php-qa-' + uuid.uuid4().hex[:12]
private = Path(tempfile.mkdtemp(prefix='atena-php-qa-'))
os.chmod(private, 0o700)
password = secrets.token_hex(24)
with socket.socket() as sock:
    sock.bind(('127.0.0.1', 0))
    port = sock.getsockname()[1]
env_file = private / '.env'
variables = dict.fromkeys(['POSTGRES_PASSWORD','ESCOLAR_DB_PASSWORD','RELATORIOS_DB_PASSWORD','SERVICE_TOKEN','ADMIN_PASSWORD','LEGADO_DB_PASSWORD','LEGADO_ADMIN_PASSWORD'], password)
variables.update(ADMIN_EMAIL='secretaria@qa.invalid', LEGADO_ADMIN_EMAIL='secretaria@legado.qa.invalid', LEGADO_PORT=str(port))
env_file.write_text(''.join(f'{key}={value}\n' for key,value in variables.items()))
os.chmod(env_file, 0o600)
command = ['docker','compose','--project-name',run_id,'--env-file',str(env_file),'--profile','legado']
base = f'http://localhost:{port}/'
results = []
restore_container = run_id + "-restore"
login_container = run_id + "-login"

def compose(*args, input=None, capture=True):
    result = subprocess.run(command+list(args), input=input, stdout=subprocess.PIPE if capture else None,
                            stderr=subprocess.PIPE, cwd=ROOT, check=False)
    if result.returncode:
        diagnostic = (result.stdout or b'') + (result.stderr or b'')
        diagnostic = diagnostic.replace(password.encode(), b'[REDACTED]')
        diagnostic = re.sub(rb'(Cookie|Set-Cookie|Authorization):[^\n]*', rb'\1: [REDACTED]', diagnostic, flags=re.I)
        (EVIDENCE / ('legado-' + str(args[0]) + '-falha.log')).write_bytes(diagnostic)
        raise RuntimeError('Operação Docker falhou: ' + str(args[0]) + '; diagnóstico saneado em evidence')
    return result.stdout or b''

class NoRedirect(urllib.request.HTTPRedirectHandler):
    def redirect_request(self, req, fp, code, msg, headers, newurl):
        return None

class Client:
    def __init__(self):
        self.opener = urllib.request.build_opener(urllib.request.HTTPCookieProcessor(http.cookiejar.CookieJar()),NoRedirect())
        self.csrf = ''

    def request(self, path, data=None, expected=200, csrf=True, file=None):
        headers = {'User-Agent':'Atena-QA'}
        body = None
        if data is not None:
            if file:
                boundary = 'atena'+secrets.token_hex(12)
                pieces=[]
                for key,value in data.items():
                    pieces.append(f'--{boundary}\r\nContent-Disposition: form-data; name="{key}"\r\n\r\n{value}\r\n'.encode())
                field,name,bytes_,media=file
                pieces.append(f'--{boundary}\r\nContent-Disposition: form-data; name="{field}"; filename="{name}"\r\nContent-Type: {media}\r\n\r\n'.encode()+bytes_+b'\r\n')
                body=b''.join(pieces)+f'--{boundary}--\r\n'.encode()
                headers['Content-Type']='multipart/form-data; boundary='+boundary
            else:
                body=urllib.parse.urlencode(data,doseq=True).encode()
                headers['Content-Type']='application/x-www-form-urlencoded'
            if csrf:
                headers['X-CSRF-TOKEN']=self.csrf
        request=urllib.request.Request(base+path,data=body,headers=headers)
        try:
            response=self.opener.open(request,timeout=10)
        except urllib.error.HTTPError as error:
            response=error
        self.response_headers = response.headers
        bytes_=response.read()
        if response.code != expected:
            raise AssertionError(f'HTTP {request.get_method()} {path.split("?")[0]}: esperado{expected}, recebido{response.code}')
        if response.headers.get_content_type()=='text/html':
            match=re.search(rb'<meta name="atena-csrf" content="([a-f0-9]+)"',bytes_)
            if match:self.csrf=match[1].decode()
        return bytes_

    def login(self,email,matricula):
        self.request('')
        self.request('',{'acao':'Enviar','nome':email,'matricula':matricula,'senha':password},303)
        page=self.request('main/Home/')
        assert b'Sair' in page
        return self

try:
    compose('up','--build','--detach','legado')
    for _ in range(120):
        try:
            if json.loads(Client().request('health.php'))['status']=='UP':break
        except (OSError,ValueError,AssertionError):time.sleep(1)
    else:raise AssertionError('HTTP PHP não iniciou')
    lint=compose('exec','-T','legado','sh','-c',"find . -name '*.php' -not -path './Lib/vendor/*' -print0 | xargs -0 -n1 php -l")
    (EVIDENCE/'legado-lint.log').write_bytes(lint)
    fixture=json.loads(compose('exec','-T','-e','LEGADO_TEST_FIXTURES=1','legado','php','tests/preparar.php'))
    catalogs=json.dumps({'importacaoId':str(uuid.uuid4()),'registros':[{'tabela':'curso','status':'IMPORTADO','dados':{'id':'99','nome':'Curso ponte QA'}},{'tabela':'materia','status':'IMPORTADO','dados':{'id':'99','nome':'Matéria ponte QA'}}]}).encode()
    bridge=('exec','-T','-e','LEGADO_IMPORT_CATALOGOS=1','legado','php','bin/importar-catalogos.php')
    assert json.loads(compose(*bridge,input=catalogs))['inseridos']==2
    assert json.loads(compose(*bridge,input=catalogs))['inseridos']==0
    results.append({'fluxo':'ponte-catalogos-sanitizada-replay','inseridos':2,'replay':0})
    anonymous=Client()
    ajax=sorted((ROOT/'legacy/Models/ajax').glob('*.php'))
    pdf_paths=sorted((ROOT/'legacy/Models/pdf').glob('*.php'))
    for path in ajax+pdf_paths:
        anonymous.request(str(path.relative_to(ROOT/'legacy')),expected=401)
    routes=re.findall(r"Router::get\('([^']+)'",(ROOT/'legacy/index.php').read_text())
    for route in routes:
        if route=='/':continue
        anonymous.request(route.lstrip('/').replace('?','1'),expected=303)
    for path in ['config.php','Models/Seguranca.php','Views/pages/Home.php','Views/pdf/Historico.php','Views/uploads/user.jpg','bin/inicializar.php','tests/preparar.php']:
        anonymous.request(path,expected=403)
    results.append({'fluxo':'entradas-anonimas','rotas':len(routes),'ajax':len(ajax),'pdf':len(pdf_paths)})
    admin=Client().login(variables['LEGADO_ADMIN_EMAIL'],'000000')
    teacher=Client().login('docente@qa.invalid','010001')
    student=Client().login('aluno@qa.invalid','010002')
    outsider=Client().login('foraaluno@qa.invalid','010003')
    admin.request('main/cadastrar-curso',{'Cadastre-curso':'1','nome':'Curso via PHP'},csrf=False,expected=403)
    page=admin.request('main/cadastrar-curso',{'Cadastre-curso':'1','nome':'Curso via PHP'})
    assert b'Curso via PHP' in page
    admin.request('main/cadastrar-turmas',{'Cadastre-turmas':'1','ano':'2026','serie':'Série QA','curso':'Curso QA','grade':'nenhun','alunos[]':['010002']})
    results.append({'fluxo':'catalogo-matricula','status':'validado'})
    student.request('main/boletim?ano=2026')
    own=f'main/boletim?id={fixture["disciplina"]}'
    material_route=f'main/cadastrar-arquivo?id={fixture["disciplina"]}'
    teacher.request(own)
    teacher.request(f'main/cadastrar-arquivo?id={fixture["disciplinaAlheia"]}',expected=403)
    teacher.request(f'main/boletim?id={fixture["disciplinaAlheia"]}',expected=403)
    student.request('Models/ajax/CadastroNotas.php',{'salva':'1'},expected=403)
    # Conteúdo persistido deve ser literal nas respostas HTML das famílias AJAX.
    compose('exec','-T','legado','php','-r',"require 'Models/MySql.php';MySql::conectar()->exec(\"UPDATE diciplina SET nome='<script>window.qaXss=1</script>' WHERE cod_professo='010001'\");")
    for client, endpoint in [(teacher,'Pesquisa.php'),(admin,'PesquisaAdm.php'),(admin,'CadastrarHorario.php')]:
        search=client.request('Models/ajax/'+endpoint,{**dict.fromkeys(['ano','materia','curso','serie'],'todos'),'professor':'010001'})
        assert b'<script>window.qaXss' not in search and b'&lt;script&gt;window.qaXss' in search
    material_page=teacher.request(material_route)
    assert b'<script>window.qaXss' not in material_page and b'&lt;script&gt;window.qaXss' in material_page
    compose('exec','-T','legado','php','-r',"require 'Models/MySql.php';MySql::conectar()->exec(\"UPDATE diciplina SET nome='Matéria QA' WHERE cod_professo='010001'\");")
    payload='<script>window.qaXss=1</script>'
    compose('exec','-T','legado','php','-r',"require 'Models/MySql.php';MySql::conectar()->exec(\"UPDATE turmas SET serie='<script>window.qaXss=1</script>'; UPDATE alunos2 SET nome='<script>window.qaXss=1</script>' WHERE matricula='010002'; INSERT INTO horario(cod_diciplina,cod_ano,cod_serie,cod_curso,dia,horario) VALUES('<script>window.qaXss=1</script>','2026','Série QA','Curso QA','segunda','07:00')\");")
    for client, endpoint, data in [(admin,'Gestaotumas.php',dict.fromkeys(['ano','curso','serie'],'todos')), (admin,'EnviarAlaiacao.php',{'busca':'window.qaXss'}), (teacher,'Horario.php',{'ano':'2026','curso':'Curso QA','serie':'Série QA'})]:
        search=client.request('Models/ajax/'+endpoint,data)
        assert payload.encode() not in search and b'&lt;script&gt;window.qaXss' in search
    compose('exec','-T','legado','php','-r',"require 'Models/MySql.php';MySql::conectar()->exec(\"UPDATE turmas SET serie='Série QA'; UPDATE alunos2 SET nome='Aluno QA' WHERE matricula='010002'; DELETE FROM horario\");")
    results.append({'fluxo':'ajax-html-xss-persistido','familias':6,'status':'validado'})
    notes={'salva':'1','cod_curso':'Curso QA','cod_ano':'2026','cod_serie':'Série QA','professo':'010001','nome_diciplina':'Matéria QA'}
    for number in range(1,5):
        notes[f'nota0{number}010002']='80'
        notes[f'rec0{number}010002']=''
    notes['media010002']='80'
    teacher.request('Models/ajax/CadastroNotas.php',notes,csrf=False,expected=403)
    teacher.request('Models/ajax/CadastroNotas.php',notes)
    teacher.request('Models/ajax/CadastroNotas.php',{**notes,'professo':'010004'},expected=403)
    teacher.request('Models/ajax/CadastroNotas.php',{**notes,'nota01010003':'99'},expected=403)
    compose('exec','-T','legado','php','-r',"require 'Models/MySql.php';MySql::conectar()->exec(\"UPDATE datas SET datanota01='2000-01-01'\");")
    teacher.request('Models/ajax/CadastroNotas.php',{**notes,'nota01010002':'81'},expected=403)
    compose('exec','-T','legado','php','-r',"require 'Models/MySql.php';MySql::conectar()->exec(\"UPDATE datas SET datanota01='2099-12-31'\");")
    presence={'cadastar_frequencia':'1','cod_curso':'Curso QA','cod_ano':'2026','cod_serie':'Série QA','cod_diciplina':'Matéria QA','cod':'010001','bimestre':'1° bimestre','data':'01/06/2026','aulas':'2','aula-conteudo':'Conteúdo QA','faltas010002':'0'}
    teacher.request('Models/ajax/EnviarAlaiacao.php',presence)
    academic=json.loads(compose('exec','-T','legado','php','-r',"require 'Models/MySql.php';$db=MySql::conectar();echo json_encode(['notas'=>$db->query(\"SELECT n1,n2,n3,n4 FROM notas WHERE cod_aluno='010002' AND cod_professo='010001'\")->fetch(PDO::FETCH_NUM),'presencas'=>(int)$db->query(\"SELECT count(*) FROM presenca WHERE cod_aluno='010002' AND cod_professo='010001'\")->fetchColumn(),'aulas'=>(int)$db->query(\"SELECT count(*) FROM aulas WHERE cod_professo='010001'\")->fetchColumn()]);"))
    assert academic == {'notas':['80']*4,'presencas':1,'aulas':1}, 'Notas e presença devem persistir no PostgreSQL real'
    document=admin.request('Models/pdf/ListaAlunos.php')
    assert document.startswith(b'%PDF-') and len(document)>500
    student.request('main/boletim?ano=2026')
    own_pdf=student.request('gerar-pdf.php')
    assert own_pdf.startswith(b'%PDF-')
    student.request('Models/pdf/HistoricoCompleto.php?id=010003',expected=403)
    for pdf_path in ['Models/pdf/ListaProfessores.php','Models/pdf/ListaTurmas.php']:
        assert admin.request(pdf_path).startswith(b'%PDF-')
    for pdf_path in ['Models/pdf/Historico.php','Models/pdf/HistoricoCompleto.php?id=010002']:
        assert student.request(pdf_path).startswith(b'%PDF-')
    # Banco inteiro aqui contém somente as fixtures sintéticas deste projeto UUID.
    get_snapshot = "require 'Models/MySql.php';$out=[];foreach(['curso','alunos2','professo2','secretaria2','matriculados','notas','recuperacao','avaliacoes','aulas','presenca','materiais','arquivo_privado'] as $t){$out[$t]=MySql::conectar()->query('SELECT * FROM '.$t.' ORDER BY 1')->fetchAll(PDO::FETCH_ASSOC);}echo json_encode($out);"
    before=compose('exec','-T','legado','php','-r',get_snapshot)
    for client,pages in [(admin,['main/Home/',own,'main/cadastrar-curso',material_route]), (teacher,['main/Home/',own,material_route]), (student,['main/Home/','main/boletim?ano=2026'])]:
        for page in pages: client.request(page)
    for ajax_path in ['Models/ajax/CadastroAluno.php','Models/ajax/CadastroNotas.php','Models/ajax/EnviarAlaiacao.php']:
        admin.request(ajax_path)
    after=compose('exec','-T','legado','php','-r',get_snapshot)
    assert before==after, 'GET de páginas não deve alterar registros do banco'
    results.append({'fluxo':'get-sem-escrita','familias':12,'pdf_positivos':6})
    results.append({'fluxo':'notas-presenca-pdf-privacidade','pdf_bytes':len(document),'boletim_bytes':len(own_pdf)})
    teacher.request(material_route,{'upload':'1','nome':'Material QA','ano':'2026','curso':'Curso QA','serie':'Série QA','materia':'Matéria QA','matricula':'010003'},file=('arquivo','material.pdf',document,'application/pdf'))
    listing=teacher.request(material_route)
    match=re.search(rb'arquivo.php\?nome=([a-f0-9]{32}\.pdf)',listing)
    assert match, 'Material persistido deve aparecer na interface real'
    path='arquivo.php?nome='+match[1].decode()
    assert student.request(path)==document
    outsider.request(path,expected=403)
    anonymous.request(path,expected=401)
    avatar_data={'atualiza':'1','nome':'Aluno QA','matriculaatual':'010002','novasenha':'','cpf':'','cep':'00000-000','data_de_nacimento':'2010-01-01','pai':'Pai QA','mae':'Mãe QA','estado_civil':'Solteiro','telefone':'','telefoneatual':'','sexo':'M','email':'aluno@qa.invalid','emailatual':'aluno@qa.invalid','imagematual':'user.jpg'}
    def png_chunk(kind, data):
        return struct.pack('>I',len(data))+kind+data+struct.pack('>I',zlib.crc32(kind+data))
    png=b'\x89PNG\r\n\x1a\n'+png_chunk(b'IHDR',struct.pack('>IIBBBBB',1,1,8,2,0,0,0))+png_chunk(b'IDAT',zlib.compress(b'\x00\xff\xff\xff'))+png_chunk(b'IEND',b'')
    avatar_reply=json.loads(student.request('Models/ajax/Atualiza.php',avatar_data,file=('imagem','avatar.png',png,'image/png')))
    assert avatar_reply['sucesso'], 'Atualização real de avatar não teve sucesso'
    profile=student.request('main/atualiza-dados')
    avatar_name=re.search(rb'arquivo.php\?nome=([a-f0-9]{32}\.jpg)',profile)
    assert avatar_name, 'Avatar deve estar vinculado ao perfil'
    avatar_path='arquivo.php?nome='+avatar_name[1].decode()
    avatar_bytes=student.request(avatar_path)
    assert avatar_bytes.startswith(b'\xff\xd8')
    assert teacher.request(avatar_path)==avatar_bytes
    outsider.request(avatar_path,expected=403)
    compose('restart','legado')
    for _ in range(60):
        try:
            student=Client().login('aluno@qa.invalid','010002')
            break
        except (OSError,AssertionError):time.sleep(1)
    else:raise AssertionError('PHP não retomou após restart')
    assert student.request(path)==document
    assert student.request(avatar_path)==avatar_bytes
    results.append({'fluxo':'bytea-autorizacao-restart','sha256':hashlib.sha256(document).hexdigest()})
    # pg_dump/pg_restore reais; dados sintéticos ficam somente em memória/DB descartáveis.
    dump=compose('exec','-T','postgres','pg_dump','-U','postgres','--format=custom','legado')
    compose('exec','-T','postgres','createdb','-U','postgres','--owner=legado','legado_restore')
    compose('exec','-T','postgres','pg_restore','-U','postgres','--dbname=legado_restore','--no-owner','--role=legado',input=dump)
    with socket.socket() as sock:
        sock.bind(('127.0.0.1',0))
        restore_port=sock.getsockname()[1]
    compose('run','--detach','--no-deps','--name',restore_container,'--publish',f'127.0.0.1:{restore_port}:8080','--env','LEGADO_DB_DSN=pgsql:host=postgres;port=5432;dbname=legado_restore','--env',f'LEGADO_BASE_URL=http://localhost:{restore_port}/','legado','apache2-foreground')
    original_base=base
    try:
        base=f'http://localhost:{restore_port}/'
        for _ in range(60):
            try:
                restored=Client().login('aluno@qa.invalid','010002')
                break
            except (OSError,AssertionError):time.sleep(1)
        else:raise AssertionError('PHP restaurado não iniciou')
        assert restored.request(path)==document
        assert restored.request(avatar_path)==avatar_bytes
        Client().login('foraaluno@qa.invalid','010003').request(avatar_path,expected=403)
        Client().request(path,expected=401)
        results.append({'fluxo':'pg-dump-restore-aplicacao-nova','material_sha256':hashlib.sha256(document).hexdigest(),'avatar_sha256':hashlib.sha256(avatar_bytes).hexdigest()})
    finally:
        base=original_base
    # Fonte maliciosa de busca deve ser dado literal, sem causar login/bypass/alteração.
    admin.request('main/listagem-alunos',{'pesquisa':'1','busca':"' OR 1=1 --"})
    student.request('main/Home/?loggout',expected=405)
    student.request('main/Home/',{'loggout':'1'},expected=303)
    student.request(path,expected=401)
    results.append({'fluxo':'sql-parametrizado-logout','status':'validado'})
    if os.environ.get('PLAYWRIGHT_DOCKER_IMAGE'):
        # A imagem existente traz bibliotecas/browsers, sem mudar o runtime do host.
        browser_image=os.environ['PLAYWRIGHT_DOCKER_IMAGE']
        browser_modules=os.environ['PLAYWRIGHT_DOCKER_MODULES']
        browser_env={**os.environ,'LEGADO_TEST_URL':base,'LEGADO_TEST_PASSWORD':password}
        subprocess.run(['docker','run','--rm','--network','host','--mount',f'type=bind,src={ROOT},dst=/workspace','--mount',f'type=bind,src={browser_modules},dst=/browser,readonly','--workdir','/workspace','--env','LEGADO_TEST_URL','--env','LEGADO_TEST_PASSWORD','--env','PLAYWRIGHT_MODULE=/browser/node_modules/playwright',browser_image,'node','scripts/browser-legado.mjs'],env=browser_env,cwd=ROOT,check=True)
    elif os.environ.get('PLAYWRIGHT_MODULE'):
        browser_env = {**os.environ, 'LEGADO_TEST_URL': base, 'LEGADO_TEST_PASSWORD': password}
        subprocess.run(['node', 'scripts/browser-legado.mjs'], cwd=ROOT, env=browser_env, check=True)
    compose('exec','-T','legado','php','tests/limite-login.php')
    # Outra instância da mesma imagem: o limite começa vazio, sem apagar estado de workers ativos.
    with socket.socket() as sock:
        sock.bind(('127.0.0.1',0))
        login_port=sock.getsockname()[1]
    compose('run','--detach','--no-deps','--name',login_container,'--publish',f'127.0.0.1:{login_port}:8080',
            '--env',f'LEGADO_BASE_URL=http://localhost:{login_port}/','legado','apache2-foreground')
    original_base=base
    try:
        base=f'http://localhost:{login_port}/'
        for _ in range(60):
            try:
                if json.loads(Client().request('health.php'))['status']=='UP':break
            except (OSError,ValueError,AssertionError):time.sleep(1)
        else:raise AssertionError('PHP de limite não iniciou')
        for tentativa in range(20):
            anonymous=Client()
            anonymous.request('')
            anonymous.request('',{'acao':'Enviar','nome':'ausente@qa.invalid','matricula':'invalid','senha':'invalid'})
        anonymous=Client()
        anonymous.request('')
        anonymous.request('',{'acao':'Enviar','nome':'ausente@qa.invalid','matricula':'invalid','senha':'invalid'},expected=429)
        assert 1 <= int(anonymous.response_headers['Retry-After']) <= 60
    finally:
        base=original_base
    results.append({'fluxo':'limite-login','tentativas':20,'nova_sessao':'HTTP 429','janela':'expiração determinística validada'})
    (EVIDENCE/'legado-fluxo.json').write_text(json.dumps({'status':'success','resultados':results},indent=2))
    print('Fluxo PHP PostgreSQL completo: SUCCESS')
except BaseException as error:
    (EVIDENCE/'legado-fluxo.json').write_text(json.dumps({'status':'failed','resultados':results,'falha':str(error)},indent=2))
    raise
finally:
    primary_failure = sys.exc_info()[0] is not None
    cleanup_errors = []
    try:
        logs=compose('logs','--no-color')
        logs=logs.replace(password.encode(),b'[REDACTED]')
        logs=re.sub(rb'\?[^ "\n]*',b'?[REDACTED]',logs)
        (EVIDENCE/'legado-compose.log').write_bytes(logs)
    except Exception as error:
        cleanup_errors.append(str(error))
    try:
        subprocess.run(['docker','rm','--force',login_container],stdout=subprocess.DEVNULL,stderr=subprocess.DEVNULL,check=False)
        subprocess.run(['docker','rm','--force',restore_container],stdout=subprocess.DEVNULL,stderr=subprocess.DEVNULL,check=False)
        compose('down','--volumes','--remove-orphans') # Apenas projeto UUID, nunca a stack local.
    except Exception as error:
        cleanup_errors.append(str(error))
    finally:
        env_file.unlink(missing_ok=True)
        private.rmdir()
    if cleanup_errors:
        (EVIDENCE/'legado-limpeza.json').write_text(json.dumps({'falhas':cleanup_errors},indent=2))
        if not primary_failure:
            raise RuntimeError('Limpeza do ambiente sintético falhou; consultar evidência')
