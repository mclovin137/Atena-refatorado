<?php

namespace Models;



class MainMolde
{
        public static function ola()
        {
            echo "ola";
        }
        public static function sair()
        {
            session_destroy();
            HomeMolde::redirect(INCLUDE_PATH);
        }

        public static function deletegrade()
        {
            @$curso = strip_tags($_POST['curso']);
            @$ano = strip_tags($_POST['ano']);
            @$serie = strip_tags($_POST['serie']);



                if (@$_POST['materia'])
                {
                        $materia = @$_POST['materia'];
                        $nome = MainMolde::select('grade_curricular','id = ?',array($materia));
                        $nomeMateria = $nome[1];
                        $verificaatividades = MainMolde::selectAll('avaliacoes','cod_materia = ? AND cod_ano = ? AND cod_curso = ? AND cod_serie = ?',array($nomeMateria,$ano,$curso,$serie));
                        $countarividades = ceil(count($verificaatividades));
                        $verificapresença = MainMolde::selectAll('presenca','cod_materia = ? AND cod_ano = ? AND cod_curso = ? AND cod_serie = ?',array($nomeMateria,$ano,$curso,$serie));
                        $countpreseça = ceil(count($verificapresença));
                        $verificahorario = MainMolde::selectAll('horario','cod_diciplina = ? AND cod_ano = ? AND cod_curso = ? AND cod_serie = ?',array($nomeMateria,$ano,$curso,$serie));
                        $counthorario = ceil(count($verificahorario));

                        if($counthorario == 0 && $countpreseça == 0 && $countarividades == 0)
                        {
                            $sql = \MySql::conectar()->prepare("DELETE FROM diciplina  WHERE nome = ?");
                            $sql->execute(array($nomeMateria));
                            $sqlg = \MySql::conectar()->prepare("DELETE FROM grade_curricular  WHERE id = ?");
                            $sqlg->execute(array($materia));
                            echo '<div class="box-sucesso"><i class="fa fa-check"></i> exclusao efetuado com sucesso!</div>';
                        }
                        else
                         echo '<div class="erro-box"><i class="fa fa-times"></i> Nao e permitido altera materias, que ja estao em produçao</div>';



                }
                else
                  echo '<div class="erro-box"><i class="fa fa-times"></i> Nenhuma materia selecionada!</div>';

        }

        public static function editargrade()
        {
                   @$curso = strip_tags($_POST['curso']);
                   @$ano = strip_tags($_POST['ano']);
                   @$serie = strip_tags($_POST['serie']);
                   $cadastroinvalido = false;
                   $i = 0;



                   if (@$_POST['materias'])
                   {
                       foreach (@$_POST['nome'] as $valor)
                       {
                          $nome[] = $valor;
                       }
                       foreach (@$_POST['ch'] as $ch)
                       {
                          $chs[] = $ch;
                       }
                       foreach (@$_POST['nomeatual'] as $nt)
                       {
                          $nomeatual[] = $nt;
                       }
                       foreach (@$_POST['materias'] as $materia)
                       {

                            if($nome[$i] != $nomeatual[$i])
                            {
                                $verifica = MainMolde::selectAll('grade_curricular','nome = ? AND cod_ano = ? AND cod_curso = ? AND cod_serie = ?',
                                array($nome[$i],$ano,$curso,$serie));
                                $count = ceil(count($verifica));
                            }
                            else
                            {
                                $count = 0 ;
                            }

                            $verificaatividades = MainMolde::selectAll('avaliacoes','cod_materia = ? AND cod_ano = ? AND cod_curso = ? AND cod_serie = ?',array($nomeatual[$i],$ano,$curso,$serie));
                            $countarividades = ceil(count($verificaatividades));
                            $verificapresença = MainMolde::selectAll('presenca','cod_materia = ? AND cod_ano = ? AND cod_curso = ? AND cod_serie = ?',array($nomeatual[$i],$ano,$curso,$serie));
                            $countpreseça = ceil(count($verificapresença));
                            $verificahorario = MainMolde::selectAll('horario','cod_diciplina = ? AND cod_ano = ? AND cod_curso = ? AND cod_serie = ?',array($nomeatual[$i],$ano,$curso,$serie));
                            $counthorario = ceil(count($verificahorario));

                            if($counthorario == 0 && $countpreseça == 0 && $countarividades == 0 && $count == 0)
                            {
                                $sql = \MySql::conectar()->prepare("UPDATE diciplina SET nome = ?,carga_horaria = ?  WHERE nome = ? AND cod_ano = ? AND cod_curso = ? AND cod_serie = ?");
                                $sql->execute(array($nome[$i],$chs[$i],$nomeatual[$i],$ano,$curso,$serie));
                                $sql = \MySql::conectar()->prepare("UPDATE grade_curricular SET nome = ?,ch = ?  WHERE id = ?");
                                $sql->execute(array($nome[$i],$chs[$i],$materia));
                                $i++;
                            }
                            else
                            {
                                $cadastroinvalido = true;
                                break;
                            }
                       }

                        if ($cadastroinvalido == true)
                             echo '<div class="erro-box"><i class="fa fa-times"></i> Nao e permitido altera materias, que ja estao em produçao</div>';
                        else
                            echo '<div class="box-sucesso"><i class="fa fa-check"></i> ediçao efetuado com sucesso!</div>';
                   }
                    else
                      echo '<div class="erro-box"><i class="fa fa-times"></i> Nenhuma materia selecionada!</div>';




        }

