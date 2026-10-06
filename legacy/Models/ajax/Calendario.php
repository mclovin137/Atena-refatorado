<?php
    require_once __DIR__ . '/../../config.php';
    use \Models\MainMolde;

    $data['sucesso'] = true;
    $data['mensagem'] = "";

    if(isset($_POST['acao']) && $_POST['acao'] == 'inserir')
    {
        $data = [];
        $data['tarefa'] = strip_tags($_POST['tarefa']);
        $date = $_POST['data'];
        $sql = \MySql::conectar()->prepare("INSERT INTO caledario (id, tarefa, data) VALUES (DEFAULT,?,?)");
        $sql->execute(array($data['tarefa'],$date));
        die(json_encode($data));
    }
    else if(isset($_POST['acao']) && $_POST['acao'] == 'puxar')
    {
        $data = $_POST['data'];
        $sql = \MySql::conectar()->prepare("SELECT * FROM caledario WHERE data = ? ORDER BY id DESC");
        $sql->execute([$data]);
        $info = $sql->fetchAll();
        $box = "";
        foreach ($info as $key => $value)
        {
            /*
        <div class="box-tarefas-single">
            <h2><i class="fa fa-pencil"></i> <?php echo $value['tarefa']; ?></h2>
        </div>
        */
            $box.='<div class="box-tarefas-single">';
            $box.='<h2><i class="fa fa-pencil"></i> '.\Seguranca::texto($value['tarefa']).'</h2>';
            $box.="</div>";
        }
        die($box);
    }
?>
