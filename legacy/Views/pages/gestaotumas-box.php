<?php
    use \Models\MainMolde;
	if(isset($_GET['id']))
	{
		$id = (int)$_GET['id']; 
	    $_SESSION['id'] = $id;   
		$turma = MainMolde::select('turmas','id = ?',array($id));
		$ano = $turma[1];
		$curso = $turma[2];	
		$serie = $turma[3];  
		$alunos = MainMolde::selectAll('matriculados','cod_curso = ? AND cod_serie = ? AND cod_ano = ? ',array($curso,$serie,$ano));
		
	}
	else 
	{
		\Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
	}
?>
<div class="content">
<div class="box-content-diario">
	  
	    <div class="listagem">
	         <h2 class="text-center">Alunos</h2><br><br>
	         <?php if (@$alunos != "") { ?>
	         <button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>grade-curricular?id=<?php echo $id ?>">Grade Curricular</button>
			 <?php }?>
			 <button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>gerir-turmas">Voltar<a></button>
			
	         <div class="wraper-table">				
				<table>

					<tr>
						<td class="coluna-tabela text-center">Nome</td>
						<td class="coluna-tabela text-center">Serie</td>
						<td class="coluna-tabela text-center">Curso</td>	
					    <td class="coluna-tabela text-center">Ano</td>
					   													
					</tr>

					<?php foreach ($alunos as $key => $value) {
                       $matricula = $value['cod_aluno']; 
                       $aluno = MainMolde::select('alunos2','matricula = ? ',array($matricula));
					?>
					
					<tr>	
						<td class="coluna-tabela text-center"><a href="<?php echo INCLUDE_PATH_MAIN ?>historico?id=<?php print_r($aluno[0]) ?>"><?php print_r($aluno[1])  ?> <a></td>
						<td class="coluna-tabela text-center"> <?php echo $value['cod_serie']?> </td>
						<td class="coluna-tabela text-center"> <?php echo $value['cod_curso']?> </td>
						<td class="coluna-tabela text-center"> <?php echo $value['cod_ano']?> </td>	
						<?php 
							  $materias = MainMolde::selectAll('notas','cod_aluno = ? AND media > 60',array($matricula));
							  $count = ceil(count($materias));
						?>
																				
					</tr>
	  
				   <?php } ?>

			    </table>	
		    </div><!--wraper-table-->
	    </div><!--listagem-->

		     <?php if (@$alunos != "") { ?>
		         <button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>grade-curricular?id=<?php echo $id ?>">Grade Curricular</button>
			 <?php }?>
	         <button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>gerir-turmas">Voltar<a></button>

</div><!--box-content-->
</div><!--content -- >