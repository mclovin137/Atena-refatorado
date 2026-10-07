<?php
// Ponte opt-in para exportação SANITIZADA do staging Java; nunca interpreta SQL/dump.
if (PHP_SAPI !== 'cli' || getenv('LEGADO_IMPORT_CATALOGOS') !== '1') {
    http_response_code(404);
    exit;
}
require_once __DIR__ . '/../Models/MySql.php';
try {
    $input = stream_get_contents(STDIN, 1048577);
    if (strlen($input) > 1048576) throw new InvalidArgumentException('Limite da fonte excedido');
    $source = json_decode($input, true, 64, JSON_THROW_ON_ERROR);
    if (!is_array($source) || !is_string($source['importacaoId'] ?? null) ||
        !preg_match('/^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$/Di', $source['importacaoId']) ||
        !is_array($source['registros'] ?? null)) throw new InvalidArgumentException('Contrato inválido');
    $rows = [];
    foreach ($source['registros'] as $row) {
        if (!is_array($row) || !in_array($row['tabela'] ?? null, ['curso', 'materia'], true) ||
            ($row['status'] ?? null) !== 'IMPORTADO' || !is_array($row['dados'] ?? null) ||
            array_diff(array_keys($row['dados']), ['id', 'nome'])) throw new InvalidArgumentException('Registro não permitido');
        $name = $row['dados']['nome'] ?? null;
        if (!is_string($name) || trim($name) === '' || mb_strlen($name) > 150) throw new InvalidArgumentException('Nome inválido');
        $rows[] = [$row['tabela'], $name];
    }
    $db = MySql::conectar();
    $db->beginTransaction();
    // Mesma transação e lock serializam replay; não usa IDs 2020 em identidade 2024.
    $db->exec('LOCK TABLE curso, materia IN SHARE ROW EXCLUSIVE MODE');
    $inserted = 0;
    foreach ($rows as [$table, $name]) {
        $query = $db->prepare("INSERT INTO $table (nome) VALUES (?) ON CONFLICT (nome) DO NOTHING");
        $query->execute([$name]);
        $inserted += $query->rowCount(); // DML, não SELECT.
    }
    $db->commit();
    echo json_encode(['registros' => count($rows), 'inseridos' => $inserted], JSON_THROW_ON_ERROR) . PHP_EOL;
} catch (Throwable $error) {
    if (isset($db) && $db->inTransaction()) $db->rollBack();
    fwrite(STDERR, "Ponte de catálogos recusada; fonte inválida ou persistência indisponível.\n");
    exit(1);
}
