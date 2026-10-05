<?php
    include('../../Lib/vendor/autoload.php');
   
	ob_start();
		include('../../Views/pdf/ListarProd.php');
		$conteudo = ob_get_contents();
	ob_end_clean();

	$mpdf = new \Mpdf\Mpdf();
	$mpdf->WriteHTML($conteudo);
	$mpdf->Output('Professores.pdf','I');
?>
