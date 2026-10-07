<?php
/** Operação exclusivamente CLI; entrada/saída privada pelo processo controlador. */
if (PHP_SAPI !== 'cli') { http_response_code(404); exit; }
require_once dirname(__DIR__) . '/Models/MySql.php';

function colunasDemo(): array
{
    $pessoa = ['nome','email','matricula','estado','imagem','senha'];
    $vinculo = ['cod_ano','cod_curso','cod_serie','cod_professo'];
    return [
        'secretaria2'=>array_merge($pessoa,['diretor']), 'professo2'=>$pessoa, 'alunos2'=>$pessoa,
        'curso'=>['nome'], 'serie'=>['nome'], 'materia'=>['nome'], 'ano'=>['nome'],
        'turmas'=>['ano','curso','serie'],
        'diciplina'=>array_merge($vinculo,['nome','carga_horaria']),
        'aulas'=>array_merge($vinculo,['cod_materia','data','conteudo','aulas']),
        'matriculados'=>['cod_ano','cod_curso','cod_serie','cod_aluno','nome','dependencia'],
        'notas'=>array_merge($vinculo,['cod_aluno','cod_diciplina','n1']),
        'presenca'=>array_merge($vinculo,['cod_aluno','cod_materia','data','bimestre','faltas'])
    ];
}

function selecionarDemo(PDO $pdo, string $table, array $identity): array
{
    $where = implode(' AND ', array_map(fn($field) => '"'.$field.'" = ?', array_keys($identity)));
    $query = $pdo->prepare('SELECT * FROM "'.$table.'" WHERE '.$where);
    $query->execute(array_values($identity));
    return $query->fetchAll(PDO::FETCH_ASSOC);
}

function validarDemo(array $row, array $columns): void
{
    $table = $row['table'];
    if (!isset($columns[$table]) || !$row['identity'] || !$row['data']
        || array_diff(array_keys($row['data']), $columns[$table])
        || array_diff(array_keys($row['identity']), array_keys($row['data']))
        || isset($row['identity']['senha'])) throw new RuntimeException('Entrada inválida');
    foreach ($row['identity'] as $key=>$value) {
        if ((string)$row['data'][$key] !== (string)$value) throw new RuntimeException('Identidade inválida');
    }
    $marker = $row['data']['cod_curso'] ?? $row['data']['curso'] ?? $row['data']['nome'] ?? '';
    if ($table !== 'ano' && !str_starts_with($marker, 'DEMO ')) throw new RuntimeException('Marcador ausente');
    if (isset($row['data']['senha'])) {
        if (strlen($row['data']['senha']) < 12 || strlen($row['data']['senha']) > 72
            || !str_starts_with($row['data']['matricula'], 'DEMO-')
            || !str_ends_with($row['data']['email'], '@demo.atena.example.test')) {
            throw new RuntimeException('Conta inválida');
        }
    }
}

function verificarDemo(PDO $pdo, array $rows, array $ids): array
{
    $missing = [];
    $columns = colunasDemo();
    $keys = [];
    foreach ($rows as $row) {
        validarDemo($row, $columns);
        $key = $row['key']; $table = $row['table'];
        if (isset($keys[$key])) throw new RuntimeException('Chave repetida');
        $keys[$key] = true;
        $matches = selecionarDemo($pdo, $table, $row['identity']);
        if (isset($ids[$key])) {
            if (count($matches) !== 1) throw new RuntimeException('Identidade removida');
            $locator = $table === 'notas' ? $row['identity'] : ['id'=>(int)$matches[0]['id']];
            if ($ids[$key] != $locator) throw new RuntimeException('Identidade substituída');
            continue;
        }
        if (isset($row['data']['senha'])) {
            foreach (['secretaria2','professo2','alunos2'] as $accountTable) {
                $query = $pdo->prepare('SELECT 1 FROM "'.$accountTable.'" WHERE email=? OR matricula=? LIMIT 1');
                $query->execute([$row['data']['email'], $row['data']['matricula']]);
                if ($query->fetchColumn()) throw new RuntimeException('Conta reservada existente');
            }
        }
        if ($table === 'ano' && count($matches) === 1) continue; // Apenas leitura de catálogo existente.
        if ($matches) throw new RuntimeException('Colisão com identidade reservada');
        $missing[] = $row;
    }
    if (array_diff(array_keys($ids), array_keys($keys))) throw new RuntimeException('Manifesto incompatível');
    return $missing;
}

try {
    $input = json_decode(stream_get_contents(STDIN), true, 32, JSON_THROW_ON_ERROR);
    if (!in_array($input['operacao'], ['verificar','aplicar'], true)) throw new RuntimeException('Operação inválida');
    $pdo = MySql::conectar();
    $pdo->beginTransaction();
    if ($input['operacao'] === 'aplicar') $pdo->query("SELECT pg_advisory_xact_lock(724615903)");
    $missing = verificarDemo($pdo, $input['rows'], $input['ids']);
    $ids = $input['ids'];
    if ($input['operacao'] === 'aplicar') {
        foreach ($missing as $row) {
            $data = $row['data']; $table = $row['table'];
            if (isset($data['senha'])) $data['senha'] = password_hash($data['senha'], PASSWORD_BCRYPT);
            $fields = implode(',', array_map(fn($field) => '"'.$field.'"', array_keys($data)));
            $sql = 'INSERT INTO "'.$table.'" ('.$fields.') VALUES ('.implode(',',array_fill(0,count($data),'?')).')';
            if ($table !== 'notas') $sql .= ' RETURNING id';
            $query = $pdo->prepare($sql);
            $query->execute(array_values($data));
            $ids[$row['key']] = $table === 'notas' ? $row['identity'] : ['id'=>(int)$query->fetchColumn()];
        }
    }
    $pdo->commit();
    echo json_encode(['status'=>'SUCCESS','ids'=>$ids,'pendentes'=>count($missing)], JSON_THROW_ON_ERROR);
} catch (Throwable $error) {
    if (isset($pdo) && $pdo->inTransaction()) $pdo->rollBack();
    fwrite(STDERR, "Carga DEMO PHP recusada; dados privados omitidos.\n");
    exit(1);
}