        public static function excluir($table)
        {
            \Seguranca::validarTabela($table);
             $nome  = strip_tags($_POST['nome']);

             $sql = \MySql::conectar()->prepare("DELETE FROM  $table  WHERE nome = ? ");
             $sql->execute(array($nome));
        }

        public static function editar($table)
        {
            \Seguranca::validarTabela($table);
             $nome  = strip_tags($_POST['nome']);
             $nomeatual  = strip_tags($_POST['nomeatual']);

             $verifica = \MySql::conectar()->prepare("SELECT COUNT(*) FROM $table WHERE nome = ?");
             $verifica->execute(array($nome));
        $verificaQuantidade = (int) $verifica->fetchColumn();

             if($verificaQuantidade != 0 )
             {

                  echo '<div class="erro-box"><i class="fa fa-times"></i> por favor tente  outro nome!</div>';

             }
             else
             {
                 $sql = \MySql::conectar()->prepare("UPDATE $table SET nome = ? WHERE nome = ? ");
                 $sql->execute(array($nome,$nomeatual));
                 echo '<div class="box-sucesso"><i class="fa fa-check"></i> cadastro efetuado com sucesso</div>';

             }
        }


        public static function uploadFile($file)
        {
            require_once BASE_DIR . 'Models/Arquivo.php';
            return \Arquivo::avatar($file);
        }

        public static function upload()
        {
            require_once BASE_DIR . 'Models/Arquivo.php';
            if (\Seguranca::perfil() !== 'SECRETARIA') \Seguranca::verificarOfertaPost();
            $nome = strip_tags((string) ($_POST['nome'] ?? ''));
            $arquivo = \Arquivo::material($_FILES['arquivo'] ?? []);
            $query = \MySql::conectar()->prepare('INSERT INTO materiais (nome,data,cod_professo,cod_ano,cod_materia,cod_curso,cod_serie,arquivo) VALUES (?,?,?,?,?,?,?,?)');
            $query->execute([$nome, date('Y-m-d H:i:s'), $_SESSION['matricula'], $_POST['ano'], $_POST['materia'], $_POST['curso'], $_POST['serie'], $arquivo]);
            echo '<div class="box-sucesso">Arquivo cadastrado.</div>';
        }

        public static function imagemValida($imagem)
        {
            if (!is_array($imagem) || ($imagem['error'] ?? UPLOAD_ERR_NO_FILE) !== UPLOAD_ERR_OK || ($imagem['size'] ?? 0) > 5242880) return false;
            $info = @getimagesize($imagem['tmp_name']);
            return $info && in_array($info[2], [IMAGETYPE_JPEG, IMAGETYPE_PNG], true) && $info[0] <= 4096 && $info[1] <= 4096;
        }

        public static function cadastro_curso()
        {
                $nome = strip_tags($_POST['nome']);


                        $verifica = \MySql::conectar()->prepare("SELECT COUNT(*) FROM curso WHERE nome = ?");
                        $verifica->execute(array($nome));
        $verificaQuantidade = (int) $verifica->fetchColumn();

                        if($verificaQuantidade != 0 )
                        {

                             echo '<div class="erro-box"><i class="fa fa-times"></i> Curso existente,por favor tente  outro!</div>';

                        }

                        else
                        {
                            $sql = \MySql::conectar()->prepare("INSERT INTO curso (id, nome) VALUES (DEFAULT,?)");
                            $sql->execute(array($nome));
                            echo '<div class="box-sucesso"><i class="fa fa-check"></i> O cadastro do curso '.$nome.' foi feito com sucesso</div>';
                        }

        }

