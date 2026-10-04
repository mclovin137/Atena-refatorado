<?php
    use \Models\MainMolde; 
	$mes = isset($_GET['mes']) ? (int)$_GET['mes'] : date('m',time());
	@$mes_indetifica = (int)$_GET['mes'];
	if(isset($_GET['mes']))
    {
	    $url = explode('/',$_GET['mes']);
	    if($url[0] < 10)
	    {
	      $mes = $url[0];
	    }
	    if($url[0] == 00)
	    {
          $mes = 12;
	    }
	}
	 
	
    $id = (int)$_GET['id']; 
	$ano = (int)$_GET['ano'];    
	
	if($mes > 12)
		$mes = 12;
	
    $anoo = $ano;	
	$numeroDias = cal_days_in_month(CAL_GREGORIAN,$mes,$anoo);
	$diaInicialdoMes = date('N',strtotime("$ano-$mes-01"));

	$diaDeHoje = date('d',time());

	$diaDeHoje = "$ano-$mes-$diaDeHoje";

	$meses = array('Janeiro','Fevereiro','Março','Abril','Maio','Junho','Julho','agosto','Setembro','Outubro','Novembro','Dezembro');

	//Nome do mês no formato de string!
	$nomeMes = $meses[(int)$mes-1];

	
	$AnoAtual = date('Y');
	if($anoo != $AnoAtual)
		\Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
?>
<div class="content">
<div class="box-content">
	<h2><i class="fa fa-calendar" aria-hidden="true"></i> Calendário e Agenda | <u><?php echo $nomeMes ?></u>/<?php echo $ano; ?></h2></h2>

    <div class="wraper-table">
		<table class="calendario-table">	
			<tr>
				<td>Domingo</td>
				<td>Segunda</td>
				<td>Terça</td>
				<td>Quarta</td>
				<td>Quinta</td>
				<td>Sexta</td>
				<td>Sabado</td>
			</tr>

			<?php
				$n = 1;
				$z = 0;
				$numeroDias+=$diaInicialdoMes;
				while ($n <= $numeroDias) {
					if($diaInicialdoMes == 7 && $z != $diaInicialdoMes)
					{
						$z = 7;
						$n = 8;
					}
					if($n % 7 == 1)
					{
						echo '<tr>';
					}

					if($z >= $diaInicialdoMes)
					{
						
						$dia = $n - $diaInicialdoMes;

						if($dia < 10)
						{
							$dia = str_pad($dia, strlen($dia)+1, "0", STR_PAD_LEFT);
						}

						$atual = "$ano-$mes-$dia";
						$id = $_SESSION['id']; 
						$turma = MainMolde::select('diciplina','id = ?',array($id));
						$data = $atual;
						$diciplina = $turma[1];
						$ano = $turma[2];
						$ano = substr($ano,-4);	
						$curso = $turma[3];	
						$serie = $turma[4];
					    $cod = $turma[5]; 
						$falta = MainMolde::select('horario','cod_curos = ? AND cod_serie = ? AND cod_ano = ? AND cod_diciplina = ? AND data = ? AND cod_professo = ? ',array($curso,$serie,$ano,$diciplina,$atual,$cod));
						$atividade = MainMolde::selectAll('presenca','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia = ? AND data = ? AND cod_professo = ?',array($curso,$serie,$ano,$diciplina,$atual,$cod));
                        $atividade_day = ceil(count($atividade));
                       
                       
                        if ($atual == $falta[6]) 
                        {
                        	 if($atividade_day == 0)
	                         {
                        	    echo '<td dia="'.$atual.'" class="day-tarefa-pendente">'.$dia.'</td>';
                        	 }
                        	 else
                        	 {
                        	 	echo '<td dia="'.$atual.'" class="day-tarefa-concluida">'.$dia.'</td>';
                        	 }
						}
						else
						{
							if($atividade_day != 0 )
							{
								echo '<td dia="'.$atual.'" class="day-tarefa-concluida">'.$dia.'</td>';
							}
							else
							{
								echo '<td dia="'.$atual.'" class="day">'.$dia.'</td>';
							}
						}
					}
					else
					{
				        	echo "<td></td>";
							$z++;
						
					}
					if($n % 7 == 0)
					{
						echo '</tr>';
					}
					$n++;
					    
				}
			?>
		</table>
    </div><!-- warper-table-->	
  
    <button class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN?>planeja-aula?mes=<?php echo 0,$mes - 1?>&&id=<?php echo $id ?>&&ano=<?php echo $ano ?>"> mes anterio</a></button>
    <button class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN?>planeja-aula?mes=<?php echo 0,$mes + 1?>&&id=<?php echo $id ?>&&ano=<?php echo $ano?> "> proximo mes </a></button>
    <button class="btn btn-primary card-link"><a href="<?php echo INCLUDE_PATH_MAIN?>planeja-aula-box?dia=<?php echo date('d/m/Y',time()); ?>"> Planeja</a></button>
    <button type="button" class="btn btn-primary" id="btn-voltar"><a href="<?php echo INCLUDE_PATH_MAIN ?>diario-box?id=<?php echo $id ?>">Voltar<a></button>
	
	
</div><!--box-content-->
</div><!--content -- >