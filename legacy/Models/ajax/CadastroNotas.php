<?php
include('../../config.php');
use \Models\MainMolde;
use \Models\HomeMolde;
            
            $data['sucesso'] = true;
	        $data['mensagem'] = "";
            $data['script'] = "";
	        $serie = $_POST['cod_serie'];
	        $curso = $_POST['cod_curso'];
	        $ano = $_POST['cod_ano'];
	        $professo = $_POST['professo'];   
	        $diciplina = $_POST['nome_diciplina'];
	        $alunos = MainMolde::selectAll('matriculados','cod_curso = ? AND cod_serie = ? AND cod_ano = ?',array($curso,$serie,$ano));	
	        $n = ceil(count($alunos));
	        $x = "";
            $z = 0;
	        $verifica = MainMolde::selectAll('notas','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND cod_professo = ?',array($curso,$serie,$ano,$diciplina,$professo));
	        $nv = ceil(count($verifica));
            
	        	      
            if(isset($_POST['salva']))
			{ 
			  		foreach ($alunos as $key => $value) 
				  	{
                        @$nota1[] = $_POST['nota01'.$value['cod_aluno']];
                        @$nota2[] = $_POST['nota02'.$value['cod_aluno']];
                        @$nota3[] = $_POST['nota03'.$value['cod_aluno']];
                        @$nota4[] = $_POST['nota04'.$value['cod_aluno']];
                        @$rec1[]  = $_POST['rec01'.$value['cod_aluno']];
                        @$rec2[]  = $_POST['rec02'.$value['cod_aluno']];
                        @$rec3[]  = $_POST['rec03'.$value['cod_aluno']];
                        @$rec4[]  = $_POST['rec04'.$value['cod_aluno']];	
                        @$media[] = $_POST['media'.$value['cod_aluno']];	
                        $aluno[] = $value['cod_aluno'];  	   
				  	}
			


				  	if($nv == 0 )
				  	{   
					  		for ($y = 0; $y < $n; $y++) 
				            {  
						            $sql = \MySql::conectar()->prepare("INSERT INTO `notas` VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
									$sql->execute(array($aluno[$y],$curso,$serie,$ano,$diciplina,$professo,$x,$x,$x,$x,$x,$x,$x,$x,$x,$x,$z,$z,$z,$z,$z,$z,$z,$z,$z));
			                 }
             
				  	}
				  	
				     for ($i = 0; $i < $n; $i++) 
				     {  
                            $media_parcial = 0; 
                            $aux = 0;

                            if ($nota1[$i] > 100) 
                            {
                                $nota1[$i] = 100;
                            }
                            if ($nota2[$i] > 100) 
                            {
                                $nota2[$i] = 100;
                            }
                            if ($nota3[$i] > 100) 
                            {
                                $nota3[$i] = 100;
                            }
                            if ($nota4[$i] > 100) 
                            {
                                $nota4[$i] = 100;
                            }

                            if ($rec1[$i] > 60) 
                            {
                                $rec1[$i] = 60;
                            }
                            if ($rec2[$i] > 60) 
                            {
                                $rec2[$i] = 60;
                            }
                            if ($rec3[$i] > 60) 
                            {
                                $rec3[$i] = 60;
                            }
                            if ($rec4[$i] > 60) 
                            {
                                $rec4[$i] = 60;
                            }
                                

                            if ($nota1[$i] == "") 
                            {
                                $n1[$i] = 0;
                            }
                            else
                            {
                            	if (($nota1[$i] < 60) && ($rec1[$i] > $nota1[$i]))
                            	{
                            	   $n1[$i] = $rec1[$i];

                            	}
                            	else
                            	{
                                   $n1[$i] = $nota1[$i];
                            	}

                                @$media_parcial += $n1[$i];
                                $aux ++;
                            	
                            }

                            if ($nota2[$i] == "") 
                            {
                                $n2[$i] = 0;
                            }
                            else
                            {
                                if (($nota2[$i] < 60) && ($rec2[$i] > $nota2[$i]))
                            	{
                            	   $n2[$i] = $rec2[$i];
                            	}
                            	else
                            	{
                                   $n2[$i] = $nota2[$i];
                            	}

                                $media_parcial += $n2[$i];
                                $aux ++;
                            	
                            }

                            if ($nota3[$i] == "") 
                            {
                                $n3[$i] = 0;
                            }
                            else
                            {
                            	if (($nota3[$i] < 60) && ($rec3[$i] > $nota3[$i]))
                            	{
                            	   $n3[$i] = $rec3[$i];
                            	}
                            	else
                            	{
                                   $n3[$i] = $nota3[$i];
                            	}

                                $media_parcial += $n3[$i];
                                $aux ++;
                            }

                            if ($nota4[$i] == "") 
                            {
                                $n4[$i] = 0;
                            }
                            else
                            {
                            	if (($nota4[$i] < 60) && ($rec4[$i] > $nota4[$i]))
                            	{
                            	   $n4[$i] = $rec4[$i];
                            	}
                            	else
                            	{
                                   $n4[$i] = $nota4[$i];
                            	}

                                $media_parcial += $n4[$i];
                                $aux ++;
                            }
                            
                            if($aux == 0)
                                $aux = 1;

                            @$media[$i] = ($n1[$i] + $n2[$i] + $n3[$i] + $n4[$i]) / 4;                     
                            $media_parcial = $media_parcial / $aux;

                            $faltas = MainMolde::selectAll('presenca','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_professo = ? AND cod_aluno = ? AND cod_materia = ?',array($curso,$serie,$ano,$professo,$aluno[$i],$diciplina)); 
                            $turma = MainMolde::select('diciplina','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_professo = ?',array($curso,$serie,$ano,$professo)); 
                            $carga_horaria = $turma[6];
                            $falta = 0;
                           

                            foreach ($faltas as $key => $value) 
                            {
                                $falta += $value['faltas'];
                              
                            }
                            
                            $carga_horaria = $carga_horaria * 3600;
                            $carga_horaria_minima = $carga_horaria * 75 /(100);
                            $falta = $falta * 3300;                   
                           
                            if(($media[$i] >= 60) && ($aux == 4))
						  		 $aprovado[$i] = 1;
                            else if($falta > $carga_horaria_minima)
                                 $aprovado[$i] = 3;
						    else if (($media[$i] < 60) && ($aux == 4))
						  		 $aprovado[$i] = 4;
                            else
                                 $aprovado[$i] = 0;

                            		    
                            $sql = \MySql::conectar()->prepare("UPDATE `notas` SET n1 = ?, r1 = ?, n2 = ?, r2 = ?, n3 = ?, r3 = ?, n4 = ?, r4 = ?, media = ?, aprovado = ?,media_parcial = ?  WHERE cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND cod_professo = ? AND cod_aluno = $aluno[$i]");
						    $sql->execute(array($nota1[$i],$rec1[$i],$nota2[$i],$rec2[$i],$nota3[$i],$rec3[$i],$nota4[$i],$rec4[$i],$media[$i],$aprovado[$i],$media_parcial,$curso,$serie,$ano,$diciplina,$professo));
                              
						    
                           
                          
		  		     }
                     $data['mensagem'] = "<script>location.reload();</script>"; 
		     }
			  
	die(json_encode($data));
?>
