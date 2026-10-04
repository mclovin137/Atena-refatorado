<?php
	use \Models\MainMolde;
	@$dados = MainMolde::select('unidade_escolar','',array());


if(isset($_GET['id']))
	{
		$email = $_GET['id'];
		if(isset($_GET['ano']))
		   $ano = $_GET['ano'];
		else
		   $ano = date('Y');

		$aluno = MainMolde::select('alunos2','id = ?',array($email));	
		$turma = MainMolde::select('matriculados','cod_aluno = ? AND cod_ano = ?  ',array($aluno[3],$ano));
		$turmas = MainMolde::selectAll('matriculados','cod_aluno = ? ',array($aluno[3]));
		foreach($turmas as $key => $value)
		{
			@$anos[] .= $value['cod_ano'];
		}
		
		$quantidade = ceil(count($anos));
		$curso = $turma[1];
	    $serie = $turma[2];
		$cod = $turma[5];
	    $aluno_id = $aluno[3];
	    $i = 0;
        $x = 0;
		$diciplina = MainMolde::selectAll('diciplina','cod_curso = ? AND cod_serie = ? AND cod_ano = ?',array($curso,$serie,$ano));
	    foreach ($diciplina as $key => $value) 
	    {
	    	       $faltas = MainMolde::selectAll('presenca','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_professo = ? AND cod_aluno = ? AND cod_materia = ?',array($curso,$serie,$ano,$value['cod_professo'],$aluno_id,$value['nome'])); 			       
				               
				                 
				    foreach ($faltas as $key => $value) 
				    {
				          @$falta[$i] += $value['faltas'];
				                              
				    }

				    if (@$falta[$i] == "") 
				    {
				           @$falta[$i] = 0;
				    }
	        $i++;
		}
   
		$_SESSION['IDaluno'] = $aluno_id;
		$_SESSION['IDano'] = $ano;
   

		
	}
	else
	{
		\Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
	}
?>

<div class="content">
<div class="box-content-diario">
	<div class="cabeçario">
		<h2 class="text-center"><?php echo @$dados[1] ?></h2>
			
	</div><!--cabeçario-->
	<Br>	


	<h2 class="text-center"><i class="fa fa-list-alt"></i>   Boletim <?php echo $ano?></h2><br><br>
	

	<?php for($i = 0; $i < $quantidade ; $i++)
    {?>
		<a class="btn btn-info" href="<?php echo INCLUDE_PATH_MAIN ?>historico?id=<?php print_r($email) ?>&&ano=<?php echo $anos[$i] ?>">
				<?php echo $anos[$i] ?>
		<a>

	<?php }?>
	<a class="btn btn-info" href="<?php echo INCLUDE_PATH?>Models/pdf/Historico.php">Gerar Pdf</a>
	
	<div class="wraper-table">
		<table>
			<tr>
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
                          
					       foreach ($diciplina as $key => $value) {
						   $notas = MainMolde::select('notas','cod_curso = ? AND cod_serie = ? AND cod_aluno = ? AND cod_diciplina = ? AND cod_ano = ?',array($curso,$serie,$aluno_id,$value['nome'],$ano));
						
							

			?>
            
			<tr>	
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
								<td class="coluna-tabela text-center"><?php echo @$falta[$x] ?></td>							
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
							    } ?></td> 
								
								
									
			 </tr>
			<?php $x++; } ?>	
		</table>
	</div><!--wraper-table-->

</div><!--box-content-->
</div><!--box-content -- >