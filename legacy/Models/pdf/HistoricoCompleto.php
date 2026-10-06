<?php
    require_once __DIR__ . '/../../config.php';
    include(BASE_DIR . 'Lib/vendor/autoload.php');

    ob_start();
        require_once __DIR__ . '/../../config.php';
        if($_GET['id'])
         $_SESSION['IdHistorico'] = $_GET['id'];

        include(BASE_DIR . 'Views/pdf/HistoricoCompleto.php');
        $conteudo = ob_get_contents();
    ob_end_clean();

    $mpdf = new \Mpdf\Mpdf(['tempDir' => '/tmp/atena-mpdf']);
    error_reporting(0);
    $conteudo = preg_replace('~<(?:link|img)\b[^>]*>~i', '', $conteudo);
    $mpdf->WriteHTML(file_get_contents(BASE_DIR . 'Views/estilo/pdf.css'), \Mpdf\HTMLParserMode::HEADER_CSS);
    $mpdf->WriteHTML($conteudo);
    $mpdf->Output('Historico.pdf','I');
?>
