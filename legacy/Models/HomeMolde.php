<?php

namespace Models;

class HomeMolde
{
     public static function login()
     {      

		    
		    $nome = strip_tags($_POST['nome']);
		    $matricula = strip_tags($_POST['matricula']);
			$senha = md5(strip_tags($_POST['senha']));
			$_SESSION['user'] =  md5($_SERVER['REMOTE_ADDR'].$_SERVER['HTTP_USER_AGENT']);
		   
       
			$professo = \MySql::conectar()->prepare("SELECT * FROM `professo2` WHERE email = ? AND matricula = ?  AND senha = ?");
			$professo ->execute(array($nome,$matricula,$senha));
			$alunos = \MySql::conectar()->prepare("SELECT * FROM `alunos2` WHERE email = ? AND matricula = ?  AND senha = ?");
			$alunos ->execute(array($nome,$matricula,$senha));
			$secretaria = \MySql::conectar()->prepare("SELECT * FROM `secretaria2` WHERE email = ? AND matricula = ?  AND senha = ?");
			$secretaria ->execute(array($nome,$matricula,$senha));
			
			if($alunos->rowCount() == 1)
			{
					$info = $alunos->fetch();
					//Logamos com sucesso.
					$_SESSION['login_Aluno'] = true;
					$_SESSION['nome'] = $info['nome'];
					$_SESSION['id'] = $info['id'];
					$_SESSION['imagem'] = $info['imagem'];
					$_SESSION['matricula'] = $info['matricula'];
				    $_SESSION['email'] = $info['email'];
					$_SESSION['sessao'] = md5($_SESSION['matricula'].$_SERVER['REMOTE_ADDR'].$_SERVER['HTTP_USER_AGENT']);
					HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
                  
			}
			else if($professo->rowCount() == 1) 
			{        
				    
					$info = $professo ->fetch();
					if($info['estado'] == 1)
					{
						$_SESSION['login_Professo'] = true;
						$_SESSION['nome'] = $info['nome'];
						$_SESSION['id'] = $info['id'];
						$_SESSION['imagem'] = $info['imagem'];
						$_SESSION['matricula'] = $info['matricula'];
						$_SESSION['email'] = $info['email'];
						$_SESSION['sessao'] = md5($_SESSION['matricula'].$_SERVER['REMOTE_ADDR'].$_SERVER['HTTP_USER_AGENT']);
						HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
					}
					else
					{       
		
						echo '<div class="erro-box"><i class="fa fa-times"></i> Voce foi desativado !</div>';			
					 
		   			}	

			}
			else if($secretaria->rowCount() == 1) 
			{
				    $info = $secretaria ->fetch();
			
					if($info['estado'] == 1)
					{
						$_SESSION['login_secretaria'] = true;
						$_SESSION['nome'] = $info['nome'];
						$_SESSION['imagem'] = $info['imagem'];
						$_SESSION['id'] = $info['id'];
						$_SESSION['matricula'] = $info['matricula'];
						$_SESSION['email'] = $info['email'];
						$_SESSION['sessao'] = md5($_SESSION['matricula'].$_SERVER['REMOTE_ADDR'].$_SERVER['HTTP_USER_AGENT']);
						HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
					}
					else
					{       
						@$cont = @$_SESSION['erros'];
						echo '<div class="erro-box"><i class="fa fa-times"></i> Voce foi desativado !</div>';			
					 
		   			}	
			}
			else
			{       
				     @$cont = @$_SESSION['erros'];
					 echo '<div class="erro-box"><i class="fa fa-times"></i> Usuário ou senha incorretos!</div>';					
					 
		    }	

	}
	
	      
    



	public static function redirect($url)
	{
			echo '<script>location.href="'.$url.'"</script>';
			die();
	}

}
?>
        