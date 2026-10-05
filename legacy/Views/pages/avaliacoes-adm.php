<?php
    use \Models\MainMolde;
	if(isset($_GET['id']))
	{
		$id = (int)$_GET['id']; 
		$turma = MainMolde::select('diciplina','id = ?',array($id));
		$diciplina = $turma[1];
		$ano = $turma[2];
		$dataAtual = date('Y-m-d');
		$data = MainMolde::select('datas','',array());
		$curso = $turma[3];	
		$i = 0;
		$serie = $turma[4];
		$professo = $turma[5];
		$cod = $turma[5];     
		$alunos = MainMolde::selectAll('matriculados','cod_curso = ? AND cod_serie = ? AND cod_ano = ? ',array($curso,$serie,$ano));
		$nota_1_bm = MainMolde::selectAll('avaliacoes','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND cod_professo = ?    AND bimestre = "1° bimestre"',array($curso,$serie,$ano,$diciplina,$cod));

		$nota_1_rec = MainMolde::selectAll('recuperacao','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND cod_professo = ?    AND bimestre = "1° bimestre"',array($curso,$serie,$ano,$diciplina,$cod));

		$nota_2_bm = MainMolde::selectAll('avaliacoes','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND cod_professo = ?  AND bimestre = "2° bimestre"',array($curso,$serie,$ano,$diciplina,$cod));

		$nota_2_rec = MainMolde::selectAll('recuperacao','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND cod_professo = ?    AND bimestre = "2° bimestre"',array($curso,$serie,$ano,$diciplina,$cod));

		$nota_3_bm = MainMolde::selectAll('avaliacoes','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND cod_professo = ?  AND bimestre = "3° bimestre"',array($curso,$serie,$ano,$diciplina,$cod));

		$nota_3_rec = MainMolde::selectAll('recuperacao','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND cod_professo = ?    AND bimestre = "3° bimestre"',array($curso,$serie,$ano,$diciplina,$cod));

		$nota_4_bm = MainMolde::selectAll('avaliacoes','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND cod_professo = ?  AND bimestre = "4° bimestre"',array($curso,$serie,$ano,$diciplina,$cod));


		$nota_4_rec = MainMolde::selectAll('recuperacao','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND cod_professo = ?    AND bimestre = "4° bimestre"',array($curso,$serie,$ano,$diciplina,$cod));

		$verificaturmas = MainMolde::selectAll('diciplina', 'nome = ? AND cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_professo = ?',array($diciplina,$curso,$serie,$ano,$cod));
		
		$num_turmas = ceil(count($verificaturmas));
		$coun_notas_1bm = ceil(count($nota_1_bm));
		$rec_1bm = ceil(count($nota_1_rec));
		$coun_notas_2bm = ceil(count($nota_2_bm));
	    $rec_2bm = ceil(count($nota_2_rec));
		$coun_notas_3bm = ceil(count($nota_3_bm));
	    $rec_3bm = ceil(count($nota_3_rec));
		$coun_notas_4bm = ceil(count($nota_4_bm));
	    $rec_4bm = ceil(count($nota_4_rec));
		foreach ($alunos as $key => $value) 
		{ 
	           $notas = MainMolde::select('notas','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_aluno = ? AND cod_diciplina = ? AND cod_professo = ?',array($value['cod_curso'] ,
			   $value['cod_serie'],$value['cod_ano'],$value['cod_aluno'],$diciplina,$professo )); 
			   $i++;
												                           
	          
        }									                

	
	}
	else 
	{
		\Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
	}
	
	$AnoAtual = date('Y');
	$anoo = substr($ano,-4);	
	if($anoo != $AnoAtual)
		\Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
?>

