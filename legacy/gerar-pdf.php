<?php
    include('Lib/vendor/autoload.php');
   
	
	ob_start();
	    include('config.php');
		include('Views/pages/boletim-aluno-pdf.php');
		$conteudo = ob_get_contents();
	ob_end_clean();

	$mpdf = new \Mpdf\Mpdf();
	error_reporting(0); 
	$mpdf->WriteHTML($conteudo);
    $mpdf->Output('Boletim.pdf','I');
?>
