<?php
namespace Controller;
use \Views\mainview;

    
class MainController
{
	   	public function index()
	   	{

	   	    $token = md5(@$_SESSION['matricula'].$_SERVER['REMOTE_ADDR'].$_SERVER['HTTP_USER_AGENT']);

		    if ($token != @$_SESSION['sessao'])
		    {
		    	\Models\HomeMolde::redirect(INCLUDE_PATH);
		    }
		    
	   	    if (isset($_GET['loggout'])) 
		    {
		     	\Models\MainMolde::sair();
		    }			
	   	    	   	    
	   		if(@$_SESSION['login_Aluno'] != true && @$_SESSION['login_Professo'] != true && @$_SESSION['login_secretaria'] != true)
		    {
               \Models\HomeMolde::redirect(INCLUDE_PATH);
		    }
	   		
	   		if(@$_SESSION['login_Aluno'] == true)
	   		{
				mainView::render('aluno-home.php',[],'headeraluno.php','footerhome.php');
	   		}
			else if(@$_SESSION['login_Professo'] == true)
			{
				mainView::render('professo-home.php',[],'headerprofessor.php','footerhome.php');
			}
			else if(@$_SESSION['login_secretaria'] == true)
			{
				mainView::render('secretaria-home.php',[],'headersecretaria.php','footerhome.php');
			}

			
        }

}
?>