<div class="content">
<div class="box-content-diario">  
	  <div class="avaliacoes">
	         <h2 class="text-center">Avaliaçoes Qualificativa</h2><br>

         
        <div class="listagem-notas">
	        <div class="accordion" id="accordionExample">
				<div class="card">
					    <div class="card-header" id="headingOne">
					      <h2 class="mb-0">
					        <button class="btn btn-link  collapsed" type="button" data-toggle="collapse" data-target="#collapseOne" aria-expanded="false" aria-controls="collapseOne">
					          1° bimestre 
					        </button><i class="fa fa-angle-down" data-toggle="collapse" data-target="#collapseOne"></i>
					      </h2>
					    </div><!--card-header-->

					    <div id="collapseOne" class="collapse" aria-labelledby="headingOne" data-parent="#accordionExample">
					            <div class="card-body">
					               <form class="ajax-bm1" action="<?php echo INCLUDE_PATH?>Models/ajax/EnviarAlaiacao.php" method="post" enctype="multipart/form-data"><br>
					                	 <input type="submit" name="editar_avaliacao_qualificativa" value="Editar"  class="btn btn-primary"
									     <?php if($coun_notas_1bm == 0){ ?> disabled <?php } ?>>
									     <input type="submit" name="media_avaliacao_qualificativa" value="Media"  class="btn btn-primary"
									     <?php if($coun_notas_1bm == 0){ ?> disabled <?php } ?>>
									     <input type="submit" name="soma_avaliacao_qualificativa" value="Soma"  class="btn btn-primary"
									     <?php if($coun_notas_1bm == 0){ ?> disabled <?php } ?>>
										 <input type="submit" name="lanca_avaliacao_qualificativa" actionBtn="enviar-boletim" value="Enviar"  class="btn btn-primary"
										 <?php if ($coun_notas_1bm == 0) { ?>  disabled  <?php } ?>>
										 <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalScrollable" <?php if ($coun_notas_1bm == 0) { ?>  disabled  <?php } ?>>Ver Avaliaçoes</button>
										 <button type="button" class="btn btn-primary" id="btn-cadastra-notas">Cadastra</button>
											<!-- Modal -->
											<div class="modal fade " id="exampleModalScrollable" tabindex="-1" role="dialog" aria-labelledby="exampleModalScrollableTitle" aria-hidden="true">
											  <div class="modal-dialog modal-dialog-scrollable modal-lg" role="document">
											    <div class="modal-content">
											      <div class="modal-header">
											        <h5 class="modal-title" id="exampleModalScrollableTitle">Avaliaçoes Cadastradas</h5>
											        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
											          <span aria-hidden="true">&times;</span>
											        </button>
											      </div>
											      <div class="modal-body ">
											      	<ul>
												      	<?php $avaliacoes_1_bm = MainMolde::selectAll('avaliacoes','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND cod_professo = ? AND bimestre = "1° bimestre"',array($curso,$serie,$ano,$diciplina,$cod));
												      	    $aux ="";										      	  
	                                                        foreach ($avaliacoes_1_bm as $key => $value) 
	                                                        { ?>   

		                                                         <?php if($aux != $value['nome']) 
		                                                         { ?>
		                                                          
																    <li class="list-group-item-avaliacoes"> 
																      <input type="radio" name="atividades[]" value="<?php echo $value['nome']?>">
																      <span class="txt-alera-notas">Nota:</span class="nome-avaliacao"><?php echo $value['nome']?></span>                                                 
																      <span class="txt-alera-notas">Valor:</span>
																      <input class="mudar-nota-input" type="text" name="valor-altera<?php echo $value['nome']?>" value="<?php echo $value['valor']?> " > 
																      <input type="hidden" name="cod" value="<?php echo $cod ?>">
																	  <input type="hidden" name="cod_serie" value="<?php echo $value['cod_serie']?>">
																	  <input type="hidden" name="cod_curso" value="<?php echo $value['cod_curso']?>">
																	  <input type="hidden" name="cod_ano"   value="<?php echo $value['cod_ano']?>">
																	  <input type="hidden" name="bimestre"  value="1° bimestre">
																	  <input type="hidden" name="cod_diciplina" value="<?php echo $diciplina ?>" >
																	  <input type="hidden" name="valor" value="<?php echo $value['valor'] ?>" >	
																	</li>

		                                                         <?php } ?>  

												        <?php $aux = $value['nome']; }?>
											        </ul>
											      </div>
											      <div class="modal-footer">
											        <button type="button" class="btn btn-secondary" data-dismiss="modal">Fechar</button>
											      
												    <input type="submit" actionBtn="exluir-atividade-qualificativa"  name="exluir-atividade-qualificativa" class="btn-delete btn" value="Excluir">
												    <input type="submit" name="altera-atividade-qualificativa" class="btn editar_avaliacao_qualificativa" value="Salva">	 											       
											      </div>
											    </div>
											  </div>
											</div>
											<!-- Modal -->

								         <div class="wraper-table">					
											<table>

												<tr>													
												    <td class="coluna-tabela text-center">Avaliaçao</td>
												    <td class="coluna-tabela text-center">Materia</td>
												    <td class="coluna-tabela text-center">Aluno</td>
													<td class="coluna-tabela text-center">Valor</td>
													<td class="coluna-tabela text-center">Valor obtidor</td>								
												</tr>

												<?php foreach ($nota_1_bm as $key => $value) { ?>
												  
												<tr>												   
												    <td class="coluna-tabela text-center"><?php echo $value['nome']?></td>
												    <td class="coluna-tabela text-center"><?php echo $diciplina ?></td>
												    <td class="coluna-tabela text-center"> <?php $matricula = $value['cod_aluno']; $aluno = MainMolde::select('alunos2','matricula = ? ',array($matricula)); print_r($aluno[1]) ?></td>
													<td class="coluna-tabela text-center"><?php echo $value['valor']?></td>
													<td class="coluna-tabela text-center"><input type="txt"  class="nota" name="valor_obtido<?php echo $value['cod_aluno'],$value['nome']?>" value="<?php echo $value['valor_obtido']?>"></td>
													<input type="hidden" name="cod" value="<?php echo $cod ?>">
													<input type="hidden" name="nome" value="<?php echo $value['nome']?>"> 
													<input type="hidden" name="cod_serie" value="<?php echo $value['cod_serie']?>">
													<input type="hidden" name="cod_curso" value="<?php echo $value['cod_curso']?>">
													<input type="hidden" name="cod_ano"   value="<?php echo $value['cod_ano']?>">
													<input type="hidden" name="bimestre"  value="1° bimestre">
													<input type="hidden" name="cod_diciplina" value="<?php echo $diciplina ?>" >
													<input type="hidden" name="valor" value="<?php echo $value['valor'] ?>" >										
												</tr>

											   <?php } ?>

										    </table>	
									     </div><!--wraper-table-->
									     <input type="submit" name="editar_avaliacao_qualificativa" value="Editar"  class="btn btn-primary"
									     <?php if($coun_notas_1bm == 0){ ?> disabled <?php } ?>>
									     <input type="submit" name="media_avaliacao_qualificativa" value="Media"  class="btn btn-primary"
									     <?php if($coun_notas_1bm == 0){ ?> disabled <?php } ?>>
									     <input type="submit" name="soma_avaliacao_qualificativa" value="Soma"  class="btn btn-primary"
									     <?php if($coun_notas_1bm == 0){ ?> disabled <?php } ?>>
										 <input type="submit" name="lanca_avaliacao_qualificativa" actionBtn="enviar-boletim" value="Enviar"  class="btn btn-primary"
										 <?php if ($coun_notas_1bm == 0) { ?>  disabled  <?php } ?>>
										 <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalScrollable" <?php if ($coun_notas_1bm == 0) { ?>  disabled  <?php } ?>>Ver Avaliaçoes</button>
										 <button type="button" class="btn btn-primary" id="btn-cadastra-notas">Cadastra</button>
										
								    </form>
						        </div><!--card-body-->
						</div><!--collpse-->
	            </div><!--card-->
                <?php if ($rec_1bm != 0) { ?>               
	            <div class="card">
					    <div class="card-header" id="headingOne">
					      <h2 class="mb-0">
					        <button class="btn btn-link  collapsed" type="button" data-toggle="collapse" data-target="#collapseFive" aria-expanded="false" aria-controls="collapseOne">
					          rec ( 1°bm )
					        </button><i class="fa fa-angle-down" data-toggle="collapse" data-target="#collapseFive"></i>
					      </h2>
					    </div><!--card-header-->

					    <div id="collapseFive" class="collapse" aria-labelledby="headingOne" data-parent="#accordionExample">
					            <div class="card-body">
					                <form class="ajax-rec1" action="<?php echo INCLUDE_PATH?>Models/ajax/EnviarAlaiacao.php" method="post" enctype="multipart/form-data"><br>
					                	 <input type="submit" name="editar_rec" value="Salva"  class="btn btn-primary"								   
										 <?php if($rec_1bm == 0){ ?> disabled <?php } ?>>
										 <input type="submit" name="lanca_rec" actionBtn="enviar-boletim" value="Enviar"  class="btn btn-primary"
										 <?php if ($rec_1bm == 0) { ?>  disabled  <?php } ?>>
										 
								         <div class="wraper-table">					
											<table>

												<tr>													
												    <td class="coluna-tabela text-center">Avaliaçao</td>
												    <td class="coluna-tabela text-center">Materia</td>
												    <td class="coluna-tabela text-center">Aluno</td>
													<td class="coluna-tabela text-center">Valor</td>
													<td class="coluna-tabela text-center">Valor obtidor</td>								
												</tr>

												<?php foreach ($nota_1_rec as $key => $value) { ?>
												  
												<tr>												   
												    <td class="coluna-tabela text-center"><?php echo $value['nome']?></td>
												    <td class="coluna-tabela text-center"><?php echo $diciplina ?></td>
												    <td class="coluna-tabela text-center"> <?php $matricula = $value['cod_aluno']; $aluno = MainMolde::select('alunos2','matricula = ? ',array($matricula)); print_r($aluno[1]) ?></td>
													<td class="coluna-tabela text-center"><?php echo $value['valor']?></td>
													<td class="coluna-tabela text-center"><input type="txt"  class="nota" name="valor_obtido<?php echo $value['cod_aluno'],$value['nome']?>" value="<?php echo $value['valor_obtido']?>"></td>
													<input type="hidden" name="cod" value="<?php echo $cod ?>">
													<input type="hidden" name="nome" value="<?php echo $value['nome']?>"> 
													<input type="hidden" name="cod_serie" value="<?php echo $value['cod_serie']?>">
													<input type="hidden" name="cod_curso" value="<?php echo $value['cod_curso']?>">
													<input type="hidden" name="cod_ano"   value="<?php echo $value['cod_ano']?>">
													<input type="hidden" name="bimestre"  value="1° bimestre">
													<input type="hidden" name="cod_diciplina" value="<?php echo $diciplina ?>" >
													<input type="hidden" name="valor" value="<?php echo $value['valor'] ?>" >										
												</tr>

											   <?php } ?>

										    </table>	
									     </div><!--wraper-table-->
									     <input type="submit" name="editar_rec" value="Salva"  class="btn btn-primary"						    
										 <?php if($coun_notas_1bm == 0){ ?> disabled <?php } ?>>
										 <input type="submit" name="lanca_rec" actionBtn="enviar-boletim" value="Enviar"  class="btn btn-primary"
										 <?php if ($rec_1bm == 0) { ?>  disabled  <?php } ?>>
									
								    </form>
						        </div><!--card-body-->
						</div><!--collpse-->
	            </div><!--card-->
	        <?php } ?>
	               
	               <div class="card">
					    <div class="card-header" id="headingTwo">
					      <h2 class="mb-0">
					        <button class="btn btn-link collapsed" type="button" data-toggle="collapse" data-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
					          2° bimestre 
					        </button><i class="fa fa-angle-down" data-toggle="collapse" data-target="#collapseTwo"></i>
					      </h2>
					    </div>
					    <div id="collapseTwo" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionExample">
					        <div class="card-body">
						         <form class="ajax-bm2"  action="<?php echo INCLUDE_PATH?>Models/ajax/EnviarAlaiacao.php" method="post" enctype="multipart/form-data"><br>
						         	     <input type="submit" name="editar_avaliacao_qualificativa" value="Editar"  class="btn btn-primary"
									     <?php if($coun_notas_2bm == 0){ ?> disabled <?php } ?>>
									     <input type="submit" name="lanca_avaliacao_qualificativa" actionBtn="enviar-boletim" value="Enviar"  class="btn btn-primary" <?php if ($coun_notas_2bm == 0) { ?>  disabled  <?php } ?>>
									     <input type="submit" name="media_avaliacao_qualificativa" value="Media"  class="btn btn-primary"
									     <?php if($coun_notas_2bm == 0){ ?> disabled <?php } ?>>
									     <input type="submit" name="soma_avaliacao_qualificativa" value="Soma"  class="btn btn-primary"
									     <?php if($coun_notas_2bm == 0){ ?> disabled <?php } ?>>
									     <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalScrollable1"<?php if($coun_notas_2bm == 0){ ?> disabled <?php } ?>>Ver Avaliaçoes</button>
									     <button type="button" class="btn btn-primary" id="btn-cadastra-notass">Cadastra</button>


											<!-- Modal -->
											<div class="modal fade " id="exampleModalScrollable1" tabindex="-1" role="dialog" aria-labelledby="exampleModalScrollableTitle" aria-hidden="true">
											  <div class="modal-dialog modal-dialog-scrollable modal-lg" role="document">
											    <div class="modal-content">
											      <div class="modal-header">
											        <h5 class="modal-title" id="exampleModalScrollableTitle">Avaliaçoes Cadastradas</h5>
											        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
											          <span aria-hidden="true">&times;</span>
											        </button>
											      </div>
											      <div class="modal-body ">
											      	<ul>
												      	<?php $avaliacoes_2_bm = MainMolde::selectAll('avaliacoes','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND cod_professo = ? AND bimestre = "2° bimestre"',array($curso,$serie,$ano,$diciplina,$cod));
												      	    $aux ="";										      	  
	                                                        foreach ($avaliacoes_2_bm as $key => $value) 
	                                                        { ?>   

		                                                         <?php if($aux != $value['nome']) 
		                                                         { ?>
		                                                          
																    <li class="list-group-item-avaliacoes"> 
																      <input type="radio" name="atividades[]" value="<?php echo $value['nome']?>">
																      <span class="txt-alera-notas">Nota:</span class="nome-avaliacao"><?php echo $value['nome']?></span>                                                 
																      <span class="txt-alera-notas">Valor:</span>
																      <input class="mudar-nota-input" type="text" name="valor-altera<?php echo $value['nome']?>" value="<?php echo $value['valor']?> " > 
																      <input type="hidden" name="cod" value="<?php echo $cod ?>">
																	  <input type="hidden" name="cod_serie" value="<?php echo $value['cod_serie']?>">
																	  <input type="hidden" name="cod_curso" value="<?php echo $value['cod_curso']?>">
																	  <input type="hidden" name="cod_ano"   value="<?php echo $value['cod_ano']?>">
																	  <input type="hidden" name="bimestre"  value="2° bimestre">
																	  <input type="hidden" name="cod_diciplina" value="<?php echo $diciplina ?>" >
																	  <input type="hidden" name="valor" value="<?php echo $value['valor'] ?>" >	
																	</li>

		                                                         <?php } ?>  

												        <?php $aux = $value['nome']; }?>
											        </ul>
											      </div>
											      <div class="modal-footer">
											        <button type="button" class="btn btn-secondary" data-dismiss="modal">Fechar</button>
											      
												    <input type="submit" actionBtn="exluir-atividade-qualificativa"  name="exluir-atividade-qualificativa" class="btn-delete btn" value="Excluir">
												    <input type="submit" name="altera-atividade-qualificativa" class="btn editar_avaliacao_qualificativa" value="Salva">	 											       
											      </div>
											    </div>
											  </div>
											</div>
											<!-- Modal -->

								         <div class="wraper-table">					
											<table>

												<tr>												
												    <td class="coluna-tabela text-center">Avaliaçao</td>
												    <td class="coluna-tabela text-center">Materia</td>
												    <td class="coluna-tabela text-center">Aluno</td>
													<td class="coluna-tabela text-center">Valor</td>
													<td class="coluna-tabela text-center">Valor obtidor</td>								
												</tr>

												<?php foreach ($nota_2_bm as $key => $value) { ?>
												  
												<tr>												   
												    <td class="coluna-tabela text-center"><?php echo $value['nome']?></td>
												    <td class="coluna-tabela text-center"><?php echo $diciplina ?></td>
												    <td class="coluna-tabela text-center"> <?php $matricula = $value['cod_aluno']; $aluno = MainMolde::select('alunos2','matricula = ? ',array($matricula)); print_r($aluno[1]) ?></td>
													<td class="coluna-tabela text-center"><?php echo $value['valor']?></td>
													<td class="coluna-tabela text-center"><input type="txt"  class="nota" name="valor_obtido<?php echo $value['cod_aluno'],$value['nome']?>" value="<?php echo $value['valor_obtido']?>"></td>
													<input type="hidden" name="cod" value="<?php echo $cod ?>">
													<input type="hidden" name="nome" value="<?php echo $value['nome']?>"> 
													<input type="hidden" name="cod_serie" value="<?php echo $value['cod_serie']?>">
													<input type="hidden" name="cod_curso" value="<?php echo $value['cod_curso']?>">
													<input type="hidden" name="cod_ano"   value="<?php echo $value['cod_ano']?>">
													<input type="hidden" name="bimestre"  value="2° bimestre">
													<input type="hidden" name="cod_diciplina" value="<?php echo $diciplina ?>" >
													<input type="hidden" name="valor" value="<?php echo $value['valor'] ?>" >										
												</tr>

											   <?php } ?>

										    </table>	
									     </div><!--wraper-table-->
									     <input type="submit" name="editar_avaliacao_qualificativa" value="Editar"  class="btn btn-primary"
									     <?php if($coun_notas_2bm == 0){ ?> disabled <?php } ?>>
									     <input type="submit" name="lanca_avaliacao_qualificativa" actionBtn="enviar-boletim" value="Enviar"  class="btn btn-primary"
									     <?php if ($coun_notas_2bm == 0) { ?>  disabled  <?php } ?>>
									     <input type="submit" name="media_avaliacao_qualificativa" value="Media"  class="btn btn-primary"
									     <?php if($coun_notas_2bm == 0){ ?> disabled <?php } ?>>
									     <input type="submit" name="soma_avaliacao_qualificativa" value="Soma"  class="btn btn-primary"
									     <?php if($coun_notas_2bm == 0){ ?> disabled <?php } ?>>
									     <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalScrollable1"<?php if($coun_notas_2bm == 0){ ?> disabled <?php } ?>>Ver Avaliaçoes</button>
									     <button type="button" class="btn btn-primary" id="btn-cadastra-notass">Cadastra</button>
									    
								    </form>
						    </div><!--card-body-->
						</div><!--collpse-->
	                </div><!--card-->

	                <?php if ($rec_2bm != 0) { ?>               
				            <div class="card">
								    <div class="card-header" id="headingOne">
								      <h2 class="mb-0">
								        <button class="btn btn-link  collapsed" type="button" data-toggle="collapse" data-target="#collapseSix" aria-expanded="false" aria-controls="collapseOne">
								          rec ( 2°bm )
								        </button><i class="fa fa-angle-down" data-toggle="collapse" data-target="#collapseSix"></i>
								      </h2>
								    </div><!--card-header-->

								    <div id="collapseSix" class="collapse" aria-labelledby="headingOne" data-parent="#accordionExample">
								            <div class="card-body">
								                <form class="ajax-rec2" action="<?php echo INCLUDE_PATH?>Models/ajax/EnviarAlaiacao.php" method="post" enctype="multipart/form-data"><br>
								                	 <input type="submit" name="editar_rec" value="Salva"  class="btn btn-primary"								   
													 <?php if($rec_2bm == 0){ ?> disabled <?php } ?>>
													 <input type="submit" name="lanca_rec" actionBtn="enviar-boletim" value="Enviar"  class="btn btn-primary"
													 <?php if ($rec_2bm == 0) { ?>  disabled  <?php } ?>>
													 
											         <div class="wraper-table">					
														<table>

															<tr>																
															    <td class="coluna-tabela text-center">Avaliaçao</td>
															    <td class="coluna-tabela text-center">Materia</td>
															    <td class="coluna-tabela text-center">Aluno</td>
																<td class="coluna-tabela text-center">Valor</td>
																<td class="coluna-tabela text-center">Valor obtidor</td>								
															</tr>

															<?php foreach ($nota_2_rec as $key => $value) { ?>
															  
															<tr>															   
															    <td class="coluna-tabela text-center"><?php echo $value['nome']?></td>
															    <td class="coluna-tabela text-center"><?php echo $diciplina ?></td>
															    <td class="coluna-tabela text-center"> <?php $matricula = $value['cod_aluno']; $aluno = MainMolde::select('alunos2','matricula = ? ',array($matricula)); print_r($aluno[1]) ?></td>
																<td class="coluna-tabela text-center"><?php echo $value['valor']?></td>
																<td class="coluna-tabela text-center"><input type="txt"  class="nota" name="valor_obtido<?php echo $value['cod_aluno'],$value['nome']?>" value="<?php echo $value['valor_obtido']?>"></td>
																<input type="hidden" name="cod" value="<?php echo $cod ?>">
																<input type="hidden" name="nome" value="<?php echo $value['nome']?>"> 
																<input type="hidden" name="cod_serie" value="<?php echo $value['cod_serie']?>">
																<input type="hidden" name="cod_curso" value="<?php echo $value['cod_curso']?>">
																<input type="hidden" name="cod_ano"   value="<?php echo $value['cod_ano']?>">
																<input type="hidden" name="bimestre"  value="2° bimestre">
																<input type="hidden" name="cod_diciplina" value="<?php echo $diciplina ?>" >
																<input type="hidden" name="valor" value="<?php echo $value['valor'] ?>" >										
															</tr>

														   <?php } ?>

													    </table>	
												     </div><!--wraper-table-->
												     <input type="submit" name="editar_rec" value="Salva"  class="btn btn-primary"						    
													 <?php if($rec_2bm == 0){ ?> disabled <?php } ?>>
													 <input type="submit" name="lanca_rec" actionBtn="enviar-boletim" value="Enviar"  class="btn btn-primary"
													 <?php if ($rec_2bm == 0) { ?>  disabled  <?php } ?>>
												
											    </form>
									        </div><!--card-body-->
									</div><!--collpse-->
				            </div><!--card-->
				        <?php } ?>

				    <div class="card">
					    <div class="card-header" id="headingThree">
					      <h2 class="mb-0">
					        <button class="btn btn-link collapsed" type="button" data-toggle="collapse" data-target="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
					           3° bimestre 
					        </button><i class="fa fa-angle-down" data-toggle="collapse" data-target="#collapseThree" ></i>
						      </h2>
						    </div><!--card-hrader-->
						    <div id="collapseThree" class="collapse" aria-labelledby="headingThree" data-parent="#accordionExample">
						        <div class="card-body">
						            <form class="ajax-bm3" action="<?php echo INCLUDE_PATH?>Models/ajax/EnviarAlaiacao.php" method="post" enctype="multipart/form-data"><br>
						            	 <input type="submit" name="editar_avaliacao_qualificativa" value="Editar" class="btn btn-primary"
									     <?php if($coun_notas_3bm == 0){ ?> disabled <?php } ?>>
									     <input type="submit" name="lanca_avaliacao_qualificativa" actionBtn="enviar-boletim" value="Enviar"  class="btn btn-primary"
									     <?php if ($coun_notas_3bm == 0) { ?>  disabled  <?php } ?>>
									     <input type="submit" name="media_avaliacao_qualificativa" value="Media"  class="btn btn-primary"
									     <?php if($coun_notas_3bm == 0){ ?> disabled <?php } ?>>
									     <input type="submit" name="soma_avaliacao_qualificativa" value="Soma"  class="btn btn-primary"
									     <?php if($coun_notas_3bm == 0){ ?> disabled <?php } ?>>
									     <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalScrollable2"<?php if($coun_notas_3bm == 0){ ?> disabled <?php } ?> >Ver Avaliaçoes</button>
									     <button type="button" class="btn btn-primary" id="btn-cadastra-notasss3">Cadastra</button>


                                         <!-- Modal -->
											<div class="modal fade " id="exampleModalScrollable2" tabindex="-1" role="dialog" aria-labelledby="exampleModalScrollableTitle" aria-hidden="true">
											  <div class="modal-dialog modal-dialog-scrollable modal-lg" role="document">
											    <div class="modal-content">
											      <div class="modal-header">
											        <h5 class="modal-title" id="exampleModalScrollableTitle">Avaliaçoes Cadastradas</h5>
											        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
											          <span aria-hidden="true">&times;</span>
											        </button>
											      </div>
											      <div class="modal-body ">
											      	<ul>
												      	<?php $avaliacoes_3_bm = MainMolde::selectAll('avaliacoes','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND cod_professo = ? AND bimestre = "3° bimestre"',array($curso,$serie,$ano,$diciplina,$cod));
												      	    $aux ="";										      	  
	                                                        foreach ($avaliacoes_3_bm as $key => $value) 
	                                                        { ?>   

		                                                         <?php if($aux != $value['nome']) 
		                                                         { ?>
		                                                          
																    <li class="list-group-item-avaliacoes"> 
																      <input type="radio" name="atividades[]" value="<?php echo $value['nome']?>">
																      <span class="txt-alera-notas">Nota:</span class="nome-avaliacao"><?php echo $value['nome']?></span>                                                 
																      <span class="txt-alera-notas">Valor:</span>
																      <input class="mudar-nota-input" type="text" name="valor-altera<?php echo $value['nome']?>" value="<?php echo $value['valor']?> " > 
																      <input type="hidden" name="cod" value="<?php echo $cod ?>">
																	  <input type="hidden" name="cod_serie" value="<?php echo $value['cod_serie']?>">
																	  <input type="hidden" name="cod_curso" value="<?php echo $value['cod_curso']?>">
																	  <input type="hidden" name="cod_ano"   value="<?php echo $value['cod_ano']?>">
																	  <input type="hidden" name="bimestre"  value="2° bimestre">
																	  <input type="hidden" name="cod_diciplina" value="<?php echo $diciplina ?>" >
																	  <input type="hidden" name="valor" value="<?php echo $value['valor'] ?>" >	
																	</li>

		                                                         <?php } ?>  

												        <?php $aux = $value['nome']; }?>
											        </ul>
											      </div>
											      <div class="modal-footer">
											        <button type="button" class="btn btn-secondary" data-dismiss="modal">Fechar</button>
											      
												    <input type="submit" actionBtn="exluir-atividade-qualificativa"  name="exluir-atividade-qualificativa" class="btn-delete btn" value="Excluir">
												    <input type="submit" name="altera-atividade-qualificativa" class="btn editar_avaliacao_qualificativa" value="Salva">	 											       
											      </div>
											    </div>
											  </div>
											</div>
											<!-- Modal -->

								         <div class="wraper-table">					
											<table>

												<tr>											
												    <td class="coluna-tabela text-center">Avaliaçao</td>
												    <td class="coluna-tabela text-center">Materia</td>
												    <td class="coluna-tabela text-center">Aluno</td>
													<td class="coluna-tabela text-center">Valor</td>
													<td class="coluna-tabela text-center">Valor obtidor</td>								
												</tr>

												<?php foreach ($nota_3_bm as $key => $value) { ?>
												  
												<tr>												    
												    <td class="coluna-tabela text-center"><?php echo $value['nome']?></td>
												    <td class="coluna-tabela text-center"><?php echo $diciplina ?></td>
												    <td class="coluna-tabela text-center"> <?php $matricula = $value['cod_aluno']; $aluno = MainMolde::select('alunos2','matricula = ? ',array($matricula)); print_r($aluno[1]) ?></td>
													<td class="coluna-tabela text-center"><?php echo $value['valor']?></td>
													<td class="coluna-tabela text-center"><input type="txt"  class="nota" name="valor_obtido<?php echo $value['cod_aluno'],$value['nome']?>" value="<?php echo $value['valor_obtido']?>"></td>
													<input type="hidden" name="cod" value="<?php echo $cod ?>">
													<input type="hidden" name="nome" value="<?php echo $value['nome']?>"> 
													<input type="hidden" name="cod_serie" value="<?php echo $value['cod_serie']?>">
													<input type="hidden" name="cod_curso" value="<?php echo $value['cod_curso']?>">
													<input type="hidden" name="cod_ano"   value="<?php echo $value['cod_ano']?>">
													<input type="hidden" name="bimestre"  value="3° bimestre">
													<input type="hidden" name="cod_diciplina" value="<?php echo $diciplina ?>" >
													<input type="hidden" name="valor" value="<?php echo $value['valor'] ?>" >										
												</tr>

											   <?php } ?>

										    </table>	
									     </div><!--wraper-table-->
									     <input type="submit" name="editar_avaliacao_qualificativa" value="Editar" class="btn btn-primary"
									     <?php if($coun_notas_3bm == 0){ ?> disabled <?php } ?>>
									     <input type="submit" name="lanca_avaliacao_qualificativa" actionBtn="enviar-boletim" value="Enviar"  class="btn btn-primary"
									     <?php if ($coun_notas_3bm == 0) { ?>  disabled  <?php } ?>>
									     <input type="submit" name="media_avaliacao_qualificativa" value="Media"  class="btn btn-primary"
									     <?php if($coun_notas_3bm == 0){ ?> disabled <?php } ?>>
									     <input type="submit" name="soma_avaliacao_qualificativa" value="Soma"  class="btn btn-primary"
									     <?php if($coun_notas_3bm == 0){ ?> disabled <?php } ?>>								  
									     <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalScrollable2"<?php if($coun_notas_3bm == 0){ ?> disabled <?php } ?> >Ver Avaliaçoes</button>
									        <button type="button" class="btn btn-primary" id="btn-cadastra-notasss3">Cadastra</button>

								    </form>
						        </div><!--card-body-->
						    </div><!--collpse-->
	                </div><!--card-->

	                <?php if ($rec_3bm != 0) { ?>               
				            <div class="card">
								    <div class="card-header" id="headingOne">
								      <h2 class="mb-0">
								        <button class="btn btn-link  collapsed" type="button" data-toggle="collapse" data-target="#collapseSeven" aria-expanded="false" aria-controls="collapseOne">
								          rec ( 3°bm )
								        </button><i class="fa fa-angle-down" data-toggle="collapse" data-target="#collapseSeven"></i>
								      </h2>
								    </div><!--card-header-->

								    <div id="collapseSeven" class="collapse" aria-labelledby="headingOne" data-parent="#accordionExample">
								            <div class="card-body">
								                <form class="ajax-rec3" action="<?php echo INCLUDE_PATH?>Models/ajax/EnviarAlaiacao.php" method="post" enctype="multipart/form-data"><br>
								                	 <input type="submit" name="editar_rec" value="Salva"  class="btn btn-primary"								   
													 <?php if($rec_3bm == 0){ ?> disabled <?php } ?>>
													 <input type="submit" name="lanca_rec" actionBtn="enviar-boletim" value="Enviar"  class="btn btn-primary"
													 <?php if ($rec_3bm == 0) { ?>  disabled  <?php } ?>>
													 
											         <div class="wraper-table">					
														<table>

															<tr>															
															    <td class="coluna-tabela text-center">Avaliaçao</td>
															    <td class="coluna-tabela text-center">Materia</td>
															    <td class="coluna-tabela text-center">Aluno</td>
																<td class="coluna-tabela text-center">Valor</td>
																<td class="coluna-tabela text-center">Valor obtidor</td>								
															</tr>

															<?php foreach ($nota_3_rec as $key => $value) { ?>
															  
															<tr>															    
															    <td class="coluna-tabela text-center"><?php echo $value['nome']?></td>
															    <td class="coluna-tabela text-center"><?php echo $diciplina ?></td>
															    <td class="coluna-tabela text-center"> <?php $matricula = $value['cod_aluno']; $aluno = MainMolde::select('alunos2','matricula = ? ',array($matricula)); print_r($aluno[1]) ?></td>
																<td class="coluna-tabela text-center"><?php echo $value['valor']?></td>
																<td class="coluna-tabela text-center"><input type="txt"  class="nota" name="valor_obtido<?php echo $value['cod_aluno'],$value['nome']?>" value="<?php echo $value['valor_obtido']?>"></td>
																<input type="hidden" name="cod" value="<?php echo $cod ?>">
																<input type="hidden" name="nome" value="<?php echo $value['nome']?>"> 
																<input type="hidden" name="cod_serie" value="<?php echo $value['cod_serie']?>">
																<input type="hidden" name="cod_curso" value="<?php echo $value['cod_curso']?>">
																<input type="hidden" name="cod_ano"   value="<?php echo $value['cod_ano']?>">
																<input type="hidden" name="bimestre"  value="3° bimestre">
																<input type="hidden" name="cod_diciplina" value="<?php echo $diciplina ?>" >
																<input type="hidden" name="valor" value="<?php echo $value['valor'] ?>" >										
															</tr>

														   <?php } ?>

													    </table>	
												     </div><!--wraper-table-->
												     <input type="submit" name="editar_rec" value="Salva"  class="btn btn-primary"						    
													 <?php if($rec_3bm == 0){ ?> disabled <?php } ?>>
													 <input type="submit" name="lanca_rec" actionBtn="enviar-boletim" value="Enviar"  class="btn btn-primary"
													 <?php if ($rec_3bm == 0) { ?>  disabled  <?php } ?>>
												
											    </form>
									        </div><!--card-body-->
									</div><!--collpse-->
				            </div><!--card-->
				        <?php } ?>

	                <div class="card">
						    <div class="card-header" id="headingThree">
						      <h2 class="mb-0">
						        <button class="btn btn-link collapsed" type="button" data-toggle="collapse" data-target="#collapsefor" aria-expanded="false" aria-controls="collapsefor">
						         4° bimestre 
						        </button><i class="fa fa-angle-down" data-toggle="collapse" data-target="#collapsefor"></i>
						      </h2>
						    </div><!--card-header-->
						    <div id="collapsefor" class="collapse" aria-labelledby="headingfor" data-parent="#accordionExample">
						        <div class="card-body">
						           <form class="ajax-bm4" action="<?php echo INCLUDE_PATH?>Models/ajax/EnviarAlaiacao.php" method="post" enctype="multipart/form-data"><br>
						             	 <input type="submit" name="editar_avaliacao_qualificativa" value="Editar"  class="btn btn-primary"
									     <?php if($coun_notas_4bm == 0){ ?> disabled <?php } ?>>
									     <input type="submit" name="lanca_avaliacao_qualificativa" actionBtn="enviar-boletim" value="Enviar"  class="btn btn-primary"
									     <?php if ($coun_notas_4bm == 0) { ?>  disabled  <?php } ?>>
									     <input type="submit" name="media_avaliacao_qualificativa" value="Media"  class="btn btn-primary"
									     <?php if($coun_notas_4bm == 0){ ?> disabled <?php } ?>>
									     <input type="submit" name="soma_avaliacao_qualificativa" value="Soma"  class="btn btn-primary"
									     <?php if($coun_notas_4bm == 0){ ?> disabled <?php } ?>>								     
									     <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalScrollable3" <?php if($coun_notas_4bm == 0){ ?> disabled <?php } ?> >Ver Avaliaçoes</button>
									     <button type="button" class="btn btn-primary" id="btn-cadastra-notaas">Cadastra</button>

									     <!-- Modal -->
											<div class="modal fade " id="exampleModalScrollable3" tabindex="-1" role="dialog" aria-labelledby="exampleModalScrollableTitle" aria-hidden="true">
											  <div class="modal-dialog modal-dialog-scrollable modal-lg" role="document">
											    <div class="modal-content">
											      <div class="modal-header">
											        <h5 class="modal-title" id="exampleModalScrollableTitle">Avaliaçoes Cadastradas</h5>
											        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
											          <span aria-hidden="true">&times;</span>
											        </button>
											      </div>
											      <div class="modal-body ">
											      	<ul>
												      	<?php $avaliacoes_4_bm = MainMolde::selectAll('avaliacoes','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND cod_professo = ? AND bimestre = "4° bimestre"',array($curso,$serie,$ano,$diciplina,$cod));
												      	    $aux ="";										      	  
	                                                        foreach ($avaliacoes_4_bm as $key => $value) 
	                                                        { ?>   

		                                                         <?php if($aux != $value['nome']) 
		                                                         { ?>
		                                                          
																    <li class="list-group-item-avaliacoes"> 
																      <input type="radio" name="atividades[]" value="<?php echo $value['nome']?>">
																      <span class="txt-alera-notas">Nota:</span class="nome-avaliacao"><?php echo $value['nome']?></span>                                                 
																      <span class="txt-alera-notas">Valor:</span>
																      <input class="mudar-nota-input" type="text" name="valor-altera<?php echo $value['nome']?>" value="<?php echo $value['valor']?> " > 
																      <input type="hidden" name="cod" value="<?php echo $cod ?>">
																	  <input type="hidden" name="cod_serie" value="<?php echo $value['cod_serie']?>">
																	  <input type="hidden" name="cod_curso" value="<?php echo $value['cod_curso']?>">
																	  <input type="hidden" name="cod_ano"   value="<?php echo $value['cod_ano']?>">
																	  <input type="hidden" name="bimestre"  value="2° bimestre">
																	  <input type="hidden" name="cod_diciplina" value="<?php echo $diciplina ?>" >
																	  <input type="hidden" name="valor" value="<?php echo $value['valor'] ?>" >	
																	</li>

		                                                         <?php } ?>  

												        <?php $aux = $value['nome']; }?>
											        </ul>
											      </div>
											      <div class="modal-footer">
											        <button type="button" class="btn btn-secondary" data-dismiss="modal">Fechar</button>
											      
												    <input type="submit" actionBtn="exluir-atividade-qualificativa"  name="exluir-atividade-qualificativa" class="btn-delete btn" value="Excluir">
												    <input type="submit" name="altera-atividade-qualificativa" class="btn editar_avaliacao_qualificativa" value="Salva">	 											       
											      </div>
											    </div>
											  </div>
											</div>
											<!-- Modal -->

								         <div class="wraper-table">					
											<table>

												<tr>													
												    <td class="coluna-tabela text-center">Avaliaçao</td>
												    <td class="coluna-tabela text-center">Materia</td>
												    <td class="coluna-tabela text-center">Aluno</td>
													<td class="coluna-tabela text-center">Valor</td>
													<td class="coluna-tabela text-center">Valor obtidor</td>								
												</tr>

												<?php foreach ($nota_4_bm as $key => $value) { ?>
												  
												<tr>												    
												    <td class="coluna-tabela text-center"><?php echo $value['nome']?></td>
												    <td class="coluna-tabela text-center"><?php echo $diciplina ?></td>
												    <td class="coluna-tabela text-center"> <?php $matricula = $value['cod_aluno']; $aluno = MainMolde::select('alunos2','matricula = ? ',array($matricula)); print_r($aluno[1]) ?></td>
													<td class="coluna-tabela text-center"><?php echo $value['valor']?></td>
													<td class="coluna-tabela text-center"><input type="txt"  class="nota" name="valor_obtido<?php echo $value['cod_aluno'],$value['nome']?>" value="<?php echo $value['valor_obtido']?>"></td>
													<input type="hidden" name="cod" value="<?php echo $cod ?>">
													<input type="hidden" name="nome" value="<?php echo $value['nome']?>"> 
													<input type="hidden" name="cod_serie" value="<?php echo $value['cod_serie']?>">
													<input type="hidden" name="cod_curso" value="<?php echo $value['cod_curso']?>">
													<input type="hidden" name="cod_ano"   value="<?php echo $value['cod_ano']?>">
													<input type="hidden" name="bimestre"  value="4° bimestre">
													<input type="hidden" name="cod_diciplina" value="<?php echo $diciplina ?>" >
													<input type="hidden" name="valor" value="<?php echo $value['valor'] ?>" >										
												</tr>

											   <?php } ?>

										    </table>	
									     </div><!--wraper-table-->
									     <input type="submit" name="editar_avaliacao_qualificativa" value="Editar"  class="btn btn-primary"
									     <?php if($coun_notas_4bm == 0){ ?> disabled <?php } ?>>
									     <input type="submit" name="lanca_avaliacao_qualificativa" actionBtn="enviar-boletim" value="Enviar"  class="btn btn-primary"
									     <?php if ($coun_notas_4bm == 0) { ?>  disabled  <?php } ?>>
									     <input type="submit" name="media_avaliacao_qualificativa" value="Media"  class="btn btn-primary"
									     <?php if($coun_notas_4bm == 0){ ?> disabled <?php } ?>>
									     <input type="submit" name="soma_avaliacao_qualificativa" value="Soma"  class="btn btn-primary"
									     <?php if($coun_notas_4bm == 0){ ?> disabled <?php } ?>>									    
									     <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalScrollable3"  <?php if($coun_notas_4bm == 0){ ?> disabled <?php } ?>>Ver Avaliaçoes</button>
									     <button type="button" class="btn btn-primary" id="btn-cadastra-notaas4">Cadastra</button>
								    </form>
						        </div><!--card-body-->
						    </div><!--collpse-->
	                  </div><!--card-->
	                  <?php if ($rec_4bm != 0) { ?>               
			            <div class="card">
							    <div class="card-header" id="headingOne">
							      <h2 class="mb-0">
							        <button class="btn btn-link  collapsed" type="button" data-toggle="collapse" data-target="#collapseNine" aria-expanded="false" aria-controls="collapseOne">
							          rec ( 4°bm )
							        </button><i class="fa fa-angle-down" data-toggle="collapse" data-target="#collapseNine"></i>
							      </h2>
							    </div><!--card-header-->

							    <div id="collapseNine" class="collapse" aria-labelledby="headingOne" data-parent="#accordionExample">
							            <div class="card-body">
							                <form class="ajax-rec4" action="<?php echo INCLUDE_PATH?>Models/ajax/EnviarAlaiacao.php" method="post" enctype="multipart/form-data"><br>
							                	 <input type="submit" name="editar_rec" value="Salva"  class="btn btn-primary"								   
												 <?php if($rec_4bm == 0){ ?> disabled <?php } ?>>
												 <input type="submit" name="lanca_rec" actionBtn="enviar-boletim" value="Enviar"  class="btn btn-primary"
												 <?php if ($rec_4bm == 0) { ?>  disabled  <?php } ?>>
												 
										         <div class="wraper-table">					
													<table>

														<tr>														
														    <td class="coluna-tabela text-center">Avaliaçao</td>
														    <td class="coluna-tabela text-center">Materia</td>
														    <td class="coluna-tabela text-center">Aluno</td>
															<td class="coluna-tabela text-center">Valor</td>
															<td class="coluna-tabela text-center">Valor obtidor</td>								
														</tr>

														<?php foreach ($nota_4_rec as $key => $value) { ?>
														  
														<tr>
														    
														    <td class="coluna-tabela text-center"><?php echo $value['nome']?></td>
														    <td class="coluna-tabela text-center"><?php echo $diciplina ?></td>
														    <td class="coluna-tabela text-center"> <?php $matricula = $value['cod_aluno']; $aluno = MainMolde::select('alunos2','matricula = ? ',array($matricula)); print_r($aluno[1]) ?></td>
															<td class="coluna-tabela text-center"><?php echo $value['valor']?></td>
															<td class="coluna-tabela text-center"><input type="txt"  class="nota" name="valor_obtido<?php echo $value['cod_aluno'],$value['nome']?>" value="<?php echo $value['valor_obtido']?>"></td>
															<input type="hidden" name="cod" value="<?php echo $cod ?>">
															<input type="hidden" name="nome" value="<?php echo $value['nome']?>"> 
															<input type="hidden" name="cod_serie" value="<?php echo $value['cod_serie']?>">
															<input type="hidden" name="cod_curso" value="<?php echo $value['cod_curso']?>">
															<input type="hidden" name="cod_ano"   value="<?php echo $value['cod_ano']?>">
															<input type="hidden" name="bimestre"  value="4° bimestre">
															<input type="hidden" name="cod_diciplina" value="<?php echo $diciplina ?>" >
															<input type="hidden" name="valor" value="<?php echo $value['valor'] ?>" >										
														</tr>

													   <?php } ?>

												    </table>	
											     </div><!--wraper-table-->
											     <input type="submit" name="editar_rec" value="Salva"  class="btn btn-primary"						    
												 <?php if($rec_4bm == 0){ ?> disabled <?php } ?>>
												 <input type="submit" name="lanca_rec" actionBtn="enviar-boletim" value="Enviar"  class="btn btn-primary"
												 <?php if ($rec_4bm == 0) { ?>  disabled  <?php } ?>>
											
										    </form>
								        </div><!--card-body-->
								</div><!--collpse-->
			            </div><!--card-->
			        <?php } ?>
             </div><!--accordion-->
             <br><br><button type="button" class="btn btn-primary" ><a href="<?php echo INCLUDE_PATH_MAIN ?>diario-box?id=<?php echo $id ?>">Voltar<a></button>
        </div><!--listagem-->

           
        <div class="cadastro-notas">
             <br>  <h2 class="text-center"> Cadastra Avaliaçoes Qualificativa</h2><br>
               <form class="ajax-cadastro" action="<?php echo INCLUDE_PATH?>Models/ajax/EnviarAlaiacao.php" method="post" enctype="multipart/form-data">
               	  <div class="diario-from">
				        <div class="form-group">
						    <label>Bimestre:</label><br>
		                  
                             <select name="bimestre" class="select-bm1">
		                                                           	
		                               <option class="op-bimetre" selected="select" value="1° bimestre">1° Bimestre</option>	 
									   <option class="op-bimetre"  value="2° bimestre">2° Bimestre</option>
									   <option class="op-bimetre" value="3° bimestre">3° Bimestre</option>	
									   <option class="op-bimetre" value="4° bimestre">4° Bimestre</option>

			                </select>

		                    <select name="bimestre" class="select-bm2">
                               	
		                               <option class="op-bimetre" value="1° bimestre">1° Bimestre</option>	 
									   <option class="op-bimetre" selected="select" value="2° bimestre">2° Bimestre</option>		
									   <option class="op-bimetre" value="3° bimestre">3° Bimestre</option>
									   <option class="op-bimetre" value="4° bimestre">4° Bimestre</option>
								
			                </select>

			                <select name="bimestre" class="select-bm3">

		                                                             	
		                               <option class="op-bimetre" value="1° bimestre">1° Bimestre</option>	 
									   <option class="op-bimetre" value="2° bimestre">2° Bimestre</option>
									   <option class="op-bimetre" selected="select" value="3° bimestre">3° Bimestre</option>		
									   <option class="op-bimetre" value="4° bimestre">4° Bimestre</option>
									  
			                </select>

			                <select name="bimestre" class="select-bm4">
                          	
		                               <option class="op-bimetre" value="1° bimestre">1° Bimestre</option>	 
									   <option class="op-bimetre" value="2° bimestre">2° Bimestre</option>
									   <option class="op-bimetre" value="3° bimestre">3° Bimestre</option>
									   <option class="op-bimetre" selected="select" value="4° bimestre">4° Bimestre</option>
									
			                </select>

				        </div><!--form-group-->	

				         <div class="form-group">
						    <label>Valor:</label><br>
		                    <input type="text" name="valor" class="nota" required>
				        </div><!--form-group-->	

				        <div class="form-group">
						    <label>Nome:</label><br>
		                    <input type="text" name="nome" required>
				        </div><!--form-group-->	
			        </div><!--diario-from--><BR><BR>


			                   
		            <div class="wraper-table">				
							<table>

								<tr>									
									<td class="coluna-tabela text-center">Curso</td>
									<td class="coluna-tabela text-center">Serie</td>								
									<td class="coluna-tabela text-center">Materia</td>	
								    <td class="coluna-tabela text-center">Aluno</td>
									<td class="coluna-tabela text-center">Valor obtido</td>								
								</tr>

								<?php foreach ($alunos as $key => $value) { ?>

								<tr>																	
								    <td class="coluna-tabela text-center"> <?php echo $value['cod_curso'] ?></td>
									<td class="coluna-tabela text-center"> <?php echo $value['cod_serie'] ?></td>
									<td class="coluna-tabela text-center"> <?php echo $diciplina?></td>
									<td class="coluna-tabela text-center"> <?php $matricula = $value['cod_aluno']; $aluno = MainMolde::select('alunos2','matricula = ? ',array($matricula)); print_r($aluno[1]) ?></td>		
									<td class="coluna-tabela text-center"> <input class="nota" size="2" type="text" name="valor_obtido<?php echo $value['cod_aluno'] ?>" > </td>
							        <input type="hidden" name="cod" value="<?php echo $cod ?>"> 
									<input type="hidden" name="cod_serie" value="<?php echo $value['cod_serie']?>">
									<input type="hidden" name="cod_curso" value="<?php echo $value['cod_curso']?>">
									<input type="hidden" name="cod_ano"   value="<?php echo $value['cod_ano']?>">
									<input type="hidden" name="cod_diciplina" value="<?php echo $diciplina ?>" >													
								</tr>
								
							   <?php } ?>

						    </table>	
			        </div><!--wraper-table--><br>	
                   		
			
			    <button type="button" class="btn btn-primary" ><a href="<?php echo INCLUDE_PATH_MAIN ?>avaliacoes?id=<?php echo $id ?>">Voltar<a></button>
			 	<input type="submit" name="cadastar_avaliacao_qualificativa" value="Salva"  class="btn btn-primary">
			 	
			 </form>
		</div><!--cadastro-notas-->
			   
</div><!--avaliaçoes-->
<div><!--box-content-diario-->
</div><!--content -- >