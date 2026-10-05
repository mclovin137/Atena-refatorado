<?php 
	 use \Models\MainMolde;

   
?>

<div class="content">
<section class="box-content center">
<h2><i class="fa fa-pencil"></i> Cadastrar Horarios</h2>
     <form  class="formch" method="post" enctype="multipart/form-data" action="<?php echo INCLUDE_PATH?>Models/ajax/CadastrarHorario.php">
		       

                <section class="top-letest-product-section" >
						<div class="container">
								<div class="section-title">
									<h2>Professor</h2>
								</div><!--section-title-->
					            <div class="product-slider owl-carousel">

									<?php
										$alunos = MainMolde::selectAll('professo2','estado = 1',array());	
										foreach ($alunos as $key => $value) {									
									?>
									<div class="product-item">
										<div class="pi-pic">
										    <img src="<?php echo INCLUDE_PATH?>Views/uploads/<?php echo $value['imagem']; ?>" />
											<div class="pi-links">										
												<!-- 	<a for="check" class="add-card"><i class="fa fa-user-plus"></i><span for="check">adicionar<input type="checkbox" name="alunos[]" value="<php echo $value['matricula'] ?>"></span></a> -->
												<input type="radio" name="professor" value="<?php echo $value['matricula']?>" required>	
											</div><!--pi-links-->
										</div><!--pi-pc-->
										<div class="pi-text">
											<h2 class="text-center"><?php echo $value['nome'] ?></h2>
										</div><!--pi-text-->
									</div><!--product-item-->
								  <?php } ?>
						
						        </div><!--product-slider owl-carousel-->
						 </div><!--container-->
				</section><!--top-letest-product-section-->

                <div class="form-group">
                   <label>Curso:</label>
                   <select name="curso" required>
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
                   <select name="ano" required>
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
                   <select name="serie" required>
						<?php

						    $serie = MainMolde::selectAll('serie','',array());							
					
							foreach ($serie as $key => $value) 
							{
								echo '<option value="'.$value['nome'].'">'.$value['nome'].'</option>';
							}
						?>
					</select>
				 </div><!--form-group-->
                 

				<div class="form-group ">
					<div class="materia">
					   <label>Materia:</label>
	                   <select name="materia"  required>
							
						</select>
				    </div>
			     </div><!--form-group-->

				  <div class="form-group">
				   <label>Datas:</label>
						<input type="checkbox" name="segunda" >Segunda</option>							
						<input type="checkbox" name="terca" >Terca</option>							 
						<input type="checkbox" name="quarta" >Quarta</option>
						<input type="checkbox" name="quinta" >Quinta</option>							 
						<input type="checkbox" name="sexta" >Sexta</option>						
				   </div><!--form-group-->

				  <div class="form-group">
				   <label>Horario:</label>
                   <select name="horario" required>
						<option value="07:00">07:00</option>
						<option value="08:55">08:55</option>
						<option value="09:10">09:10</option>
						<option value="10:05">10:05</option>
				   </select>
				 </div><!--form-group-->
			 
				 <div class="form-group">
			         <input type="submit" name="Cadastre-horario" value="Cadastrar!">
		         </div><!--form-group-->
     </form>
</section><!--box-principal-->

</div><!-- content -- >