        public static function enviar_mensagem()
        {
            $mensagem =  strip_tags($_POST['mensagem']);
            $remetente = strip_tags($_POST['remetente']);
            $destino =   strip_tags($_POST['destino']);
            $visualizado = strip_tags($_POST['visualizado']);
            $horarioAtual = date('Y-m-d H:i:s');
            $sql = \MySql::conectar()->prepare("INSERT INTO chat (id, cod_remetente, cod_destino, mensagem, visualizado, data) VALUES (DEFAULT,?,?,?,?,?)");
            $sql->execute(array($remetente,$destino,$mensagem,$visualizado,$horarioAtual));
        }

        public static function cadastro_materia()
        {
                $nome = strip_tags($_POST['nome']);


                        $verifica = \MySql::conectar()->prepare("SELECT COUNT(*) FROM materia WHERE nome = ?");
                        $verifica->execute(array($nome));
        $verificaQuantidade = (int) $verifica->fetchColumn();

                        if($verificaQuantidade != 0 )
                        {

                             echo '<div class="erro-box"><i class="fa fa-times"></i> materia existente,por favor tente outra!</div>';

                        }

                        else
                        {
                            $sql = \MySql::conectar()->prepare("INSERT INTO materia (id, nome) VALUES (DEFAULT,?)");
                            $sql->execute(array($nome));
                            echo '<div class="box-sucesso"><i class="fa fa-check"></i> O cadastro da materia '.$nome.' foi feito com sucesso</div>';
                        }

        }

        public static function cadastro_serie()
        {
                $nome = strip_tags($_POST['nome']);


                        $verifica = \MySql::conectar()->prepare("SELECT COUNT(*) FROM serie WHERE nome = ?");
                        $verifica->execute(array($nome));
        $verificaQuantidade = (int) $verifica->fetchColumn();

                        if($verificaQuantidade != 0 )
                        {

                             echo '<div class="erro-box"><i class="fa fa-times"></i> Serir existente,por favor tente outra!</div>';

                        }

                        else
                        {
                            $sql = \MySql::conectar()->prepare("INSERT INTO serie (id, nome) VALUES (DEFAULT,?)");
                            $sql->execute(array($nome));
                            echo '<div class="box-sucesso"><i class="fa fa-check"></i> O cadastro da serie '.$nome.' foi feito com sucesso</div>';
                        }

        }

        public static function limita_tempo()
        {
                $data = strip_tags($_POST['data1']);
                $data1 = strip_tags($_POST['data2']);
                $data2 = strip_tags($_POST['data3']);
                $data3 = strip_tags($_POST['data4']);
                $dataAtual = date('Y-m-d');
                $verifica = MainMolde::selectAll('datas','',array());
                $nv = ceil(count($verifica));
                $aux = "";

                        if($nv == 0 )
                        {
                            $sql = \MySql::conectar()->prepare("INSERT INTO datas (datanota01, datanota02, datanota03, datanota04, id) VALUES (?,?,?,?,DEFAULT)");
                            $sql->execute(array($aux,$aux,$aux,$aux));
                        }

                        if($data > $dataAtual )
                        {

                                $sql = \MySql::conectar()->prepare("UPDATE datas SET datanota01 = ?");
                                $sql->execute(array($data));

                                $sql = \MySql::conectar()->prepare("UPDATE datas SET datanota02 = ? ");
                                $sql->execute(array($data1));

                                $sql = \MySql::conectar()->prepare("UPDATE datas SET datanota03 = ? ");
                                $sql->execute(array($data2));

                                $sql = \MySql::conectar()->prepare("UPDATE datas SET datanota04 = ? ");
                                $sql->execute(array($data3));

                                echo '<div class="box-sucesso"><i class="fa fa-check"></i> data inserida com sucesso !</div>';

                        }
                        else
                        {
                             echo '<div class="erro-box"><i class="fa fa-times"></i> data invalida!</div>';
                        }

        }


