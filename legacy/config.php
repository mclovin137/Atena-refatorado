<?php
require_once __DIR__ . '/Models/Seguranca.php';
Seguranca::iniciar();
date_default_timezone_set('America/Sao_Paulo');
define('BASE_DIR', __DIR__ . '/');
$base = getenv('LEGADO_BASE_URL') ?: '/';
if (!str_starts_with($base, '/') && !preg_match('~^https?://[^\s]+/$~D', $base)) {
    throw new RuntimeException('URL base inválida');
}
if (getenv('LEGADO_ENV') === 'production' && !str_starts_with($base, 'https://')) {
    throw new RuntimeException('HTTPS obrigatório');
}
define('INCLUDE_PATH', rtrim($base, '/') . '/');
define('INCLUDE_PATH_MAIN', INCLUDE_PATH . 'main/');
define('INCLUDE_PATH_MAIN_HOME', INCLUDE_PATH_MAIN . 'Home/');
define('INCLUDE_PATH_GOOGLE', 'https://www.google.com/');
require_once BASE_DIR . 'Models/Router.php';
require_once BASE_DIR . 'Models/MySql.php';
require_once BASE_DIR . 'Models/MainMolde.php';
require_once BASE_DIR . 'Models/HomeMolde.php';
require_once BASE_DIR . 'Views/mainviw.php';
spl_autoload_register(function (string $class): void {
    if (!preg_match('~^(Controller|Models|Views)\\\\[A-Za-z0-9_]+$~D', $class)) return;
    $path = BASE_DIR . str_replace('\\', '/', $class) . '.php';
    if (is_file($path)) require_once $path;
});
Seguranca::autorizar();
