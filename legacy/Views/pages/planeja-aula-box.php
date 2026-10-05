<?php
use \Models\MainMolde;
$id = $_SESSION['id']; 
$turma = MainMolde::select('diciplina','id = ?',array($id));
$data = isset($_GET['dia']) ? $_GET['dia'] : \Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
$url = explode('/',$_GET['dia']);
$dia = $url[0];
$mes = $url[1];
$ano_date = substr($url[2],-4);
$dia_anterio = $dia - 1;
$proximo_dia = $dia + 1;
$numeroDias = cal_days_in_month(CAL_GREGORIAN,$mes,$ano_date);
if($dia <= 1)
{
  
	$dia_anterio = 01;
}

if($dia >= $numeroDias)
{
   $proximo_dia = $numeroDias;
}

$diciplina = $turma[1];
$anoo = $turma[2];
$ano = substr($anoo,-4);	
$curso = $turma[3];	
$serie = $turma[4];
$i = 0;
$dataAtual = date('Y-m-d');
$datas = MainMolde::select('datas','',array());
$cod = $turma[5]; 
$alunos = MainMolde::selectAll_ASC('matriculados','cod_curso = ? AND cod_serie = ? AND cod_ano = ?','nome',array($curso,$serie,$anoo));
$conteudos = MainMolde::select('aulas','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND data = ?  AND cod_professo = ? AND cod_materia = ?',array($curso,$serie,$anoo,$data,$cod,$diciplina));
foreach ($alunos as $key => $value) 
{ 
	           $notas = MainMolde::select('notas','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_aluno = ? AND cod_diciplina = ? AND cod_professo = ?',array($value['cod_curso'] ,
			   $value['cod_serie'],$value['cod_ano'],$value['cod_aluno'],$diciplina,$cod )); 
			   $i++;
												                           
	           if (@$notas[21] == 1 || @$datas[0] == "" || @$datas[0] == 0000-00-00) 
               {
			          $datas[0] = $dataAtual;
			   }
               if (@$notass[22] == 1 || @$datas[1] == "" || @$datas[1] == 0000-00-00) 
	           {
					  $datas[1] = $dataAtual;
	           }
	           if (@$notas[23] == 1 || @$datas[2] == "" || @$datas[2] == 0000-00-00) 
			   {
					   $datas[2] = $dataAtual;
			   }
		       if (@$notas[24] == 1 || @$datas[3] == "" || @$datas[3] == 0000-00-00) 
			   {
					   $datas[3] = $dataAtual;
			   }
}

$AnoAtual = date('Y');

	if($ano != $AnoAtual)
		\Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
