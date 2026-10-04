<?php
namespace Controller;
use \Views\mainview;

    
class CalendarioController
{
	   
	public function index()
	{
             $token = md5(@$_SESSION['matricula'].$_SERVER['REMOTE_ADDR'].$_SERVER['HTTP_USER_AGENT']);
             
             if ($token != @$_SESSION['sessao'])
             {
               \Models\HomeMolde::redirect(INCLUDE_PATH);
             }

     	      if(@$_SESSION['login_Aluno'] != true && @$_SESSION['login_Professo'] != true && @$_SESSION['login_secretaria'] != true)
		        {
               \Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
		        }
	       
            if(@$_SESSION['login_secretaria'] == true)	
            {	 	
            	mainView::render('calendario.php',[],'headersecretaria.php');
            }
            else if(@$_SESSION['login_Professo'] == true)
            {
            	mainView::render('calendario-user.php',[],'headerprofessor.php');
            }
            else if (@$_SESSION['login_Aluno'] == true) 
            {
               mainView::render('calendario-user.php',[],'headeraluno.php');
            }
            
	}
}
?>