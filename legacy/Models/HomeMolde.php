<?php
namespace Models;

class HomeMolde
{
    public static function login(): void
    {
        $espera = (new \LimiteLogin(sys_get_temp_dir() . '/atena-legado-login'))->consumir($_SERVER['REMOTE_ADDR'] ?? 'desconhecido');
        if ($espera > 0) {
            header('Retry-After: ' . $espera);
            http_response_code(429);
            echo '<div class="erro-box">Muitas tentativas. Aguarde e tente novamente.</div>';
            return;
        }
        $email = trim((string) ($_POST['nome'] ?? ''));
        $matricula = trim((string) ($_POST['matricula'] ?? ''));
        $senha = (string) ($_POST['senha'] ?? '');
        $contas = [
            'alunos2' => 'login_Aluno',
            'professo2' => 'login_Professo',
            'secretaria2' => 'login_secretaria',
        ];
        foreach ($contas as $table => $flag) {
            $query = \MySql::conectar()->prepare("SELECT * FROM $table WHERE email = ? AND matricula = ?");
            $query->execute([$email, $matricula]);
            $info = $query->fetch(\PDO::FETCH_ASSOC);
            if (!$info || !password_verify($senha, $info['senha']) || (int) $info['estado'] !== 1) continue;
            session_regenerate_id(true);
            $_SESSION = [
                $flag => true,
                'nome' => $info['nome'], 'id' => $info['id'],
                'imagem' => $info['imagem'], 'matricula' => $info['matricula'], 'email' => $info['email'],
                // Compatibilidade com guardas históricos; autorização real é Seguranca::autorizar.
                'sessao' => md5($info['matricula'] . ($_SERVER['REMOTE_ADDR'] ?? '') . ($_SERVER['HTTP_USER_AGENT'] ?? '')),
                'csrf' => bin2hex(random_bytes(32)),
            ];
            self::redirect(INCLUDE_PATH_MAIN_HOME);
        }
        echo '<div class="erro-box">Usuário ou senha incorretos!</div>';
    }

    public static function redirect(string $url): never
    {
        header('Location: ' . $url, true, 303);
        exit;
    }
}
