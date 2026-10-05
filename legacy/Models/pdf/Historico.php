<?php
    include('../../Lib/vendor/autoload.php');

    
	ob_start();
		include('../../Views/pdf/Historico.php');
		$conteudo = ob_get_contents();
	ob_end_clean();

	$mpdf = new \Mpdf\Mpdf();
	error_reporting(0); 
	$mpdf->WriteHTML($conteudo);
	$mpdf->Output('Boletim.pdf','I');
?>
