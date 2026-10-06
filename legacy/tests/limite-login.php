<?php
if (PHP_SAPI !== 'cli') { http_response_code(404); exit; }
require __DIR__ . '/../Models/LimiteLogin.php';
$dir = sys_get_temp_dir() . '/atena-limite-teste-' . bin2hex(random_bytes(8));
try {
    $limite = new LimiteLogin($dir, 2);
    for ($n = 0; $n < 20; $n++) {
        if ($limite->consumir('127.0.0.1', 100) !== 0) throw new RuntimeException('Limite antecipado');
    }
    if ((new LimiteLogin($dir, 2))->consumir('127.0.0.1', 100) !== 60) throw new RuntimeException('Novo processo perde contador');
    if ($limite->consumir('127.0.0.2', 100) !== 0) throw new RuntimeException('Outro IP bloqueado');
    if ($limite->consumir('127.0.0.3', 100) !== 60) throw new RuntimeException('Capacidade excedida');
    if ($limite->consumir('127.0.0.1', 160) !== 0) throw new RuntimeException('Janela não expira');
    if ((fileperms($dir) & 0777) !== 0700 || (fileperms($dir . '/contador') & 0777) !== 0600) throw new RuntimeException('Permissões inseguras');
    echo "Limite login: SUCCESS\n";
} finally {
    if (is_file($dir . '/contador')) unlink($dir . '/contador');
    if (is_dir($dir)) rmdir($dir);
}
