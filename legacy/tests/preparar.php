<?php
if (PHP_SAPI !== 'cli' || getenv('LEGADO_TEST_FIXTURES') !== '1') { http_response_code(404); exit; }
require_once __DIR__ . '/../Models/MySql.php';
$password = getenv('LEGADO_ADMIN_PASSWORD');
$hash = password_hash($password, PASSWORD_DEFAULT);
$db = MySql::conectar();
$db->beginTransaction();
$query = $db->prepare('INSERT INTO professo2 (nome,senha,matricula,email,estado,imagem) VALUES (?,?,?,?,1,?)');
$query->execute(['Docente QA', $hash, '010001', 'docente@qa.invalid', 'user.jpg']);
$query->execute(['Outro docente QA', $hash, '010004', 'outrodocente@qa.invalid', 'user.jpg']);
$query = $db->prepare('INSERT INTO alunos2 (nome,senha,matricula,email,estado,imagem) VALUES (?,?,?,?,1,?)');
$query->execute(['Aluno QA', $hash, '010002', 'aluno@qa.invalid', 'user.jpg']);
$query->execute(['Aluno fora QA', $hash, '010003', 'foraaluno@qa.invalid', 'user.jpg']);
$db->exec("INSERT INTO curso (nome) VALUES ('Curso QA'),('Curso Fora QA'); INSERT INTO materia (nome) VALUES ('Matéria QA'),('Matéria Fora QA'); INSERT INTO serie (nome) VALUES ('Série QA'); INSERT INTO ano (nome) VALUES ('2026')");
$query = $db->prepare('INSERT INTO diciplina (nome,cod_ano,cod_curso,cod_serie,cod_professo,carga_horaria) VALUES (?,?,?,?,?,?) RETURNING id');
$query->execute(['Matéria QA','2026','Curso QA','Série QA','010001','60']);
$own = (int) $query->fetchColumn();
$query->execute(['Matéria Fora QA','2026','Curso Fora QA','Série QA','010004','60']);
$other = (int) $query->fetchColumn();
$query = $db->prepare('INSERT INTO grade_curricular (nome,cod_serie,cod_ano,cod_curso,estado,ch) VALUES (?,?,?,?,?,?)');
$query->execute(['Matéria QA','Série QA','2026','Curso QA',1,60]);
$query = $db->prepare('INSERT INTO unidade_escolar (nome,endereço,diretor,quantidade_aulas,minutos_aulas,telefone,cnpj) VALUES (?,?,?,?,?,?,?)');
$query->execute(['Escola QA','Endereço sintético','Diretor QA','100','60','000','00.000.000/0000-00']);
$query = $db->prepare('INSERT INTO datas (datanota01,datanota02,datanota03,datanota04) VALUES (?,?,?,?)');
$query->execute(array_fill(0,4,'2099-12-31'));
$db->commit();
echo json_encode(['disciplina' => $own,'disciplinaAlheia' => $other], JSON_THROW_ON_ERROR);
