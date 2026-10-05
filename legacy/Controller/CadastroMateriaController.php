<?php
namespace Controller;
use \Views\mainview;

    
class CadastroMateriaController
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

		    if(isset($_POST['Cadastre-materia']))
			{
				\Models\MainMolde::cadastro_materia();
			}

			if(isset($_POST['excluir']))
			{
				$tablela = "materia";
				\Models\MainMolde::excluir($tablela);
			}

			if(isset($_POST['editar']))
			{
				$tablela = "materia";
				\Models\MainMolde::editar($tablela);
			}

		 	if(@$_SESSION['login_secretaria'] == true)
		    {
		    	mainView::render('cadastrar-materia.php',[],'headersecretaria.php');
		    }
	}
}
?>