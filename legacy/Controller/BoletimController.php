<?php
namespace Controller;
use \Views\mainview;

    
class BoletimController
{
	   
	public function index()
	{
		    $token = md5(@$_SESSION['matricula'].$_SERVER['REMOTE_ADDR'].$_SERVER['HTTP_USER_AGENT']);

		    if ($token != @$_SESSION['sessao'])
		    {
		    	\Models\HomeMolde::redirect(INCLUDE_PATH);
		    }
		    
     	    if((@$_SESSION['login_Professo'] != true ) && (@$_SESSION['login_secretaria'] != true) && (@$_SESSION['login_Aluno'] != true) )
		    {
               \Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
		    }

		    if((isset($_POST['1bm'])) || (isset($_POST['2bm'])) || (isset($_POST['3bm'])) || (isset($_POST['4bm']) ))
			{
				\Models\MainMolde::lanca_notas();
			}

			if((isset($_POST['libera-1bm'])) || (isset($_POST['libera-2bm'])) || (isset($_POST['libera-3bm'])) || (isset($_POST['libera-4bm']) ))
			{
				\Models\MainMolde::libera_notas();
			}

            if(@$_SESSION['login_Professo'] == true)
            {
		 	   mainView::render('boletim.php',[],'headerprofessor.php');
            }

            else if (@$_SESSION['login_secretaria'] == true) 
            {
               mainView::render('boletim-adm.php',[],'headersecretaria.php');
            }
		 	 
		 	else if (@$_SESSION['login_Aluno'] == true) 
            {
               mainView::render('boletim-aluno.php',[],'headeraluno.php');
            } 
	}
}
?>