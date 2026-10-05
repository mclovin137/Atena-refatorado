
<?php
    use \Models\MainMolde;
	$aluno = $_SESSION['matricula'];
	@$dados = MainMolde::select('unidade_escolar','',array());
	if(isset($_GET['ano']))
		$ano = $_GET['ano'];
	else
		$ano = date('Y');

	
	$turmas = MainMolde::selectAll('matriculados','cod_aluno = ? ',array($aluno));
	foreach($turmas as $key => $value)
	{
		@$anos[] .= $value['cod_ano'];
	}
	
	@$quantidade = ceil(count(@$anos));
    $alunos = MainMolde::select('matriculados','cod_aluno = ? AND cod_ano = ?',array($aluno,$ano));
    $curso = $alunos[1];
    $serie = $alunos[2];
    $x = 0;
	$_SESSION['ano'] = $ano;
    $turma = MainMolde::selectAll('diciplina','cod_curso = ? AND cod_serie = ? AND cod_ano = ? ',array($curso,$serie,$ano));
   
   
?>

<div class="content">
<div class="box-content-diario">
	<div class="cabeçario">
		<h2 class="text-center"><?php echo @$dados[1] ?></h2>
		<h2 class="text-center">Turma</h2>
		<?php for($x = 0; $x < $quantidade ; $x++)
    	{?>
			<a class="btn btn-info" href="<?php echo INCLUDE_PATH_MAIN ?>boletim?ano=<?php echo $anos[$x] ?>">
					<?php echo $anos[$x] ?>
			<a>
		<?php }?>
			<div class="wraper-table">								  
				<table>

					<tr>
						<td class="coluna-tabela text-center">Ano</td>
						<td class="coluna-tabela text-center">Curso</td>
						<td class="coluna-tabela text-center">Serie</td>
							
					</tr>
					
					<tr>	
						<td class="coluna-tabela text-center"> <?php echo $ano       ?> </td>
						<td class="coluna-tabela text-center"> <?php echo $curso     ?></td>
						<td class="coluna-tabela text-center"> <?php echo $serie     ?> </td>														
					</tr>

			    </table>		  
	        </div><!--wraper-table-->
	</div><!--cabeçario-->
	<Br>	


	<h2 class="text-center"><i class="fa fa-list-alt"></i>   Boletim</h2>
	<div class="wraper-table">
		<table>
			<tr>
				<td class="coluna-tabela text-center">Matricula</td>
				<td class="coluna-tabela text-center">Diciplina</td>
				<td class="coluna-tabela text-center">Carga horaria</td>
				<td class="coluna-tabela text-center">1°bm</td>
				<td class="coluna-tabela text-center">rec1</td>
				<td class="coluna-tabela text-center">2°bm</td>
				<td class="coluna-tabela text-center">rec2</td>
				<td class="coluna-tabela text-center">3°bm</td>
				<td class="coluna-tabela text-center">rec3</td>
				<td class="coluna-tabela text-center">4°bm</td>
				<td class="coluna-tabela text-center">rec4</td>
				<td class="coluna-tabela text-center">faltas</td>
				<td class="coluna-tabela text-center">Media</td>
				<td class="coluna-tabela text-center">Md final</td>
				<td class="coluna-tabela text-center">situaçao</td>
				
			</tr>

			<?php
                          
					       foreach ($turma as $key => $value) {
						   $notas = MainMolde::select('notas','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_aluno = ? AND cod_diciplina = ?',
						   array($curso,$serie,$ano,$aluno,$value['nome']));					
						   $faltas = MainMolde::selectAll('presenca','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_aluno = ? AND cod_materia = ?',
						   array($curso,$serie,$ano,$aluno,$value['nome'])); 			       
												  
													
									   foreach ($faltas as $key => $valuee) 
									   {
											 @$falta += $valuee['faltas'];
																 
									   }
					   
									   if (@$falta == "") 
									   {
											  @$falta = 0;
									   }
							
						

			?>
            
			<tr>	
				                <td class="coluna-tabela text-center"><?php echo @$aluno ?></td>	
								<td class="coluna-tabela text-center"><?php echo @$value['nome'] ?></td>
							    <td class="coluna-tabela text-center"><?php echo @$value['carga_horaria'] ?></td>							
								<td class="coluna-tabela text-center"><?php echo @$notas[6] ?></td>
								<td class="coluna-tabela text-center"><?php echo @$notas[7] ?></td>
								<td class="coluna-tabela text-center"><?php echo @$notas[8] ?></td>
								<td class="coluna-tabela text-center"><?php echo @$notas[9] ?></td>
								<td class="coluna-tabela text-center"><?php echo @$notas[10] ?></td> 
								<td class="coluna-tabela text-center"><?php echo @$notas[11] ?></td>
								<td class="coluna-tabela text-center"><?php echo @$notas[12] ?></td>
								<td class="coluna-tabela text-center"><?php echo @$notas[13] ?></td>
									<td class="coluna-tabela text-center"><?php echo @$falta ?></td>							
								<td class="coluna-tabela text-center"><?php echo @$notas[20] ?></td>
								<td class="coluna-tabela text-center"><?php echo @$notas[14] ?></td>
								<td class="coluna-tabela text-center">
							    <?php switch ($notas[15]) {
							    	case 1:
							    		echo "Aprovado";
							    		break;
							    	 case 0:
							    	     echo "Cursando";
							    	     break;
							    	 case 2;
							    	     echo "Reprovado";
							    	     break;
							    	 case 3:
							    	     echo "Rp faltas";
							    	     break;
							    	 case 4;
							    	     echo "Reprovado";
							    	     break;

							    	 default:
							    		break;
							    } $falta = 0 ?></td> 
								
								
									
			</tr>
        
		<?php } ?>	
	    </table>
	    <?php 
	          if(isset($_GET['url'])){ 
	               $url = explode('/',$_GET['url']);
	                    if($url[1] == 'boletim'){ ?> 
	        <a href="<?php echo INCLUDE_PATH?>gerar-pdf.php?ano=<?php echo $ano ?>" >Gerar PDF</a>
	    <?php } }?>
	</div><!--wraper-table-->


</div><!--box-content-->
</div><!--content -- >



