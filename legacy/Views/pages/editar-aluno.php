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
					  <input type="text" name="nome" id="nome" value="<?php echo $aluno['nome']; ?>" required  pattern = ".{4,}" >
					  <input type="hidden" name="nomeatual" value="<?php echo $aluno['nome']; ?>">
			    </div><!--form-group-->

                <div class="form-group">
					 <input type="hidden" name="matriculaatual" value="<?php echo $aluno['matricula']; ?>">
			    </div><!--form-group-->
				
				<div class="form-group">
					  <label>Cpf:</label>
				      <input type="text" name="cpf" value="<?php echo $aluno['cpf']; ?>"  required  pattern = ".{14}" >
				      <input type="hidden" name="cpfatual" value="<?php echo $aluno['cpf']; ?>">
				</div><!--form-group-->

				<div class="form-group">
					  <label>Cep:</label>
				      <input type="text" name="cep" value="<?php echo $aluno['cep']; ?>"required  pattern = ".{9}" >
				</div><!--form-group-->

				<div class="form-group">
					  <label>Endereço:</label>
					  <input type="text"  name = "endereço" value="<?php echo $aluno['endereço']; ?>" required  >
				</div><!--form-group-->

				<div class="form-group">
					  <label>Nome Do Pai (opcional) :</label>
				      <input type="text" name="pai" value="<?php echo $aluno['pai']; ?>" pattern = ".{4,}" >
				</div><!--form-group-->

				<div class="form-group">
					  <label>Nome Da Mae ou Responsavel:</label>
				      <input type="text" name="mae" value="<?php echo $aluno['mae']; ?>" required pattern = ".{4,}" >
				</div><!--form-group-->

				<div class="form-group">
					  <label>Data de Nacimento:</label>
				      <input type="date" name="data_de_nacimento" min="2000-01-01" max="2019-01-01" value="<?php echo $aluno['data_de_nacimento']; ?>" required  >
				</div><!--form-group-->

				<div class="form-group">
					  <label>Telefone do Responsavel: </label>
				      <input type="text" class = "telefone"  value="<?php echo $aluno['telefone_responsavel']; ?>" name="telefone_responsavel" required pattern = ".{15}">
				</div><!--form-group-->

				<div class="form-group">
					  <label>E-mail do Responsavel: </label>
				      <input type="text" name="email_responsavel" value="<?php echo $aluno['email_responsavel']; ?>" required pattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$">
				</div><!--form-group-->


				<div class="form-group">
					  <label>Telefone:</label>
				      <input type="text" name="telefone"  value="<?php echo $aluno['telefone']; ?>" required pattern = ".{15}">
				      <input type="hidden" name="telefoneatual" value="<?php echo $aluno['telefone']; ?>">
				</div><!--form-group-->

				<div class="form-group">
					  <label>Email:</label>
				      <input type="text" name="email"  value="<?php echo $aluno['email']; ?>"  required pattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" >
				      <input type="hidden" name="emailatual"  value="<?php echo $aluno['email']; ?>" >
				</div><!--form-group-->

					 <div class="form-group">
					  <label>Estado Civil</label>
					  <select name="estado_civil" value="<?php echo $aluno['estado_civil']; ?>">
					  	    <option value="<?php echo $aluno['estado_civil']; ?>" selected="selected"><?php echo  $aluno['estado_civil'];; ?></option>
						    <option value="Solteiro">Solteiro</option>
						    <option value="Viuvo">Viuvo</option>
						    <option value="Casado">Casado</option>
			         </select>
				</div><!--form-group-->	

		        <div class="form-group">
					  <label>Sexo:</label>
					  <select name="sexo" value="<?php echo $aluno['sexo']; ?>">
					  	    <option value="<?php echo $aluno['sexo']; ?>" selected="selected"><?php echo  $aluno['sexo']; ?></option>
						    <option value="Masculino">Masculino</option>
						    <option value="Femenino">Femenino</option>
			         </select>
				</div><!--form-group-->	

				<div class="form-group">
					  <label>Senha:</label>
					  <input type="hidden" name="senhaatual"  value="<?php echo $aluno['senha']; ?>">
					  <input type="text" name="novasenha"  placeholder=" nova senha" pattern = ".{4,}">
				</div><!--form-group-->	

			    <div class="form-group">
					  <label> Nova Imagem:</label>
					  <input type="file" name="imagem" >
					  <input type="hidden" name="imagematual"  value="<?php echo $aluno['imagem']; ?>">
			    </div><!--form-group-->
                
              
				<div class="form-group">
			         <input type="submit" name="Editar-aluno" value="Atualiza!">
		        </div><!--form-group-->
     </form>
</section><!--box-content-->
</div><!-- content -- >