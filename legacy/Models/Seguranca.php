<?php
/** Proteção das entradas reais; as regras acadêmicas continuam nos modelos existentes. */
final class Seguranca
{
    public static function iniciar(): void
    {
        if (session_status() === PHP_SESSION_ACTIVE) return;
        ini_set('display_errors', '0');
        ini_set('session.use_strict_mode', '1');
        session_name('ATENA_LEGADO');
        session_set_cookie_params([
            'lifetime' => 0, 'path' => '/', 'httponly' => true, 'samesite' => 'Strict',
            'secure' => getenv('LEGADO_ENV') === 'production' || getenv('LEGADO_COOKIE_SECURE') === 'true',
        ]);
        session_start();
        $_SESSION['csrf'] ??= bin2hex(random_bytes(32));
        header('X-Content-Type-Options: nosniff');
        header('X-Frame-Options: DENY');
        header('Referrer-Policy: no-referrer');
        header('Cache-Control: no-store');
        set_exception_handler(function (Throwable $error): void {
            while (ob_get_level() > 0) ob_end_clean();
            error_log('atena.legado falha=' . get_class($error));
            self::negar(500, 'Não foi possível processar a solicitação.');
        });
        ob_start(function (string $html): string {
            if (!str_contains(strtolower($html), '<html')) return $html;
            $token = htmlspecialchars($_SESSION['csrf'], ENT_QUOTES, 'UTF-8');
            $html = preg_replace('~(<form\b[^>]*>)~i', '$1<input type="hidden" name="csrf" value="' . $token . '">', $html);
            $script = '<meta name="atena-csrf" content="' . $token . '"><script src="' .
                htmlspecialchars(INCLUDE_PATH . 'Views/js/seguranca.js', ENT_QUOTES, 'UTF-8') . '"></script>';
            return preg_replace('~(<head\b[^>]*>)~i', '$1' . $script, $html, 1);
        });
    }

    public static function perfil(): string
    {
        if (!empty($_SESSION['login_secretaria'])) return 'SECRETARIA';
        if (!empty($_SESSION['login_Professo'])) return 'PROFESSOR';
        if (!empty($_SESSION['login_Aluno'])) return 'ALUNO';
        return '';
    }

