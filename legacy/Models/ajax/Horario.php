<?php

include('../../config.php');
use \Models\MainMolde;
$data = array();
$data['sucesso'] = true;
$data = "";

    @$ano = $_POST['ano'];
    @$curso = $_POST['curso'];
    @$serie = $_POST['serie'];
  
    
    if(($ano != "todos") &&  ($curso != "todos") && ($serie != "todos"))
    {    
       
	
			    $horariosegunda01 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND dia = 'segunda' AND horario = '07:00' ");
                $horariosegunda01->execute(array($ano,$serie,$curso));
		        $horariosegunda01 = $horariosegunda01->fetch();

		        $horariosegunda02 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND  dia = 'segunda' AND horario = '08:55' ");
                $horariosegunda02->execute(array($ano,$serie,$curso));
		        $horariosegunda02 = $horariosegunda02->fetch();

		        $horariosegunda03 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND dia = 'segunda' AND horario = '09:10' ");
                $horariosegunda03->execute(array($ano,$serie,$curso));
		        $horariosegunda03 = $horariosegunda03->fetch();

		        $horariosegunda04 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND dia = 'segunda' AND horario = '10:05' ");
                $horariosegunda04->execute(array($ano,$serie,$curso));
		        $horariosegunda04 = $horariosegunda04->fetch();
		        

                $horarioterca01 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND dia = 'terca' AND horario = '07:00' ");
                $horarioterca01->execute(array($ano,$serie,$curso));
		        $horarioterca01 = $horarioterca01->fetch();

		        $horarioterca02 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND dia = 'terca' AND horario = '08:55' ");
                $horarioterca02->execute(array($ano,$serie,$curso));
		        $horarioterca02 = $horarioterca02->fetch();

		        $horarioterca03 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND dia = 'terca' AND horario = '09:10' ");
                $horarioterca03->execute(array($ano,$serie,$curso));
		        $horarioterca03 = $horarioterca03->fetch();

		        $horarioterca04 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND dia = 'terca' AND horario = '10:05' ");
                $horarioterca04->execute(array($ano,$serie,$curso));
		        $horarioterca04 = $horarioterca04->fetch();
		    

                $horarioquarta01 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND dia = 'quarta' AND horario = '07:00' ");
                $horarioquarta01->execute(array($ano,$serie,$curso));
		        $horarioquarta01 = $horarioquarta01->fetch();

		        $horarioquarta02 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND dia = 'quarta' AND horario = '08:55' ");
                $horarioquarta02->execute(array($ano,$serie,$curso));
		        $horarioquarta02 = $horarioquarta02->fetch();

		        $horarioquarta03 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND dia = 'quarta' AND horario = '09:10' ");
                $horarioquarta03->execute(array($ano,$serie,$curso));
		        $horarioquarta03 = $horarioquarta03->fetch();

		        $horarioquarta04 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND dia = 'quarta' AND horario = '10:05' ");
                $horarioquarta04->execute(array($ano,$serie,$curso));
		        $horarioquarta04 = $horarioquarta04->fetch();
		  


                $horarioquinta01 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND dia = 'quinta' AND horario = '07:00' ");
                $horarioquinta01->execute(array($ano,$serie,$curso));
		        $horarioquinta01 = $horarioquinta01->fetch();
 
		        $horarioquinta02 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND dia = 'quinta' AND horario = '08:55' ");
                $horarioquinta02->execute(array($ano,$serie,$curso));
		        $horarioquinta02 = $horarioquinta02->fetch();

		        $horarioquinta03 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND dia = 'quinta' AND horario = '09:10' ");
                $horarioquinta03->execute(array($ano,$serie,$curso));
		        $horarioquinta03 = $horarioquinta03->fetch();

		        $horarioquinta04 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND dia = 'quinta' AND horario = '10:05' ");
                $horarioquinta04->execute(array($ano,$serie,$curso));
		        $horarioquinta04 = $horarioquinta04->fetch();
		    

                $horariosexta01 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND dia = 'sexta' AND horario = '07:00' ");
                $horariosexta01->execute(array($ano,$serie,$curso));
		        $horariosexta01 = $horariosexta01->fetch();

		        $horariosexta02 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND dia = 'sexta' AND horario = '08:55' ");
                $horariosexta02->execute(array($ano,$serie,$curso));
		        $horariosexta02 = $horariosexta02->fetch();

		        $horariosexta03 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND dia = 'sexta' AND horario = '09:10' ");
                $horariosexta03->execute(array($ano,$serie,$curso));
		        $horariosexta03 = $horariosexta03->fetch();

		        $horariosexta04 = MySql::conectar()->prepare("SELECT * FROM `horario` WHERE cod_ano = ? AND cod_serie = ? AND cod_curos = ? AND dia = 'sexta' AND horario = '10:05' ");
                $horariosexta04->execute(array($ano,$serie,$curso));
		        $horariosexta04 = $horariosexta04->fetch();


	
		   $data.='<div class="content-turmas">';	
		   $data.='<div class="wraper-table">
					
					<table>

						<tr>
						    <td class="coluna-tabela text-center">Horario</td>
							<td class="coluna-tabela text-center">Segunda</td>
							<td class="coluna-tabela text-center">Terça</td>
							<td class="coluna-tabela text-center">Quarta</td>
							<td class="coluna-tabela text-center">Quinta</td>
						    <td class="coluna-tabela text-center">Sexta</td>
								
						</tr>';
				

		            
			$data.='
					
						<tr>	
					        <td class="coluna-tabela text-center">07:00 - 07:55</td>
							<td class="coluna-tabela text-center">'.@$horariosegunda01[2].'</td>
							<td class="coluna-tabela text-center">'.@$horarioterca01[2].'</td>
							<td class="coluna-tabela text-center">'.@$horarioquarta01[2].'</td>
							<td class="coluna-tabela text-center">'.@$horarioquinta01[2].'</td>	
							<td class="coluna-tabela text-center">'.@$horariosexta01[2].'</td>																	
						</tr>

						<tr>	
					        <td class="coluna-tabela text-center">07:55 - 08:50</td>
							<td class="coluna-tabela text-center">'.@$horariosegunda02[2].'</td>
							<td class="coluna-tabela text-center">'.@$horarioterca02[2].'</td>
							<td class="coluna-tabela text-center">'.@$horarioquarta02[2].'</td>
							<td class="coluna-tabela text-center">'.@$horarioquinta02[2].'</td>	
							<td class="coluna-tabela text-center">'.@$horariosexta02[2].'</td>															
						</tr> 

						<tr>	
					        <td class="coluna-tabela text-center">09:10 - 10:05</td>
							<td class="coluna-tabela text-center">'.@$horariosegunda03[2].'</td>
							<td class="coluna-tabela text-center">'.@$horarioterca03[2].'</td>
							<td class="coluna-tabela text-center">'.@$horarioquarta03[2].'</td>
							<td class="coluna-tabela text-center">'.@$horarioquinta03[2].'</td>	
							<td class="coluna-tabela text-center">'.@$horariosexta03[2].'</td>																		
						</tr>

						<tr>	
					        <td class="coluna-tabela text-center">10:05 - 11:00</td>
							<td class="coluna-tabela text-center">'.@$horariosegunda04[2].'</td>
							<td class="coluna-tabela text-center">'.@$horarioterca04[2].'</td>
							<td class="coluna-tabela text-center">'.@$horarioquarta04[2].'</td>
							<td class="coluna-tabela text-center">'.@$horarioquinta04[2].'</td>	
							<td class="coluna-tabela text-center">'.@$horariosexta04[2].'</td>																	
						</tr>';

			

		    $data.='	
				    </table>	
			    </div><!--wraper-table--> ';

			$data.='</div><!--content-turmas--> ';
	}

     
     echo $data;
  
?>
