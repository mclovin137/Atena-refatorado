<?php
    use \Models\MainMolde;
	if(isset($_GET['id']))
	{
		$id = (int)$_GET['id']; 
		$_SESSION['id'] = $id;   
		@$dados = MainMolde::select('unidade_escolar','',array());
		$turma = MainMolde::select('diciplina','id = ?',array($id));
		$diciplina = $turma[1];
		$ch = $turma[6];
		$anoo = $turma[2] ;
		$ano = substr($anoo,-4);
		if($dados != "")
			@$minutos = @$dados[5];
		else
			$minutos = 50;

		$AnoAtual = date('Y');
		$dataAtual = date('Y-m-d');
		$curso = $turma[3];	
		$serie = $turma[4];
		$professo = $turma[5];
		$cod = $_SESSION['matricula']; 
		$notas = MainMolde::selectAll('notas','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND cod_professo = ?',array($curso,$serie,$ano,$diciplina,$professo )); 
		$alunos = MainMolde::selectAll('matriculados','cod_curso = ? AND cod_serie = ? AND cod_ano = ? ',array($curso,$serie,$anoo));
		$nota_1_bm = MainMolde::selectAll('avaliacoes','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND cod_professo = ?    AND bimestre = "1° bimestre"',array($curso,$serie,$ano,$diciplina,$cod));
		$nota_2_bm = MainMolde::selectAll('avaliacoes','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND cod_professo = ?  AND bimestre = "2° bimestre"',array($curso,$serie,$ano,$diciplina,$cod));
		$nota_3_bm = MainMolde::selectAll('avaliacoes','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND cod_professo = ?  AND bimestre = "3° bimestre"',array($curso,$serie,$ano,$diciplina,$cod));
		$nota_4_bm = MainMolde::selectAll('avaliacoes','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND cod_professo = ?  AND bimestre = "4° bimestre"',array($curso,$serie,$ano,$diciplina,$cod));	
		$verificaturmas = MainMolde::selectAll('diciplina', 'nome = ? AND cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_professo = ?',array($diciplina,$curso,$serie,$anoo,$professo));
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
			    <?php if($ano == $AnoAtual) { ?>
					<button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>avaliacoes?id=<?php echo $id ?>">Avaliaçoes</button>
					<button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>cadastrar-arquivo?id=<?php echo $id ?>">Arquivos<a></button>
					<button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>planeja-aula?id=<?php echo $id ?>&&ano=<?php echo $ano ?>">Planejamento de Aula<a></button>	
				<?php }?>
				<button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>boletim?id=<?php echo $id ?>">Boletim<a></button>
				
			 <?php }?>
			 <?php if($ano == $AnoAtual) { ?>
			 	<button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>troca-professor?id=<?php echo $id ?>">Troca o Responsavel<a></button>
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
						<td class="coluna-tabela text-center"> <?php echo $ch?> </td>	
						<td class="coluna-tabela text-center"> <?php echo $resultado = substr( @$ch_m/60, 0, 4 ) ?> </td>														
					</tr>
	  
				   <?php } ?>

			    </table>	
		    </div><!--wraper-table-->
	   
		
			<?php if (@$alunos != "") { ?>
			    <?php if($ano == $AnoAtual) { ?>
					<button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>avaliacoes?id=<?php echo $id ?>">Avaliaçoes</button>
					<button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>cadastrar-arquivo?id=<?php echo $id ?>">Arquivos<a></button>
					<button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>planeja-aula?id=<?php echo $id ?>&&ano=<?php echo $ano ?>">Planejamento de Aula<a></button>	
				<?php }?>
				<button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>boletim?id=<?php echo $id ?>">Boletim<a></button>
				
			 <?php }?>
			 <?php if($ano == $AnoAtual) { ?>
			 	<button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>troca-professor?id=<?php echo $id ?>">Troca o Responsavel<a></button>
			 <?php }?>
	         <button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>diario">Voltar<a></button>

	   </div><!--listagem-->
</div><!-- box-content-diario-->
</div><!--content  -- > 

<!-- Modal -->
			<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
			  <div class="modal-dialog" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h5 class="modal-title" id="exampleModalLabel">Bloquear lançamento</h5>
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
			          <span aria-hidden="true">&times;</span>
			        </button>
			      </div>
			            <form  method="post" enctype="multipart/form-data" >
						      <div class="modal-body text-center">
						          <?php if(@$notas[16] == 0) {?>
						          <input type="submit" actionBtn="enviar" name="1bm" value="1° Bimestre" class="btn btn-lg btn-primary" target><BR>
						          <?php }?>
						          <?php if(@$notas[17] == 0) {?>
						          <input type="submit" actionBtn="enviar" name="2bm" value="2° Bimestre" class="btn btn-lg btn-primary" target><BR>
						          <?php }?>
						          <?php if(@$notas[18] == 0) {?>
						          <input type="submit" actionBtn="enviar" name="3bm" value="3° Bimestre" class="btn btn-lg btn-primary" target><BR>
                                  <?php }?>
						          <?php if(@$notas[19] == 0) {?>
						          <input type="submit" actionBtn="enviar" name="4bm" value="4° Bimestre" class="btn btn-lg btn-primary" target><BR>
						          <?php }?>
						      </div>
						</form>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
			      </div>
			    </div>
			  </div>
			</div>
<!-- Modsl -- >

<!-- Modal -->
			<div class="modal fade" id="exampleModal1" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
			  <div class="modal-dialog" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h5 class="modal-title" id="exampleModalLabel">libera lançamento</h5>
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
			          <span aria-hidden="true">&times;</span>
			        </button>
			      </div>
			            <form  method="post" enctype="multipart/form-data" >
						      <div class="modal-body text-center">
						      	  <?php if(@$notas[16] == 1  || $dataAtual > $data[0]) {?>
						          <input type="submit" actionBtn="liberar" name="libera-1bm" value="1° Bimestre" class="btn btn-lg btn-primary" target><BR>
						          <?php }?>
						          <?php if(@$notas[17] == 1  || $dataAtual > $data[1] ) {?>
						          <input type="submit" actionBtn="liberar" name="libera-2bm" value="2° Bimestre" class="btn btn-lg btn-primary" target><BR>
						          <?php }?>
						          <?php if(@$notas[18] == 1  || $dataAtual > $data[2]) {?>
						          <input type="submit" actionBtn="liberar" name="libera-3bm" value="3° Bimestre" class="btn btn-lg btn-primary" target><BR>
						          <?php }?>
						          <?php if(@$notas[19] == 1  || $dataAtual > $data[3])  {?>
						          <input type="submit" actionBtn="liberar" name="libera-4bm" value="4° Bimestre" class="btn btn-lg btn-primary" target><BR>
						          <?php }?>
						      </div>
						</form>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
			      </div>
			    </div>
			  </div>
			</div>
<!-- Modsl -- >