    public static function autorizar(): void
    {
        if (PHP_SAPI === 'cli') return;
        $path = parse_url($_SERVER['REQUEST_URI'] ?? '/', PHP_URL_PATH);
        $script = basename($_SERVER['SCRIPT_FILENAME'] ?? '');
        $ajax = str_contains($path, '/Models/ajax/');
        $pdf = str_contains($path, '/Models/pdf/');
        $route = trim((string) ($_GET['url'] ?? ''), '/');
        $method = $_SERVER['REQUEST_METHOD'] ?? 'GET';
        if (!in_array($method, ['GET', 'POST'], true)) self::negar(405);
        if ($method === 'POST') {
            $token = $_SERVER['HTTP_X_CSRF_TOKEN'] ?? $_POST['csrf'] ?? '';
            if (!is_string($token) || !hash_equals($_SESSION['csrf'], $token)) self::negar(403);
        }
        if ($script === 'index.php' && $route === '') return;
        $perfil = self::perfil();
        if ($perfil === '') {
            if ($ajax || $pdf || $script === 'arquivo.php') self::negar(401);
            header('Location: ' . INCLUDE_PATH, true, 303);
            exit;
        }
        self::verificarConta($perfil);
        if (isset($_GET['loggout'])) self::negar(405);
        if (isset($_POST['loggout'])) {
            $_SESSION = [];
            session_destroy();
            header('Location: ' . INCLUDE_PATH, true, 303);
            exit;
        }
        if ($script === 'Atualiza.php' && $ajax) {
            self::autorizarAjax($script, $perfil);
            return;
        }
        if ($perfil === 'SECRETARIA') {
            if ($script === 'CadastroSectario.php' || in_array($route, ['main/cadastrar-sectario','main/listagem-sectarios','main/editar-sectarios'], true)) {
                $query = MySql::conectar()->prepare('SELECT diretor FROM secretaria2 WHERE matricula = ?');
                $query->execute([$_SESSION['matricula']]);
                if ((int) $query->fetchColumn() !== 1) self::negar(403);
            }
            return;
        }
        if ($script === 'gerar-pdf.php') {
            if ($perfil !== 'ALUNO') self::negar(403);
            return;
        }
        if ($pdf) {
            if ($script !== 'Historico.php' && $script !== 'HistoricoCompleto.php') self::negar(403);
            if ($perfil !== 'ALUNO') self::negar(403);
            if (isset($_GET['id']) && $_GET['id'] !== $_SESSION['matricula']) self::negar(403);
            if (isset($_SESSION['IDaluno']) && $_SESSION['IDaluno'] !== $_SESSION['matricula']) self::negar(403);
            $_SESSION['IDaluno'] = $_SESSION['matricula'];
            $_SESSION['IdHistorico'] = $_SESSION['matricula'];
            $_SESSION['IDano'] = $_SESSION['ano'] ?? (string) date('Y');
            return;
        }
        if ($script === 'arquivo.php') return; // Arquivo::baixar aplica autorização pelo registro.
        if ($ajax) {
            self::autorizarAjax($script, $perfil);
            return;
        }
        $allowed = ['main', 'main/Home', 'main/home', 'main/atualiza-dados', 'main/calendario',
            'main/boletim', 'main/diario', 'main/avaliacoes', 'main/aulas', 'main/listagem-horario',
            'main/chat', 'main/chat-box', 'main/cadastrar-arquivo'];
        if ($perfil === 'PROFESSOR') {
            $allowed = array_merge($allowed, ['main/diario-box', 'main/planeja-aula', 'main/planeja-aula-box']);
        }
        if (!in_array($route, $allowed, true)) self::negar(403);
        if ($route === 'main/chat-box') self::verificarChat();
        if ($perfil === 'ALUNO' && $method === 'POST' && $route !== 'main/chat-box' && $route !== 'main/cadastrar-arquivo') self::negar(403);
        if ($perfil === 'PROFESSOR' && isset($_GET['id']) && in_array($route, ['main/boletim', 'main/diario-box', 'main/planeja-aula-box', 'main/cadastrar-arquivo'], true)) {
            self::verificarOfertaId($_GET['id']);
        }
        if ($perfil === 'PROFESSOR' && $method === 'POST' && $route === 'main/boletim') {
            if (!isset($_GET['id'])) self::negar(403);
            self::verificarOfertaId($_GET['id']);
        }
    }

    private static function verificarConta(string $perfil): void
    {
        $table = ['SECRETARIA' => 'secretaria2', 'PROFESSOR' => 'professo2', 'ALUNO' => 'alunos2'][$perfil];
        $query = MySql::conectar()->prepare("SELECT estado FROM $table WHERE matricula = ?");
        $query->execute([$_SESSION['matricula'] ?? '']);
        $state = $query->fetchColumn();
        if ($state === false || (int) $state !== 1) self::negar(403);
    }

