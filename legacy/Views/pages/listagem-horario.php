<?php 
     use \Models\MainMolde;  
?>
<div class="content">
<h2 class="text-center"><i class="fa fa-id-card-o" aria-hidden="true"></i> Horario </h2><br>
<br>		
<div class="filtros text-center">
    <form  class="form" method="post" action="<?php echo INCLUDE_PATH?>Models/ajax/Horario.php">
    	    
			

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
								        <option value="todos" selected="selected"></option>

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
                                        <option value="todos" selected="selected"></option>

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
								       <option value="todos" selected="selected">  </option>

							</select>
		     </div><!--group-op-->

		     
		   
    </form>
</div><!--filtros-->

<div class="wraper-table">
<div class="content-turmas ">


</div><!--content-turmas--> 
</div><!--content -- >

