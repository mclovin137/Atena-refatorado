<?php
include('../../config.php');
use \Models\MainMolde;

            $data['sucesso'] = true;
			$data['mensagem'] = "";
			$data['msn'] = "";
			if(isset($_POST['lanca_avaliacao_qualificativa']))
			{
				        
				        $serie = $_POST['cod_serie'];
						$curso = $_POST['cod_curso'];
						$diciplina = $_POST['cod_diciplina'];
						$bimestre = $_POST['bimestre'];
						$ano = $_POST['cod_ano'];
						$valor = strip_tags($_POST['valor']);
						$nome = strip_tags($_POST['nome']);
						$cod = $_POST['cod'];
						$i = 0 ;
						$c = 0;
						$alunos = MainMolde::selectAll('matriculados','cod_curso = ? AND cod_serie = ? AND cod_ano = ?',array($curso,$serie,$ano));	
					    $notas = MainMolde::selectAll('avaliacoes','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND bimestre = ? AND cod_professo = ?',array($curso,$serie,$ano,$diciplina,$bimestre,$cod));
					     $n = ceil(count($alunos));
					     $verifica = MainMolde::selectAll('notas','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND cod_professo = ?',array($curso,$serie,$ano,$diciplina,$cod));
				         $nv = ceil(count($verifica));				        
				         $x = "";
				         $z = 0;
				         $aux2 = "";


				        foreach ($alunos as $key => $value) 
					    {
			                $aluno_verifica[] = $value['cod_aluno']; 			                     
					    }
							  	

				        if($nv == 0 )
					    {   
							for ($y = 0; $y < $n; $y++) 
							{  
								$sql = \MySql::conectar()->prepare("INSERT INTO `notas` VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
								$sql->execute(array($aluno_verifica[$y],$curso,$serie,$ano,$diciplina,$cod,$x,$x,$x,$x,$x,$x,$x,$x,$x,$x,$z,$z,$z,$z,$z,$z,$z,$z,$z));
						    }
			             
					    }


			            							  
							  	
					    foreach ($notas as $key => $value) 
					    {
					        $aluno[$i] = $value['cod_aluno']; 
					        $nota_aluno[$i] = MainMolde::selectAll('avaliacoes','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND bimestre = ? AND cod_professo = ? AND cod_aluno = ?',array($curso,$serie,$ano,$diciplina,$bimestre,$cod,$aluno[$i]));
					        $aux = ceil(count($nota_aluno[$i]));	
					        $notas_aluno = MainMolde::select('notas','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_aluno = ? AND cod_diciplina = ? AND cod_professo = ?',array($value['cod_curso'] ,
			                $value['cod_serie'],$value['cod_ano'],$value['cod_aluno'],$diciplina,$cod ));
			                $rec = MainMolde::selectAll('recuperacao','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND cod_professo = ? AND bimestre = ? AND cod_aluno = ?',array($curso,$serie,$ano,$diciplina,$cod,$bimestre,$aluno[$i]));
					        $alunosrec = ceil(count($rec)); 

					        foreach ($nota_aluno[$i] as $key => $value) 
					        {

					        	@$nota[$i] += $value['valor_obtido'];					        						        
					        	@$valor_total[$i] += $value['valor'];					        	
					        	
					        }
					        if ($valor_total[$i] < 100) 
					        {
					        	$data['sucesso'] = false;
					        	$data['mensagem']= "O valor da avaliaçao esta abaixo dos 100 prontos ";					        
					        
					        }

					     
                            		                   			                                                     
	                        if( $data['sucesso'] == true)  
	                        { 
						        if ($bimestre == "1° bimestre") 
						        {						           
							            if ($nota[$i] > 100) 
							            {
							            	  $nota[$i] = 100;                         			
							            }	


							            if ($nota[$i] < 60 && $alunosrec == 0) 
							            {   
							            	
							            	$sql = \MySql::conectar()->prepare("INSERT INTO recuperacao VALUES(NULL,?,?,?,?,?,?,?,?,?,?)");
									        $sql->execute(array("recuperaçao","1° bimestre",$ano,$diciplina,$curso,$serie,$cod,$aluno[$i],"100",$x));
							            }


								  	    if($nota[$i] >= 60 && $alunosrec != 0)
								  	    {
								  	    	 $sql = \MySql::conectar()->prepare("DELETE FROM  `recuperacao`  WHERE  cod_aluno = ? AND cod_curso = ? AND  cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND bimestre = ? AND cod_professo = ? ");
									         $sql->execute(array($aluno[$i],$curso,$serie,$ano,$diciplina,$bimestre,$cod));
								  	    }

							            $sql = \MySql::conectar()->prepare("UPDATE `notas` SET n1 = ? WHERE cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND cod_professo = ? AND cod_aluno = $aluno[$i]");
									    $sql->execute(array($nota[$i],$curso,$serie,$ano,$diciplina,$cod));
									    $i++;
							    }

							    else if ($bimestre == "2° bimestre") 
						        {	
			                         
				                        if ($nota[$i] > 100) 
								        {
								             $nota[$i] = 100;                         			
								        }
								        if ($nota[$i] < 60 && $alunosrec == 0) 
							            {   
							            	
							            	$sql = \MySql::conectar()->prepare("INSERT INTO recuperacao VALUES(NULL,?,?,?,?,?,?,?,?,?,?)");
									        $sql->execute(array("recuperaçao","2° bimestre",$ano,$diciplina,$curso,$serie,$cod,$aluno[$i],"100",$x));
							            }	

								  	    if($nota[$i] >= 60 && $alunosrec != 0)
								  	    {
								  	    	 $sql = \MySql::conectar()->prepare("DELETE FROM  `recuperacao`  WHERE  cod_aluno = ? AND cod_curso = ? AND  cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND bimestre = ? AND cod_professo = ? ");
									         $sql->execute(array($aluno[$i],$curso,$serie,$ano,$diciplina,$bimestre,$cod));
								  	    }                           				   
								           
							            $sql = \MySql::conectar()->prepare("UPDATE `notas` SET n2 = ? WHERE cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND cod_professo = ? AND cod_aluno = $aluno[$i]");
									    $sql->execute(array($nota[$i],$curso,$serie,$ano,$diciplina,$cod));
									    $i++;
							    }

							    else if ($bimestre == "3° bimestre") 
						        {		           				             	
						            
			                            if ($nota[$i] > 100) 
							            {
							            	  $nota[$i] = 100;                         			
							            }

							            if ($nota[$i] < 60 && $alunosrec == 0 ) 
							            {   
							            	
							            	$sql = \MySql::conectar()->prepare("INSERT INTO recuperacao VALUES(NULL,?,?,?,?,?,?,?,?,?,?)");
									        $sql->execute(array("recuperaçao","3° bimestre",$ano,$diciplina,$curso,$serie,$cod,$aluno[$i],"100",$x));
							            } 


								  	    if($nota[$i] >= 60 && $alunosrec != 0)
								  	    {
								  	    	 $sql = \MySql::conectar()->prepare("DELETE FROM  `recuperacao`  WHERE  cod_aluno = ? AND cod_curso = ? AND  cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND bimestre = ? AND cod_professo = ? ");
									         $sql->execute(array($aluno[$i],$curso,$serie,$ano,$diciplina,$bimestre,$cod));
								  	    }                          				   
							             
							            $sql = \MySql::conectar()->prepare("UPDATE `notas` SET n3 = ? WHERE cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND cod_professo = ? AND cod_aluno = $aluno[$i]");
									    $sql->execute(array($nota[$i],$curso,$serie,$ano,$diciplina,$cod));
									    $i++;
							    }

							    else if ($bimestre == "4° bimestre") 
						        {		           				             	
	                                    if ($nota[$i] > 100) 
							            {
							            	  $nota[$i] = 100;                         			
							            }

							            if ($nota[$i] < 60 && $alunosrec == 0) 
							            {   
							            
							            	$sql = \MySql::conectar()->prepare("INSERT INTO recuperacao VALUES(NULL,?,?,?,?,?,?,?,?,?,?)");
									        $sql->execute(array("recuperaçao","4° bimestre",$ano,$diciplina,$curso,$serie,$cod,$aluno[$i],"100",$x));
							            }	


								  	    if($nota[$i] >= 60 && $alunosrec != 0)
								  	    {
								  	    	 $sql = \MySql::conectar()->prepare("DELETE FROM  `recuperacao`  WHERE  cod_aluno = ? AND cod_curso = ? AND  cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND bimestre = ? AND cod_professo = ? ");
									         $sql->execute(array($aluno[$i],$curso,$serie,$ano,$diciplina,$bimestre,$cod));
								  	    }                           				   
							           
							            $sql = \MySql::conectar()->prepare("UPDATE `notas` SET n4 = ? WHERE cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND cod_professo = ? AND cod_aluno = $aluno[$i]");
									    $sql->execute(array($nota[$i],$curso,$serie,$ano,$diciplina,$cod));
									    $i++;
							    }

							    $notas_aluno = MainMolde::select('notas','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_aluno = ? AND cod_diciplina = ? AND cod_professo = ?',array($value['cod_curso'] ,
				                $value['cod_serie'],$value['cod_ano'],$value['cod_aluno'],$diciplina,$cod )); 

							    if(1 == 1)
							    {
	                                $media_parcial = 0; 
		                            $aux3 = 0;

		                          

		                            if ($notas_aluno[6] == "") 
		                            {
		                                $n1[$c] = 0;
		                            }
		                            else
		                            {
		                            	if (($notas_aluno[6] < 60) && ($notas_aluno[7] > $notas_aluno[6]))
		                            	{
		                            	   $n1[$c] = $notas_aluno[7];

		                            	}
		                            	else
		                            	{
		                                   $n1[$c] = $notas_aluno[6];
		                            	}

		                                @$media_parcial += $n1[$c];
		                                $aux3 ++;
		                            	
		                            }

									if ($notas_aluno[8] == "") 
		                            {
		                                $n2[$c] = 0;
		                            }
		                            else
		                            {
		                            	if (($notas_aluno[8] < 60) && ($notas_aluno[9] > $notas_aluno[8]))
		                            	{
		                            	   $n2[$c] = $notas_aluno[9];

		                            	}
		                            	else
		                            	{
		                                   $n2[$c] = $notas_aluno[8];
		                            	}

		                                @$media_parcial += $n2[$c];
		                                $aux3 ++;
		                            	
		                            }

		                            if ($notas_aluno[10] == "") 
		                            {
		                                $n3[$c] = 0;
		                            }
		                            else
		                            {
		                            	if (($notas_aluno[10] < 60) && ($notas_aluno[11] > $notas_aluno[10]))
		                            	{
		                            	   $n3[$c] = $notas_aluno[11];

		                            	}
		                            	else
		                            	{
		                                   $n3[$c] = $notas_aluno[10];
		                            	}

		                                @$media_parcial += $n3[$c];
		                                $aux3 ++;
		                            	
		                            }
		                            if ($notas_aluno[12] == "") 
		                            {
		                                $n4[$c] = 0;
		                            }
		                            else
		                            {
		                            	if (($notas_aluno[12] < 60) && ($notas_aluno[13] > $notas_aluno[12]))
		                            	{
		                            	   $n4[$c] = $notas_aluno[13];

		                            	}
		                            	else
		                            	{
		                                   $n4[$c] = $notas_aluno[12];
		                            	}

		                                @$media_parcial += $n4[$c];
		                                $aux3 ++;
		                            	
		                            }
		                            if($aux3 == 0)
		                                $aux3 = 1;

		                            @$media[$c] = ($n1[$c] + $n2[$c] + $n3[$c] + $n4[$c]) / 4;                     
		                            $media_parcial = $media_parcial / $aux3;

		                            $faltas = MainMolde::selectAll('presenca','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_professo = ? AND cod_aluno = ? AND cod_materia = ?',array($curso,$serie,$ano,$cod,$aluno[$c],$diciplina)); 
		                            $turma = MainMolde::select('diciplina','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_professo = ?',array($curso,$serie,$ano,$cod)); 
		                            $carga_horaria = $turma[6];
		                            $falta = 0;
		                           

		                            foreach ($faltas as $key => $value) 
		                            {
		                                $falta += $value['faltas'];
		                              
		                            }
		                            
		                            $carga_horaria = $carga_horaria * 3600;
		                            $carga_horaria_minima = $carga_horaria * 25 /(100);
		                            $falta = $falta * 3600;                   
		                           
		                            if(($media[$c] >= 60) && ($aux3 == 4))
								  		 $aprovado[$c] = 1;
		                            else if($falta > $carga_horaria_minima)
		                                 $aprovado[$c] = 3;
								    else if (($media[$c] < 60) && ($aux3 == 4))
								  		 $aprovado[$c] = 2;
		                            else
		                                 $aprovado[$c] = 0;

		                            		    
		                            $sql = \MySql::conectar()->prepare("UPDATE `notas` SET  media = ?, aprovado = ?,media_parcial = ?  WHERE cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND cod_professo = ? AND cod_aluno = ?");
								    $sql->execute(array($media[$c],$aprovado[$c],$media_parcial,$curso,$serie,$ano,$diciplina,$cod,$aluno[$c]));
								    $c++;
								   
							    }

                                 $data['mensagem'] = "<script>location.reload();</script>";
						    }


					
				    }
		          
			                
            }
            else if(isset($_POST['lanca_rec']))
			{
				        
				        $serie = $_POST['cod_serie'];
						$curso = $_POST['cod_curso'];
						$diciplina = $_POST['cod_diciplina'];
						$bimestre = $_POST['bimestre'];
						$ano = $_POST['cod_ano'];
						$valor = strip_tags($_POST['valor']);
						$nome = strip_tags($_POST['nome']);
						$cod = $_POST['cod'];
						$i = 0 ;
						$c = 0;						
					    $notas_recuperacao = MainMolde::selectAll('recuperacao','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND bimestre = ? AND cod_professo = ?',array($curso,$serie,$ano,$diciplina,$bimestre,$cod));						   			     						  
				        
									  
							  	
					    foreach ($notas_recuperacao as $key => $value) 
					    {
					        $aluno[$i] = $value['cod_aluno']; 
					        $notas_aluno[$i] = MainMolde::selectAll('recuperacao','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND bimestre = ? AND cod_professo = ? AND cod_aluno = ?',array($curso,$serie,$ano,$diciplina,$bimestre,$cod,$aluno[$i]));
					       

					        foreach ($notas_aluno[$i] as $key => $value) 
					        {

					        	@$nota[$i] += $value['valor_obtido'];					        	
					        	
					        }

					     
                            		                   			                                                     
	                           
					        if ($bimestre == "1° bimestre") 
					        {						           
						            if ($nota[$i] > 60) 
						            {
						            	  $nota[$i] = 60;                         			
						            }	                           				   
						            
						            $sql = \MySql::conectar()->prepare("UPDATE `notas` SET r1 = ? WHERE cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND cod_professo = ? AND cod_aluno = $aluno[$i]");
								    $sql->execute(array($nota[$i],$curso,$serie,$ano,$diciplina,$cod));
								    $i++;
						    }

						    else if ($bimestre == "2° bimestre") 
					        {	
		                         
			                        if ($nota[$i] > 60) 
							        {
							             $nota[$i] = 60;                       			
							        }
							                         				   
							           
						            $sql = \MySql::conectar()->prepare("UPDATE `notas` SET r2 = ? WHERE cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND cod_professo = ? AND cod_aluno = $aluno[$i]");
								    $sql->execute(array($nota[$i],$curso,$serie,$ano,$diciplina,$cod));
								    $i++;
						    }

						    else if ($bimestre == "3° bimestre") 
					        {		           				             	
					            
		                            if ($nota[$i] > 60) 
						            {
						            	  $nota[$i] = 60;                         			
						            }	
                   				   
						             
						            $sql = \MySql::conectar()->prepare("UPDATE `notas` SET r3 = ? WHERE cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND cod_professo = ? AND cod_aluno = $aluno[$i]");
								    $sql->execute(array($nota[$i],$curso,$serie,$ano,$diciplina,$cod));
								    $i++;
						    }

						    else if ($bimestre == "4° bimestre") 
					        {		           				             	
                                    if ($nota[$i] > 60) 
						            {
						            	  $nota[$i] = 60;                         			
						            }
						           
						            $sql = \MySql::conectar()->prepare("UPDATE `notas` SET r4 = ? WHERE cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND cod_professo = ? AND cod_aluno = $aluno[$i]");
								    $sql->execute(array($nota[$i],$curso,$serie,$ano,$diciplina,$cod));
								    $i++;
						    }
					       
					    

					      $notass = MainMolde::select('notas','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_aluno = ? AND cod_diciplina = ? AND cod_professo = ?',array($value['cod_curso'] ,
			              $value['cod_serie'],$value['cod_ano'],$aluno[$c],$diciplina,$cod )); 
			             

					        if(1 == 1)
						    {
                                $media_parcial = 0; 
	                            $aux3 = 0;

	                          

	                            if ($notass[6] == "") 
	                            {
	                                $n1[$c] = 0;
	                            }
	                            else
	                            {
	                            	if (($notass[6] < 60) && ($notass[7] > $notass[6]))
	                            	{
	                            	   $n1[$c] = $notass[7];

	                            	}
	                            	else
	                            	{
	                                   $n1[$c] = $notass[6];
	                            	}

	                                @$media_parcial += $n1[$c];
	                                $aux3 ++;
	                            	
	                            }

								if ($notass[8] == "") 
	                            {
	                                $n2[$c] = 0;
	                            }
	                            else
	                            {
	                            	if (($notass[8] < 60) && ($notass[9] > $notass[8]))
	                            	{
	                            	   $n2[$c] = $notass[9];

	                            	}
	                            	else
	                            	{
	                                   $n2[$c] = $notass[8];
	                            	}

	                                @$media_parcial += $n2[$c];
	                                $aux3 ++;
	                            	
	                            }

	                            if ($notass[10] == "") 
	                            {
	                                $n3[$c] = 0;
	                            }
	                            else
	                            {
	                            	if (($notass[10] < 60) && ($notass[11] > $notass[10]))
	                            	{
	                            	   $n3[$c] = $notass[11];

	                            	}
	                            	else
	                            	{
	                                   $n3[$c] = $notass[10];
	                            	}

	                                @$media_parcial += $n3[$c];
	                                $aux3 ++;
	                            	
	                            }
	                            if ($notass[12] == "") 
	                            {
	                                $n4[$c] = 0;
	                            }
	                            else
	                            {
	                            	if (($notass[12] < 60) && ($notass[13] > $notass[12]))
	                            	{
	                            	   $n4[$c] = $notass[13];

	                            	}
	                            	else
	                            	{
	                                   $n4[$c] = $notass[12];
	                            	}

	                                @$media_parcial += $n4[$c];
	                                $aux3 ++;
	                            	
	                            }
	                            if($aux3 == 0)
	                                $aux3 = 1;

	                            @$media[$c] = ($n1[$c] + $n2[$c] + $n3[$c] + $n4[$c]) / 4;                     
	                            $media_parcial = $media_parcial / $aux3;

	                            $faltas = MainMolde::selectAll('presenca','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_professo = ? AND cod_aluno = ? AND cod_materia = ?',array($curso,$serie,$ano,$cod,$aluno[$c],$diciplina)); 
	                            $turma = MainMolde::select('diciplina','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_professo = ?',array($curso,$serie,$ano,$cod)); 
	                            $carga_horaria = $turma[6];
	                            $falta = 0;
	                           

	                            foreach ($faltas as $key => $value) 
	                            {
	                                $falta += $value['faltas'];
	                              
	                            }
	                            
	                            $carga_horaria = $carga_horaria * 3600;
	                            $carga_horaria_minima = $carga_horaria * 75 /(100);
	                            $falta = $falta * 3300;                   
	                           
	                            if(($media[$c] >= 60) && ($aux3 == 4))
							  		 $aprovado[$c] = 1;
	                            else if($falta > $carga_horaria_minima)
	                                 $aprovado[$c] = 3;
							    else if (($media[$c] < 60) && ($aux3 == 4))
							  		 $aprovado[$c] = 4;
	                            else
	                                 $aprovado[$c] = 0;

	                            		    
	                            $sql = \MySql::conectar()->prepare("UPDATE `notas` SET  media = ?, aprovado = ?,media_parcial = ?  WHERE cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND cod_professo = ? AND cod_aluno = ?");
							    $sql->execute(array($media[$c],$aprovado[$c],$media_parcial,$curso,$serie,$ano,$diciplina,$cod,$aluno[$c]));
							    $c++;
							   
						    }
					       
            }
                           

			            $data['mensagem'] = "As notas foram enviadas com sucesso"; 
			                
            }
            else if(isset($_POST['media_avaliacao_qualificativa']))
            {
            	        $serie = $_POST['cod_serie'];
						$curso = $_POST['cod_curso'];
						$diciplina = $_POST['cod_diciplina'];
						$bimestre = $_POST['bimestre'];
						$ano = $_POST['cod_ano'];
						$valor = strip_tags($_POST['valor']);
						$nome = strip_tags($_POST['nome']);
						$cod = $_POST['cod'];
						$i = 0 ;
						$w = 0;
						$alunos = MainMolde::selectAll('matriculados','cod_curso = ? AND cod_serie = ? AND cod_ano = ?',array($curso,$serie,$ano));	
					    $notas = MainMolde::selectAll('avaliacoes','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND bimestre = ? AND cod_professo = ?',array($curso,$serie,$ano,$diciplina,$bimestre,$cod));
					    $notas_aux = MainMolde::selectAll('avaliacoes','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND bimestre = ? AND cod_professo = ? AND nome = ?',array($curso,$serie,$ano,$diciplina,$bimestre,$cod,$nome));
					    $cont_notas = ceil(count($notas_aux));
					    $n = ceil(count($alunos));				   
				        
                        
					    foreach ($notas as $key => $value) 
					    {
					        $aluno[$i] = $value['cod_aluno']; 
					        $notas_aluno[$i] = MainMolde::selectAll('avaliacoes','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND bimestre = ? AND cod_professo = ? AND cod_aluno = ?',array($curso,$serie,$ano,$diciplina,$bimestre,$cod,$aluno[$i]));
					        $aux = ceil(count($notas_aluno[$i]));


					        

					        foreach ($notas_aluno[$i] as $key => $value) 
					        {
                             
					        	@$nota[$w] += $value['valor_obtido'];					        	
					        	@$aluno_nota[$w]  = $value['cod_aluno'];
					        	@$valor_total[$w] += $value['valor'];

					        }

					         $w++;

					        	
					       						   				       
					    }
                        if($aux > 1)
                        {

						    for ($g=0; $g < $cont_notas; $g++) 
						    { 
						    	 $sql = \MySql::conectar()->prepare("DELETE  FROM `avaliacoes`  WHERE  cod_aluno = ? AND cod_curso = ? AND  cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND bimestre = ? AND cod_professo = ? ");
								 $sql->execute(array($aluno_nota[$g],$curso,$serie,$ano,$diciplina,$bimestre,$cod));
								 $sql = \MySql::conectar()->prepare("INSERT INTO `avaliacoes` VALUES (null,?,?,?,?,?,?,?,?,?,?)");
							     $sql->execute(array($curso,$ano,$serie,$aluno_nota[$g],$diciplina,$valor_total[$g]/$aux,$nota[$g]/$aux,$bimestre,$nome,$cod));
							    
						    }
						       

				             $data['mensagem'] = "<script>location.reload();</script>";

                        }
                        else
                        {
                        	$data['sucesso'] = false;
                        	$data['mensagem'] = "Para realizar a media ser necessita de no minimo 2 avaliaçoes";
                        }





            }
            else if(isset($_POST['soma_avaliacao_qualificativa']))
            {
            	        $serie = $_POST['cod_serie'];
						$curso = $_POST['cod_curso'];
						$diciplina = $_POST['cod_diciplina'];
						$bimestre = $_POST['bimestre'];
						$ano = $_POST['cod_ano'];
						$valor = $_POST['valor'];
						$nome = $_POST['nome'];
						$cod = $_POST['cod'];
						$i = 0 ;
						$w = 0;
						$alunos = MainMolde::selectAll('matriculados','cod_curso = ? AND cod_serie = ? AND cod_ano = ?',array($curso,$serie,$ano));	
					    $notas = MainMolde::selectAll('avaliacoes','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND bimestre = ? AND cod_professo = ?',array($curso,$serie,$ano,$diciplina,$bimestre,$cod));
					    $notas_aux = MainMolde::selectAll('avaliacoes','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND bimestre = ? AND cod_professo = ? AND nome = ?',array($curso,$serie,$ano,$diciplina,$bimestre,$cod,$nome));
					    $cont_notas = ceil(count($notas_aux));
					    $n = ceil(count($alunos));				   
				     
                        
					    foreach ($notas as $key => $value) 
					    {
					        $aluno[$i] = $value['cod_aluno']; 
					        $notas_aluno[$i] = MainMolde::selectAll('avaliacoes','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND bimestre = ? AND cod_professo = ? AND cod_aluno = ?',array($curso,$serie,$ano,$diciplina,$bimestre,$cod,$aluno[$i]));
					        $aux = ceil(count($notas_aluno[$i]));


					        

					        foreach ($notas_aluno[$i] as $key => $value) 
					        {
                             
					        	@$nota[$w] += $value['valor_obtido'];					        	
					        	@$aluno_nota[$w]  = $value['cod_aluno'];
					        	@$valor_total[$w] += $value['valor'];

					        }

					         $w++;

					        	
					       						   				       
					    }
                        if($aux > 1)
                        {

						    for ($g=0; $g < $cont_notas; $g++) 
						    { 
						    	 
                                 if ($nota[$g] > 100) 
                                 {
                                 	$nota[$g] = 100;
                                 }
                                 if ($valor_total[$g] > 100) 
                                 {
                                 	$valor_total[$g] = 100;
                                 }
						    	 $sql = \MySql::conectar()->prepare("DELETE  FROM `avaliacoes`  WHERE  cod_aluno = ? AND cod_curso = ? AND  cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND bimestre = ? AND cod_professo = ? ");
								 $sql->execute(array($aluno_nota[$g],$curso,$serie,$ano,$diciplina,$bimestre,$cod));
								 $sql = \MySql::conectar()->prepare("INSERT INTO `avaliacoes` VALUES (null,?,?,?,?,?,?,?,?,?,?)");
							     $sql->execute(array($curso,$ano,$serie,$aluno_nota[$g],$diciplina,$valor_total[$g],$nota[$g],$bimestre,$nome,$cod));
							    
						    }
						       

				             $data['mensagem'] = "<script>location.reload();</script>";

                        }
                        else
                        {
                        	$data['sucesso'] = false;
                        	$data['mensagem'] = "Para realizar soma ser necessita de no minimo 2 avaliaçoes";
                        }





            }

            else if (isset($_POST['editar_avaliacao_qualificativa'])) 
            {

            	 $serie = $_POST['cod_serie'];
	             $curso = $_POST['cod_curso'];
	             $diciplina = $_POST['cod_diciplina'];
	             $bimestre = $_POST['bimestre'];
	             $ano = $_POST['cod_ano'];
	             $cod = $_POST['cod'];	         
	             $notas = MainMolde::selectAll('avaliacoes','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND bimestre = ? AND cod_professo = ?',array($curso,$serie,$ano,$diciplina,$bimestre,$cod));
	             $n = ceil(count($notas));

	            
                    foreach ($notas as $key => $value) 
				  	{
	                        @$valorobtido[] = $_POST['valor_obtido'.$value['cod_aluno'].$value['nome']];
	                        $aluno[] = $value['cod_aluno'];
	                        @$valor[] = $value['valor'];
	                        @$nome[]  = $value['nome'];  	    		    
 				  	}

 				  	for ($i=0; $i <$n ; $i++) 
 				  	{ 
 				  			if($valor[$i] > 100)
					  	    {
					  	    	$valor[$i] = 100;
					  	    }
	                        if ($valorobtido[$i]  > $valor[$i]) 
					  	    {
                               $valorobtido[$i] = $valor[$i];
					  	    }				  	  
	 				  		$sql = \MySql::conectar()->prepare("UPDATE `avaliacoes` SET valor_obtido = ? WHERE cod_aluno = ? AND nome = ? AND cod_curso = ? AND  cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND bimestre = ? AND cod_professo = ? ");
						    $sql->execute(array($valorobtido[$i],$aluno[$i],$nome[$i],$curso,$serie,$ano,$diciplina,$bimestre,$cod));
						    $aux = 1;
						
 				  	}

				 						  	

                       $data['mensagem'] = "notas editadas com sucesso";
            	
            }
            else if (isset($_POST['editar_rec'])) 
            {

            	 $serie = $_POST['cod_serie'];
	             $curso = $_POST['cod_curso'];
	             $diciplina = $_POST['cod_diciplina'];
	             $bimestre = $_POST['bimestre'];
	             $ano = $_POST['cod_ano'];
	             $cod = $_POST['cod'];
	             $notas = MainMolde::selectAll('recuperacao','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND bimestre = ? AND cod_professo = ?',array($curso,$serie,$ano,$diciplina,$bimestre,$cod));
	             $n = ceil(count($notas));

	            

				   

                    foreach ($notas as $key => $value) 
				  	{
	                        @$valorobtido[] = $_POST['valor_obtido'.$value['cod_aluno'].$value['nome']];
	                        $aluno[] = $value['cod_aluno'];
	                        @$valor[] = $value['valor'];
	                        @$nome[]  = $value['nome'];                     	                       				 
 				  		    
 				  	}

 				  	for ($i=0; $i <$n ; $i++) 
 				  	{ 
 				  		 
 				  		    if($valor[$i] > 100)
					  	    {
					  	    	$valor[$i] = 100;
					  	    }
	                        if ($valorobtido[$i]  > $valor[$i]) 
					  	    {
                               $valorobtido[$i] = $valor[$i];
					  	    }

	 				  		$sql = \MySql::conectar()->prepare("UPDATE `recuperacao` SET valor_obtido = ? WHERE cod_aluno = ? AND nome = ? AND cod_curso = ? AND  cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND bimestre = ? AND cod_professo = ? ");
						    $sql->execute(array($valorobtido[$i],$aluno[$i],$nome[$i],$curso,$serie,$ano,$diciplina,$bimestre,$cod));
						    $aux = 1;
						
 				  	}

				 						  	

                       $data['mensagem'] = " A ativida foi editada com sucesso";
            	
            }


            else if(isset($_POST['cadastar_avaliacao_qualificativa']))
            {

	             $serie = $_POST['cod_serie'];
	             $curso = $_POST['cod_curso'];
	             $diciplina = $_POST['cod_diciplina'];
	             $bimestre = $_POST['bimestre'];
	             $ano = $_POST['cod_ano'];
	             $valor = strip_tags($_POST['valor']);
	             $nome = strip_tags($_POST['nome']);
	             $cod = $_POST['cod'];
	             $alunos = MainMolde::selectAll('matriculados','cod_curso = ? AND cod_serie = ? AND cod_ano = ?',array($curso,$serie,$ano));
	             $n = ceil(count($alunos));	
	             $verifica = MainMolde::selectAll('avaliacoes','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND nome = ? AND cod_professo = ? AND bimestre = ?',array($curso,$serie,$ano,$diciplina,$nome,$cod,$bimestre));
	             $cont = ceil(count($verifica));
	             $aux = 0;
	             $x = 0;
	            
	                    foreach ($alunos as $key => $value) 
					  	{
	                        @$valorobtido[] = $_POST['valor_obtido'.$value['cod_aluno']];
	                        $aluno[] = $value['cod_aluno'];  	   
					  	}
					  

					  	for($i = 0; $i < $n; $i++)
					  	{  
	                            if ($valor > 100) 
	                            {
	                            	$valor = 100;
	                            }
						  	    if ($valorobtido[$i]  > $valor) 
						  	    {
	                                $valorobtido[$i] = $valor;
						  	    }
						  	    if ($valorobtido[$i]  == "") 
						  	    {
	                                $valorobtido[$i] = 0;
						  	    }

                                if ($cont == 0) 
                                {
                                	$sql = \MySql::conectar()->prepare("INSERT INTO `avaliacoes` VALUES (null,?,?,?,?,?,?,?,?,?,?)");
						            $sql->execute(array($curso,$ano,$serie,$aluno[$i],$diciplina,$valor,$valorobtido[$i],$bimestre,$nome,$cod));
						            $aux = 1;
                                }
                                else
                                {
                                	$data['sucesso'] = false;
					     	        $data['mensagem'] = "Nome da avaliacao invalida, por favor tente com outro nome";
					     	        break;
                                }
					  	     	
					    }

					     if($aux != 0)
					        $data['mensagem'] = "Avaliaçao cadastrada com sucsso"; 
					
            }
            else if(isset($_POST['cadastar_frequencia']))
            {
                     
                     $serie = $_POST['cod_serie'];
					 $curso = $_POST['cod_curso'];
					 $aulas = $_POST['aulas'];
		             $diciplina = $_POST['cod_diciplina'];
		             $ano = $_POST['cod_ano'];
		             $cod = $_POST['cod'];
		             $data_form = $_POST['data'];
		             $conteudo = strip_tags($_POST['aula-conteudo']);
		             $date = DateTime::createFromFormat('d/m/Y', $data_form);
					 $date = $date->format('Y-m-d');
		             $alunos = MainMolde::selectAll('matriculados','cod_curso = ? AND cod_serie = ? AND cod_ano = ?',array($curso,$serie,$ano));
		             $n = ceil(count($alunos));	
		             $falta = MainMolde::selectAll('presenca','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND data = ? AND cod_professo = ? ',array($curso,$serie,$ano,$diciplina,$date,$cod));
		             $verifica_data = ceil(count($falta));	
		             $dataAtual = date('Y-m-d');
		             $bimestre = $_POST['bimestre'];		            
		             $turma = MainMolde::select('diciplina',' nome = ? AND cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_professo = ?',array($diciplina,$curso,$serie,$ano,$cod)); 
		             $carga_horaria = $turma[6];
					 $x = "";
					 $aviso = "";
				     $z = 0;
		                           
                                         

                    foreach ($alunos as $key => $value) 
				  	{

                        @$faltas[] = $_POST['faltas'.$value['cod_aluno']];
                        $aluno[] = $value['cod_aluno']; 

				  	}
				  	
	                    if($verifica_data == 0 && $date <= $dataAtual)
	                    {
						  	for($i = 0; $i < $n; $i++)
						  	{  
						  	

						  		if ($faltas[$i] == "") 
							  	{
							  		$faltas[$i] = 0;
							  	}

								if($faltas[$i] > $aulas)
								{
									$faltas[$i] = $aulas;
									$aviso = "  ( A quantidade de faltas de alguns alunos foram reajustadas )";
								}
		                        $sql = \MySql::conectar()->prepare("INSERT INTO  `presenca` VALUES (null,?,?,?,?,?,?,?,?,?)");
						        $sql->execute(array($aluno[$i],$cod,$curso,$serie,$ano,$diciplina,$faltas[$i],$date,$bimestre));
						        $faltas_alunos = MainMolde::selectAll('presenca','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_professo = ? AND cod_aluno = ? AND cod_materia = ?',array($curso,$serie,$ano,$cod,$aluno[$i],$diciplina)); 
						   
						        foreach ($faltas_alunos as $key => $value) 
						        {
						            @$faltas_aluno[$i] += $value['faltas'];
						                              
						        }
						      
		                        $carga_horaria_base = $carga_horaria * 3600;
		                        $carga_horaria_minima = $carga_horaria_base * 25 /(100);
		                        $falta[$i] = $faltas_aluno[$i] * 3600;
		                        if ($falta[$i] > $carga_horaria_minima) 
		                        {
                                     $alunos = MainMolde::selectAll('matriculados','cod_curso = ? AND cod_serie = ? AND cod_ano = ?',array($curso,$serie,$ano));	
								     $n = ceil(count($alunos));
								     $verifica = MainMolde::selectAll('notas','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND cod_professo = ?',array($curso,$serie,$ano,$diciplina,$cod));
							         $nv = ceil(count($verifica));				        
							         $x = "";
							         $z = 0;
							         $aux2 = "";


							        foreach ($alunos as $key => $value) 
								    {
						                $aluno_verifica[] = $value['cod_aluno']; 			                     
								    }
										  	

							        if($nv == 0 )
								    {   
										for ($y = 0; $y < $n; $y++) 
										{  
											$sql = \MySql::conectar()->prepare("INSERT INTO `notas` VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
											$sql->execute(array($aluno_verifica[$y],$curso,$serie,$ano,$diciplina,$cod,$x,$x,$x,$x,$x,$x,$x,$x,$x,$x,$x,$z,$z,$z,$z,$z,$z,$z,$z));
									    }
						             
					   				}
		                            $sql = \MySql::conectar()->prepare("UPDATE `notas` SET   aprovado = 3 WHERE cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_professo = ? AND cod_aluno = ? AND cod_diciplina = ? ");
								    $sql->execute(array($curso,$serie,$ano,$cod,$aluno[$i],$diciplina));
		                           
		                        }   

						  	}


					        $sql = \MySql::conectar()->prepare("INSERT INTO  `aulas` VALUES (null,?,?,?,?,?,?,?,?)");
							$sql->execute(array($data_form,$cod,$serie,$ano,$curso,$diciplina,$conteudo,$aulas)); 

							if(@$aviso == "")
								$data['mensagem'] = "A frequecia foi cadastrada com sucesso";
							else
						    	$data['mensagem'] = "A frequecia foi cadastrada com sucesso".$aviso;
						   					       
                        }
					    else if($verifica_data != 0)
	                    {
						  	for($u = 0; $u < $n; $u++)
						  	{  
						  	

						  		if ($faltas[$u] == "") 
							  	{
							  		$faltas[$u] = 0;
								}
								  
								if($faltas[$u] > $aulas)
								{
									$faltas[$u] = $aulas;
									$aviso = "  ( A quantidade de faltas de alguns alunos foram reajustadas )";
								}

		                        $sql = \MySql::conectar()->prepare("UPDATE `presenca`  SET faltas = ?,bimestre = ? WHERE cod_professo = ? AND cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND data = ? AND cod_aluno = ?  ");
						        $sql->execute(array($faltas[$u],$bimestre,$cod,$curso,$serie,$ano,$diciplina,$date,$aluno[$u]));
						        $faltas_alunos = MainMolde::selectAll('presenca','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_professo = ? AND cod_aluno = ? AND cod_materia = ?',array($curso,$serie,$ano,$cod,$aluno[$u],$diciplina)); 
						   
						        foreach ($faltas_alunos as $key => $value) 
						        {
						            @$faltas_aluno[$u] += $value['faltas'];
						                              
						        }
						      
		                        $carga_horaria_base = $carga_horaria * 3600;
		                        $carga_horaria_minima = $carga_horaria_base * 25 /(100);
		                        $falta[$u] = $faltas_aluno[$u] * 3600;
		                        $notas = MainMolde::select('notas','cod_aluno = ? AND cod_ano = ? AND cod_curso = ? AND cod_serie = ? AND cod_diciplina = ?',array($aluno[$u],$ano,$curso,$serie,$diciplina));
		                        if ( $falta[$u] > $carga_horaria_minima ) 
		                        {
		                        	@$aprovado[$u] = 3;
		                        }
		                      
		                        else if( ($falta[$u] <= $carga_horaria_minima) && ($notas[14] >= 60) && ( is_numeric( $notas[12] )) )
		                        {
		                        	@$aprovado[$u] = 1;
		                        }
		                        else if( ($falta[$u] <= $carga_horaria_minima ) && ($notas[14] < 60) && ( is_numeric( $notas[12] )) )
		                        {
		                        	@$aprovado[$u] = 4;
		                        }
		                        else if( ( $falta[$u] <= $carga_horaria_minima)  && ( is_null( $notas[12] )) )
		                        {
		                        	@$aprovado[$u] = 0;
		                        }
		                        
		                       

                                     $alunos = MainMolde::selectAll('matriculados','cod_curso = ? AND cod_serie = ? AND cod_ano = ?',array($curso,$serie,$ano));	
								     $n = ceil(count($alunos));
								     $verifica = MainMolde::selectAll('notas','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND cod_professo = ?',array($curso,$serie,$ano,$diciplina,$cod));
							         $nv = ceil(count($verifica));				        
							         $x = "";
							         $z = 0;
							         $aux2 = "";


							        foreach ($alunos as $key => $value) 
								    {
						                $aluno_verifica[] = $value['cod_aluno']; 			                     
								    }
										  	

							        if($nv == 0 )
								    {   
										for ($j = 0; $j < $n; $j++) 
										{  
											$sql = \MySql::conectar()->prepare("INSERT INTO `notas` VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
											$sql->execute(array($aluno_verifica[$j],$curso,$serie,$ano,$diciplina,$cod,$x,$x,$x,$x,$x,$x,$x,$x,$x,$x,$x,$z,$z,$z,$z,$z,$z,$z,$z));
									    }
						             
					   				}
		                            $sql = \MySql::conectar()->prepare("UPDATE `notas` SET   aprovado = ? WHERE cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_professo = ? AND cod_aluno = ? AND cod_diciplina = ? ");
								    $sql->execute(array(@$aprovado[$u],$curso,$serie,$ano,$cod,$aluno[$u],$diciplina));
		                           
		                        }   

						    

                             
						      $sql = \MySql::conectar()->prepare("UPDATE `aulas`  SET conteudo = ?,aulas = ? WHERE cod_professo = ? AND cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND data = ?   ");
						      $sql->execute(array($conteudo,$aulas,$cod,$curso,$serie,$ano,$diciplina,$data_form));
				
							  if(@$aviso == "")
								$data['mensagem'] = "A frequecia foi atualizada com sucesso";
							  else
						    	$data['mensagem'] = "A frequecia foi atualizada com sucesso".$aviso;

						    
						}
						else
						{
							$data['sucesso'] = false;
						    $data['mensagem'] = "data invalida";
						}

               

            } 
            else if(isset($_POST['exluir-atividade-qualificativa']))
            {
                 $serie = $_POST['cod_serie'];
	             $curso = $_POST['cod_curso'];
	             $diciplina = $_POST['cod_diciplina'];
	             $bimestre = $_POST['bimestre'];
	             $ano = $_POST['cod_ano'];
	             $cod = $_POST['cod'];

                 if (@$_POST['atividades']) 
                 {
	                 foreach (@$_POST['atividades'] as $atividade)
					 {
			             $sql = \MySql::conectar()->prepare("DELETE  FROM `avaliacoes` WHERE cod_professo = ? AND cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND nome = ? AND bimestre = ? ");
				         $sql->execute(array($cod,$curso,$serie,$ano,$diciplina,$atividade,$bimestre));
				     }

			        $data['mensagem'] = "<script>location.reload();</script>";
			     }
			     else
			     {  
			        $data['mensagem'] = "<script>location.reload();</script>";			     	
			     }
			     
				
            }
            else if(isset($_POST['altera-atividade-qualificativa']))
            {
                 $serie = $_POST['cod_serie'];
	             $curso = $_POST['cod_curso'];
	             $diciplina = $_POST['cod_diciplina'];
	             $bimestre = $_POST['bimestre'];
	             $ano = $_POST['cod_ano'];
	             $cod = $_POST['cod'];
	             
                 if (@$_POST['atividades']) 
                 {
                 	
	                 foreach (@$_POST['atividades'] as $atividade)
					 {
					 	
					     $valor[] = $_POST['valor-altera'.$atividade];
					 	 $i = 0;
					 	 if ($valor[$i] > 100) 
					     {
					     	 $valor[$i] = 100;
						 }
						 
			             $sql = \MySql::conectar()->prepare("UPDATE  `avaliacoes` SET valor = ? WHERE cod_professo = ? AND cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND nome = ? AND bimestre = ? ");
				         $sql->execute(array($valor[$i],$cod,$curso,$serie,$ano,$diciplina,$atividade,$bimestre));
				         $sql = \MySql::conectar()->prepare("UPDATE  `avaliacoes` SET valor_obtido = ? WHERE cod_professo = ? AND cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND nome = ? AND bimestre = ? AND valor_obtido > ? or valor_obtido = '100' ");
						 $sql->execute(array($valor[$i],$cod,$curso,$serie,$ano,$diciplina,$atividade,$bimestre,$valor[$i]));
				         $i++;
				     }

			         $data['mensagem'] = "<script>location.reload();</script>";
			     }
			     else
			     {  
			        $data['mensagem'] = "<script>location.reload();</script>";			     	
			     }
			     
				
			}
			else if (isset($_POST['cadastro-turmas-confirma']))
			{   
				$serie = $_POST['serie'];
				$curso = $_POST['curso'];
				$ano = $_POST['ano'];
				$padrao = $_POST['grade'];

				if($padrao == "nenhun")
				{
					$materias = MainMolde::selectAll('diciplina','cod_ano = ? AND cod_serie = ? AND cod_curso = ?'
					,array( $ano,$serie,$curso ));
				}
				else 
				{
					$materias =  MainMolde::selectAll('grade_curricular_padroes','grade = ?',array($padrao));
				}

				foreach($materias as $key => $value)
			    {
				
					$data['msn'].= 
					'
					<div class="card-materias">
						<i class="fa fa-book" aria-hidden="true"></i>
						<p >'.$value['nome'] .'</p>
					</div>				
					';
				}

				$data['msn'] .= ' <div class="clear"></div>';
				$data['sucesso'] = true;
					
			}
			else if(isset($_POST['Editar-responssavel']))
			{
				$professo = $_POST['professor'];
				$profatual = $_POST['profatual'];
				$diciplina = $_POST['diciplina'];
				$diciplina = " '$diciplina' ";	
				$curso = $_POST['curso'];
				$ano = $_POST['ano'];
				$serie = $_POST['serie'];


				$sql = \MySql::conectar()->prepare("UPDATE `aulas` SET cod_professo = ?  WHERE cod_professo = $profatual AND cod_materia = $diciplina AND cod_curso = ? AND cod_ano = ? AND cod_serie = ?");
				$sql->execute(array($professo,$curso,$ano,$serie));
				$sql = \MySql::conectar()->prepare("UPDATE `avaliacoes` SET cod_professo = ?  WHERE cod_professo = $profatual AND cod_materia = $diciplina AND cod_curso = ? AND cod_ano = ? AND cod_serie = ?");
				$sql->execute(array($professo,$curso,$ano,$serie));
				$sql = \MySql::conectar()->prepare("UPDATE `diciplina` SET cod_professo = ?  WHERE cod_professo = $profatual AND nome = $diciplina AND cod_curso = ? AND cod_ano = ? AND cod_serie = ? ");
				$sql->execute(array($professo,$curso,$ano,$serie));
				$sql = \MySql::conectar()->prepare("UPDATE `horario` SET cod_professo = ?  WHERE cod_professo = $profatual AND cod_diciplina = $diciplina AND cod_curos = ? AND cod_ano = ? AND cod_serie = ?");
				$sql->execute(array($professo,$curso,$ano,$serie));
				$sql = \MySql::conectar()->prepare("UPDATE `notas` SET cod_professo = ?  WHERE cod_professo = $profatual AND cod_diciplina = $diciplina AND cod_curso = ? AND cod_ano = ? AND cod_serie = ?");
				$sql->execute(array($professo,$curso,$ano,$serie));
				$sql = \MySql::conectar()->prepare("UPDATE `presenca` SET cod_professo = ?  WHERE cod_professo = $profatual AND cod_materia = $diciplina AND cod_curso = ? AND cod_ano = ? AND cod_serie = ?");
				$sql->execute(array($professo,$curso,$ano,$serie));
				$sql = \MySql::conectar()->prepare("UPDATE `recuperacao` SET cod_professo = ?  WHERE cod_professo = $profatual AND cod_diciplina = $diciplina AND cod_curso = ? AND cod_ano = ? AND cod_serie = ?");
				$sql->execute(array($professo,$curso,$ano,$serie));
				
				$data['mensagem'] = "Sucesso";
			}
			else if (isset($_POST['Cadastre-turmas']))
			{
				$ano = strip_tags($_POST['ano']);
			    $serie = strip_tags($_POST['serie']);
			    $curso = strip_tags($_POST['curso']);
				$alunoInvalido = false;
				$padrao = $_POST['grade'];
				$aux =  false;
				
				

			   

	            $verificanotas = MainMolde::selectAll('notas','cod_curso = ? AND cod_serie = ? AND cod_ano = ? ',array($curso,$serie,$ano));
	            $nvn = ceil(count($verificanotas));

	            $verificaturmas = MainMolde::selectAll('turmas','curso = ? AND serie = ? AND ano = ? ',array($curso,$serie,$ano));
	            $nvt = ceil(count($verificaturmas));
			   
				@$materias =  MainMolde::selectAll('diciplina','cod_curso = ? AND cod_serie = ? AND cod_ano = ?',array($curso,$serie,$ano));
				$cont_materias = ceil(count($materias));
				
				if(@$padrao != "nenhun" && $cont_materias == 0 )
				{
					$materias =  MainMolde::selectAll('grade_curricular_padroes','grade = ?',array($padrao));
					foreach($materias as $key => $value)
					{
						$nome = MainMolde::select('materia','id = ?',array($value[1]));
						$sql = \MySql::conectar()->prepare("INSERT INTO `diciplina` VALUES (null,?,?,?,?,?,?)");
						$sql->execute(array($nome[1],$ano,$curso,$serie,606060,$value[2]));
						$aux = true;
						
				    }
				}
              

                @$materias =  MainMolde::selectAll('diciplina','cod_curso = ? AND cod_serie = ? AND cod_ano = ?',array($curso,$serie,$ano));
				$cont_materias = ceil(count($materias));
				
				if($cont_materias != 0)
				{
					 foreach (@$materias as $materia) 
					 {
					 	   											
	                        $verificagrade = MainMolde::selectAll('grade_curricular','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND nome = ?',array($curso,$serie,$ano,$materia[1]));
	                        $nvg = ceil(count($verificagrade));
				                
				             if ($nvg == 0 ) 
				             {
				                  $sql = \MySql::conectar()->prepare("INSERT INTO `grade_curricular` VALUES (null,?,?,?,?,?,?)");
							      $sql->execute(array($materia[1],$serie,$ano,$curso,1,$materia[6]));
				             }   
				              
											
				     }
				}
				    
				if($nvt == 0 && @$_POST['alunos'] != "")
				{
					$sql = \MySql::conectar()->prepare("INSERT INTO `turmas` VALUES (null,?,?,?)");
					$sql->execute(array($ano,$curso,$serie));
				}

				
			  
				   
						
			    if(@$_POST['alunos'])  
				{	
				   foreach (@$_POST['alunos'] as $alunos)
				   {
				          
			                $verifica = MainMolde::selectAll('matriculados','cod_aluno = ? AND cod_ano = ? ',array($alunos,$ano));
				            $nv = ceil(count($verifica));
		
                            if ($nv == 0 && $nvn == 0) 
                            {
	                            	$cod_aluno = \MySql::conectar()->prepare("SELECT * FROM `alunos2` WHERE matricula = ? ");
					                $cod_aluno->execute(array($alunos));
									$cod_aluno = $cod_aluno->fetch();
									$nome = $cod_aluno[1];
						            $sql = \MySql::conectar()->prepare("INSERT INTO `matriculados` VALUES (null,?,?,?,?,?,?)");
									$sql->execute(array($curso,$serie,$ano,$alunos,$nome,0));
									$i = 1;
									$sql2 = \MySql::conectar()->prepare("UPDATE `alunos2` SET matriculado = ?  WHERE matricula = $alunos");
									$sql2->execute(array($i));
														
                            }
                            else 
                            {
                                   $alunoInvalido = true;
                                   break;
                            }
				    } 

				    if ($alunoInvalido == true) 
                	{
						  $data['mensagem'] = "Aluno ja cadastrado!";
						  $data['sucesso'] = false;
                	}
                	else
                	{  
						$data['mensagem'] = " O cadastro  foi feito com sucesso";
						$data['sucesso'] = true;
                	}
                          
                }
				else
				{
					$data['mensagem'] =" Nenhum aluno foi selecionado!";
					$data['sucesso'] = false;
				}

			}
			else if(isset($_POST['busca']))
			{
				$busca = $_POST['busca'];
				$query = " WHERE nome LIKE '%$busca%' ";
				$data = "";
				$alunos = MySql::conectar()->prepare("SELECT * FROM `alunos2` $query");
				$alunos->execute();
				$alunos = $alunos->fetchAll();
                $data.= '<div class="boxes">';
				foreach($alunos as $key => $value)
				{
					$data.= '
						<div class="box-single-wraper">
							<div class="box-single">
							<div class="topo-box">										
								<img src="'.INCLUDE_PATH.'Views/uploads/'.$value['imagem'].' "/>
							</div><!--topo-box-->
							<div class="body-box">
									<p><b><i class="fa fa-pencil"></i> Nome:</b> '.$value['nome'].'</p>
									<input type="checkbox" name="alunos[]" value="'.$value['matricula'].'">
							</div><!--body-box-->
							</div><!--box-single-->
						</div>
					';
									
				}
				$data.= '</div>';
				echo $data;
				die();

				
			}

	die(json_encode($data));
?>