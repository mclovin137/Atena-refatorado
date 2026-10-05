<?php 
        use \Models\MainMolde; 
        $aluno = $_SESSION['matricula'];
        $ano = date('Y',time());
	    $alunos = MainMolde::select('matriculados','cod_aluno = ? AND cod_ano = ?',array($aluno,$ano));
	    $curso = $alunos[1];
	    $serie = $alunos[2];
	    $cod = $alunos[5];
      

			    $horariosegunda01 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND  dia = 'segunda' AND horario = '07:00' ");
                $horariosegunda01->execute(array($ano,$serie,$curso));
		        $horariosegunda01 = $horariosegunda01->fetch();

		        $horariosegunda02 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND  dia = 'segunda' AND horario = '08:55' ");
                $horariosegunda02->execute(array($ano,$serie,$curso));
		        $horariosegunda02 = $horariosegunda02->fetch();

		        $horariosegunda03 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND  dia = 'segunda' AND horario = '09:10' ");
                $horariosegunda03->execute(array($ano,$serie,$curso));
		        $horariosegunda03 = $horariosegunda03->fetch();

		        $horariosegunda04 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND  dia = 'segunda' AND horario = '10:05' ");
                $horariosegunda04->execute(array($ano,$serie,$curso));
		        $horariosegunda04 = $horariosegunda04->fetch();
		        

                $horarioterca01 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND  dia = 'terca' AND horario = '07:00' ");
                $horarioterca01->execute(array($ano,$serie,$curso));
		        $horarioterca01 = $horarioterca01->fetch();

		        $horarioterca02 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND  dia = 'terca' AND horario = '08:55' ");
                $horarioterca02->execute(array($ano,$serie,$curso));
		        $horarioterca02 = $horarioterca02->fetch();

		        $horarioterca03 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND  dia = 'terca' AND horario = '09:10' ");
                $horarioterca03->execute(array($ano,$serie,$curso));
		        $horarioterca03 = $horarioterca03->fetch();

		        $horarioterca04 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND  dia = 'terca' AND horario = '10:05' ");
                $horarioterca04->execute(array($ano,$serie,$curso));
		        $horarioterca04 = $horarioterca04->fetch();
		    

                $horarioquarta01 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND  dia = 'quarta' AND horario = '07:00' ");
                $horarioquarta01->execute(array($ano,$serie,$curso));
		        $horarioquarta01 = $horarioquarta01->fetch();

		        $horarioquarta02 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND  dia = 'quarta' AND horario = '08:55' ");
                $horarioquarta02->execute(array($ano,$serie,$curso));
		        $horarioquarta02 = $horarioquarta02->fetch();

		        $horarioquarta03 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND  dia = 'quarta' AND horario = '09:10' ");
                $horarioquarta03->execute(array($ano,$serie,$curso));
		        $horarioquarta03 = $horarioquarta03->fetch();

		        $horarioquarta04 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND  dia = 'quarta' AND horario = '10:05' ");
                $horarioquarta04->execute(array($ano,$serie,$curso));
		        $horarioquarta04 = $horarioquarta04->fetch();
		  


                $horarioquinta01 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND  dia = 'quinta' AND horario = '07:00' ");
                $horarioquinta01->execute(array($ano,$serie,$curso));
		        $horarioquinta01 = $horarioquinta01->fetch();
 
		        $horarioquinta02 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND  dia = 'quinta' AND horario = '08:55' ");
                $horarioquinta02->execute(array($ano,$serie,$curso));
		        $horarioquinta02 = $horarioquinta02->fetch();

		        $horarioquinta03 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND  dia = 'quinta' AND horario = '09:10' ");
                $horarioquinta03->execute(array($ano,$serie,$curso));
		        $horarioquinta03 = $horarioquinta03->fetch();

		        $horarioquinta04 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND  dia = 'quinta' AND horario = '10:05' ");
                $horarioquinta04->execute(array($ano,$serie,$curso));
		        $horarioquinta04 = $horarioquinta04->fetch();
		    

                $horariosexta01 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND  dia = 'sexta' AND horario = '07:00' ");
                $horariosexta01->execute(array($ano,$serie,$curso));
		        $horariosexta01 = $horariosexta01->fetch();

		        $horariosexta02 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND  dia = 'sexta' AND horario = '08:55' ");
                $horariosexta02->execute(array($ano,$serie,$curso));
		        $horariosexta02 = $horariosexta02->fetch();

		        $horariosexta03 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND dia = 'sexta' AND horario = '09:10' ");
                $horariosexta03->execute(array($ano,$serie,$curso));
		        $horariosexta03 = $horariosexta03->fetch();

		        $horariosexta04 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND  dia = 'sexta' AND horario = '10:05' ");
                $horariosexta04->execute(array($ano,$serie,$curso));
		        $horariosexta04 = $horariosexta04->fetch();
		    
			 

			
	    
 