    private static function autorizarAjax(string $script, string $perfil): void
    {
        if ($script === 'Atualiza.php') {
            $flag = ['ALUNO' => 'atualiza', 'PROFESSOR' => 'atualiza-prof', 'SECRETARIA' => 'atualiza-adm'][$perfil];
            if (!isset($_POST[$flag]) || ($_POST['matriculaatual'] ?? '') !== $_SESSION['matricula']) self::negar(403);
            // Hash persistido não deve vir de campo escondido controlável pelo cliente.
            $table = ['ALUNO' => 'alunos2', 'PROFESSOR' => 'professo2', 'SECRETARIA' => 'secretaria2'][$perfil];
            $query = MySql::conectar()->prepare("SELECT senha, imagem FROM $table WHERE matricula = ?");
            $query->execute([$_SESSION['matricula']]);
            $record = $query->fetch(PDO::FETCH_ASSOC);
            $_POST['senhaatual'] = $record['senha'];
            $_POST['imagematual'] = $record['imagem'];
            return;
        }
        if ($script === 'Calendario.php' && ($_POST['acao'] ?? '') === 'puxar') return;
        if ($script === 'Horario.php') {
            $table = $perfil === 'ALUNO' ? 'matriculados' : 'diciplina';
            $person = $perfil === 'ALUNO' ? 'cod_aluno' : 'cod_professo';
            $query = MySql::conectar()->prepare("SELECT id FROM $table WHERE $person = ? AND cod_ano = ? AND cod_curso = ? AND cod_serie = ?");
            $query->execute([$_SESSION['matricula'], $_POST['ano'] ?? '', $_POST['curso'] ?? '', $_POST['serie'] ?? '']);
            if ($query->fetchColumn() === false) self::negar(403);
            return;
        }
        if ($perfil === 'PROFESSOR' && $script === 'Pesquisa.php') return;
        if ($perfil === 'PROFESSOR' && $script === 'CadastroNotas.php') {
            self::verificarOfertaPost();
            self::verificarLancamentoNotas();
            return;
        }
        if ($perfil === 'PROFESSOR' && $script === 'EnviarAlaiacao.php') {
            $actions = ['lanca_avaliacao_qualificativa','lanca_rec','media_avaliacao_qualificativa',
                'soma_avaliacao_qualificativa','editar_avaliacao_qualificativa','editar_rec',
                'cadastar_avaliacao_qualificativa','cadastar_frequencia','exluir-atividade-qualificativa','altera-atividade-qualificativa'];
            $selected = array_values(array_intersect($actions, array_keys($_POST)));
            if (count($selected) !== 1) self::negar(403);
            self::verificarOfertaPost();
            if ($selected[0] !== 'cadastar_frequencia') self::verificarPrazoAvaliacao();
            return;
        }
        self::negar(403);
    }

    public static function verificarOfertaId(mixed $id): void
    {
        if (!is_scalar($id) || !ctype_digit((string) $id)) self::negar(400);
        $query = MySql::conectar()->prepare('SELECT cod_professo FROM diciplina WHERE id = ?');
        $query->execute([$id]);
        if ($query->fetchColumn() !== $_SESSION['matricula']) self::negar(403);
    }

    public static function verificarOfertaPost(): void
    {
        $teacher = $_POST['cod'] ?? $_POST['professo'] ?? $_POST['cod_professo'] ?? $_SESSION['matricula'];
        if ($teacher !== $_SESSION['matricula']) self::negar(403);
        $course = $_POST['cod_curso'] ?? $_POST['curso'] ?? null;
        $year = $_POST['cod_ano'] ?? $_POST['ano'] ?? null;
        $series = $_POST['cod_serie'] ?? $_POST['serie'] ?? null;
        $subject = $_POST['cod_diciplina'] ?? $_POST['nome_diciplina'] ?? $_POST['materia'] ?? null;
        if (!$course || !$year || !$series || !$subject) self::negar(403);
        $query = MySql::conectar()->prepare('SELECT id FROM diciplina WHERE cod_curso = ? AND cod_ano = ? AND cod_serie = ? AND nome = ? AND cod_professo = ?');
        $query->execute([$course, $year, $series, $subject, $_SESSION['matricula']]);
        if ($query->fetchColumn() === false) self::negar(403);
        $_POST['cod'] = $_SESSION['matricula'];
        $_POST['professo'] = $_SESSION['matricula'];
    }

    private static function verificarPrazoAvaliacao(): void
    {
        $period = array_search($_POST['bimestre'] ?? '', ['1° bimestre','2° bimestre','3° bimestre','4° bimestre'], true);
        if ($period === false) self::negar(400);
        $query = MySql::conectar()->query('SELECT datanota01,datanota02,datanota03,datanota04 FROM datas');
        $deadlines = $query->fetch(PDO::FETCH_NUM);
        if (!$deadlines || $deadlines[$period] === '' || date('Y-m-d') > $deadlines[$period]) self::negar(403);
        $flag = 'nota0' . ($period + 1);
        $query = MySql::conectar()->prepare("SELECT 1 FROM notas WHERE cod_curso = ? AND cod_ano = ? AND cod_serie = ? AND cod_diciplina = ? AND cod_professo = ? AND $flag = '1'");
        $query->execute([$_POST['cod_curso'], $_POST['cod_ano'], $_POST['cod_serie'], $_POST['cod_diciplina'], $_SESSION['matricula']]);
        if ($query->fetchColumn() !== false) self::negar(403);
    }

