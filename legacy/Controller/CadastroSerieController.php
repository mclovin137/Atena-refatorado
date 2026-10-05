<?php
namespace Controller;
use \Views\mainview;

    
class CadastroSerieController
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

		    if(isset($_POST['Cadastre-serie']))
			{
				\Models\MainMolde::cadastro_serie();
			}

			if(isset($_POST['excluir']))
			{
				$tablela = "serie";
				\Models\MainMolde::excluir($tablela);
			}

			if(isset($_POST['editar']))
			{
				$tablela = "serie";
				\Models\MainMolde::editar($tablela);
			}


		 	if(@$_SESSION['login_secretaria'] == true)
		    {
		    	mainView::render('cadastrar-serie.php',[],'headersecretaria.php');
		    }
	}
}
?>