?>
<div class="content">
<div class="box-content-diario">
<h2 class="text-center"><i class="fa fa-id-card-o" aria-hidden="true"></i> Horario </h2><br>
<br>		

<div class="content-turmas ">
     <div class="wraper-table">				
				<table>

						<tr>
						    <td class="coluna-tabela text-center">Horario</td>
							<td class="coluna-tabela text-center">Segunda</td>
							<td class="coluna-tabela text-center">Terça</td>
							<td class="coluna-tabela text-center">Quarta</td>
							<td class="coluna-tabela text-center">Quinta</td>
						    <td class="coluna-tabela text-center">Sexta</td>
								
						</tr>
				

		   				<tr>	
					        <td class="coluna-tabela text-center">07:00 - 07:55</td>
							<td class="coluna-tabela text-center"><?php print_r(@$horariosegunda01[2]) ?></td>
							<td class="coluna-tabela text-center"><?php print_r(@$horarioterca01[2]) ?></td>
							<td class="coluna-tabela text-center"><?php print_r(@$horarioquarta01[2]) ?></td>
							<td class="coluna-tabela text-center"><?php print_r(@$horarioquinta01[2]) ?></td>	
							<td class="coluna-tabela text-center"><?php print_r(@$horariosexta01[2]) ?></td>																	
						</tr>

						<tr>	
					        <td class="coluna-tabela text-center">07:55 - 08:50</td>
							<td class="coluna-tabela text-center"><?php print_r(@$horariosegunda02[2]) ?></td>
							<td class="coluna-tabela text-center"><?php print_r(@$horarioterca02[2]) ?></td>
							<td class="coluna-tabela text-center"><?php print_r(@$horarioquarta02[2]) ?></td>
							<td class="coluna-tabela text-center"><?php print_r(@$horarioquinta02[2]) ?></td>	
							<td class="coluna-tabela text-center"><?php print_r(@$horariosexta02[2]) ?></td>																															
						</tr> 

						<tr>	
					        <td class="coluna-tabela text-center">09:10 - 10:05</td>
							<td class="coluna-tabela text-center"><?php print_r(@$horariosegunda03[2]) ?></td>
							<td class="coluna-tabela text-center"><?php print_r(@$horarioterca03[2]) ?></td>
							<td class="coluna-tabela text-center"><?php print_r(@$horarioquarta03[2]) ?></td>
							<td class="coluna-tabela text-center"><?php print_r(@$horarioquinta03[2]) ?></td>	
							<td class="coluna-tabela text-center"><?php print_r(@$horariosexta03[2]) ?></td>																																
						</tr>

						<tr>	
					        <td class="coluna-tabela text-center">10:05 - 11:00</td>
							<td class="coluna-tabela text-center"><?php print_r(@$horariosegunda04[2]) ?></td>
							<td class="coluna-tabela text-center"><?php print_r(@$horarioterca04[2]) ?></td>
							<td class="coluna-tabela text-center"><?php print_r(@$horarioquarta04[2]) ?></td>
							<td class="coluna-tabela text-center"><?php print_r(@$horarioquinta04[2]) ?></td>	
							<td class="coluna-tabela text-center"><?php print_r(@$horariosexta04[2]) ?></td>																															
						</tr>		

		
				</table>	
	</div><!--wraper-table--> 
</div><!--content-turmas--> 
</div><!-- content-diario-->
</div><!--content -- >