<?php 
     use \Models\MainMolde;
     if(isset($_GET['id']))
	 {
		$id = (int)$_GET['id'];
		@$idlink = (int)$_GET['turmas'];
		$turma = MainMolde::select('diciplina','id = ?',array($id));
		$ano = $turma[2];
		$AnoAtual = date('Y');
		$diciplina = $turma[1];
		$curso = $turma[3];
		$serie = $turma[4];
		$ano = $turma[2];
		$aux = $turma[5];	
		$prof = MainMolde::select('professo2','matricula = ?',array($aux));	
	 }
	 else
	 {
		\Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
	 }
	$anoo = substr($ano,-4);	
	if($anoo != $AnoAtual)
		\Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
?>

<div class="content">
<section class="box-content center">
     <form class="ajax"  method="post" enctype="multipart/form-data" action="<?php echo INCLUDE_PATH?>Models/ajax/EnviarAlaiacao.php">
		       

                <section class="top-letest-product-section" >
						<div class="container">
								<div class="section-title">
									<h2>Escolha o professor que substituira o <?php echo $prof['nome']?></h2>
								</div><!--section-title-->
					            <div class="product-slider owl-carousel">

									<?php
										$alunos = MainMolde::selectAll('professo2','estado = 1 AND matricula != ?',array($aux));	
										foreach ($alunos as $key => $value) {									
									?>
									<div class="product-item">
										<div class="pi-pic">
										    <img src="<?php echo INCLUDE_PATH?>Views/uploads/<?php echo $value['imagem']; ?>" />
											<div class="pi-links">										
												<!-- 	<a for="check" class="add-card"><i class="fa fa-user-plus"></i><span for="check">adicionar<input type="checkbox" name="alunos[]" value="<php echo $value['matricula'] ?>"></span></a> -->
                                                <input type="radio" name="professor" value="<?php echo $value['matricula']?>" required>	
												<input type = "hidden" name = "profatual" value="<?php echo $aux?>" >
												<input type = "hidden" name = "curso" value="<?php echo $curso?>" >
												<input type = "hidden" name = "serie" value="<?php echo $serie?>" >
												<input type = "hidden" name = "ano" value="<?php echo $ano?>" >
												<input type = "hidden" name = "diciplina" value= "<?php echo $diciplina?>" >
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
			         <input type="submit" name="Editar-responssavel" value="Atualiza!">
					 <?php 
					   if($idlink != "")
					   {?>
                        <button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>grade-curricular?id=<?php echo $idlink ?>">Voltar<a></button>
					  <?php }else{ ?>
					 <button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>diario-box?id=<?php echo $id ?>">Voltar<a></button>
					 <?php }?>
		        </div><!--form-group-->
               
     </form>
</section><!--box-principal-->

</div><!-- content -- >
