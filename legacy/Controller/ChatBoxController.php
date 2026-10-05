<?php
namespace Controller;
use \Views\mainview;

    
class ChatBoxController
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
	   
            if(isset($_POST['enviar-msn']))
			{     		       			
			   \Models\MainMolde::enviar_mensagem();
		    }
		    
		    if (@$_SESSION['login_Professo'] == true) 
		 	{
		 		mainView::render('chat-box.php',[],'headerprofessor.php');
		 	}
		 	else if (@$_SESSION['login_secretaria'] == true) 
		 	{
		 		mainView::render('chat-box.php',[],'headersecretaria.php');
		 	}
		    else if (@$_SESSION['login_Aluno'] == true) 
		 	{
		 		mainView::render('chat-box.php',[],'headeraluno.php');
		 	}
	}
}
?>