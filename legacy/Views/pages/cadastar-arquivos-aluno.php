<?php 
     use \Models\MainMolde;
     $matricula = $_SESSION['matricula'];
     $ano = date('Y',time());
     $turma = MainMolde::select('matriculados','cod_aluno = ? AND cod_ano = ?',array($matricula,$ano));
     $curso = $turma[1];
     $ano = $turma[3];
     $serie = $turma[2];
     $materiais = MainMolde::selectAll('materiais','cod_curso = ? AND cod_serie = ? AND cod_ano = ?',array($curso,$serie,$ano));
?>
<div class="content">
<section class="box-content center">

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