<?php
    use \Models\MainMolde;
    if(isset($_GET['id']))
    {
        $id = (int)$_GET['id'];
        $aluno = MainMolde::select('alunos2','id = ?',array($id));
    }
    else
    {
        \Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
    }
?>
<div class="content">
<section class="box-content center">
    <h2><i class="fa fa-pencil"></i> Editar Aluno</h2>
     <form class="ajaxx"  action="<?php echo INCLUDE_PATH?>Models/ajax/CadastroAluno.php" method="post" enctype="multipart/form-data" >
               <div class="form-group">
                      <label>Nome:</label>
                      <input type="text" name="nome" id="nome" value="<?php echo \Seguranca::texto($aluno['nome']); ?>" required  pattern = ".{4,}" >
                      <input type="hidden" name="nomeatual" value="<?php echo \Seguranca::texto($aluno['nome']); ?>">
                </div><!--form-group-->

                <div class="form-group">
                     <input type="hidden" name="matriculaatual" value="<?php echo \Seguranca::texto($aluno['matricula']); ?>">
                </div><!--form-group-->

                <div class="form-group">
                      <label>Cpf:</label>
                      <input type="text" name="cpf" value="<?php echo \Seguranca::texto($aluno['cpf']); ?>"  required  pattern = ".{14}" >
                      <input type="hidden" name="cpfatual" value="<?php echo \Seguranca::texto($aluno['cpf']); ?>">
                </div><!--form-group-->

                <div class="form-group">
                      <label>Cep:</label>
                      <input type="text" name="cep" value="<?php echo \Seguranca::texto($aluno['cep']); ?>"required  pattern = ".{9}" >
                </div><!--form-group-->

                <div class="form-group">
                      <label>Endereço:</label>
                      <input type="text"  name = "endereço" value="<?php echo \Seguranca::texto($aluno['endereço']); ?>" required  >
                </div><!--form-group-->

                <div class="form-group">
                      <label>Nome Do Pai (opcional) :</label>
                      <input type="text" name="pai" value="<?php echo \Seguranca::texto($aluno['pai']); ?>" pattern = ".{4,}" >
                </div><!--form-group-->

                <div class="form-group">
                      <label>Nome Da Mae ou Responsavel:</label>
                      <input type="text" name="mae" value="<?php echo \Seguranca::texto($aluno['mae']); ?>" required pattern = ".{4,}" >
                </div><!--form-group-->

                <div class="form-group">
                      <label>Data de Nacimento:</label>
                      <input type="date" name="data_de_nacimento" min="2000-01-01" max="2019-01-01" value="<?php echo \Seguranca::texto($aluno['data_de_nacimento']); ?>" required  >
                </div><!--form-group-->

                <div class="form-group">
                      <label>Telefone do Responsavel: </label>
                      <input type="text" class = "telefone"  value="<?php echo \Seguranca::texto($aluno['telefone_responsavel']); ?>" name="telefone_responsavel" required pattern = ".{15}">
                </div><!--form-group-->

                <div class="form-group">
                      <label>E-mail do Responsavel: </label>
                      <input type="text" name="email_responsavel" value="<?php echo \Seguranca::texto($aluno['email_responsavel']); ?>" required pattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$">
                </div><!--form-group-->


                <div class="form-group">
                      <label>Telefone:</label>
                      <input type="text" name="telefone"  value="<?php echo \Seguranca::texto($aluno['telefone']); ?>" required pattern = ".{15}">
                      <input type="hidden" name="telefoneatual" value="<?php echo \Seguranca::texto($aluno['telefone']); ?>">
                </div><!--form-group-->

                <div class="form-group">
                      <label>Email:</label>
                      <input type="text" name="email"  value="<?php echo \Seguranca::texto($aluno['email']); ?>"  required pattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" >
                      <input type="hidden" name="emailatual"  value="<?php echo \Seguranca::texto($aluno['email']); ?>" >
                </div><!--form-group-->

                     <div class="form-group">
                      <label>Estado Civil</label>
                      <select name="estado_civil" value="<?php echo \Seguranca::texto($aluno['estado_civil']); ?>">
                            <option value="<?php echo \Seguranca::texto($aluno['estado_civil']); ?>" selected="selected"><?php echo \Seguranca::texto($aluno['estado_civil']);; ?></option>
                            <option value="Solteiro">Solteiro</option>
                            <option value="Viuvo">Viuvo</option>
                            <option value="Casado">Casado</option>
                     </select>
                </div><!--form-group-->

                <div class="form-group">
                      <label>Sexo:</label>
                      <select name="sexo" value="<?php echo \Seguranca::texto($aluno['sexo']); ?>">
                            <option value="<?php echo \Seguranca::texto($aluno['sexo']); ?>" selected="selected"><?php echo \Seguranca::texto($aluno['sexo']); ?></option>
                            <option value="Masculino">Masculino</option>
                            <option value="Femenino">Femenino</option>
                     </select>
                </div><!--form-group-->

                <div class="form-group">
                      <label>Senha:</label>
                      <input type="hidden" name="senhaatual"  value="">
                      <input type="text" name="novasenha"  placeholder=" nova senha" pattern = ".{4,}">
                </div><!--form-group-->

                <div class="form-group">
                      <label> Nova Imagem:</label>
                      <input type="file" name="imagem" >
                      <input type="hidden" name="imagematual"  value="<?php echo \Seguranca::texto($aluno['imagem']); ?>">
                </div><!--form-group-->


                <div class="form-group">
                     <input type="submit" name="Editar-aluno" value="Atualiza!">
                </div><!--form-group-->
     </form>
</section><!--box-content-->
</div><!-- content -- >
