<?php 
        use \Models\MainMolde; 
        $prof = $_SESSION['matricula'];
        $ano = date('Y',time());

		
		
			    $horariosegunda01 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE dia = 'segunda' AND horario = '07:00'  AND cod_professo = ? AND cod_ano = $ano");
                $horariosegunda01->execute(array($prof));
		        $horariosegunda01 = $horariosegunda01->fetch();

		        $horariosegunda02 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE dia = 'segunda' AND horario = '08:55' AND cod_professo = ? AND cod_ano = $ano");
                $horariosegunda02->execute(array($prof));
		        $horariosegunda02 = $horariosegunda02->fetch();

		        $horariosegunda03 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE dia = 'segunda' AND horario = '09:10' AND cod_professo = ? AND cod_ano = $ano");
                $horariosegunda03->execute(array($prof));
		        $horariosegunda03 = $horariosegunda03->fetch();

		        $horariosegunda04 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE dia = 'segunda' AND horario = '10:05' AND cod_professo = ? AND cod_ano = $ano");
                $horariosegunda04->execute(array($prof));
		        $horariosegunda04 = $horariosegunda04->fetch();
		  

                $horarioterca01 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE dia = 'terca' AND horario = '07:00'   AND cod_professo = ? AND cod_ano = $ano");
                $horarioterca01->execute(array($prof));
		        $horarioterca01 = $horarioterca01->fetch();

		        $horarioterca02 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE dia = 'terca' AND horario = '08:55'   AND cod_professo = ? AND cod_ano = $ano");
                $horarioterca02->execute(array($prof));
		        $horarioterca02 = $horarioterca02->fetch();

		        $horarioterca03 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE dia = 'terca' AND horario = '09:10'   AND cod_professo = ? AND cod_ano = $ano");
                $horarioterca03->execute(array($prof));
		        $horarioterca03 = $horarioterca03->fetch();

		        $horarioterca04 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE dia = 'terca' AND horario = '10:05'   AND cod_professo = ? AND cod_ano = $ano");
                $horarioterca04->execute(array($prof));
		        $horarioterca04 = $horarioterca04->fetch();
		     
			
                $horarioquarta01 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE dia = 'quarta' AND horario = '07:00'  AND cod_professo = ? AND cod_ano = $ano");
                $horarioquarta01->execute(array($prof));
		        $horarioquarta01 = $horarioquarta01->fetch();

		        $horarioquarta02 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE dia = 'quarta' AND horario = '08:55'   AND cod_professo = ? AND cod_ano = $ano");
                $horarioquarta02->execute(array($prof));
		        $horarioquarta02 = $horarioquarta02->fetch();

		        $horarioquarta03 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE dia = 'quarta' AND horario = '09:10'   AND cod_professo = ? AND cod_ano = $ano");
                $horarioquarta03->execute(array($prof));
		        $horarioquarta03 = $horarioquarta03->fetch();

		        $horarioquarta04 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE dia = 'quarta' AND horario = '10:05'   AND cod_professo = ? AND cod_ano = $ano");
                $horarioquarta04->execute(array($prof));
		        $horarioquarta04 = $horarioquarta04->fetch();

		
                $horarioquinta01 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE dia = 'quinta' AND horario = '07:00'   AND cod_professo = ? AND cod_ano = $ano");
                $horarioquinta01->execute(array($prof));
		        $horarioquinta01 = $horarioquinta01->fetch();
 
		        $horarioquinta02 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE dia = 'quinta' AND horario = '08:55'   AND cod_professo = ? AND cod_ano = $ano");
                $horarioquinta02->execute(array($prof));
		        $horarioquinta02 = $horarioquinta02->fetch();

		        $horarioquinta03 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE dia = 'quinta' AND horario = '09:10'   AND cod_professo = ? AND cod_ano = $ano");
                $horarioquinta03->execute(array($prof));
		        $horarioquinta03 = $horarioquinta03->fetch();

		        $horarioquinta04 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE dia = 'quinta' AND horario = '10:05'   AND cod_professo = ? AND cod_ano = $ano");
                $horarioquinta04->execute(array($prof));
		        $horarioquinta04 = $horarioquinta04->fetch();

		    
                $horariosexta01 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE  dia = 'sexta' AND horario = '07:00'   AND cod_professo = ? AND cod_ano = $ano");
                $horariosexta01->execute(array($prof));
		        $horariosexta01 = $horariosexta01->fetch();

		        $horariosexta02 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE  dia = 'sexta' AND horario = '08:55'   AND cod_professo = ? AND cod_ano = $ano");
                $horariosexta02->execute(array($prof));
		        $horariosexta02 = $horariosexta02->fetch();

		        $horariosexta03 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE  dia = 'sexta' AND horario = '09:10'   AND cod_professo = ? AND cod_ano = $ano");
                $horariosexta03->execute(array($prof));
		        $horariosexta03 = $horariosexta03->fetch();

		        $horariosexta04 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE  dia = 'sexta' AND horario = '10:05'   AND cod_professo = ? AND cod_ano = $ano");
                $horariosexta04->execute(array($prof));
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
							<td class="coluna-tabela text-center"><?php print_r(@$horariosegunda01[2]) ?> <?php print_r(@$horariosegunda01[3]) ?> - <?php print_r(@$horariosegunda01[5]) ?></td>
							<td class="coluna-tabela text-center"><?php print_r(@$horarioterca01[2]) ?>   <?php print_r(@$horarioterca01[3]) ?> - <?php print_r(@$horarioterca01[5]) ?></td>
							<td class="coluna-tabela text-center"><?php print_r(@$horarioquarta01[2]) ?>  <?php print_r(@$horarioquarta01[3]) ?> - <?php print_r(@$horarioquarta01[5]) ?></td>
							<td class="coluna-tabela text-center"><?php print_r(@$horarioquinta01[2]) ?>  <?php print_r(@$horarioquinta01[3]) ?> - <?php print_r(@$horarioquinta01[5]) ?></td>	
							<td class="coluna-tabela text-center"><?php print_r(@$horariosexta01[2]) ?> <?php print_r(@$horariosexta01[3]) ?> - <?php print_r(@$horariosexta01[5]) ?></td>																	
						</tr>

						<tr>	
					        <td class="coluna-tabela text-center">07:55 - 08:50</td>
					        <td class="coluna-tabela text-center"><?php print_r(@$horariosegunda02[2]) ?> <?php print_r(@$horariosegunda02[3]) ?> - <?php print_r(@$horariosegunda02[5]) ?></td>
							<td class="coluna-tabela text-center"><?php print_r(@$horarioterca02[2]) ?>   <?php print_r(@$horarioterca02[3]) ?> - <?php print_r(@$horarioterca02[5]) ?></td>
							<td class="coluna-tabela text-center"><?php print_r(@$horarioquarta02[2]) ?>  <?php print_r(@$horarioquarta02[3]) ?> - <?php print_r(@$horarioquarta02[5]) ?></td>
							<td class="coluna-tabela text-center"><?php print_r(@$horarioquinta02[2]) ?>  <?php print_r(@$horarioquinta02[3]) ?> - <?php print_r(@$horarioquinta02[5]) ?></td>	
							<td class="coluna-tabela text-center"><?php print_r(@$horariosexta02[2]) ?> <?php print_r(@$horariosexta02[3]) ?> - <?php print_r(@$horariosexta02[5]) ?></td>																																	
						</tr> 

						<tr>	
					        <td class="coluna-tabela text-center">09:10 - 10:05</td>
					        <td class="coluna-tabela text-center"><?php print_r(@$horariosegunda03[2]) ?> <?php print_r(@$horariosegunda03[3]) ?> - <?php print_r(@$horariosegunda03[5]) ?></td>
							<td class="coluna-tabela text-center"><?php print_r(@$horarioterca03[2]) ?>   <?php print_r(@$horarioterca03[3]) ?> - <?php print_r(@$horarioterca03[5]) ?></td>
							<td class="coluna-tabela text-center"><?php print_r(@$horarioquarta03[2]) ?>  <?php print_r(@$horarioquarta03[3]) ?> - <?php print_r(@$horarioquarta03[5]) ?></td>
							<td class="coluna-tabela text-center"><?php print_r(@$horarioquinta03[2]) ?>  <?php print_r(@$horarioquinta03[3]) ?> - <?php print_r(@$horarioquinta03[5]) ?></td>	
							<td class="coluna-tabela text-center"><?php print_r(@$horariosexta03[2]) ?> <?php print_r(@$horariosexta03[3]) ?> - <?php print_r(@$horariosexta03[5]) ?></td>																																		
						</tr>

						<tr>	
					        <td class="coluna-tabela text-center">10:05 - 11:00</td>
					       <td class="coluna-tabela text-center"><?php print_r(@$horariosegunda04[2]) ?> <?php print_r(@$horariosegunda04[3]) ?> - <?php print_r(@$horariosegunda04[5]) ?></td>
							<td class="coluna-tabela text-center"><?php print_r(@$horarioterca04[2]) ?>   <?php print_r(@$horarioterca04[3]) ?> - <?php print_r(@$horarioterca04[5]) ?></td>
							<td class="coluna-tabela text-center"><?php print_r(@$horarioquarta04[2]) ?>  <?php print_r(@$horarioquarta04[3]) ?> - <?php print_r(@$horarioquarta04[5]) ?></td>
							<td class="coluna-tabela text-center"><?php print_r(@$horarioquinta04[2]) ?>  <?php print_r(@$horarioquinta04[3]) ?> - <?php print_r(@$horarioquinta04[5]) ?></td>	
							<td class="coluna-tabela text-center"><?php print_r(@$horariosexta04[2]) ?> <?php print_r(@$horariosexta04[3]) ?> - <?php print_r(@$horariosexta04[5]) ?></td>																													
						</tr>		

		
				</table>	
	</div><!--wraper-table--> 
</div><!--content-turmas--> 
</div><!-- content-diario-->
</div><!--content -- >