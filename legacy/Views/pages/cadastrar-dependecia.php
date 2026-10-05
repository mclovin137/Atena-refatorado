<?php 
     use \Models\MainMolde;
?>
<div class="content">
<section class="box-content center">
<h2><i class="fa fa-pencil"></i> Adicionar Diciplina  de Dependecia</h2>
     <form  method="post" enctype="multipart/form-data">
		       <div class="form-group">
					   <label>Materia:</label>
	                   <select name="nome" required>
							<?php
   
								$materia = MainMolde::selectAll('materia','',array());
														
								foreach ($materia as $key => $value) 
								{
									echo '<option value="'.$value['nome'].'">'.$value['nome']. '</option>';
								}
								
							?>
						</select>
			     </div><!--form-group-->

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
                 

				 <div class="form-group">
				   <label>Carga Horaria:</label>
                   <input type="number" step="4" min="4"  name="carga_horaria" required>
				 </div><!--form-group-->
				 

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
												<input type="radio" name="professor" value="<?php echo $value['matricula']?>" required >	
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

                    <section class="top-letest-product-section" >
						<div class="container">
								<div class="section-title">
									<h2>Alunos</h2>
								</div><!--section-title-->
					            <div class="product-slider owl-carousel">

									<?php
										$alunoss = MainMolde::selectAll('alunos2','dp = 0',array());	
										foreach ($alunoss as $key => $value) {									
									?>
									<div class="product-item">
										<div class="pi-pic">
										    <img src="<?php echo INCLUDE_PATH?>Views/uploads/<?php echo $value['imagem']; ?>" />
											<div class="pi-links">										
												<!-- 	<a for="check" class="add-card"><i class="fa fa-user-plus"></i><span for="check">adicionar<input type="checkbox" name="alunos[]" value="<php echo $value['matricula'] ?>"></span></a> -->
												<input type="checkbox" name="alunos[]" value="<?php echo $value['matricula']?>">	
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
			         <input type="submit" name="Cadastre-diciplina" value="Cadastrar!">
		         </div><!--form-group-->
     </form>
</section><!--box-principal-->
</div><!-- content -- >