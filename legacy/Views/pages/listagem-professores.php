<div class="content">
<div class="box-content">
    <h2><i class="fa fa-id-card-o" aria-hidden="true"></i> Professores Cadastrados</h2>
    <div class="busca">
        <h4><i class="fa fa-search"></i> Realizar uma busca</h4>
        <form method="post">
            <input placeholder="Procure por: Nome, Matricula, Email, Cpf" type="text" name="busca">
            <input type="submit" name="pesquisa" value="Buscar!">
        </form>
    </div><!--busca-->
    <div class="boxes">
    <?php
        $query = "";
        if(isset($_POST['pesquisa']))
        {
            $busca = $_POST['busca'];
            $query = " WHERE nome ILIKE ? OR matricula ILIKE ? OR email ILIKE ? OR cpf ILIKE ? ";
        }

        $professo = MySql::conectar()->prepare("SELECT * FROM professo2 $query");
        $professo->execute(isset($_POST['pesquisa']) ? array_fill(0,4,'%'.$busca.'%') : []);
        $professo = $professo->fetchAll();

        if(isset($_POST['pesquisa']))
        {
            echo '<div style="width:100%;" class="busca-result"><p>Foram encontrados <b>'.count($professo).'</b> resultado(s)</p></div>';
        }

        foreach($professo as $value){
    ?>
        <div class="box-single-wraper">
            <div class="box-single">
                <div class="topo-box">
                    <?php
                        if($value['imagem'] == ''){
                    ?>
                    <h2><i class="fa fa-user"></i></h2>
                    <?php }else{ ?>
                        <img src="<?php echo INCLUDE_PATH?>arquivo.php?nome=<?php echo \Seguranca::texto($value['imagem']); ?>" />
                    <?php } ?>
                </div><!--topo-box-->
                <div class="body-box">
                        <p><b><i class="fa fa-pencil"></i> Nome:</b>      <?php echo \Seguranca::texto($value['nome']); ?></p>
                        <p><b><i class="fa fa-pencil"></i> Email:</b>     <?php echo \Seguranca::texto($value['email']); ?></p>
                        <p><b><i class="fa fa-pencil"></i> Matricula:</b>     <?php echo \Seguranca::texto($value['matricula']); ?></p>
                        <p><b><i class="fa fa-pencil"></i> Cpf:</b>       <?php echo \Seguranca::texto($value['cpf']); ?></p>
                        <p><b><i class="fa fa-pencil"></i> Telefone:</b>  <?php echo \Seguranca::texto($value['telefone']); ?></p>
                        <div class="group-btn">
                            <a class="btn edit w100" href="<?php echo INCLUDE_PATH_MAIN ?>editar-professor?id=<?php echo \Seguranca::texto($value['id']); ?>"> <i class="fa fa-pencil"></i> Editar</a>
                        </div><!--group-btn-->
                </div><!--body-box-->
            </div><!--box-single-->
        </div><!--box-single-wraper-->

        <?php } ?>

        <div class="clear"></div>

    </div><!--boxes-->

</div><!--box-content-->

</div><!-- content -- >
