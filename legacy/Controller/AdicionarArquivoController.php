<?php
namespace Controller;
use \Views\mainview;

    
class AdicionarArquivoController
{
	   
	public function index()
	{
		    $token = md5(@$_SESSION['matricula'].$_SERVER['REMOTE_ADDR'].$_SERVER['HTTP_USER_AGENT']);

		    if ($token != @$_SESSION['sessao'])
		    {
		    	\Models\HomeMolde::redirect(INCLUDE_PATH);
		    }

     	    if(@$_SESSION['login_Aluno'] != true && @$_SESSION['login_Professo'] != true && @$_SESSION['login_secretaria'] != true )
		    {
                \Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
		    }
	
	        if(isset($_POST['upload']))
			{
				\Models\MainMolde::upload();
			}

            if(@$_SESSION['login_Professo'] == true)
            {
		 	  mainView::render('arquivos-adm.php',[],'headerprofessor.php');
		    }
		    if(@$_SESSION['login_Aluno'] == true)
		    {
		      mainView::render('cadastar-arquivos-aluno.php',[],'headeraluno.php');
		    }
		    if(@$_SESSION['login_secretaria'] == true)
		    {
		 		mainView::render('arquivos-adm.php',[],'headersecretaria.php');
            }
		   
	}
}
?>