        public static function cadastro_diciplina()
        {
                $nome = strip_tags($_POST['nome']);
                $ano = strip_tags($_POST['ano']);
                $serie = strip_tags($_POST['serie']);
                $curso = strip_tags($_POST['curso']);
                @$professor = strip_tags($_POST['professor']);
                $carga_horaria = strip_tags($_POST['carga_horaria']);
                $aux = "";
                $alunos = MainMolde::selectAll('matriculados','cod_curso = ? AND cod_serie = ? AND cod_ano = ?',array($curso,$serie,$ano));
                $n = ceil(count($alunos));
                $x = "";
                $z = 0;
                $verifica = MainMolde::selectAll('notas','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND cod_professo = ?',array($curso,$serie,$ano,$nome,$professor));
                $nv = ceil(count($verifica));

                $verificaturmas = MainMolde::selectAll('turmas','curso = ? AND serie = ? AND ano = ? ',array($curso,$serie,$ano));
                $nvt = ceil(count($verificaturmas));

                        if($nvt == 0 )
                        {
                            $sql = \MySql::conectar()->prepare("INSERT INTO turmas (id, ano, curso, serie) VALUES (DEFAULT,?,?,?)");
                            $sql->execute(array($ano,$curso,$serie));
                        }

                        foreach ($alunos as $key => $value)
                        {
                            $aluno[] = $value['cod_aluno'];
                        }


                        $verifica = \MySql::conectar()->prepare("SELECT COUNT(*) FROM diciplina WHERE nome = ? AND cod_ano = ? AND cod_curso = ? AND cod_serie = ?");
                        $verifica->execute(array($nome,$ano,$curso,$serie));
        $verificaQuantidade = (int) $verifica->fetchColumn();



                        $verificagrade = MainMolde::selectAll('grade_curricular','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND nome = ?',array($curso,$serie,$ano,$nome));
                        $nvg = ceil(count($verificagrade));

                        if ($nvg == 0 )
                        {
                                $sql = \MySql::conectar()->prepare("INSERT INTO grade_curricular (id, nome, cod_serie, cod_ano, cod_curso, estado, ch) VALUES (DEFAULT,?,?,?,?,?,?)");
                                $sql->execute(array($nome,$serie,$ano,$curso,1,$carga_horaria));
                        }




                        if($verificaQuantidade != 0 )
                        {
                            echo '<div class="erro-box"><i class="fa fa-times"></i> diciplina existente,por favor tente com outro!</div>';
                        }

                        else
                        {
                            $sql = \MySql::conectar()->prepare("INSERT INTO diciplina (id, nome, cod_ano, cod_curso, cod_serie, cod_professo, carga_horaria) VALUES (DEFAULT,?,?,?,?,?,?)");
                            $sql->execute(array($nome,$ano,$curso,$serie,$professor,$carga_horaria));
                            echo '<div class="box-sucesso"><i class="fa fa-check"></i> O cadastro da diciplina '.$nome.' foi feito com sucesso</div>';

                        }

        }
        public static function cadastro_dp()
        {
                $nome = strip_tags($_POST['nome']." (dp)" );
                $aux = uniqid();
                $ano = "(dp) ".$_POST['ano'];
                $serie = "(dp ".strip_tags($_POST['serie']) .")";
                $curso = strip_tags($_POST['curso']);
                @$professor = strip_tags($_POST['professor']);
                $carga_horaria = strip_tags($_POST['carga_horaria']);
                $verifica = MainMolde::selectAll('notas','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND cod_professo = ?',array($curso,$serie,$ano,$nome,$professor));
                $slq =  MainMolde::selectAll('diciplina','',array());
                $diciplina = ceil(count($slq));

                        $verifica = \MySql::conectar()->prepare("SELECT COUNT(*) FROM diciplina WHERE nome = ? AND cod_ano = ? AND cod_curso = ? AND cod_serie = ?");
                        $verifica->execute(array($nome,$ano,$curso,$serie));
        $verificaQuantidade = (int) $verifica->fetchColumn();

                        if($verificaQuantidade != 0 )
                        {
                            echo '<div class="erro-box"><i class="fa fa-times"></i> diciplina existente,por favor tente com outro!</div>';
                        }
                        else if(@$_POST['alunos'])
                        {
                           foreach (@$_POST['alunos'] as $alunos)
                           {

                                $cod_aluno = \MySql::conectar()->prepare("SELECT * FROM alunos2 WHERE matricula = ? ");
                                $cod_aluno->execute(array($alunos));
                                $cod_aluno = $cod_aluno->fetch();
                                $nomee = $cod_aluno[1];
                                // $sql = \MySql::conectar()->prepare("INSERT INTO dependencia (id, cod_aluno, cod_diciplina) VALUES (DEFAULT,?,?)");
                                // $sql->execute(array($alunos,$diciplina + 1));
                                $sql = \MySql::conectar()->prepare("INSERT INTO matriculados (id, cod_curso, cod_serie, cod_ano, cod_aluno, nome, dependencia) VALUES (DEFAULT,?,?,?,?,?,?)");
                                $sql->execute(array($curso,$serie,$ano,$alunos,$nomee,1));


                           }

                                $sql = \MySql::conectar()->prepare("INSERT INTO diciplina (id, nome, cod_ano, cod_curso, cod_serie, cod_professo, carga_horaria) VALUES (DEFAULT,?,?,?,?,?,?)");
                                $sql->execute(array($nome,$ano,$curso,$serie,$professor,$carga_horaria));
                                echo '<div class="box-sucesso"><i class="fa fa-check"></i> O cadastro da diciplina '.$nome.' foi feito com sucesso</div>';
                        }
                        else
                            echo '<div class="erro-box"><i class="fa fa-times"></i> diciplina existente,por favor tente com outro!</div>';


        }

