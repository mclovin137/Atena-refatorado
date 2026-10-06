<?php
final class Arquivo
{
    public static function avatar(array $file): string
    {
        self::validarUpload($file, 5242880);
        $bytes = file_get_contents($file['tmp_name']);
        $info = @getimagesizefromstring($bytes);
        if (!$info || !in_array($info[2], [IMAGETYPE_JPEG, IMAGETYPE_PNG], true) || $info[0] > 4096 || $info[1] > 4096) Seguranca::negar(400, 'Imagem inválida.');
        $image = @imagecreatefromstring($bytes);
        if (!$image) Seguranca::negar(400, 'Imagem inválida.');
        ob_start();
        imagejpeg($image, null, 85);
        $bytes = ob_get_clean();
        imagedestroy($image);
        return self::guardar($bytes, 'image/jpeg', 'AVATAR', 'jpg');
    }

    public static function material(array $file): string
    {
        self::validarUpload($file, 10485760);
        $extension = strtolower(pathinfo($file['name'], PATHINFO_EXTENSION));
        $bytes = file_get_contents($file['tmp_name']);
        $media = (new finfo(FILEINFO_MIME_TYPE))->buffer($bytes);
        $allowed = ['pdf' => ['application/pdf'], 'doc' => ['application/msword', 'application/CDFV2'],
            'zip' => ['application/zip'], 'rar' => ['application/vnd.rar', 'application/x-rar']];
        if (!isset($allowed[$extension]) || !in_array($media, $allowed[$extension], true)) Seguranca::negar(400, 'Tipo de arquivo não permitido.');
        return self::guardar($bytes, $media, 'MATERIAL', $extension);
    }

    private static function validarUpload(array $file, int $limit): void
    {
        if (($file['error'] ?? UPLOAD_ERR_NO_FILE) !== UPLOAD_ERR_OK || !is_uploaded_file($file['tmp_name'] ?? '') || ($file['size'] ?? 0) <= 0 || $file['size'] > $limit) Seguranca::negar(400, 'Arquivo inválido ou excede o limite.');
    }

    private static function guardar(string $bytes, string $media, string $type, string $extension): string
    {
        $name = bin2hex(random_bytes(16)) . '.' . $extension;
        $query = MySql::conectar()->prepare('INSERT INTO arquivo_privado (nome,autor,tipo,media_type,conteudo) VALUES (?,?,?,?,?)');
        $query->bindValue(1, $name);
        $query->bindValue(2, $_SESSION['matricula']);
        $query->bindValue(3, $type);
        $query->bindValue(4, $media);
        $query->bindValue(5, $bytes, PDO::PARAM_LOB);
        $query->execute();
        return $name;
    }

    public static function baixar(string $name): never
    {
        if ($name === 'user.jpg') {
            header('Content-Type: image/svg+xml');
            echo '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64"><circle cx="32" cy="24" r="14" fill="#999"/><path d="M6 64a26 26 0 0 1 52 0" fill="#999"/></svg>';
            exit;
        }
        if (!preg_match('/^[a-f0-9]{32}\.(jpg|pdf|doc|zip|rar)$/D', $name)) Seguranca::negar(404);
        $query = MySql::conectar()->prepare('SELECT autor,tipo,media_type FROM arquivo_privado WHERE nome = ?');
        $query->execute([$name]);
        $metadata = $query->fetch(PDO::FETCH_ASSOC);
        if (!$metadata) Seguranca::negar(404);
        self::autorizarArquivo($name, $metadata);
        $query = MySql::conectar()->prepare('SELECT conteudo FROM arquivo_privado WHERE nome = ?');
        $query->execute([$name]);
        $bytes = $query->fetchColumn();
        if (is_resource($bytes)) $bytes = stream_get_contents($bytes);
        header('Content-Type: ' . $metadata['media_type']);
        header('Content-Disposition: ' . ($metadata['tipo'] === 'AVATAR' ? 'inline' : 'attachment') . '; filename="' . $name . '"');
        echo $bytes;
        exit;
    }

    private static function autorizarArquivo(string $name, array $metadata): void
    {
        $perfil = Seguranca::perfil();
        if ($perfil === 'SECRETARIA') return;
        if ($metadata['tipo'] === 'AVATAR') {
            $table = $perfil === 'ALUNO' ? 'alunos2' : 'professo2';
            $query = MySql::conectar()->prepare("SELECT id FROM $table WHERE matricula = ? AND imagem = ?");
            $query->execute([$_SESSION['matricula'], $name]);
            if ($query->fetchColumn() !== false) return;
            if ($perfil === 'PROFESSOR') {
                $query = MySql::conectar()->prepare('SELECT 1 FROM alunos2 a JOIN matriculados m ON m.cod_aluno = a.matricula JOIN diciplina d ON d.cod_curso = m.cod_curso AND d.cod_serie = m.cod_serie AND d.cod_ano = m.cod_ano WHERE a.imagem = ? AND d.cod_professo = ?');
            } else {
                $query = MySql::conectar()->prepare('SELECT 1 FROM professo2 p JOIN diciplina d ON d.cod_professo = p.matricula JOIN matriculados m ON m.cod_curso = d.cod_curso AND m.cod_serie = d.cod_serie AND m.cod_ano = d.cod_ano WHERE p.imagem = ? AND m.cod_aluno = ?');
            }
            $query->execute([$name, $_SESSION['matricula']]);
            if ($query->fetchColumn() === false) Seguranca::negar(403);
            return;
        }
        $query = MySql::conectar()->prepare('SELECT cod_curso,cod_serie,cod_ano,cod_materia,cod_professo FROM materiais WHERE arquivo = ?');
        $query->execute([$name]);
        $material = $query->fetch(PDO::FETCH_ASSOC);
        if (!$material) Seguranca::negar(403);
        if ($perfil === 'ALUNO') {
            $query = MySql::conectar()->prepare('SELECT id FROM matriculados WHERE cod_aluno = ? AND cod_curso = ? AND cod_serie = ? AND cod_ano = ?');
            $query->execute([$_SESSION['matricula'], $material['cod_curso'], $material['cod_serie'], $material['cod_ano']]);
        } else {
            $query = MySql::conectar()->prepare('SELECT id FROM diciplina WHERE cod_professo = ? AND cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND nome = ?');
            $query->execute([$_SESSION['matricula'], $material['cod_curso'], $material['cod_serie'], $material['cod_ano'], $material['cod_materia']]);
        }
        if ($query->fetchColumn() === false) Seguranca::negar(403);
    }
}
