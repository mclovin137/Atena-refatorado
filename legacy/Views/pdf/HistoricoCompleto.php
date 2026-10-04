<?php 
   
   
    use \Models\MySql;
	use \Models\MainMolde;
	
    $id =  $_SESSION['IdHistorico'];
	@$escolar = MainMolde::select('unidade_escolar','',array());
	$turmas = MainMolde::selectAll('matriculados','cod_aluno = ? AND dependencia = 0 ',array($id));
	$aluno_nome = MainMolde::select('alunos2','matricula = ?',array($id));

	foreach($turmas as $key => $value)
	{
		@$anos[] .= $value['cod_ano'];
	}
		
	$quantidade = ceil(count($anos));

	for($i = 0; $i < $quantidade; $i++)
	{
		@$materias[] = MainMolde::select('matriculados','cod_aluno = ? AND cod_ano = ? ',array( $id,$anos[$i] ));
	}


	for($x = 0; $x < $quantidade ; $x++)
	{
		$diciplina = $materias[$x];
		@$materiass[] = MainMolde::selectAll('diciplina','cod_serie = ? AND cod_ano = ? AND cod_curso = ? ',array( $diciplina[2], $diciplina[3], $diciplina[1] ));
	}

	
	@$materias_dp = MainMolde::selectAll('matriculados','cod_aluno = ?  AND dependencia = 1',array( $id ));
	
	foreach($materias_dp as $key => $value)
	{
		@$materiass_dp[] = MainMolde::selectAll('diciplina','cod_serie = ? AND cod_ano = ? AND cod_curso = ? ',
		array( $value['cod_serie'] , $value['cod_ano'], $value['cod_curso'] ));
	}

    

	for($x = 0; $x < $quantidade ; $x++)
	{
		foreach($materiass[$x] as $key => $value)
		{
			 $nomes[] = $value['nome'];
		}
	}

	for($x = 0; $x < $quantidade ; $x++)
	{
		foreach($materiass_dp[$x] as $key => $value)
		{
			$nomes[] = $value['nome'];
		}
	}

	

	$nomes = array_unique($nomes);
	
?>

<html>
<head>
		<link rel="stylesheet" href="<?php echo INCLUDE_PATH ?>Views/estilo/pdf.css">
</head>

<body>
    
	<div >	
		<h2> <?php echo $escolar[1]; ?> </h2>

		<table> 
			<tr >
				 <th>TELEFONE</th> 
				 <th>ENDEREÇO</th>
				 <th>CNPJ</th>
			</tr>
			<tr >
				 <td> <?php echo $escolar[6]; ?> </td> 
				 <td> <?php echo $escolar[2]; ?> </td>
				 <td> <?php echo $escolar[7]; ?> </td> 
			</tr>
		</table> 

		<h2> HISTORICO ESCOLAR </h2>
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
            
		<table>  		
				<tr >
					<th >Diciplina</th>
					<?php for ($z = 0; $z < $quantidade ; $z++) { ?>
					  <th> <?php echo $anos[$z] ?> </th>
					  <th >Media</th>
					<?php } ?>
					
					
				</tr>
				
				<?php 			
					foreach ($nomes as  $value) 
					{
																				
                       
					?>          
				
					<tr>	
						   <td ><?php echo @$value;?></td>
						   <?php for ($g = 0; $g < $quantidade; $g++) {							   
							   foreach ($materiass[$g] as $key => $valuee) 
							   {	
								   @$notas = MainMolde::select('notas','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_aluno = ? AND cod_diciplina = ?',
								   array($valuee['cod_curso'],$valuee['cod_serie'],$anos[$g],$id,$value));
								   $serie = $valuee['cod_serie'];
								   if(is_null($notas[14]))
									  $notas[14] = "-";
								   
							   }	  
						   ?>
								<td ><?php echo $serie?></td>	
								<td ><?php echo @$notas[14];?></td>	
						   <?php }?>
																
					</tr>
			
					<?php   } ?>	

	    </table>  
		
	</div>
</body>
</html>

