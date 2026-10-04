<?php 
     use \Models\MainMolde;
	 $matricula = $_SESSION['matricula'];     
?>
<div class="content">
<h2 class="text-center"><i class="fa fa-id-card-o" aria-hidden="true"></i>  Minhas Turmas</h2><br>
<br>		
<div class="filtros">
    <form  class="form" method="post" action="<?php echo INCLUDE_PATH?>Models/ajax/Pesquisa.php">
    	    
			 <div class="group-op">
						   <label>Materia:</label>
		                   <select name="materia" value="todos">
								<?phP  
									$diciplina = MainMolde::selectAll_ASC('diciplina','cod_professo = ? ','nome',array($matricula));
									$x = "";
																						    							
									foreach ($diciplina as $key => $value) 
									{
                                       
						                   
   					                    if ($value['nome'] != $x  ) 
						                {
						                	 $materia[] = $value['nome'];
						                	
						                }
						                else
						                {
						                	continue;
						                }

						                 $x = $value['nome'];
						               
						               
						            }
									
									foreach ($materia  as $key => $value) 
									{
										echo '<option value="'.$value.'">'.$value.'</option>';
									}
									
								?>
								        <option value="todos" selected="selected">Todos</option>
							</select>
		     </div><!--gronp-op-->

		     <div class="group-op">
						   <label>Ano:</label>
		                   <select name="ano" >
								<?php

								    $Ano = MainMolde::selectAll('ano','',array());;
																
									foreach ($Ano as $key => $value) 
									{
										echo '<option value="'.$value['nome'].'">'.$value['nome'].'</option>';
									}
									
								?>
								        <option value="todos" selected="selected">Todos</option>

							</select>
		     </div><!--group-op-->

		     <div class="group-op">
						   <label>Curso:</label>
		                   <select name="curso" >
									<?phP  
									$diciplina = MainMolde::selectAll_ASC('diciplina','cod_professo = ? ','cod_curso',array($matricula));
									$x = "";
																						    							
									foreach ($diciplina as $key => $value) 
									{
                                       
						                   
   					                    if ($value['cod_curso'] != $x  ) 
						                {
						                	 $curso[] = $value['cod_curso'];
						                	
						                }
						                else
						                {
						                	continue;
						                }

						                 $x = $value['cod_curso'];
						               
						               
						            }
									
									foreach ($curso  as $key => $value) 
									{
										echo '<option value="'.$value.'">'.$value.'</option>';
									}
									
								?>
                                        <option value="todos" selected="selected">Todos</option>

							</select>
		     </div><!--form-group-->

		     <div class="group-op">
						   <label>Serie:</label>
		                   <select name="serie">
								<?php

								    $serie = MainMolde::selectAll('serie','',array());;
									//$i  = ceil(count($serie) + 1);
									//$serie[$i] = ""; 
							
									foreach ($serie as $key => $value) 
									{
									
										echo '<option value="'.$value['nome'].'">'.$value['nome'].'</option>';
										
									}
									
								?>
								       <option value="todos" selected="selected">Todos</option>

							</select>
		     </div><!--group-op-->
		   
    </form>
</div><!--filtros-->
<div class="content-turmas">


			<?php
                        
						    $diciplina = MainMolde::selectAll('diciplina','cod_professo = ?',array($matricula));				
							foreach ($diciplina as $key => $value) {
			?>
                   
                   <ul class="list-group">
					  <li class="list-group-item"><?php echo $value['cod_serie']?> - <?php echo $value['cod_curso']?> - <?php echo $value['nome'] ?>  (<?php echo $value['cod_ano'] ?>)</li>
					  <li class="list-group-item"><a href="<?php echo INCLUDE_PATH_MAIN ?>diario-box?id=<?php echo $value['id']; ?>"> Ir para o diario</a></li>
				  </ul>

							
            <?php } ?>
</div><!--content-turmas-->
</div><!--content -- >

