<?php
    include('../../Lib/vendor/autoload.php');
   
	ob_start();
		include('../../Views/pdf/ListarAlunos.php');
		$conteudo = ob_get_contents();
	ob_end_clean();

	$mpdf = new \Mpdf\Mpdf();
	$mpdf->WriteHTML($conteudo);
	$mpdf->Output('ListarAlunos.pdf','I');
?>
