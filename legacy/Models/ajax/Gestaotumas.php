<?php

include('../../config.php');
use \Models\MainMolde;
$data = array();
$data['sucesso'] = true;
$data = "";

    $ano = $_POST['ano'];
    $curso = $_POST['curso'];
    $serie = $_POST['serie'];

  
    if(($ano == "todos") &&  ($curso == "todos") && ($serie == "todos"))
    {    
        $diciplina = MySql::conectar()->prepare("SELECT * FROM `turmas` ");
        $diciplina->execute(array());
        $diciplina = $diciplina->fetchAll();
    }
    else if(($ano != "todos") &&  ($curso == "todos") && ($serie == "todos"))
    {    
        $diciplina = MySql::conectar()->prepare("SELECT * FROM `turmas` WHERE ano = ? ");
        $diciplina->execute(array($ano));
        $diciplina = $diciplina->fetchAll();
    }
    else if(($ano != "todos") &&  ($curso != "todos") && ($serie == "todos"))
    {    
        $diciplina = MySql::conectar()->prepare("SELECT * FROM `turmas` WHERE ano = ? AND curso = ? ");
        $diciplina->execute(array($ano,$curso));
        $diciplina = $diciplina->fetchAll();
    }

    else if(($ano != "todos") &&  ($curso != "todos") && ($serie != "todos"))
    {    
        $diciplina = MySql::conectar()->prepare("SELECT * FROM `turmas` WHERE ano = ? AND curso = ? AND serie = ? ");
        $diciplina->execute(array($ano,$curso,$serie));
        $diciplina = $diciplina->fetchAll();
    }
    else if(($ano != "todos") &&  ($curso == "todos") && ($serie != "todos"))
    {    
        $diciplina = MySql::conectar()->prepare("SELECT * FROM `turmas` WHERE ano = ? AND serie = ?");
        $diciplina->execute(array($ano,$serie));
        $diciplina = $diciplina->fetchAll();
    }
    else if(($ano == "todos") &&  ($curso != "todos") && ($serie == "todos"))
    {    
        $diciplina = MySql::conectar()->prepare("SELECT * FROM `turmas` WHERE curso = ?");
        $diciplina->execute(array($curso));
        $diciplina = $diciplina->fetchAll();
    }

    else if(($ano == "todos") &&  ($curso == "todos") && ($serie != "todos"))
    {    
        $diciplina = MySql::conectar()->prepare("SELECT * FROM `turmas` WHERE serie = ?");
        $diciplina->execute(array($serie));
        $diciplina = $diciplina->fetchAll();
    }
    else if(($ano == "todos") &&  ($curso != "todos") && ($serie == "todos"))
    {    
        $diciplina = MySql::conectar()->prepare("SELECT * FROM `turmas` WHERE curso = ?");
        $diciplina->execute(array($curso));
        $diciplina = $diciplina->fetchAll();
    }
    else if(($ano == "todos") &&  ($curso != "todos") && ($serie != "todos"))
    {    
        $diciplina = MySql::conectar()->prepare("SELECT * FROM `turmas` WHERE curso = ? AND serie = ?");
        $diciplina->execute(array($curso,$serie));
        $diciplina = $diciplina->fetchAll();
    }
  
  
   
	
   $data.='<div class="content-turmas">';	 

   foreach ($diciplina as $key => $value) 
    {           
		$data.=
		'
				<ul class="list-group">
					  <li class="list-group-item">'.$value['serie'].' - '.$value['curso'].' - '.$value['ano'].' </li>
					  <li class="list-group-item"><a href="'.INCLUDE_PATH_MAIN.'gestaotumas-box?id='.$value['id'] .'">Ir para o diario</a></li>

				</ul
                <div class="clear"></div>';		
	}

	$data.='</div><!--content-turmas--> ';

     
     echo $data;

?>