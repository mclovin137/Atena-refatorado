<?php
    require_once __DIR__ . '/../../config.php';
    include(BASE_DIR . 'Lib/vendor/autoload.php');

    ob_start();
        include(BASE_DIR . 'Views/pdf/ListarAlunos.php');
        $conteudo = ob_get_contents();
    ob_end_clean();

    $mpdf = new \Mpdf\Mpdf(['tempDir' => '/tmp/atena-mpdf']);
    $conteudo = preg_replace('~<(?:link|img)\b[^>]*>~i', '', $conteudo);
    $mpdf->WriteHTML(file_get_contents(BASE_DIR . 'Views/estilo/pdf.css'), \Mpdf\HTMLParserMode::HEADER_CSS);
    $mpdf->WriteHTML($conteudo);
    $mpdf->Output('ListarAlunos.pdf','I');
?>
