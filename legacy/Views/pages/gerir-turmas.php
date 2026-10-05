<?php 
     use \Models\MainMolde;
	 $matricula = $_SESSION['matricula'];     
?>
<div class="content">
<h2 class="text-center"><i class="fa fa-id-card-o" aria-hidden="true"></i>  Minhas Turmas</h2><br>
<br>		
<div class="filtros text-center">
    <form  class="form" method="post" action="<?php echo INCLUDE_PATH?>Models/ajax/Gestaotumas.php">
    	    
			
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
									<?php

									    $Ano = MainMolde::selectAll('curso','',array());;
																	
										foreach ($Ano as $key => $value) 
										{
											echo '<option value="'.$value['nome'].'">'.$value['nome'].'</option>';
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
                        
				    $diciplina = MainMolde::selectAll('turmas','',array());				
					foreach ($diciplina as $key => $value) {
			?>
                   
                   <ul class="list-group">
					  <li class="list-group-item"><?php echo $value['serie']?> - <?php echo $value['curso']?> - <?php echo $value['ano'] ?> </li>
					  <li class="list-group-item"><a href="<?php echo INCLUDE_PATH_MAIN ?>gestaotumas-box?id=<?php echo $value['id']; ?>"> Ir para o diario</a></li>
				  </ul>

							
            <?php } ?>
</div><!--content-turmas-->
</div><!--content -- >

