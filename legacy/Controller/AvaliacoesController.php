<?php
namespace Controller;
use \Views\mainview;

    
class AvaliacoesController 
{
	   
	public function index()
	{
     	    $token = md5(@$_SESSION['matricula'].$_SERVER['REMOTE_ADDR'].$_SERVER['HTTP_USER_AGENT']);
     	    $msn = "ola mundo";

		    if ($token != @$_SESSION['sessao'])
		    {
		    	\Models\HomeMolde::redirect(INCLUDE_PATH);
		    }
		    
     	    if(@$_SESSION['login_Professo'] != true && @$_SESSION['login_secretaria'] != true)
		    {
               \Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
		    }
	
	       

            if(@$_SESSION['login_Professo'] == true)
		    {
		 	    mainView::render('avaliacoes.php',[],'headerprofessor.php');
		 	}
		 	else if(@$_SESSION['login_secretaria'] == true)
		    {
		 	    mainView::render('avaliacoes-adm.php',[],'headersecretaria.php');
		 	}
	}
}
?>