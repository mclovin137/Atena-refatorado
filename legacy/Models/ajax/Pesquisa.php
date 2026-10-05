<?php

include('../../config.php');
use \Models\MainMolde;
$data = array();
$data['sucesso'] = true;
$data = "";

    $ano = $_POST['ano'];
    $materia = $_POST['materia'];
    $curso = $_POST['curso'];
    $serie = $_POST['serie'];
    $matricula = $_SESSION['matricula'];
  
    
    if(($ano == "todos") && ($materia == "todos") && ($curso == "todos") && ($serie == "todos"))
    {    
        $diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_professo = ? ");
		$diciplina->execute(array($matricula));
		$diciplina = $diciplina->fetchAll();
    }

    // cobinaçoes de filtros possiveis //
    
    else if(($ano != "todos") && ($materia == "todos") && ($curso == "todos") && ($serie == "todos"))
    {  	
	    $diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_ano = ? AND cod_professo = ? ");
		$diciplina->execute(array($ano,$matricula));
		$diciplina = $diciplina->fetchAll();
    }
    else if(($ano != "todos") && ($materia != "todos") && ($curso == "todos") && ($serie == "todos"))
    {  	
	    $diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_ano = ? AND cod_professo = ?  AND nome = ?");
		$diciplina->execute(array($ano,$matricula,$materia));
		$diciplina = $diciplina->fetchAll();
    }
    else if(($ano != "todos") && ($materia != "todos") && ($curso != "todos") && ($serie == "todos"))
    {  	
	    $diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_ano = ? AND cod_professo = ? AND NOME = ? AND cod_curso = ?");
		$diciplina->execute(array($ano,$matricula,$materia,$curso));
		$diciplina = $diciplina->fetchAll();
    }
    else if(($ano != "todos") && ($materia != "todos") && ($curso != "todos") && ($serie != "todos"))
    {  	
	    $diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_ano = ? AND cod_professo = ? AND NOME = ? AND cod_curso = ? AND cod_serie = ?");
		$diciplina->execute(array($ano,$matricula,$materia,$curso,$serie));
		$diciplina = $diciplina->fetchAll();
    }

    else if(($ano != "todos") && ($materia == "todos") && ($curso == "todos") && ($serie != "todos"))
    {   
        $diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_ano = ? AND cod_serie = ? AND cod_professo = ?");
        $diciplina->execute(array($ano,$serie,$matricula));
        $diciplina = $diciplina->fetchAll();
    }
    
    else if(($ano == "todos") && ($materia != "todos") && ($curso == "todos") && ($serie == "todos"))
    {   	
		$diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE nome = ?  AND cod_professo  = ? ");
		$diciplina->execute(array($materia,$matricula));
		$diciplina = $diciplina->fetchAll();
    }
    
    else if(($ano == "todos") && ($materia != "todos") && ($curso != "todos") && ($serie == "todos"))
    {   	
		$diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE nome = ?  AND cod_professo  = ? AND cod_curso = ?");
		$diciplina->execute(array($materia,$matricula,$curso));
		$diciplina = $diciplina->fetchAll();
    }

    else if(($ano == "todos") && ($materia != "todos") && ($curso != "todos") && ($serie != "todos"))
    {   	
		$diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE nome = ?  AND cod_professo  = ? AND cod_curso = ? AND cod_serie = ?");
		$diciplina->execute(array($materia,$matricula,$curso,$serie));
		$diciplina = $diciplina->fetchAll();
    }

    else if(($ano == "todos") && ($materia != "todos") && ($curso == "todos") && ($serie != "todos"))
    {   	
		$diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE nome = ?  AND cod_professo  = ?  AND cod_serie = ?");
		$diciplina->execute(array($materia,$matricula,$serie));
		$diciplina = $diciplina->fetchAll();
    }

    else if(($ano == "todos") && ($materia == "todos") && ($curso != "todos") && ($serie == "todos"))
    {
		$diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_curso = ? AND cod_professo = ? ");
		$diciplina->execute(array($curso,$matricula));
		$diciplina = $diciplina->fetchAll();
    }

    else if(($ano == "todos") && ($materia == "todos") && ($curso != "todos") && ($serie != "todos"))
    {
		$diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_curso = ? AND cod_professo = ? AND cod_serie = ? ");
		$diciplina->execute(array($curso,$matricula,$serie));
		$diciplina = $diciplina->fetchAll();
    }

    else if(($ano != "todos") && ($materia == "todos") && ($curso != "todos") && ($serie != "todos"))
    {
		$diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_curso = ? AND cod_professo = ? AND cod_serie = ? AND cod_ano = ?");
		$diciplina->execute(array($curso,$matricula,$serie,$ano));
		$diciplina = $diciplina->fetchAll();
    }

    else if(($ano != "todos") && ($materia == "todos") && ($curso != "todos") && ($serie == "todos"))
    {
		$diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_curso = ? AND cod_professo = ?  AND cod_ano = ?");
		$diciplina->execute(array($curso,$matricula,$ano));
		$diciplina = $diciplina->fetchAll();
    }

    else if(($ano != "todos") && ($materia == "todos") && ($curso == "todos") && ($serie != "todos"))
    {
		$diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_curso = ? AND cod_professo = ?  AND cod_ano = ? AND cod_serie = ?");
		$diciplina->execute(array($curso,$matricula,$ano,$serie));
		$diciplina = $diciplina->fetchAll();
    }

    else if(($ano == "todos") && ($materia == "todos") && ($curso == "todos") && ($serie != "todos"))
    {  	
		$diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_serie = ? AND cod_professo = ? ");
		$diciplina->execute(array($serie,$matricula));
		$diciplina = $diciplina->fetchAll();
    }

    else if(($ano == "todos") && ($materia != "todos") && ($curso == "todos") && ($serie != "todos"))
    {  	
		$diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_serie = ? AND cod_professo = ? AND nome = ? ");
		$diciplina->execute(array($serie,$matricula,$materia));
		$diciplina = $diciplina->fetchAll();
    }

    else if(($ano != "todos") && ($materia != "todos") && ($curso == "todos") && ($serie != "todos"))
    {  	
		$diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_serie = ? AND cod_professo = ? AND nome = ? AND cod_ano = ? ");
		$diciplina->execute(array($serie,$matricula,$materia,$ano));
		$diciplina = $diciplina->fetchAll();
    }
   
    // cobinaçoes de filtros possiveis fim //
   
	
   $data.='<div class="content-turmas">';	 

   foreach ($diciplina as $key => $value) 
    {           
		$data.=
		'
				<ul class="list-group">
					  <li class="list-group-item">'.$value['cod_serie'].' - '.$value['cod_curso'].' - '.$value['nome'].' ('.$value['cod_ano'] .')</li>
					  <li class="list-group-item"><a href="'.INCLUDE_PATH_MAIN.'diario-box?id='.$value['id'] .'">Ir para o diario</a></li>

				</ul
                <div class="clear"></div>';		
	}

	$data.='</div><!--content-turmas--> ';

     
     echo $data;

?>