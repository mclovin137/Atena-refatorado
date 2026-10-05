<?php
    use \Models\MainMolde;
	if(isset($_GET['id']))
	{
		$id = (int)$_GET['id'];    
		$turma = MainMolde::select('diciplina','id = ?',array($id));
		$diciplina = $turma[1];
		@$dados = MainMolde::select('unidade_escolar','',array());
		$ano = $turma[2];
		$dataAtual = date('Y-m-d');
		$curso = $turma[3];	
		$serie = $turma[4];
		$professo = $turma[5];
		$alunos = MainMolde::selectAll('matriculados','cod_curso = ? AND cod_serie = ? AND cod_ano = ?',array($curso,$serie,$ano));
		$verificaturmas = MainMolde::selectAll('diciplina', 'nome = ? AND cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_professo = ?',array($diciplina,$curso,$serie,$ano,$_SESSION['matricula']));
		$data = MainMolde::select('datas','',array());
		$num_turmas = ceil(count($verificaturmas));
		$notass = MainMolde::selectAll('notas','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND cod_professo = ?',array($curso,$serie,$ano,$diciplina,$professo )); 
		$i = 0; 
		$cont_notas = ceil(count($notass));
		foreach ($alunos as $key => $value) 
		{ 
	           $notas = MainMolde::select('notas','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_aluno = ? AND cod_diciplina = ? AND cod_professo = ?',array($value['cod_curso'] ,
			   $value['cod_serie'],$value['cod_ano'],$value['cod_aluno'],$diciplina,$professo )); 			   
        }			

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

	<h2 class="text-center"><?php echo @$dados[1] ?></h2>
	<h2 class="text-center">Boletim</h2><br>

<form class="ajax"  action="<?php echo INCLUDE_PATH?>Models/ajax/CadastroNotas.php" method="post" enctype="multipart/form-data" >
<div class="cabeçario">
		<div class="wraper-table">
			
			<table>

				<tr>
					<td class="coluna-tabela text-center">Materia</td>
					<td class="coluna-tabela text-center">Ano</td>
					<td class="coluna-tabela text-center">Curso</td>
					<td class="coluna-tabela text-center">Serie</td>
						
				</tr>
				
				<tr>	
					<td class="coluna-tabela text-center"> <?php echo $diciplina ?></td>
					<td class="coluna-tabela text-center"> <?php echo $ano       ?> </td>
					<td class="coluna-tabela text-center"> <?php echo $curso     ?></td>
					<td class="coluna-tabela text-center"> <?php echo $serie     ?> </td>														
				</tr>

		    </table>	
	    </div><!--wraper-table-->
</div><!--cabeçario-->
<Br>
        
		  <button type="button" class="btn btn-primary" id="btn-voltar"><a href="<?php echo INCLUDE_PATH_MAIN ?>diario-box?id=<?php echo $id ?>">Voltar<a></button><br>

<div class="wraper-table">
		<table>
                 
			<tr>
				<td class="coluna-tabela text-center">Nome</td>
				<td class="coluna-tabela text-center">1°bm</td>
				<td class="coluna-tabela text-center">rec1</td>
				<td class="coluna-tabela text-center">2°bm</td>
				<td class="coluna-tabela text-center">rec2</td>
				<td class="coluna-tabela text-center">3°bm</td>
				<td class="coluna-tabela text-center">rec3</td>
				<td class="coluna-tabela text-center">4°bm</td>
				<td class="coluna-tabela text-center">rec4</td>
				<td class="coluna-tabela text-center">Faltas</td>
				<td class="coluna-tabela text-center">Media</td>
				<td class="coluna-tabela text-center">Md final</td>
				<td class="coluna-tabela text-center">situaçao</td>
				
			</tr>

			<?php
                          
							foreach ($alunos as $key => $value) {
						    $notas = MainMolde::select('notas','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_aluno = ? AND cod_diciplina = ? AND cod_professo = ?',array($value['cod_curso'] ,
			                $value['cod_serie'],$value['cod_ano'],$value['cod_aluno'],$diciplina,$professo )); 
			                $faltas = MainMolde::selectAll('presenca','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_professo = ? AND cod_aluno = ? AND cod_materia = ?',array($curso,$serie,$ano,$professo,
			                $value['cod_aluno'],$diciplina)); 			       
			                $i++;
                           

                            foreach ($faltas as $key => $value) 
                            {
                                @$falta[$i] += $value['faltas'];
                              
                            }

                            if (@$falta[$i] == "") 
                            {
                            	@$falta[$i] = 0;
                            }

			                if (@$notas[21] == 1 || @$data[0] == "" || @$data[0] == 0000-00-00) 
			                {
			                	$data[0] = $dataAtual;
			                }
			                if (@$notas[22] == 1 || @$data[1] == "" || @$data[1] == 0000-00-00) 
			                {
			                	$data[1] = $dataAtual;
			                }
			                if (@$notas[23] == 1 || @$data[2] == "" || @$data[2] == 0000-00-00) 
			                {
			                	$data[2] = $dataAtual;
			                }
			                if (@$notas[24] == 1 || @$data[3] == "" || @$data[3] == 0000-00-00) 
			                {
			                	$data[3] = $dataAtual;
			                }
			                		                

			?>
            
			<tr>	
				                <input type="hidden" name="cod_aluno"       value="<?php echo $value['cod_aluno'] ?>" >
							    <input type="hidden" name="cod_curso"       value="<?php echo $value['cod_curso'] ?>" >
							    <input type="hidden" name="cod_serie"       value="<?php echo $value['cod_serie'] ?>" >
							    <input type="hidden" name="cod_ano"         value="<?php echo $value['cod_ano'] ?>" >
							    <input type="hidden" name="nome_diciplina"  value="<?php echo $diciplina ?>" >
							    <input type="hidden" name="professo"        value="<?php echo $professo ?>" >
								<td class="coluna-tabela text-center"><?php $matricula = $value['cod_aluno']; $aluno = MainMolde::select('alunos2','matricula = ? ',array($matricula)); print_r($aluno[1]) ?></td>
								<td class="coluna-tabela text-center"><input class="nota" value= "<?php print_r($notas[6]) ?>"  type="text" name="nota01<?php echo $value['cod_aluno'] ?>"
								  Readonly ></td>
								<td class="coluna-tabela text-center"><input class="nota" value= "<?php print_r($notas[7]) ?>"  type="text" name="rec01<?php echo $value['cod_aluno'] ?>" 
								  Readonly ></td>
								<td class="coluna-tabela text-center"><input class="nota" value= "<?php print_r($notas[8]) ?>"  type="text" name="nota02<?php echo $value['cod_aluno'] ?>"  
							      Readonly ></td>
								<td class="coluna-tabela text-center"><input class="nota" value= "<?php print_r($notas[9]) ?>"  type="text" name="rec02<?php echo $value['cod_aluno'] ?>" 
								´ Readonly ></td> 
								<td class="coluna-tabela text-center"><input class="nota" value= "<?php print_r($notas[10]) ?>" type="text" name="nota03<?php echo $value['cod_aluno'] ?>"  
								  Readonly></td>
								<td class="coluna-tabela text-center"><input class="nota" value= "<?php print_r($notas[11]) ?>" type="text" name="rec03<?php echo $value['cod_aluno'] ?>"   
								  Readonly ></td>
								<td class="coluna-tabela text-center"><input class="nota" value= "<?php print_r($notas[12]) ?>" type="text" name="nota04<?php echo $value['cod_aluno'] ?>"  
								  Readonly ></td>
								<td class="coluna-tabela text-center"><input class="nota" value= "<?php print_r($notas[13]) ?>" type="text" name="rec04<?php echo $value['cod_aluno'] ?>"  
								  Readonly  ></td> 
								<td class="coluna-tabela text-center"><?php echo @$falta[$i] ?></td>
								<td class="coluna-tabela text-center"><input type="text"  value= "<?php print_r($notas[20]) ?>" name="mediaparcial<?php echo $value['cod_aluno'] ?>" maxlength="2" disabled ></td>
								<td class="coluna-tabela text-center"><input type="text"  value= "<?php print_r($notas[14]) ?>" name="media<?php echo $value['cod_aluno'] ?>" maxlength="2" disabled ></td>
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
        
            <?php } ?>	
	    </table>
</div><!--wraper-table-->

    
	 <button type="button" class="btn btn-primary" id="btn-voltar"><a href="<?php echo INCLUDE_PATH_MAIN ?>diario-box?id=<?php echo $id ?>">Voltar<a></button>
	 	
</form>	

</div>
</div><!--content -- >

<!-- Modal -->
			<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
			  <div class="modal-dialog" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h5 class="modal-title" id="exampleModalLabel">Lança Notas</h5>
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
			          <span aria-hidden="true">&times;</span>
			        </button>
			      </div>
			            <form  method="post" enctype="multipart/form-data" >
						      <div class="modal-body text-center">					  					         
						          <input type="submit" actionBtn="enviar" name="1bm" value="1° Bimestre" class="btn btn-lg btn-primary" target	 
						          <?php if (@$notas[16] == 1 || $dataAtual > $data[0]) { ?>  disabled  <?php } ?> ><BR>
						          <input type="submit" actionBtn="enviar" name="2bm" value="2° Bimestre" class="btn btn-lg btn-primary" target  
						          <?php if (@$notas[17] == 1 || $dataAtual > $data[1]) { ?>  disabled  <?php } ?> ><BR>	
						          <input type="submit" actionBtn="enviar" name="3bm" value="3° Bimestre" class="btn btn-lg btn-primary" target 
						          <?php if (@$notas[18] == 1 || $dataAtual > $data[2]) { ?>  disabled <?php } ?>  ><BR>     
						          <input type="submit" actionBtn="enviar" name="4bm" value="4° Bimestre" class="btn btn-lg btn-primary" target 
						          <?php if (@$notas[19] == 1 || $dataAtual > $data[3]) { ?>  disabled  <?php } ?> ><BR> 
						      </div>
						</form>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
			      </div>
			    </div>
			  </div>
			</div>
<!-- Modsl -- >