        public static function lanca_notas()
        {
               $aux = 1;
               $id = (int)$_GET['id'];
               $turma = MainMolde::select('diciplina','id = ?',array($id));
               $diciplina = $turma[1];
               $ano = $turma[2];
               $curso = $turma[3];
               $serie = $turma[4];
               $professor = $turma[5];

               if((isset($_POST['1bm'])))
               {

                         $sql = \MySql::conectar()->prepare("UPDATE notas SET nota01 = ?, atrasada_nota01 = 0 WHERE cod_professo = ? AND cod_serie = ? AND cod_curso = ? AND cod_ano = ? AND cod_diciplina = ?");
                         $sql->execute(array($aux,$professor,$serie,$curso,$ano,$diciplina));

               }
               else  if((isset($_POST['2bm'])))
               {

                         $sql = \MySql::conectar()->prepare("UPDATE notas SET nota02 = ?, atrasada_nota02 = 0 WHERE cod_professo = ? AND cod_serie = ? AND cod_curso = ? AND cod_ano = ? AND cod_diciplina = ? ");
                         $sql->execute(array($aux,$professor,$serie,$curso,$ano,$diciplina));
               }
               else  if((isset($_POST['3bm'])))
               {

                         $sql = \MySql::conectar()->prepare("UPDATE notas SET nota03 = ?, atrasada_nota03 = 0 WHERE cod_professo = ? AND cod_serie = ? AND cod_curso = ? AND cod_ano = ? AND cod_diciplina = ?");
                         $sql->execute(array($aux,$professor,$serie,$curso,$ano,$diciplina));

               }
               else  if((isset($_POST['4bm'])))
               {

                         $sql = \MySql::conectar()->prepare("UPDATE notas SET nota04 = ?, atasada_nota04 = 0 WHERE cod_professo = ? AND cod_serie = ? AND cod_curso = ? AND cod_ano = ? AND cod_diciplina = ?");
                         $sql->execute(array($aux,$professor,$serie,$curso,$ano,$diciplina));

               }

        }

