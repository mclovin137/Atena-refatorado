<?php
    use \Models\MainMolde;
	if(isset($_GET['id']))
	{
		$id = (int)$_GET['id']; 
		@$dados = MainMolde::select('unidade_escolar','',array());
	    $_SESSION['id'] = $id;   
		$turma = MainMolde::select('diciplina','id = ?',array($id));
		$diciplina = $turma[1];
		$ch = $turma[6];
		$anoo = $turma[2];
		$ano = substr($anoo,-4);
		$AnoAtual = date('Y');
		if($dados != "")
			@$minutos = @$dados[5];
		else
			$minutos = 50;
		
		$dataAtual = date('Y-m-d');
		$curso = $turma[3];	
		$serie = $turma[4];
		$professo = $turma[5];
		$cod = $_SESSION['matricula'];     
		$alunos = MainMolde::selectAll('matriculados','cod_curso = ? AND cod_serie = ? AND cod_ano = ? ',array($curso,$serie,$anoo));
		$nota_1_bm = MainMolde::selectAll('avaliacoes','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND cod_professo = ?    AND bimestre = "1° bimestre"',array($curso,$serie,$ano,$diciplina,$cod));
		$nota_2_bm = MainMolde::selectAll('avaliacoes','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND cod_professo = ?  AND bimestre = "2° bimestre"',array($curso,$serie,$ano,$diciplina,$cod));
		$nota_3_bm = MainMolde::selectAll('avaliacoes','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND cod_professo = ?  AND bimestre = "3° bimestre"',array($curso,$serie,$ano,$diciplina,$cod));
		$nota_4_bm = MainMolde::selectAll('avaliacoes','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND cod_professo = ?  AND bimestre = "4° bimestre"',array($curso,$serie,$ano,$diciplina,$cod));	
		$verificaturmas = MainMolde::selectAll('diciplina', 'nome = ? AND cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_professo = ?',array($diciplina,$curso,$serie,$anoo,$cod));
		$data = MainMolde::select('datas','',array());
		$num_turmas = ceil(count($verificaturmas));
		$aulas =  MainMolde::selectAll('aulas','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ?',array($curso,$serie,$anoo,$diciplina));
		foreach($aulas as $key => $value)
		{
			@$q_aulas += $value['aulas'];
		}
	    @$ch_m = $q_aulas * $minutos;
		

		if ($num_turmas == 0) 
		{
		    \Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
		}
	}
	else 
	{
		\Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
	}
?>



<div class="content">
<div class="box-content-diario">
	  
	  <div class="listagem">
		     <h2 class="text-center"><?php echo @$dados[1] ?></h2>
	         <h2 class="text-center">Alunos</h2><br><br>
			 <?php if (@$alunos != "") { ?>
				<?php if($ano == $AnoAtual) {?>
					<button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>avaliacoes?id=<?php echo $id ?>">Avaliaçoes</button>
					<button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>planeja-aula?id=<?php echo $id ?>&&ano=<?php echo $ano ?>">Planejamento de Aula<a></button>	
					<button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>cadastrar-arquivo?id=<?php echo $id ?>">Arquivos<a></button>
				<?php } ?>
	         <button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>boletim?id=<?php echo $id ?>">Boletim<a></button>
	         
	         <?php }?>
	         <button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>diario">Voltar<a></button>

	         <div class="wraper-table">				
				<table>

					<tr>
						<td class="coluna-tabela text-center">Nome</td>
						<td class="coluna-tabela text-center">Serie</td>
						<td class="coluna-tabela text-center">Curso</td>	
						<td class="coluna-tabela text-center">Materia</td>	
					    <td class="coluna-tabela text-center">Ano</td>	
						<td class="coluna-tabela text-center">Ch</td>	
						<td class="coluna-tabela text-center">Ch Ministada</td>														
					</tr>

					<?php foreach ($alunos as $key => $value) { ?>
					
					<tr>	
					<td class="coluna-tabela text-center"> <?php $matricula = $value['cod_aluno']; $aluno = MainMolde::select('alunos2','matricula = ? ',array($matricula)); print_r($aluno[1]) ?></td>
						<td class="coluna-tabela text-center"> <?php echo $value['cod_serie']?> </td>
						<td class="coluna-tabela text-center"> <?php echo $value['cod_curso']?> </td>
						<td class="coluna-tabela text-center"> <?php echo $diciplina ?> </td>
						<td class="coluna-tabela text-center"> <?php echo $value['cod_ano']?> </td>		
						<td class="coluna-tabela text-center"> <?php echo $ch ?> </td>	
						<td class="coluna-tabela text-center"> <?php echo $resultado = substr( @$ch_m/60, 0, 4 ) ?> </td>															
					</tr>
	  
				   <?php } ?>

			    </table>	
		    </div><!--wraper-table-->
	   
		
			<?php if (@$alunos != "") { ?>
				<?php if($ano == $AnoAtual) {?>
					<button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>avaliacoes?id=<?php echo $id ?>">Avaliaçoes</button>
					<button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>planeja-aula?id=<?php echo $id ?>&&ano=<?php echo $ano ?>">Planejamento de Aula<a></button>
					<button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>cadastrar-arquivo?id=<?php echo $id ?>">Arquivos<a></button>	
				<?php } ?>
	         <button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>boletim?id=<?php echo $id ?>">Boletim<a></button>
	         
           <?php }?>
            <button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>diario">Voltar<a></button> 

	   </div><!--listagem-->
</div><!-- box-content-diario-->
</div><!--content  -- > 

