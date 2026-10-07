"""Contratos de retomada e falha segura, sem servidor/banco de produção."""
import copy
import importlib.util
from pathlib import Path
import tempfile
import unittest
from unittest import mock

spec = importlib.util.spec_from_file_location('demo', Path(__file__).resolve().parents[1] / 'povoar-demonstracao.py')
demo = importlib.util.module_from_spec(spec)
spec.loader.exec_module(demo)


class MemoriaAPI:
    def __init__(self):
        self.rows = {table: [] for table in ('pessoas', 'cursos', 'series', 'materias', 'turmas',
                     'disciplinas', 'matriculas', 'aulas', 'notas', 'presencas')}
        self.created = 0
        self.fail_after = None

    def request(self, path, method='GET', data=None, expected=200):
        table = path.removeprefix('/api/')
        if method == 'GET':
            return copy.deepcopy(self.rows[table])
        if self.fail_after is not None and self.created == self.fail_after:
            raise demo.DemoErro('Interrupção simulada antes da escrita')
        row = copy.deepcopy(data.get('pessoa', data))
        row.pop('senha', None)
        row['id'] = len(self.rows[table]) + 1
        self.rows[table].append(row)
        self.created += 1
        return copy.deepcopy(row)


class CargaDemoTeste(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.directory = Path(self.temp.name) / 'privado' / 'demo'
        self.destinations = {'escolar': 'https://school.example.test', 'base_legado': 'QA-only'}

    def test_credenciais_duraveis_destino_e_permissoes(self):
        with demo.manifesto(self.destinations, self.directory) as (value, save):
            original = copy.deepcopy(value['contas'])
            self.assertEqual(len({v['senha'] for system in original.values() for v in system.values()}), 12)
            self.assertEqual((self.directory / 'credenciais-demo.json').stat().st_mode & 0o777, 0o600)
        with demo.manifesto(self.destinations, self.directory) as (value, save):
            self.assertEqual(value['contas'], original)
        with self.assertRaises(demo.DemoErro):
            with demo.manifesto({'escolar': 'https://other.example.test'}, self.directory):
                pass
        (self.directory / 'credenciais-demo.json').chmod(0o644)
        with self.assertRaises(demo.DemoErro):
            with demo.manifesto(self.destinations, self.directory):
                pass

    def test_manifesto_corrompido_nao_adota_ids(self):
        for broken in ({'desconhecido': 1}, {'curso': True}, {'curso': '1'}, {'curso': -1}):
            with demo.manifesto(self.destinations, self.directory) as (value, save):
                value['ids']['java'] = broken
                save()
            with self.assertRaises(demo.DemoErro):
                with demo.manifesto(self.destinations, self.directory):
                    pass
            (self.directory / 'credenciais-demo.json').unlink()

    def test_retomada_confirmada_idempotencia_e_colisao(self):
        api = MemoriaAPI()
        with demo.manifesto(self.destinations, self.directory) as (value, save):
            carga = demo.CargaJava(api, value, save)
            carga.verificar()
            api.fail_after = 8
            with self.assertRaises(demo.DemoErro):
                carga.aplicar()
            self.assertEqual(len(value['ids']['java']), 8)
        api.fail_after = None
        with demo.manifesto(self.destinations, self.directory) as (value, save):
            carga = demo.CargaJava(api, value, save)
            carga.verificar()
            self.assertEqual(carga.aplicar(), 36)
            api.rows['notas'][0]['valor'] = 99
            before = copy.deepcopy(api.rows)
            carga.verificar()
            carga.aplicar()
            self.assertEqual(before, api.rows)
            value['ids']['java'] = {}
            with self.assertRaises(demo.DemoErro):
                carga.verificar()
            self.assertEqual(before, api.rows)

    def test_gate_https_e_redirect_nao_reenvia_credenciais(self):
        for url in ('http://school.example.test', 'https://user:senha@school.example.test',
                    'https://localhost', 'https://school.example.test/api', 'https://school.example.test?x=1'):
            with self.assertRaises(demo.DemoErro):
                demo.origem_publica(url)
        with self.assertRaises(demo.DemoErro):
            demo.SemRedirect().redirect_request(None, None, 302, '', {}, 'https://evil.example.test')

    def test_cli_sem_autorizacao_e_saude_negativa_nao_abrem_privados(self):
        with mock.patch('sys.argv', ['demo']), mock.patch.object(demo, 'ler_env') as read:
            demo.main()
            read.assert_not_called()
        args = ['demo', '--aplicar', '--escolar', 'https://school.example.test',
                '--relatorios', 'https://reports.example.test', '--legado', 'https://php.example.test',
                '--imagem-legado', 'qa-only']
        with mock.patch('sys.argv', args), mock.patch.object(demo, 'ClienteHTTP') as client, \
                mock.patch.object(demo, 'ler_env') as read:
            client.return_value.request.return_value = {'status': 'DOWN'}
            with self.assertRaises(SystemExit):
                demo.main()
            read.assert_not_called()
            client.return_value.login.assert_not_called()

    def test_timeout_php_limpa_somente_container_proprio(self):
        with demo.manifesto(self.destinations, self.directory) as (value, save):
            with mock.patch.object(demo.subprocess, 'run', side_effect=[
                    demo.subprocess.TimeoutExpired('docker', 120), mock.Mock(returncode=0)]) as run:
                with self.assertRaises(demo.subprocess.TimeoutExpired):
                    demo.CargaPHP('qa-image', {}).executar('verificar', value)
                cleanup = run.call_args_list[1].args[0]
                self.assertEqual(cleanup[:3], ['docker', 'rm', '-f'])
                self.assertTrue(cleanup[3].startswith('atena-demo-cli-'))
                self.assertIn(cleanup[3], run.call_args_list[0].args[0])

    def test_turma_orfa_reservada_bloqueia_antes_dos_pais(self):
        api = MemoriaAPI()
        api.rows['turmas'].append({'id': 99, 'nome': 'DEMO Turma 1', 'cursoId': 888})
        with demo.manifesto(self.destinations, self.directory) as (value, save):
            with self.assertRaises(demo.DemoErro):
                demo.CargaJava(api, value, save).verificar()
            self.assertEqual(api.created, 0)

    def test_colisao_php_bloqueia_toda_carga_java(self):
        class PHPRecusado:
            def executar(self, operation, value):
                raise demo.DemoErro('Colisão prévia simulada')
        api = MemoriaAPI()
        with demo.manifesto(self.destinations, self.directory) as (value, save):
            with self.assertRaises(demo.DemoErro):
                demo.carregar(api, PHPRecusado(), value, save)
            self.assertEqual(api.created, 0)


if __name__ == '__main__':
    unittest.main()
