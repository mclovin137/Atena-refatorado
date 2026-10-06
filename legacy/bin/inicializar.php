<?php
if (PHP_SAPI !== 'cli') { http_response_code(404); exit; }
require_once __DIR__ . '/../Models/MySql.php';
$email = getenv('LEGADO_ADMIN_EMAIL') ?: '';
$password = getenv('LEGADO_ADMIN_PASSWORD') ?: '';
if (!filter_var($email, FILTER_VALIDATE_EMAIL) || (strlen($password) < 12 || strlen($password) > 72)) throw new RuntimeException('Bootstrap privado não configurado');
$query = MySql::conectar()->prepare('INSERT INTO secretaria2 (nome,senha,matricula,email,estado,diretor,imagem) VALUES (?,?,?,?,?,?,?) ON CONFLICT (matricula) DO NOTHING');
$query->execute(['Secretaria', password_hash($password, PASSWORD_DEFAULT), '000000', $email, 1, 1, 'user.jpg']);
fwrite(STDOUT, "Bootstrap privado verificado; senha existente preservada.\n");
