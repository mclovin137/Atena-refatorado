<?php
	include('../../Lib/vendor/autoload.php');

	ob_start();
		include('../../config.php');
		if($_GET['id'])
         $_SESSION['IdHistorico'] = $_GET['id'];

		include('../../Views/pdf/HistoricoCompleto.php');
		$conteudo = ob_get_contents();
	ob_end_clean();

	$mpdf = new \Mpdf\Mpdf();
	error_reporting(0); 
	$mpdf->WriteHTML($conteudo);
	$mpdf->Output('Historico.pdf','I');
?>
