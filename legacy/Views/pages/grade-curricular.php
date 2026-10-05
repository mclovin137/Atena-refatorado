<?php
    use \Models\MainMolde;
	if(isset($_GET['id']))
	{
		$id = (int)$_GET['id'];   
		$turma = MainMolde::select('turmas','id = ?',array($id));
		@$dados = MainMolde::select('unidade_escolar','',array());
		if($dados != "")
			@$minutos = @$dados[5];
		else
			$minutos = 50;

		$ano = $turma[1];
		$curso = $turma[2];	
		$serie = $turma[3];  
		$Materia = MainMolde::selectAll('grade_curricular','cod_ano = ? AND cod_curso = ? AND cod_serie = ?',array($ano,$curso,$serie));

		
	}
	else 
	{
		\Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
	}
?>
<div class="content">
<div class="box-content-diario">
	    <div class="listagem">
	         <h2 class="text-center">Grade Curricular</h2><br><br>
			 
	    <form method="post" enctype="multipart/form-data">
	         <button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>gestaotumas-box?id=<?php echo $id ?>">Voltar<a></button>
             <button type="submit" class="btn btn-primary" name="editar" >Editar</button> 
             <button type="submit" class="btn btn-primary" name="delete" actionBtn="exluir-atividade-qualificativa"  >Excluir</button> 
		
		         <div class="wraper-table">				
					<table>

						<tr>
							<td class="coluna-tabela text-center">#</td>
							<td class="coluna-tabela text-center">Materia</td>
							<td class="coluna-tabela text-center">Serie</td>
							<td class="coluna-tabela text-center">Curso</td>	
						    <td class="coluna-tabela text-center">Ano</td>
						    <td class="coluna-tabela text-center">Carga Horaria</td>
							<td class="coluna-tabela text-center">Ch Ministada</td>		
							<td class="coluna-tabela text-center">Responsavel</td>														
						</tr>

						<?php foreach ($Materia as $key => $value) { 
							
							$q_aulas = "";
							$aulas =  MainMolde::selectAll('aulas','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ?',array($curso,$serie,$ano,$value['nome']));
							foreach($aulas as $key => $valuue)
							{
								@$q_aulas += $valuue['aulas'];
							}
							@$ch_m = $q_aulas * $minutos;
							
						?>

						
							<tr>
							    <input type="hidden" name="serie" value="<?php echo $value['cod_serie']?>">
							    <input type="hidden" name="curso" value="<?php echo $value['cod_curso']?>">	
							    <input type="hidden" name="ano" value="<?php echo $value['cod_ano']?>">
							    <td class="coluna-tabela text-center"><input type="radio" name="materia" value="<?php echo $value['id']?>"> </td>		 
							    <input type="hidden" name="materias[]" value="<?php echo $value['id']?>">
							    <input type="hidden" name="nomeatual[]" value="<?php echo $value['nome']?>">
							    <td class="coluna-tabela text-center"><input type="text" name="nome[]" value="<?php echo $value['nome']?>"> </td>
								<td class="coluna-tabela text-center"> <?php echo $value['cod_serie']?> </td>
								<td class="coluna-tabela text-center"> <?php echo $value['cod_curso']?> </td>	
								<td class="coluna-tabela text-center"> <?php echo $value['cod_ano']?> </td>
								<td class="coluna-tabela text-center"><input type="number" step="1" min="1"  name = "ch[]" value="<?php echo $value['ch']?>"> </td>
								<td class="coluna-tabela text-center"> <?php echo $resultado = substr( @$ch_m/60, 0, 4 ) ; ?> </td>	
								<td class="coluna-tabela text-center"> <?php  $diciplina = MainMolde::select('diciplina','nome = ? AND cod_ano = ? AND cod_serie = ? AND cod_curso = ?',
								 array($value['nome'], $value['cod_ano'], $value['cod_serie'],$value['cod_curso'])); ?>
								<a href="<?php echo INCLUDE_PATH_MAIN ?>troca-professor?id=<?php echo $diciplina[0] ?>&turmas=<?php echo $id ?>"><?php  
								$matricula =  $diciplina[5];  $nome = MainMolde::select('professo2','matricula = ?',array($matricula)); echo $nome[1]; if($nome == ""){ echo "Sem professor responsavel"; }   ?> </a></td>																		
							</tr>
		  
					   <?php } ?>

				    </table>	
			    </div><!--wraper-table-->
			    <button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>gestaotumas-box?id=<?php echo $id ?>">Voltar<a></button>
                <button type="submit" class="btn btn-primary" name="editar" >Editar</button> 		
                <button type="submit" class="btn btn-primary" name="delete"  actionBtn="exluir-atividade-qualificativa"  >Excluir</button> 	   
			</form>
			

	    </div><!--listagem-->
</div><!--box-content-->
</div><!--content -- >