        public static function libera_notas()
        {
               $aux = 0;
               $id = (int)$_GET['id'];
               $turma = MainMolde::select('diciplina','id = ?',array($id));
               $diciplina = $turma[1];
               $ano = $turma[2];
               $curso = $turma[3];
               $serie = $turma[4];
               $professor = $turma[5];
               $dataAtual = date('Y-m-d');
               $data = MainMolde::select('datas','',array());
               $alunos = MainMolde::selectAll('matriculados','cod_curso = ? AND cod_serie = ? AND cod_ano = ?',array($curso,$serie,$ano));
               $n = ceil(count($alunos));
               $x = "";
               $z = 0;
               $verifica = MainMolde::selectAll('notas','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND cod_professo = ?',array($curso,$serie,$ano,$diciplina,$professor));
               $nv = ceil(count($verifica));

                    foreach ($alunos as $key => $value)
                    {
                        $aluno[] = $value['cod_aluno'];
                    }

                    if($nv == 0 )
                    {
                            for ($y = 0; $y < $n; $y++)
                            {
                                    $sql = \MySql::conectar()->prepare("INSERT INTO notas (cod_aluno, cod_curso, cod_serie, cod_ano, cod_diciplina, cod_professo, n1, r1, n2, r2, n3, r3, n4, r4, media, aprovado, nota01, nota02, nota03, nota04, media_parcial, atrasada_nota01, atrasada_nota02, atrasada_nota03, atasada_nota04) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                                    $sql->execute(array($aluno[$y],$curso,$serie,$ano,$diciplina,$professor,$x,$x,$x,$x,$x,$x,$x,$x,$x,$x,$z,$z,$z,$z,$z,$z,$z,$z,$z));
                             }

                    }


               if((isset($_POST['libera-1bm'])))
               {
                         if ($dataAtual > $data[0])
                         {
                             $sql = \MySql::conectar()->prepare("UPDATE notas SET atrasada_nota01 = 1 WHERE cod_professo = ? AND cod_serie = ? AND cod_curso = ? AND cod_ano = ? AND cod_diciplina = ? ");
                             $sql->execute(array($professor,$serie,$curso,$ano,$diciplina));
                         }
                         $sql = \MySql::conectar()->prepare("UPDATE notas SET nota01 = ? WHERE cod_professo = ? AND cod_serie = ? AND cod_curso = ? AND cod_ano = ? AND cod_diciplina = ?");
                         $sql->execute(array($aux,$professor,$serie,$curso,$ano,$diciplina));

               }
               else  if((isset($_POST['libera-2bm'])))
               {
                         if ($dataAtual > $data[1])
                         {
                             $sql = \MySql::conectar()->prepare("UPDATE notas SET atrasada_nota02 = 1 WHERE cod_professo = ? AND cod_serie = ? AND cod_curso = ? AND cod_ano = ? AND cod_diciplina = ? ");
                             $sql->execute(array($professor,$serie,$curso,$ano,$diciplina));
                         }
                         $sql = \MySql::conectar()->prepare("UPDATE notas SET nota02 = ? WHERE cod_professo = ? AND cod_serie = ? AND cod_curso = ? AND cod_ano = ? AND cod_diciplina = ? ");
                         $sql->execute(array($aux,$professor,$serie,$curso,$ano,$diciplina));
               }
               else  if((isset($_POST['libera-3bm'])))
               {
                         if ($dataAtual > $data[2])
                         {
                             $sql = \MySql::conectar()->prepare("UPDATE notas SET atrasada_nota03 = 1 WHERE cod_professo = ? AND cod_serie = ? AND cod_curso = ? AND cod_ano = ? AND cod_diciplina = ? ");
                             $sql->execute(array($professor,$serie,$curso,$ano,$diciplina));
                         }
                         $sql = \MySql::conectar()->prepare("UPDATE notas SET nota03 = ? WHERE cod_professo = ? AND cod_serie = ? AND cod_curso = ? AND cod_ano = ? AND cod_diciplina = ?");
                         $sql->execute(array($aux,$professor,$serie,$curso,$ano,$diciplina));

               }
               else  if((isset($_POST['libera-4bm'])))
               {
                         if ($dataAtual > $data[3])
                         {
                              $sql = \MySql::conectar()->prepare("UPDATE notas SET atasada_nota04 = 1 WHERE cod_professo = ? AND cod_serie = ? AND cod_curso = ? AND cod_ano = ? AND cod_diciplina = ? ");
                              $sql->execute(array($professor,$serie,$curso,$ano,$diciplina));
                         }
                         $sql = \MySql::conectar()->prepare("UPDATE notas SET nota04 = ? WHERE cod_professo = ? AND cod_serie = ? AND cod_curso = ? AND cod_ano = ? AND cod_diciplina = ?");
                         $sql->execute(array($aux,$professor,$serie,$curso,$ano,$diciplina));

               }

        }

        public static function cadastro_turmas()
        {
                $ano = strip_tags($_POST['ano']);
                $serie = strip_tags($_POST['serie']);
                $curso = strip_tags($_POST['curso']);
                $padrao = $_POST['grade'];

                $alunoInvalido = false;

                if(@$padrao != "nenhun")
                {
                    $materias =  MainMolde::selectAll('grade_curricular_padroes','nome = ?',array($padrao));
                    foreach($materias as $key => $value)
                    {
                        $nome = MainMolde::select('materia','id = ?',array($value[1]));
                        $sql = \MySql::conectar()->prepare("INSERT INTO diciplina (id, nome, cod_ano, cod_curso, cod_serie, cod_professo, carga_horaria) VALUES (DEFAULT,?,?,?,?,?,?)");
                        $sql->execute(array($nome[1],$ano,$curso,$serie,606060,$value[2]));
                    }
                }

                @$materias =  MainMolde::selectAll('diciplina','cod_curso = ? AND cod_serie = ? AND cod_ano = ?',array($curso,$serie,$ano));
                $cont_materias = ceil(count($materias));

                $verificanotas = MainMolde::selectAll('notas','cod_curso = ? AND cod_serie = ? AND cod_ano = ? ',array($curso,$serie,$ano));
                $nvn = ceil(count($verificanotas));

                $verificaturmas = MainMolde::selectAll('turmas','curso = ? AND serie = ? AND ano = ? ',array($curso,$serie,$ano));
                $nvt = ceil(count($verificaturmas));





                if($cont_materias != 0)
                {
                     foreach (@$materias as $materia)
                     {

                            $verificagrade = MainMolde::selectAll('grade_curricular','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND nome = ?',array($curso,$serie,$ano,$materia[1]));
                            $nvg = ceil(count($verificagrade));

                             if ($nvg == 0 )
                             {
                                  $sql = \MySql::conectar()->prepare("INSERT INTO grade_curricular (id, nome, cod_serie, cod_ano, cod_curso, estado, ch) VALUES (DEFAULT,?,?,?,?,?,?)");
                                  $sql->execute(array($materia[1],$serie,$ano,$curso,1,$materia[6]));
                             }


                     }
                }




                if(@$_POST['alunos'])
                {
                   foreach (@$_POST['alunos'] as $alunos)
                   {

                            $verifica = MainMolde::selectAll('matriculados','cod_aluno = ? AND cod_ano = ? AND cod_serie = ? ',array($alunos,$ano,$serie));
                            $nv = ceil(count($verifica));

                            if ($nv == 0 && $nvn == 0)
                            {
                                    $cod_aluno = \MySql::conectar()->prepare("SELECT * FROM alunos2 WHERE matricula = ? ");
                                    $cod_aluno->execute(array($alunos));
                                    $cod_aluno = $cod_aluno->fetch();
                                    $nome = $cod_aluno[1];
                                    $sql = \MySql::conectar()->prepare("INSERT INTO matriculados (id, cod_curso, cod_serie, cod_ano, cod_aluno, nome, dependencia) VALUES (DEFAULT,?,?,?,?,?,?)");
                                    $sql->execute(array($curso,$serie,$ano,$alunos,$nome,0));
                                    $i = 1;
                                    $sql2 = \MySql::conectar()->prepare("UPDATE alunos2 SET matriculado = ?  WHERE matricula = ?");
                                    $sql2->execute([$i, $alunos]);
                                    if($nvt == 0 && @$_POST['alunos'] != "")
                                    {
                                        $sql = \MySql::conectar()->prepare("INSERT INTO turmas (id, ano, curso, serie) VALUES (DEFAULT,?,?,?)");
                                        $sql->execute(array($ano,$curso,$serie));
                                    }


                            }
                            else
                            {
                                   $alunoInvalido = true;
                                   break;
                            }
                    }

                    if ($alunoInvalido == true)
                    {
                       echo '<div class="erro-box"><i class="fa fa-check"></i> Aluno ja cadastrado!</div>';
                    }
                    else
                    {
                       echo '<div class="box-sucesso"><i class="fa fa-check"></i> O cadastro  foi feito com sucesso</div>';
                    }

                }
                else
                   echo '<div class="erro-box"><i class="fa fa-check"></i> Nenhum aluno foi selecionado!</div>';




        }

        public static function selectAll($table,$query = '',$arr = [])
        {
            \Seguranca::validarTabela($table);
            if($query != false)
            {
                $sql = \MySql::conectar()->prepare("SELECT * FROM $table WHERE $query ");
                $sql->execute($arr);
            }
            else
            {
                $sql = \MySql::conectar()->prepare("SELECT * FROM $table");
                $sql->execute($arr);
            }

            return $sql->fetchAll();

        }


        public static function selectAll_ASC($table,$query = '',$order,$arr = [])
        {
            \Seguranca::validarTabela($table);
            if (!preg_match('/^[a-z_0-9]+$/D', $order)) \Seguranca::negar(400);
            if($query != false)
            {
                $sql = \MySql::conectar()->prepare("SELECT * FROM $table WHERE $query ORDER BY $order ASC");
                $sql->execute($arr);
            }
            else
            {
                $sql = \MySql::conectar()->prepare("SELECT * FROM $table");
                $sql->execute($arr);
            }

            return $sql->fetchAll();

        }


        public static function select($table,$query = '',$arr = [])
        {
            \Seguranca::validarTabela($table);
            if($query != false)
            {
                $sql = \MySql::conectar()->prepare("SELECT * FROM $table WHERE $query");
                $sql->execute($arr);
            }
            else
            {
                $sql = \MySql::conectar()->prepare("SELECT * FROM $table");
                $sql->execute();
            }
            return $sql->fetch();
        }

        public static function criagrade()
        {
            $nome = $_POST['nome'];
            $verifica = MainMolde::selectAll('grade_curricular_padroes','grade = ?',array($nome));
            $nv = ceil(count($verifica));
            $cadastrovalido = false;
            $i = 0;

            if($nv == 0)
            {
                if(@$_POST['materias'])
                {

                    foreach (@$_POST['materias'] as $materia)
                    {
                        @$ch = $_POST['ch'.$materia];
                        @$nome_diciplina = $_POST['id'.$materia];
                        if(@$ch == "")
                        {
                            echo '<div class="erro-box"><i class="fa fa-check"></i> A carga horaria nao foi selecionada para todas materias!</div>';
                            break;
                        }
                        $sql = \MySql::conectar()->prepare("INSERT INTO grade_curricular_padroes (id, materia, ch, grade, nome) VALUES (DEFAULT,?,?,?,?)");
                        $sql->execute(array($materia,$ch,$nome,$nome_diciplina));
                        $i++;
                        $cadastrovalido = true;
                    }
                    $sql = \MySql::conectar()->prepare("INSERT INTO grades_curricules (id, nome) VALUES (DEFAULT,?)");
                    $sql->execute(array($nome));

                    if($cadastrovalido == true)
                        echo '<div class="box-sucesso"><i class="fa fa-check"></i> O cadastro  foi feito com sucesso</div>';
                }
                else
                    echo '<div class="erro-box"><i class="fa fa-check"></i> Nenhuma materia foi selecionado!</div>';
            }
            else
                echo '<div class="erro-box"><i class="fa fa-check"></i> Grade curricular existente!</div>';

        }

        public static function atualizargrade()
        {
            $nome = $_POST['nome'];
            $nomenovo = $_POST['novonome'];
            $cadastrovalido = false;
            $i = 0;
            if($nome != $nomenovo)
            {
                $verifica = MainMolde::selectAll('grade_curricular_padroes','grade = ?',array($nomenovo));
                $nv = ceil(count($verifica));
            }
            else
            {
                $nv = 0;
            }

            if($nv == 0)
            {
                if(@$_POST['materias'])
                {
                    $sql = \MySql::conectar()->prepare("DELETE FROM grade_curricular_padroes WHERE  grade = ?");
                    $sql->execute(array($nome));


                    foreach (@$_POST['materias'] as $materia)
                    {
                        @$ch = $_POST['ch'.$materia];
                        @$nome_diciplina = $_POST['id'.$materia];
                        if(@$ch == "")
                        {
                            echo '<div class="erro-box"><i class="fa fa-check"></i> A carga horaria nao foi selecionada para todas materias!</div>';
                            break;
                        }
                        $sql = \MySql::conectar()->prepare("INSERT INTO grade_curricular_padroes (id, materia, ch, grade, nome) VALUES (DEFAULT,?,?,?,?)");
                        $sql->execute(array($materia,$ch,$nomenovo,$nome_diciplina));
                        $i++;
                        $cadastrovalido = true;

                    }
                    $sql = \MySql::conectar()->prepare("UPDATE grades_curricules SET nome = ? WHERE nome = ?");
                    $sql->execute(array($nomenovo,$nome));

                    if($cadastrovalido == true)
                        echo '<div class="box-sucesso"><i class="fa fa-check"></i> O cadastro  foi feito com sucesso</div>';
                }
                else
                    echo '<div class="erro-box"><i class="fa fa-check"></i> Nenhuma materia foi selecionado!</div>';
            }
            else
                echo '<div class="erro-box"><i class="fa fa-check"></i> Grade curricular existente!</div>';
        }
        public static function deletargrade()
        {
            $nome = $_POST['nome'];
            $sql = \MySql::conectar()->prepare("DELETE FROM grade_curricular_padroes WHERE  grade = ?");
            $sql->execute(array($nome));
            $sql = \MySql::conectar()->prepare("DELETE FROM grades_curricules WHERE  nome = ?");
            $sql->execute(array($nome));
            echo '<div class="box-sucesso"><i class="fa fa-check"></i> A Grade foi deletada!</div>';
            \Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
        }


}
?>
