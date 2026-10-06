<?php
    require_once __DIR__ . '/config.php';
    require_once __DIR__ . '/Lib/vendor/autoload.php';


    ob_start();
        require_once __DIR__ . '/config.php';
        include('Views/pages/boletim-aluno-pdf.php');
        $conteudo = ob_get_contents();
    ob_end_clean();

    $mpdf = new \Mpdf\Mpdf(['tempDir' => '/tmp/atena-mpdf']);
    error_reporting(0);
    $conteudo = preg_replace('~<(?:link|img)\b[^>]*>~i', '', $conteudo);
    $mpdf->WriteHTML($conteudo);
    $mpdf->Output('Boletim.pdf','I');
?>
