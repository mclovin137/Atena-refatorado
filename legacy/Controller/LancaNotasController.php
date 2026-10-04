<?php
namespace Controller;
use \Views\mainview;

    
class LancaNotasController
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

		    if(isset($_POST['limit-tempo']))
			{
				\Models\MainMolde::limita_tempo();
			}

 		    if(@$_SESSION['login_secretaria'] == true)
		    {             
		 		mainView::render('gerir-diaro.php',[],'headersecretaria.php');
		 	}
	}
}
?>