    private static function verificarLancamentoNotas(): void
    {
        $course = $_POST['cod_curso'];
        $year = $_POST['cod_ano'];
        $series = $_POST['cod_serie'];
        $subject = $_POST['nome_diciplina'];
        $query = MySql::conectar()->prepare('SELECT cod_aluno FROM matriculados WHERE cod_curso = ? AND cod_ano = ? AND cod_serie = ?');
        $query->execute([$course, $year, $series]);
        $students = $query->fetchAll(PDO::FETCH_COLUMN);
        $query = MySql::conectar()->query('SELECT datanota01,datanota02,datanota03,datanota04 FROM datas');
        $deadlines = $query->fetch(PDO::FETCH_NUM);
        foreach ($_POST as $key => $value) {
            if (!preg_match('/^(nota0[1-4]|rec0[1-4])([0-9]{1,30})$/D', $key, $match)) continue;
            $student = $match[2];
            if (!in_array($student, $students, true)) self::negar(403);
            if (!is_scalar($value) || ($value !== '' && (!is_numeric($value) || !is_finite((float) $value) || (float) $value < 0 || (float) $value > (str_starts_with($match[1], 'rec') ? 60 : 100)))) self::negar(400, 'Nota inválida.');
            $period = (int) substr($match[1], -1);
            $column = (str_starts_with($match[1], 'rec') ? 'r' : 'n') . $period;
            $flag = 'nota0' . $period;
            $query = MySql::conectar()->prepare("SELECT $column, $flag FROM notas WHERE cod_aluno = ? AND cod_curso = ? AND cod_ano = ? AND cod_serie = ? AND cod_diciplina = ? AND cod_professo = ?");
            $query->execute([$student, $course, $year, $series, $subject, $_SESSION['matricula']]);
            $old = $query->fetch(PDO::FETCH_NUM);
            $blocked = !$deadlines || $deadlines[$period - 1] === '' || date('Y-m-d') > $deadlines[$period - 1] || ($old && (int) $old[1] === 1);
            if ($blocked && (string) $value !== (string) ($old[0] ?? '')) self::negar(403);
        }
    }

    private static function verificarChat(): void
    {
        // Remetente nunca é definido pelo formulário; destinatário precisa existir.
        if (isset($_POST['remetente'])) $_POST['remetente'] = $_SESSION['matricula'];
        $destiny = $_POST['destino'] ?? $_GET['id'] ?? null;
        if ($destiny !== null && (!is_scalar($destiny) || !preg_match('/^[0-9]{1,30}$/D', (string) $destiny))) self::negar(400);
    }

    public static function texto(mixed $value): string
    {
        return htmlspecialchars((string) ($value ?? ''), ENT_QUOTES | ENT_SUBSTITUTE, 'UTF-8');
    }

    public static function senhaNova(mixed $password): string
    {
        if (!is_string($password) || strlen($password) < 12 || strlen($password) > 72) self::negar(400, 'Senha deve ter entre 12 e 72 bytes.');
        return password_hash($password, PASSWORD_DEFAULT);
    }

    public static function senhaExistente(string $table, string $matricula): string
    {
        if (!in_array($table, ['alunos2','professo2','secretaria2'], true)) self::negar(400);
        $query = MySql::conectar()->prepare("SELECT senha FROM $table WHERE matricula = ?");
        $query->execute([$matricula]);
        $hash = $query->fetchColumn();
        if ($hash === false) self::negar(404);
        return $hash;
    }

    public static function validarTabela(string $table): void
    {
        $allowed = ['alunos2','ano','aulas','avaliacoes','caledario','chat','curso','datas','dependencia',
            'diciplina','grade_curricular','grade_curricular_padroes','grades_curricules','horario','materia',
            'materiais','matriculados','notas','presenca','professo2','recuperacao','secretaria2','serie','turmas','unidade_escolar'];
        if (!in_array($table, $allowed, true)) self::negar(400);
    }

    public static function negar(int $status, string $message = 'Acesso não permitido.'): never
    {
        http_response_code($status);
        header('Content-Type: application/json; charset=utf-8');
        echo json_encode(['sucesso' => false, 'mensagem' => $message], JSON_UNESCAPED_UNICODE);
        exit;
    }
}
