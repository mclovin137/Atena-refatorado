<?php
namespace Controller;
use \Views\mainview;

    
class CadastroCursoController
{
	   
	public function index()
	{

		    $token = md5(@$_SESSION['matricula'].$_SERVER['REMOTE_ADDR'].$_SERVER['HTTP_USER_AGENT']);

		    if ($token != @$_SESSION['sessao'])
		    {
		    	\Models\HomeMolde::redirect(INCLUDE_PATH);
		    }
		    
     	    if(@$_SESSION['login_secretaria'] != true)
		    {
               \Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
		    }

		    if(isset($_POST['Cadastre-curso']))
			{
				\Models\MainMolde::cadastro_curso();
			}

			if(isset($_POST['excluir']))
			{
				$tablela = "curso";
				\Models\MainMolde::excluir($tablela);
			}

			if(isset($_POST['editar']))
			{
				$tablela = "curso";
				\Models\MainMolde::editar($tablela);
			}

   			if(@$_SESSION['login_secretaria'] == true)
		    {
		 		mainView::render('cadastrar-curso.php',[],'headersecretaria.php');
		 	}
	}
}
?>