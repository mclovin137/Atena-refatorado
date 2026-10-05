<?php 
     use \Models\MainMolde;
     $matricula = $_SESSION['matricula'];
     $ano = date('Y',time());
     $materiais = MainMolde::selectAll('materiais','cod_professo = ? AND cod_ano = ?',array($matricula,$ano));
?>
<div class="content">
<section class="box-content center">


<br>
<h2><i class="fa fa-pencil"></i> Adicionar Materiais</h2>
     <form  method="post" enctype="multipart/form-data">
		       <div class="form-group">
					   <label>Materia:</label>
	                   <select name="materia">
							<?php
   
								$materia = MainMolde::selectAll('materia','',array());
														
								foreach ($materia as $key => $value) 
								{
									echo '<option value="'.$value['nome'].'">'.$value['nome'].'</option>';
								}
								
							?>
						</select>
			     </div><!--form-group-->

                <div class="form-group">
                   <label>Curso:</label>
                   <select name="curso">
						<?php

						    $cursos =  MainMolde::selectAll('curso','',array());
							
							foreach ($cursos  as $key => $value) 
							{
								echo '<option value="'.$value['nome'].'">'.$value['nome'].'</option>';
							}
						?>
					</select>
				 </div><!--form-group-->

				
				 <div class="form-group">
				   <label>Ano:</label>
                   <select name="ano">
						<?php

						    $ano = MainMolde::selectAll('ano','',array());
							
							foreach ($ano  as $key => $value) 
							{
								echo '<option value="'.$value['nome'].'">'.$value['nome'].'</option>';
							}
						?>
					</select>
				 </div><!--form-group-->

				 <div class="form-group">
				   <label>Serie:</label>
                   <select name="serie">
						<?php

						    $serie = MainMolde::selectAll('serie','',array());							
					
							foreach ($serie as $key => $value) 
							{
								echo '<option value="'.$value['nome'].'">'.$value['nome'].'</option>';
							}
						?>
					</select>
				 </div><!--form-group-->

				 <div class="form-group">
				   <label>Nome:</label>
                   <input type="text" name="nome" required>
				 </div><!--form-group-->

				  <div class="form-group">
				   <label>Arquivo:</label>
                   <input type="file" name="arquivo" required>
				 </div><!--form-group-->

				 <div>
				 	<input type="hidden" name="matricula" value="<?php echo $matricula ?>">
				 </div>
			 
				 <div class="form-group">
			         <input type="submit" name="upload" value="Cadastrar!">
		         </div><!--form-group-->
     </form><bR>

     <h2><i class="fa fa-pencil"></i> Materiais cadastrado</h2><br>

		<div class="box-tarefas">
				<?php  foreach ($materiais as $key => $value) {	?>
					<div class="box-tarefas-single">
					  <ul>
							  <li class="list-group-item"><?php echo $value['cod_serie']?> - <?php echo $value['cod_curso']?> - <?php echo $value['nome'] ?>  (<?php echo $value['cod_ano'] ?>)</li>
							  <li class="list-group-item"><a href="<?php echo INCLUDE_PATH?>Views/uploads/<?php echo $value['arquivo']; ?>"> Baixar</a></li>

					  </ul>
					</div><!--box-tarefas-sling-->				
		       <?php }?>
		       <div class="clear"></div>
		</div><!--box-tarefas-->

</section><!--box-principal-->
</div><!-- content -- >