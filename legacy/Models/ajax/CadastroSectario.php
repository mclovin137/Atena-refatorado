<?php
require_once __DIR__ . '/../../config.php';
use \Models\MainMolde;

            $data['sucesso'] = true;
            $data['mensagem'] = "";
            if(isset($_POST['Cadastro-professor']))
            {

                $nome = strip_tags($_POST['nome']);
                $senha = \Seguranca::senhaNova($_POST['senha']);
                $cpf = strip_tags($_POST['cpf']);
                $cep = strip_tags($_POST['cep']);
                @$imagem = $_FILES['imagem'];
                $data_de_nacimento= strip_tags($_POST['data_de_nacimento']);
                $estado_civil = strip_tags($_POST['estado_civil']);
                $telefone = strip_tags($_POST['telefone']);
                $sexo = strip_tags($_POST['sexo']);
                $email = strip_tags($_POST['email']);






                        $verificaemail = \MySql::conectar()->prepare("SELECT COUNT(*) FROM professo2 WHERE email = ?");
                        $verificaemail->execute(array($email));
        $verificaemailQuantidade = (int) $verificaemail->fetchColumn();
                        $verificaemail_aluno = \MySql::conectar()->prepare("SELECT COUNT(*) FROM alunos2 WHERE email = ?");
                        $verificaemail_aluno->execute(array($email));
        $verificaemail_alunoQuantidade = (int) $verificaemail_aluno->fetchColumn();
                        $verificaemail_sec = \MySql::conectar()->prepare("SELECT COUNT(*) FROM secretaria2 WHERE email = ?");
                        $verificaemail_sec->execute(array($email));
        $verificaemail_secQuantidade = (int) $verificaemail_sec->fetchColumn();
                        $verificacpf = \MySql::conectar()->prepare("SELECT COUNT(*) FROM alunos2 WHERE cpf = ?");
                        $verificacpf->execute(array($cpf));
        $verificacpfQuantidade = (int) $verificacpf->fetchColumn();
                        $verificacpf_prof = \MySql::conectar()->prepare("SELECT COUNT(*) FROM professo2 WHERE cpf = ?");
                        $verificacpf_prof->execute(array($cpf));
        $verificacpf_profQuantidade = (int) $verificacpf_prof->fetchColumn();
                        $verificacpf_sec = \MySql::conectar()->prepare("SELECT COUNT(*) FROM secretaria2 WHERE cpf = ?");
                        $verificacpf_sec->execute(array($cpf));
        $verificacpf_secQuantidade = (int) $verificacpf_sec->fetchColumn();
                        $verificate = \MySql::conectar()->prepare("SELECT COUNT(*) FROM secretaria2 WHERE telefone = ?");
                        $verificate->execute(array($telefone));
        $verificateQuantidade = (int) $verificate->fetchColumn();

                        $alunos =  MainMolde::selectAll('alunos2','',array());
                        $cont_alunos = ceil(count($alunos));

                        $prof =  MainMolde::selectAll('professo2','',array());
                        $cont_prof = ceil(count($prof));

                        $sec =  MainMolde::selectAll('secretaria2','',array());
                        $cont_sec = ceil(count($sec));

                        $usuarios = ($cont_alunos +$cont_prof  + $cont_sec);

                        if ($usuarios < 10)
                        {
                            $cont = "00000";
                        }
                        else if($usuarios < 100)
                        {
                            $cont = "0000";
                        }
                        else if($usuarios < 1000)
                        {
                            $cont = "000";
                        }

                        $matricula = $cont.$usuarios;

                        if ($verificaemailQuantidade != 0)
                        {
                            $data['sucesso'] = false;
                            $data['mensagem'] = "email existente,por favor tente com outro!";
                        }

                        else if($verificaemail_alunoQuantidade != 0)
                        {
                            $data['sucesso'] = false;
                            $data['mensagem'] = "email existente,por favor tente com outro!";
                        }

                        else if($verificaemail_secQuantidade != 0)
                        {
                            $data['sucesso'] = false;
                            $data['mensagem'] = "email existente,por favor tente com outro!";
                        }


                        else if($verificacpfQuantidade != 0)
                        {
                            $data['sucesso'] = false;
                            $data['mensagem'] = "cpf existente,por favor tente com outro!";
                        }
                        else if($verificacpf_profQuantidade != 0)
                        {
                            $data['sucesso'] = false;
                            $data['mensagem'] = "cpf existente,por favor tente com outro!";
                        }
                        else if($verificacpf_secQuantidade != 0)
                        {
                            $data['sucesso'] = false;
                            $data['mensagem'] = "cpf existente,por favor tente com outro!";
                        }

                        else if (@$imagem != "")
                        {
                           if(MainMolde::imagemValida($imagem) == false)
                           {
                                $data['sucesso'] = false;
                                $data['mensagem'] = "imagens invalida,por favor tente com outro tipo de arquivo!";
                                $upload = false;
                           }
                           else
                           {
                                $upload = false;
                           }
                        }

                        else if($verificateQuantidade != 0)
                        {
                            $data['sucesso'] = false;
                            $data['mensagem'] = "Nimero de Telefone invalido,por favor tente com outro ";
                        }

                        if($data['sucesso'])
                        {

                            if (@$imagem == "")
                            {
                                 $imagem = "user.jpg";
                                 $upload = true;
                            }

                            if ($upload == false )
                                 $imagem = MainMolde::uploadFile($imagem);

                            $sql = \MySql::conectar()->prepare("INSERT INTO secretaria2 (id, nome, senha, matricula, cpf, cep, imagem, data_de_nacimento, estado_civil, telefone, sexo, email, estado, diretor) VALUES (DEFAULT,?,?,?,?,?,?,?,?,?,?,?,1,0)");
                            $sql->execute(array($nome,$senha,$matricula,$cpf,$cep,$imagem,$data_de_nacimento,$estado_civil,$telefone,$sexo,$email));
                            $data['mensagem'] = " O cadastro do sectario " .$nome. "  foi feito com sucesso";
                        }

            }
            else if(isset($_POST['Editar-professo']))
            {

                    $nome = strip_tags($_POST['nome']);
                    $nomeatual = strip_tags($_POST['nomeatual']);
                    $matriculaatual = strip_tags($_POST['matriculaatual']);
                    $senha = \Seguranca::senhaExistente('secretaria2', $matriculaatual);
                    @$novasenha = strip_tags($_POST['novasenha']);
                    $cpf = strip_tags($_POST['cpf']);
                    $cpfatual = strip_tags($_POST['cpfatual']);
                    $cep = strip_tags($_POST['cep']);
                    @$imagem = $_FILES['imagem'];
                    $data_de_nacimento= strip_tags($_POST['data_de_nacimento']);
                    $estado_civil = strip_tags($_POST['estado_civil']);
                    $telefone = strip_tags($_POST['telefone']);
                    $telefoneatual = strip_tags($_POST['telefoneatual']);
                    $sexo = strip_tags($_POST['sexo']);
                    $email = strip_tags($_POST['email']);
                    $emailatual= strip_tags($_POST['emailatual']);
                    $estado = strip_tags($_POST['estado']);

                       if($novasenha != "")
                       {
                            $senha = \Seguranca::senhaNova($_POST['novasenha']);

                       }


                       if ($cpf != $cpfatual)
                       {
                          $verificacpf = \MySql::conectar()->prepare("SELECT COUNT(*) FROM alunos2 WHERE cpf = ?");
                          $verificacpf->execute(array($cpf));
        $verificacpfQuantidade = (int) $verificacpf->fetchColumn();
                          $verificacpf_prof = \MySql::conectar()->prepare("SELECT COUNT(*) FROM professo2 WHERE cpf = ?");
                          $verificacpf_prof->execute(array($cpf));
        $verificacpf_profQuantidade = (int) $verificacpf_prof->fetchColumn();
                          $verificacpf_sec = \MySql::conectar()->prepare("SELECT COUNT(*) FROM secretaria2 WHERE cpf = ?");
                          $verificacpf_sec->execute(array($cpf));
        $verificacpf_secQuantidade = (int) $verificacpf_sec->fetchColumn();

                                if($verificacpfQuantidade != 0)
                                {
                                    $data['sucesso'] = false;
                                    $data['mensagem'] =  "cpf existente,por favor tente com outro!";
                                }
                                else if($verificacpf_profQuantidade != 0)
                                {
                                    $data['sucesso'] = false;
                                    $data['mensagem'] = "cpf existente,por favor tente com outro!";
                                }
                                else if($verificacpf_secQuantidade != 0)
                                {
                                    $data['sucesso'] = false;
                                    $data['mensagem'] = "cpf existente,por favor tente com outro!";
                                }
                       }
                       else if($telefone != $telefoneatual)
                       {
                          $verificate = \MySql::conectar()->prepare("SELECT COUNT(*) FROM secretaria2 WHERE telefone = ?");
                          $verificate->execute(array($telefone));
        $verificateQuantidade = (int) $verificate->fetchColumn();

                                if($verificateQuantidade != 0)
                                {
                                    $data['sucesso'] = false;
                                    $data['mensagem'] = "Nimero de Telefone invalido,por favor tente com outro ";
                                }
                       }

                       else if($email != $emailatual)
                       {
                          $verificaemail = \MySql::conectar()->prepare("SELECT COUNT(*) FROM professo2 WHERE email = ?");
                          $verificaemail->execute(array($email));
        $verificaemailQuantidade = (int) $verificaemail->fetchColumn();
                          $verificaemail_aluno = \MySql::conectar()->prepare("SELECT COUNT(*) FROM alunos2 WHERE email = ?");
                          $verificaemail_aluno->execute(array($email));
        $verificaemail_alunoQuantidade = (int) $verificaemail_aluno->fetchColumn();
                          $verificaemail_sec = \MySql::conectar()->prepare("SELECT COUNT(*) FROM secretaria2 WHERE email = ?");
                          $verificaemail_sec->execute(array($email));
        $verificaemail_secQuantidade = (int) $verificaemail_sec->fetchColumn();

                                if($verificaemailQuantidade != 0)
                                {
                                    $data['sucesso'] = false;
                                    $data['mensagem'] = "E-mail existente , por favor tente com outro";
                                }
                                else if($verificaemail_alunoQuantidade != 0)
                                {
                                    $data['sucesso'] = false;
                                    $data['mensagem'] = "email existente,por favor tente com outro!";
                                }
                                else if($verificaemail_secQuantidade != 0)
                                {
                                    $data['sucesso'] = false;
                                    $data['mensagem'] = "email existente,por favor tente com outro!";
                                }

                       }



                       else if ($imagem != "")
                       {
                           if(MainMolde::imagemValida($imagem) == false)
                           {
                                $data['sucesso'] = false;
                                $data['mensagem'] = "imagens invalida,por favor tente com outro tipo de arquivo!";
                           }
                       }

                        if($data['sucesso'])
                        {

                           if ($imagem == "")
                                 $imagem = $_POST['imagematual'];
                           else
                                 $imagem = MainMolde::uploadFile($imagem);

                            if($email != $emailatual)
                            {
                                    $sql = \MySql::conectar()->prepare("UPDATE chat SET cod_remetente = ? WHERE cod_remetente = ? ");
                                    $sql->execute(array($email,$emailatual));
                                    $sql = \MySql::conectar()->prepare("UPDATE chat SET cod_destino = ? WHERE  cod_destino = ? ");
                                    $sql->execute(array($email,$emailatual));
                                     $_SESSION['email'] = $email;
                            }

                            $sql = \MySql::conectar()->prepare("UPDATE secretaria2 SET nome = ?, senha = ?,cpf = ?, cep = ?, imagem = ?, data_de_nacimento = ?, estado_civil = ?,
                            telefone = ?, sexo = ?, email = ? , estado = ? WHERE matricula = ?");
                            $sql->execute([$nome, $senha, $cpf, $cep, $imagem, $data_de_nacimento, $estado_civil, $telefone, $sexo, $email, $estado, $matriculaatual]);


                            $data['mensagem'] = " O Funcionario " .$nome. "  foi atualizado com sucesso";


                        }

           }

    die(json_encode($data));
?>
