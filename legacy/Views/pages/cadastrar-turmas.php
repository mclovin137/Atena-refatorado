<?php 
        use \Models\MainMolde;
        $query = "";
		$alunos = MySql::conectar()->prepare("SELECT * FROM `alunos2` $query");
		$alunos->execute();
		$alunos = $alunos->fetchAll();


     
?>
<div class="content">
<section class="box-content center">
               
<h2><i class="fa fa-pencil"></i> Matriculados</h2>

	
     <form class="msn-confima" method="post" enctype="multipart/form-data" action="<?php echo INCLUDE_PATH?>Models/ajax/EnviarAlaiacao.php">
		        
                <div class="form-group">
                   <label>Curso:</label>
                   <select name="curso" id = "curso" required >
						<?php

						    $cursos = MainMolde::selectAll('curso','',array());
					
							foreach ($cursos  as $key => $value) 
							{
								echo '<option value="'.$value['nome'].'">'.$value['nome'].'</option>';
							}
						?>
					</select>
				 </div><!--form-group-->

				 <div class="form-group">
				   <label>Ano:</label>
                   <select name="ano" id = "ano" required>
						<?php

						    //$ano = MainMolde::selectAll('ano','',array());
							$ano = \MySql::conectar()->prepare("SELECT * FROM `ano` order by nome desc");
							$ano ->execute(array($nome,$matricula,$senha));
							
							foreach ($ano  as $key => $value) 
							{
								echo '<option value="'.$value['nome'].'">'.$value['nome'].'</option>';
							}
						?>
					</select>
				 </div><!--form-group-->

				 <div class="form-group">
				   <label>Serie:</label>
                   <select name="serie" id ="serie" required>
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
                   <label>Grade curricular:</label>
                   <select name="grade" id = "grade" required >
						<option value="nenhun" >Nenhum</option>
						<?php

							$cursos = MainMolde::selectAll('grade_curricular_padroes','',array());
							$x = "";
							foreach ($cursos as $key => $value) 
							{
                                       					                   
   					            if ($value['grade'] != $x  ) 
						        {
						            $materia[] = $value['grade'];						                	
						        }
						        else
						        {
						            continue;
						        }

						        $x = $value['grade'];					               
						               
						    }
				
							foreach ($materia  as $key => $value) 
							{
								echo '<option value="'.$value.'">'.$value.'</option>';
							}
						  
						?>
					</select>
				 </div><!--form-group-->

				 <div class="pesquisa-form">
				
						<div class="pesquisa-form-icone"><label><i class="fa fa-search"></i> Realizar uma busca por nome...</label></div>
						<input class="input-busca" type="text" name="busca">
						<input type="submit" class=" btn pesquisa-form" name="pesquisa" value="Buscar!">
				
				 </div><!--busca-->

				
                 <section class="top-letest-product-section" >
						<div class="container">
								<div class="section-title">
									<h2>Alunos</h2>
								</div><!--section-title-->
					            <div class="product-slider owl-carousel">
										<?php
										
											foreach ($alunos as $key => $value) {									
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
													<h4 class="text-center"><?php echo $value['nome'] ?></h4>
												</div><!--pi-text-->
											</div><!--product-item-->
									<?php } ?>					
						        </div><!--product-slider owl-carousel-->
						 </div><!--container-->
					</section><!--top-letest-product-section-->
			          
					<!-- <button type="button"  class="btn btn-primary" data-toggle="modal" data-target="#exampleModal">
						Cadastrar
					</button> -->

					<button type="submit" name="cadastro-turmas-confirma" class="btn btn-primary" data-toggle="modal" data-target="#exampleModall">
						Cadastrar
					</button>

						<!-- Modal -->
						<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
						<div class="modal-dialog" role="document">
							<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title" id="exampleModalLabel">Grade Curricular da turma sera</h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="Close">
								<span aria-hidden="true">&times;</span>
								</button>
							</div>
							<div class="modal-body">
								
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
								<button type="submit" class="btn btn-info" name="Cadastre-turmas" value="Cadastrar!">Confirma </button>					
							</div>
							</div>
						</div>
						</div>
     </form>			
	 		
</section><!--box-principal-->
</div><!-- content -- >