<?php 
     use \Models\MainMolde;  
     $diciplina = MainMolde::selectAll('diciplina','',array());  
?>
<div class="content">
<h2 class="text-center"><i class="fa fa-id-card-o" aria-hidden="true"></i>   Turmas</h2><br>
<br>		
<div class="filtros">
    <form  class="form" method="post" action="<?php echo INCLUDE_PATH?>Models/ajax/PesquisaAdm.php">
    	    
			 <div class="group-op">
						   <label>Materia:</label>
		                   <select name="materia" value="todos">
								<?phP  

									$materia = MainMolde::selectAll('materia','',array());
									$x = "";
																								    							
								
									foreach ($materia  as $key => $value) 
									{
										echo '<option value="'.$value['nome'].'">'.$value['nome'].'</option>';
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
								<?php


								    $curso = MainMolde::selectAll('curso','',array());;
									
									foreach ($curso as $key => $value) 
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

<div class="wraper-table">
<div class="content-turmas ">

			<?php
                        			
							foreach ($diciplina as $key => $value) {
			?>
                   
                   <ul class="list-group">
					  <li class="list-group-item"><?php echo $value['cod_serie']?> - <?php echo $value['cod_curso']?> - <?php echo $value['nome'] ?>  (<?php echo $value['cod_ano'] ?>)</li>
					  <li class="list-group-item"><a href="<?php echo INCLUDE_PATH_MAIN ?>diario-box?id=<?php echo $value['id']; ?>"> Ir para o diario</a></li>
				  </ul>

							
            <?php } ?>

</div><!--content-turmas--> 
</div><!--content -- >

