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
  
    
    if(($ano == "todos") && ($materia == "todos") && ($curso == "todos") && ($serie == "todos"))
    {    
        $diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` ");
		$diciplina->execute(array());
		$diciplina = $diciplina->fetchAll();
    }

    // cobinaçoes de filtros possiveis //
    
    else if(($ano != "todos") && ($materia == "todos") && ($curso == "todos") && ($serie == "todos"))
    {  	
	    $diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_ano = ? ");
		$diciplina->execute(array($ano));
		$diciplina = $diciplina->fetchAll();
    }
    else if(($ano != "todos") && ($materia != "todos") && ($curso == "todos") && ($serie == "todos"))
    {  	
	    $diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_ano = ? AND nome = ?");
		$diciplina->execute(array($ano,$materia));
		$diciplina = $diciplina->fetchAll();
    }

    else if(($ano != "todos") && ($materia == "todos") && ($curso == "todos") && ($serie != "todos"))
    {   
        $diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_ano = ? AND cod_serie = ?");
        $diciplina->execute(array($ano,$serie));
        $diciplina = $diciplina->fetchAll();
    }

    else if(($ano != "todos") && ($materia != "todos") && ($curso != "todos") && ($serie == "todos"))
    {  	
	    $diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_ano = ?  AND NOME = ? AND cod_curso = ?");
		$diciplina->execute(array($ano,$materia,$curso));
		$diciplina = $diciplina->fetchAll();
    }
    else if(($ano != "todos") && ($materia != "todos") && ($curso != "todos") && ($serie != "todos"))
    {  	
	    $diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_ano = ? AND NOME = ? AND cod_curso = ? AND cod_serie = ?");
		$diciplina->execute(array($ano,$materia,$curso,$serie));
		$diciplina = $diciplina->fetchAll();
    }
    
    else if(($ano == "todos") && ($materia != "todos") && ($curso == "todos") && ($serie == "todos"))
    {   	
		$diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE nome = ? ");
		$diciplina->execute(array($materia));
		$diciplina = $diciplina->fetchAll();
    }
    
    else if(($ano == "todos") && ($materia != "todos") && ($curso != "todos") && ($serie == "todos"))
    {   	
		$diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE nome = ? AND cod_curso = ?");
		$diciplina->execute(array($materia,$curso));
		$diciplina = $diciplina->fetchAll();
    }

    else if(($ano == "todos") && ($materia != "todos") && ($curso != "todos") && ($serie != "todos"))
    {   	
		$diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE nome = ?   AND cod_curso = ? AND cod_serie = ?");
		$diciplina->execute(array($materia,$curso,$serie));
		$diciplina = $diciplina->fetchAll();
    }

    else if(($ano == "todos") && ($materia != "todos") && ($curso == "todos") && ($serie != "todos"))
    {   	
		$diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE nome = ?  AND cod_serie = ?");
		$diciplina->execute(array($materia,$serie));
		$diciplina = $diciplina->fetchAll();
    }

    else if(($ano == "todos") && ($materia == "todos") && ($curso != "todos") && ($serie == "todos"))
    {
		$diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_curso = ? ");
		$diciplina->execute(array($curso));
		$diciplina = $diciplina->fetchAll();
    }

    else if(($ano == "todos") && ($materia == "todos") && ($curso != "todos") && ($serie != "todos"))
    {
		$diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_curso = ?  AND cod_serie = ? ");
		$diciplina->execute(array($curso,$serie));
		$diciplina = $diciplina->fetchAll();
    }

    else if(($ano != "todos") && ($materia == "todos") && ($curso != "todos") && ($serie != "todos"))
    {
		$diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_curso = ? AND cod_serie = ? AND cod_ano = ?");
		$diciplina->execute(array($curso,$serie,$ano));
		$diciplina = $diciplina->fetchAll();
    }

    else if(($ano != "todos") && ($materia == "todos") && ($curso != "todos") && ($serie == "todos"))
    {
		$diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_curso = ? AND cod_ano = ?");
		$diciplina->execute(array($curso,$ano));
		$diciplina = $diciplina->fetchAll();
    }

    else if(($ano != "todos") && ($materia == "todos") && ($curso == "todos") && ($serie != "todos"))
    {
		$diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_curso = ? AND cod_ano = ? AND cod_serie = ?");
		$diciplina->execute(array($curso,$ano,$serie));
		$diciplina = $diciplina->fetchAll();
    }

    else if(($ano == "todos") && ($materia == "todos") && ($curso == "todos") && ($serie != "todos"))
    {  	
		$diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_serie = ? ");
		$diciplina->execute(array($serie));
		$diciplina = $diciplina->fetchAll();
    }

    else if(($ano == "todos") && ($materia != "todos") && ($curso == "todos") && ($serie != "todos"))
    {  	
		$diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_serie = ? AND nome = ? ");
		$diciplina->execute(array($serie,$materia));
		$diciplina = $diciplina->fetchAll();
    }

    else if(($ano != "todos") && ($materia != "todos") && ($curso == "todos") && ($serie != "todos"))
    {  	
		$diciplina = MySql::conectar()->prepare("SELECT * FROM `diciplina` WHERE cod_serie = ? AND nome = ? AND cod_ano = ? ");
		$diciplina->execute(array($serie,$materia,$ano));
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