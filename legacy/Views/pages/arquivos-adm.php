<?php
    use \Models\MainMolde;
    if(isset($_GET['id']))
    {
        $id = (int) $_GET['id'];
        $turma = MainMolde::select('diciplina','id = ?',array($id));
        if (!$turma) \Seguranca::negar(404);
        $materia = $turma[1];
        $ano = $turma[2];
        $curso = $turma[3];
        $serie = $turma[4];
        $cod = $turma[5];
        $materiais = MainMolde::selectAll('materiais','cod_curso = ? AND cod_serie = ? AND cod_ano = ? AND cod_materia  = ?',array($curso,$serie,$ano,$materia));

    }
    else
    {
        \Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
    }

    $AnoAtual = date('Y');
    $anoo = substr($ano,-4);
    if($anoo != $AnoAtual)
        \Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
?>
<div class="content">
<section class="box-content center">
<h2><i class="fa fa-pencil"></i> Adicionar Materiais</h2>
     <form  method="post" enctype="multipart/form-data">

                 <div class="form-group">
                   <label>Nome:</label>
                   <input type="text" name="nome" required>
                 </div><!--form-group-->

                  <div class="form-group">
                   <label>Arquivo:</label>
                   <input type="file" name="arquivo" required>
                 </div><!--form-group-->

                 <div>
                    <input type="hidden" name="matricula" value="<?php echo \Seguranca::texto($cod) ?>">
                    <input type="hidden" name="curso" value="<?php echo \Seguranca::texto($curso) ?>">
                    <input type="hidden" name="materia" value="<?php echo \Seguranca::texto($materia) ?>">
                    <input type="hidden" name="serie" value="<?php echo \Seguranca::texto($serie) ?>">
                    <input type="hidden" name="ano" value="<?php echo \Seguranca::texto($ano) ?>">
                 </div>

                 <div class="form-group">
                     <input type="submit" name="upload" value="Cadastrar!">
                 </div><!--form-group-->
     </form><bR>


     <h2 class="text-center"> Materiais cadastrado</h2><br>

        <div class="listagem-csm">
                <?php  foreach ($materiais as $key => $value) { ?>
                    <div class="box-tarefas-single">
                      <ul>
                              <li class="list-group-item"><?php echo \Seguranca::texto($value['cod_serie'])?> - <?php echo \Seguranca::texto($value['cod_curso'])?> (<?php echo \Seguranca::texto($value['cod_materia'])?> ) - <?php echo \Seguranca::texto($value['nome'])?>  (<?php echo \Seguranca::texto($value['cod_ano'])?>)</li>
                              <li class="list-group-item"><a href="<?php echo INCLUDE_PATH?>arquivo.php?nome=<?php echo \Seguranca::texto($value['arquivo']); ?>"> Baixar</a></li>

                      </ul>
                    </div><!--box-tarefas-sling-->
               <?php }?>
               <div class="clear"></div>
        </div><!--box-tarefas-->

<br><br> <button type="button" class="btn btn-primary"><a href="<?php echo INCLUDE_PATH_MAIN ?>diario-box?id=<?php echo \Seguranca::texto($id) ?>">Voltar<a></button>
</section><!--box-principal-->
</div><!-- content -- >
