<?php
include('../../config.php');
use \Models\MainMolde;

            $data['sucesso'] = true;
	        $data['mensagem'] = "";
            if(isset($_POST['atualiza-adm']))
			{

				    $nome = strip_tags($_POST['nome']);
				    $nomeatual = strip_tags($_POST['nomeatual']);
					$matriculaatual = strip_tags($_POST['matriculaatual']);
					$senha = strip_tags($_POST['senhaatual']);
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


					   if($novasenha != "")
					   {
                            $senha = md5($_POST['novasenha']);

					   }

                       if ($cpf != $cpfatual) 
					   {				   	
						   $verificacpf = \MySql::conectar()->prepare("SELECT cpf FROM `alunos2` WHERE cpf = ?");
						   $verificacpf->execute(array($cpf));
						   $verificacpf_prof = \MySql::conectar()->prepare("SELECT cpf FROM `professo2` WHERE cpf = ?");
						   $verificacpf_prof->execute(array($cpf));
					       $verificacpf_sec = \MySql::conectar()->prepare("SELECT cpf FROM `secretaria2` WHERE cpf = ?");
						   $verificacpf_sec->execute(array($cpf));

						        if($verificacpf->rowCount() != 0) 
								{
									$data['sucesso'] = false;
								    $data['mensagem'] =  "cpf existente,por favor tente com outro!";
								}
								else if($verificacpf_prof->rowCount() != 0) 
								{
									$data['sucesso'] = false;
								    $data['mensagem'] =  "cpf existente,por favor tente com outro!";
								}

								else if($verificacpf_sec->rowCount() != 0) 
								{
									$data['sucesso'] = false;
								    $data['mensagem'] =  "cpf existente,por favor tente com outro!";
								}
					   }
						
					   else if($telefone != $telefoneatual)
					   {
					   	  $verificate = \MySql::conectar()->prepare("SELECT telefone FROM `secretaria2` WHERE telefone = ?");
						  $verificate->execute(array($telefone));

						        if($verificate->rowCount() != 0)
						        {
						        	$data['sucesso'] = false;
							        $data['mensagem'] = "Nimero de Telefone invalido,por favor tente com outro ";
						        }					    
					   }	

					   else if($email != $emailatual)
					   {
                         $verificaemail = \MySql::conectar()->prepare("SELECT email FROM `alunos2` WHERE email = ?");
						 $verificaemail->execute(array($email));
						 $verificaemail_prof = \MySql::conectar()->prepare("SELECT email FROM `professo2` WHERE email = ?");
						 $verificaemail_prof->execute(array($email));
						 $verificaemail_sec = \MySql::conectar()->prepare("SELECT email FROM `secretaria2` WHERE email = ?");
						 $verificaemail_sec->execute(array($email));

							    if($verificaemail->rowCount() != 0) 
								{   
									$data['sucesso'] = false;
									$data['mensagem'] = "E-mail existente , por favor tente com outro";
								}
								else if($verificaemail_prof->rowCount() != 0) 
								{   
									$data['sucesso'] = false;
									$data['mensagem'] = "email existente,por favor tente com outro!";
								}

								else if($verificaemail_sec->rowCount() != 0) 
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
								    $sql = \MySql::conectar()->prepare("UPDATE `chat` SET cod_remetente = ? WHERE cod_remetente = ? ");
								    $sql->execute(array($email,$emailatual));
								    $sql = \MySql::conectar()->prepare("UPDATE `chat` SET cod_destino = ? WHERE  cod_destino = ? ");
								    $sql->execute(array($email,$emailatual));
								     $_SESSION['email'] = $email;	
                            }
   					         
						    $sql = \MySql::conectar()->prepare("UPDATE `secretaria2` SET nome = ?, senha = ?, cpf = ?, cep = ?, imagem = ?, data_de_nacimento = ?, estado_civil = ?,
						    telefone = ?, sexo = ?, email = ?  WHERE matricula = $matriculaatual");
							$sql->execute(array($nome,$senha,$cpf,$cep,$imagem,$data_de_nacimento,$estado_civil,$telefone,$sexo,$email));
                            
                           
	                        $_SESSION['imagem'] = $imagem;	
	                        $_SESSION['nome'] = $nome;             
							$data['mensagem'] = " Seus Dados foram atualizados com sucesso";


					    }
		 
		   }
		   else if(isset($_POST['atualiza-prof']))
		   {

				    $nome = strip_tags($_POST['nome']);
				    $nomeatual = strip_tags($_POST['nomeatual']);
					$matriculaatual = strip_tags($_POST['matriculaatual']);
					@$novasenha = strip_tags($_POST['novasenha']);
					$senha = strip_tags($_POST['senhaatual']);
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

					   if($novasenha != "")
					   {
                            $senha = md5($_POST['novasenha']);

					   }

					  
					   if ($cpf != $cpfatual) 
					   {				   	
						   $verificacpf = \MySql::conectar()->prepare("SELECT cpf FROM `alunos2` WHERE cpf = ?");
						   $verificacpf->execute(array($cpf));
						   $verificacpf_prof = \MySql::conectar()->prepare("SELECT cpf FROM `professo2` WHERE cpf = ?");
						   $verificacpf_prof->execute(array($cpf));
					       $verificacpf_sec = \MySql::conectar()->prepare("SELECT cpf FROM `secretaria2` WHERE cpf = ?");
						   $verificacpf_sec->execute(array($cpf));

						        if($verificacpf->rowCount() != 0) 
								{
									$data['sucesso'] = false;
								    $data['mensagem'] =  "cpf existente,por favor tente com outro!";
								}
								else if($verificacpf_prof->rowCount() != 0) 
								{
									$data['sucesso'] = false;
								    $data['mensagem'] =  "cpf existente,por favor tente com outro!";
								}

								else if($verificacpf_sec->rowCount() != 0) 
								{
									$data['sucesso'] = false;
								    $data['mensagem'] =  "cpf existente,por favor tente com outro!";
								}
					   }

					   else if($telefone != $telefoneatual)
					   {
					   	  $verificate = \MySql::conectar()->prepare("SELECT telefone FROM `professo2` WHERE telefone = ?");
						  $verificate->execute(array($telefone));

						        if($verificate->rowCount() != 0)
						        {
						        	$data['sucesso'] = false;
							        $data['mensagem'] = "Nimero de Telefone invalido,por favor tente com outro ";
						        }					    
					   }	

					   else if($email != $emailatual)
					   {
                          $verificaemail = \MySql::conectar()->prepare("SELECT email FROM `alunos2` WHERE email = ?");
						  $verificaemail->execute(array($email));
						  $verificaemail_prof = \MySql::conectar()->prepare("SELECT email FROM `professo2` WHERE email = ?");
						  $verificaemail_prof->execute(array($email));
						  $verificaemail_sec = \MySql::conectar()->prepare("SELECT email FROM `secretaria2` WHERE email = ?");
						  $verificaemail_sec->execute(array($email));

							    if($verificaemail->rowCount() != 0) 
								{   
									$data['sucesso'] = false;
									$data['mensagem'] = "E-mail existente , por favor tente com outro";
								}
								else if($verificaemail_prof->rowCount() != 0) 
								{   
									$data['sucesso'] = false;
									$data['mensagem'] = "email existente,por favor tente com outro!";
								}

								else if($verificaemail_sec->rowCount() != 0) 
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
					    	if($email != $emailatual)
							{
								    $sql = \MySql::conectar()->prepare("UPDATE `chat` SET cod_remetente = ? WHERE cod_remetente = ? ");
								    $sql->execute(array($email,$emailatual));
								    $sql = \MySql::conectar()->prepare("UPDATE `chat` SET cod_destino = ? WHERE  cod_destino = ? ");
								    $sql->execute(array($email,$emailatual));
								     $_SESSION['email'] = $email;	
                            }

						   if ($imagem == "")						   				
						         $imagem = $_POST['imagematual'];
						   else                     
   					             $imagem = MainMolde::uploadFile($imagem);
   					         
						    $sql = \MySql::conectar()->prepare("UPDATE `professo2` SET nome = ?, senha = ?,cpf = ?, cep = ?, imagem = ?, data_de_nacimento = ?, estado_civil = ?,
						    telefone = ?, sexo = ?, email = ?  WHERE matricula = $matriculaatual");
							$sql->execute(array($nome,$senha,$cpf,$cep,$imagem,$data_de_nacimento,$estado_civil,$telefone,$sexo,$email));

							
                            
	                        $_SESSION['imagem'] = $imagem;	
	                        $_SESSION['nome'] = $nome;	                       
						    $data['mensagem'] = " Seus Dados foram atualizados com sucesso";


					    }
		 
		   }
		   else if(isset($_POST['atualiza']))
		   {

				    $nome = strip_tags($_POST['nome']);
					$matriculaatual = strip_tags($_POST['matriculaatual']);
					@$novasenha = strip_tags($_POST['novasenha']);
					$senha = strip_tags($_POST['senhaatual']);
					$cpf = strip_tags($_POST['cpf']);
					$cep = strip_tags($_POST['cep']);
					@$imagem = $_FILES['imagem'];
					$data_de_nacimento= strip_tags($_POST['data_de_nacimento']);
					$pai = strip_tags($_POST['pai']);
					$mae = strip_tags($_POST['mae']);
					$estado_civil = strip_tags($_POST['estado_civil']);
					$telefone = strip_tags($_POST['telefone']);
					$telefoneatual = strip_tags($_POST['telefoneatual']);
					$sexo = strip_tags($_POST['sexo']);
					$email = strip_tags($_POST['email']);
					$emailatual= strip_tags($_POST['emailatual']);


						   if($novasenha != "")
						   {
	                            $senha = md5($_POST['novasenha']);

						   }
						
						

						  


						   else if($telefone != $telefoneatual)
						   {
						   	  $verificate = \MySql::conectar()->prepare("SELECT telefone FROM `alunos2` WHERE telefone = ?");
							  $verificate->execute(array($telefone));

							        if($verificate->rowCount() != 0)
							        {
							        	$data['sucesso'] = false;
								        $data['mensagem'] = "Nimero de Telefone invalido,por favor tente com outro ";
							        }					    
						   }	

						   else if($email != $emailatual)
						   {
	                           $verificaemail = \MySql::conectar()->prepare("SELECT email FROM `alunos2` WHERE email = ?");
						       $verificaemail->execute(array($email));
						       $verificaemail_prof = \MySql::conectar()->prepare("SELECT email FROM `professo2` WHERE email = ?");
						       $verificaemail_prof->execute(array($email));
						       $verificaemail_sec = \MySql::conectar()->prepare("SELECT email FROM `secretaria2` WHERE email = ?");
						       $verificaemail_sec->execute(array($email));

								    if($verificaemail->rowCount() != 0) 
									{   
										$data['sucesso'] = false;
										$data['mensagem'] = "E-mail existente , por favor tente com outro";
									}
									else if($verificaemail_prof->rowCount() != 0) 
									{   
										$data['sucesso'] = false;
										$data['mensagem'] = "email existente,por favor tente com outro!";
									}

									else if($verificaemail_sec->rowCount() != 0) 
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
									    $sql = \MySql::conectar()->prepare("UPDATE `chat` SET cod_remetente = ? WHERE cod_remetente = ? ");
									    $sql->execute(array($email,$emailatual));
									    $sql = \MySql::conectar()->prepare("UPDATE `chat` SET cod_destino = ? WHERE  cod_destino = ? ");
									    $sql->execute(array($email,$emailatual));
									     $_SESSION['email'] = $email;	
	                            }
	   					         
							    $sql = \MySql::conectar()->prepare("UPDATE `alunos2` SET nome = ?, senha = ?,cpf = ?, cep = ?, imagem = ?, data_de_nacimento = ?, pai = ?, mae = ?, estado_civil = ?,
							    telefone = ?, sexo = ?, email = ?  WHERE matricula = $matriculaatual");
								$sql->execute(array($nome,$senha,$cpf,$cep,$imagem,$data_de_nacimento,$pai,$mae,$estado_civil,$telefone,$sexo,$email));

								$sql = \MySql::conectar()->prepare("UPDATE `matriculados` SET nome = ? WHERE cod_aluno = $matriculaatual ");
							    $sql->execute(array($nome));

								

	                             $_SESSION['imagem'] = $imagem;	
	                             $_SESSION['nome'] = $nome;
								 $data['mensagem'] = " Seus Dados foram atualizados com sucesso";


						    }

            }


	die(json_encode($data));
?>