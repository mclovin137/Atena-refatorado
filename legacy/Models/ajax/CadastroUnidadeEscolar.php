<?php
	include('../../config.php');
    use \Models\MainMolde;

    $data['sucesso'] = true;
    $data['mensagem'] = "";    

    $nome = $_POST['nome'];
    $telefone = $_POST['telefone'];
    $endereco = $_POST['endereco'];
    $diretor = $_POST['diretor'];
    $duracao = $_POST['duracao'];
    $cnpj = $_POST['cnpj'];
    $quantidade_aulas = $_POST['quantidade_aulas'];
    @$file = $_FILES['img'];

    if(@$file != "") 
    {
        if(MainMolde::imagemValida($file) == false)
        {
            $data['sucesso'] = false;
            $upload = false;
        }
        else
        {
                //@unlink('../../Views/uploads/user.jpg');
                $formatoArquivo = explode('.',$file['name']);
                $imagemNome = "user".'.'.'jpg';
                move_uploaded_file($file['tmp_name'],BASE_DIR.'Views/uploads/'.$imagemNome);
        }
    }
    $verificar = MainMolde::select('unidade_escolar','',array());

    if($data['sucesso'] == true)
    {
        if($verificar != "")
        { 
            $sql = \MySql::conectar()->prepare("UPDATE `unidade_escolar` SET nome = ?,telefone = ?,endereço = ?,diretor = ?,minutos_aulas = ?,quantidade_aulas = ?,cnpj = ?");
            $sql->execute(array($nome,$telefone,$endereco,$diretor,$duracao,$quantidade_aulas,$cnpj));
            $data['mensagem'] = "Os dadaos foram atualizados com sucesso!";
        }
        else
        {
            $sql = \MySql::conectar()->prepare("INSERT INTO `unidade_escolar` VALUES (null,?,?,?,?,?,?,?)");
            $sql->execute(array($nome,$endereco,$diretor,$quantidade_aulas,$duracao,$telefone,$cnpj));
            $data['mensagem'] = "Os dadaos foram Cadastrados com sucesso!";
        }
    }
    else
        $data['mensagem'] = "imagens invalida,por favor tente com outro tipo de arquivo!";
    
    
	
    die(json_encode($data));
?>