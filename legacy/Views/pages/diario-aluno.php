<?php
    use \Models\MainMolde;
	$ano = date('Y',time());
    $curso = MainMolde::select('matriculados','cod_aluno = ? AND cod_ano = ?',array($_SESSION['matricula'],$ano));
	$cursoAluno = $curso[1];
	$ano = $curso[3];
	$serie = $curso[2];
	$materias = MainMolde::selectAll('diciplina','cod_curso = ? AND cod_ano = ? AND cod_serie = ?',array($cursoAluno,$ano,$serie));
	$i = 0;

?>

<div class="content">
<div class="box-content-diario">  
	<div class="avaliacoes">
	         <h2 class="text-center">Avaliaçoes Qualificativa</h2><br>
         <div class="listagem-notas">


	       <div class="accordion" id="accordionExample">
	       <?php foreach ($materias as $key => $value) { 
              $materia = $value['nome'];
	       	?>		  
						<div class="card">
							    <div class="card-header text-center" id="headingOne">
							      <h2 class="mb-0">
							        <button class="btn btn-link collapsed" type="button" data-toggle="collapse" data-target="#collapse<?php echo $i ?>" aria-expanded="false" aria-controls="collapse<?php echo $i ?>">
							          <?php echo  $value['nome'] ?>  (<?php echo $value['carga_horaria'] ?>)
							        </button><!-- <i class="fa fa-angle-down" data-toggle="collapse" data-target="#collapseOne"></i> -->
							      </h2>
							    </div><!--card-header-->

							    <div id="collapse<?php echo $i ?>" class="collapse" aria-labelledby="heading<?php echo $i?>" data-parent="#accordionExample">
							            <div class="card-body">
							            <div class="box-tarefas">	
							              <?php 
							               $atividades = MainMolde::selectAll('avaliacoes','cod_ano = ? AND cod_serie = ? AND cod_curso = ? AND cod_materia = ? AND cod_aluno = ? ',array($ano,$serie,$cursoAluno,$value['nome'],$_SESSION['matricula']));
							                $atividades_rec = MainMolde::selectAll('recuperacao','cod_ano = ? AND cod_serie = ? AND cod_curso = ? AND cod_diciplina = ? AND cod_aluno = ? ',array($ano,$serie,$cursoAluno,$value['nome'],$_SESSION['matricula'])); 
		                                   foreach ($atividades as $key => $value) 
		                                   { ?>
		                                     							
													<div class="box-tarefas-single">
													  <ul>
															  <li class="list-group-item text-center"><?php echo $value['cod_serie']?> - <?php echo $value['cod_curso']?> - <?php echo $value['nome'] ?> </li>
															  <li class="list-group-item text-center"> <?php echo $value['bimestre'] ?></li>
															  <li class="list-group-item text-center"> Valor:  <?php echo $value['valor']?> <Br> Valor Obtido: <?php echo $value['valor_obtido']?> </li>
															
													  </ul>
													</div><!--box-tarefas-sling-->											     
										      					    							                                  
							              <?php }?>
							              <?php foreach ($atividades_rec as $key => $value) 
		                                   { ?>
		                                     							
													<div class="box-tarefas-single">
													  <ul>
															  <li class="list-group-item text-center"><?php echo $value['cod_serie']?> - <?php echo $value['cod_curso']?> - <?php echo $value['nome'] ?> </li>
															  <li class="list-group-item text-center"> <?php echo $value['bimestre'] ?></li>
															  <li class="list-group-item text-center"> Valor:  <?php echo $value['valor']?> <Br> Valor Obtido: <?php echo $value['valor_obtido']?> </li>
															
													  </ul>
													</div><!--box-tarefas-sling-->											     
										      					    							                                  
							              <?php }?>
							              <div class="clear"></div>	

							            
							              <br><button type="button" class="btn btn-primary" ><a href="<?php echo INCLUDE_PATH_MAIN ?>aulas/<?php echo $materia ?>">Ver Aulas<a></button>
							            </div><!--box-tarefas-->							                 
			                            </div><!--card-body-->
								</div><!--collapse-->						       
						</div><!--card-->

			     <?php $i++ ;}?>
			    </div><!--acordion-->
	    </div><!--listagem-notas-->
    </div><!--avaliacoes-->															
    <div><!--box-content-diario-->
</div><!--content -- >