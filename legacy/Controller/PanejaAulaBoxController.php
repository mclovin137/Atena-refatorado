<?php
namespace Controller;
use \Views\mainview;
use \Models\MainMolde;

    
class PanejaAulaBoxController
{
	
	
	// public function PegarDados()
	// {
	// 	  $id = $_SESSION['id']; 
 //          $turma = MainMolde::select('diciplina','id = ?',array($id));	
 //          $var = 1;
 //          return $turma;
	// }
	// public function PegarAlunos()
	// {
	// 	  $turma = $this->PegarTurmas();
	// 	  $diciplina = $turma[1];
	// 	  $ano = $turma[2];
	// 	  $curso = $turma[3];	
	// 	  $serie = $turma[4];
	// 	  $alunos = MainMolde::selectAll_ASC('matriculados','cod_curso = ? AND cod_serie = ? AND cod_ano = ?','nome',array($curso,$serie,$ano));
	// 	  return $alunos;
	// }
	
	public function index()
	{
		    
     	    $token = md5(@$_SESSION['matricula'].$_SERVER['REMOTE_ADDR'].$_SERVER['HTTP_USER_AGENT']);
     	   

		    if ($token != @$_SESSION['sessao'])
		    {
		    	\Models\HomeMolde::redirect(INCLUDE_PATH);
		    }
		    
     	    if(@$_SESSION['login_Professo'] != true && @$_SESSION['login_secretaria'] != true )
		    {
               \Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
		    }

		  
     	    if(@$_SESSION['login_Professo'] == true )
		    { 
		 		mainView::render('planeja-aula-box.php',[],'headerprofessor.php');
		 	}
		 	else if(@$_SESSION['login_secretaria'] == true)
		 	{
		 		mainview::render('planeja-aula-box-adm.php',[],'headersecretaria.php');
		 	}

	}



		
}

?>