?>
<div class="content">
<div class="box-content-diario">
	<div class="frequencia">
			     <h2 class="text-center"></h2>
			     <h2 class="text-center"><i class="fa fa-calendar" aria-hidden="true"></i> frequencia e planejamento | <u><?php echo $data ?></u></h2><br>
		         <form class="ajax-frequencia" action="<?php echo INCLUDE_PATH?>Models/ajax/EnviarAlaiacao.php" method="post" enctype="multipart/form-data">
				        <div class="wraper-table " >		
							<table>

								<tr>									
									<td class="coluna-tabela text-center">Curso</td>
									<td class="coluna-tabela text-center">Serie</td>
									<td class="coluna-tabela text-center">Materia</td>
									<td class="coluna-tabela text-center">Aluno</td>
									<td class="coluna-tabela text-center">Faltas</td>									
								</tr>

								<?php foreach ($alunos as $key => $value) {
									 $date = DateTime::createFromFormat('d/m/Y',$data);
	                                 $date = $date->format('Y-m-d');      
	                                 $presenca = MainMolde::select('presenca','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND data = ?  AND cod_professo = ? AND cod_materia = ? AND cod_aluno = ?',array($curso,$serie,$ano,$date,$cod,$diciplina,$value['cod_aluno']));
                                 
								?>

								<tr>
									<td class="coluna-tabela text-center"> <?php echo $value['cod_curso'] ?></td>
								    <td class="coluna-tabela text-center"> <?php echo $value['cod_serie'] ?></td>
								    <td class="coluna-tabela text-center"> <?php echo $diciplina?></td>
								    <td class="coluna-tabela text-center"> <?php echo $value['nome']  ?></td>
									<td class="coluna-tabela text-center"><input type="number" name="faltas<?php echo $value['cod_aluno']?>"  placeholder="faltas"  value="<?php  print_r($presenca[7]) ?>" step="1" max="8" min="0"></td>	

								</tr>
								
							    <?php } ?>

						    </table>	
					    </div><!--wraper-table-->		
						    
						     <input type="hidden" name="cod" value="<?php echo $cod ?>"> 
							 <input type="hidden" name="cod_serie" value="<?php echo $value['cod_serie']?>">
							 <input type="hidden" name="cod_curso" value="<?php echo $value['cod_curso']?>">
							 <input type="hidden" name="cod_ano"   value="<?php echo $value['cod_ano']?>">
							 <input type="hidden" name="cod_diciplina" value="<?php echo $diciplina ?>" >
							 <input type="hidden" name="data" value="<?php echo $data ?>" >
                              
                             
							 
						    
						    <div class="form-group">
								<label>Bimestre:</label><BR>
				                <select name="bimestre" class="form-select-bimestre-alua-box">	
		                                 <?php if ((@$notas[16] == 0  && $dataAtual <= $datas[0]) || $notas[21] == 1) { ?>				

	 		                          	      <option class="op-bimetre" value="1° bimestre">1° Bimestre</option>
										 <?php } ?>	
										 <?php if ((@$notas[17] == 0 && $dataAtual <= $datas[1]) || $notas[22] == 1) { ?>		
											   <option class="op-bimetre" value="2° bimestre">2° Bimestre</option>
										 <?php } ?>	
										 <?php if ((@$notas[18] == 0 && $dataAtual <= $datas[2]) || $notas[23] == 1) { ?>		
											   <option class="op-bimetre" value="3° bimestre">3° Bimestre</option>
										 <?php } ?>	
										 <?php if ((@$notas[19] == 0 && $dataAtual <= $datas[3]) || $notas[24] == 1) { ?>		
											   <option class="op-bimetre" value="4° bimestre">4° Bimestre</option>
										  <?php } ?>	
										  <?php if($presenca[9] != "") {?>
											   <option value="<?php print_r($presenca[9]) ?>" selected="selected"><?php  print_r($presenca[9]) ?></option>
										  <?php } ?>
										  }
					            </select>
						    </div><!--form-group--><br>

							<div class="form-group">
								<label>Quantidade de aulas:</label><br>
								<input type="number" class="form-select-bimestre-alua-box" step="1" min="1" max="8" name="aulas" value = "<?php echo $conteudos[8] ?>" required>
							</div><br>

					        <div class="form-single">
								    <label>Conteudo Ministrado:</label>
				                    <textarea type="text" name="aula-conteudo" class="conteudo-txt"  required><?php echo $conteudos[7]?></textarea> 
						    </div><!--form-group--><br>	

			
                             <?php if($dia < 9) { ?>
						 	 <button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>planeja-aula?id=<?php echo $id ?>&&ano=<?php echo $ano ?>">Volta<a></button>	
						 	 <input type="submit" name="cadastar_frequencia" value="Salva"  class="btn btn-primary">						 	
						 	 <button class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN?>planeja-aula-box?dia=<?php echo 0,$dia_anterio.'/'.$mes.'/'.$ano_date?>"> dia anterio</a></button>						 
                             <button class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN?>planeja-aula-box?dia=<?php echo 0,$proximo_dia.'/'.$mes.'/'.$ano_date?>"> proximo dia </a></button> 
                             <?php } ?>                            
                             <?php if($dia == 9) { ?>
						 	 <button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>planeja-aula?id=<?php echo $id ?>&&ano=<?php echo $ano ?>">Volta<a></button>	
						 	 <input type="submit" name="cadastar_frequencia" value="Salva"  class="btn btn-primary">						 	
						 	 <button class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN?>planeja-aula-box?dia=<?php echo 0,$dia_anterio.'/'.$mes.'/'.$ano_date?>"> dia anterio</a></button>						 
                             <button class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN?>planeja-aula-box?dia=<?php echo $proximo_dia.'/'.$mes.'/'.$ano_date?>"> proximo dia </a></button> 
                             <?php } ?>
                             <?php if($dia == 10) { ?>
						 	 <button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>planeja-aula?id=<?php echo $id ?>&&ano=<?php echo $ano ?>">Volta<a></button>	
						 	 <input type="submit" name="cadastar_frequencia" value="Salva"  class="btn btn-primary">						 	
						 	 <button class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN?>planeja-aula-box?dia=<?php echo 0,$dia_anterio.'/'.$mes.'/'.$ano_date?>"> dia anterio</a></button>						 
                             <button class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN?>planeja-aula-box?dia=<?php echo $proximo_dia.'/'.$mes.'/'.$ano_date?>"> proximo dia </a></button> 
                             <?php } ?>
                             <?php if($dia > 10) { ?>
						 	<button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>planeja-aula?id=<?php echo $id ?>&&ano=<?php echo $ano ?>">Volta<a></button>	
						 	 <input type="submit" name="cadastar_frequencia" value="Salva"  class="btn btn-primary">						 	
						 	 <button class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN?>planeja-aula-box?dia=<?php echo $dia_anterio.'/'.$mes.'/'.$ano_date?>"> dia anterio</a></button>						 
                             <button class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN?>planeja-aula-box?dia=<?php echo $proximo_dia.'/'.$mes.'/'.$ano_date?>"> proximo dia </a></button> 
                             <?php } ?>
				
				</form>
				
	</div><!--frequencia-->
</div><!--box-contet-diario-->
</div><!--content -- >