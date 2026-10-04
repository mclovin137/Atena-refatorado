<?php
    include('../../config.php');
   
    use \Models\MySql;
    use \Models\MainMolde;
    $aluno = $_SESSION['IDaluno'];
	$ano = $_SESSION['IDano'];
	$alunos = MainMolde::select('matriculados','cod_aluno = ? AND cod_ano = ?',array($aluno,$ano));
	$aluno_nome = MainMolde::select('alunos2','matricula = ?',array($aluno));
    $curso = $alunos[1];
	$serie = $alunos[2];
	$ano = $alunos[3];
	$turma = MainMolde::selectAll('diciplina','cod_curso = ? AND cod_serie = ? AND cod_ano = ?',array($curso,$serie,$ano));
	$escolar = MainMolde::select('unidade_escolar','',array());

?>
<html>
<head>
		<link rel="stylesheet" href="<?php echo INCLUDE_PATH ?>Views/estilo/pdf.css">
</head>

<body>

	<h2> <?php echo $escolar[1]; ?> </h2>

	
	<h2>BOLETIM  </h2>
	<table> 
			<tr >
				 <th>NOME</th> 
				 <th>CPF</th>
			</tr>
			<tr >
				 <td> <?php echo $aluno_nome[1]; ?> </td> 
				 <td> <?php echo $aluno_nome[4]; ?> </td> 
			</tr>
	</table> 
	<div >	
	    <table>  		
				<tr >
					<th >Diciplina</th>
					<th >1°bm</th>
					<th >rec1</th>
					<th >2°bm</th>
					<th >rec2</th>
					<th >3°bm</th>
					<th >rec3</th>
					<th >4°bm</th>
					<th >rec4</th>
					<th >Media</th>
					<th >situaçao</th>
					
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
									<td ><?php echo $value['nome']; ?></td>						
									<td ><?php echo $notas[6]; ?></td>
									<td ><?php echo $notas[7]; ?></td>
									<td ><?php echo $notas[8]; ?></td>
									<td ><?php echo $notas[9];?></td>
									<td ><?php echo $notas[10]; ?></td> 
									<td ><?php echo $notas[11]; ?></td>
									<td ><?php echo $notas[12]; ?></td>
									<td ><?php echo $notas[13]; ?></td>							
									<td ><?php echo $notas[14]; ?></td>
									<td >
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

										
									} $falta = 0 ?></td> 
									
									
										
				</tr>
        
			<?php  } ?>	
	    </table>  
	</div><!--wraper-table-->
</body>
</html>


