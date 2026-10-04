<?php
    use \Models\MainMolde;
	if(isset($_GET['id']))
	{
		$id = (int)$_GET['id'];
		$Professo = MainMolde::select('professo2','id = ?',array($id));		
	}
	else
	{
		\Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
	}
?>
<div class="content">
<section class="box-content center">
	<h2><i class="fa fa-pencil"></i> Editar Professo</h2>
	 <form class="ajaxx"  action="<?php echo INCLUDE_PATH?>Models/ajax/CadastroProfessor.php" method="post" enctype="multipart/form-data" >
	           <div class="form-group">
			          <label>Nome:</label>
					  <input type="text" name="nome" id="nome" value="<?php echo $Professo['nome']; ?>" required  pattern = ".{4,}" >
					  <input type="hidden" name="nomeatual" value="<?php echo $Professo['nome']; ?>">
			    </div><!--form-group-->

				<div class="form-group">
					  <input type="hidden" name="matriculaatual" value="<?php echo $Professo['matricula']; ?>">
			    </div><!--form-group-->

				<div class="form-group">
					  <label>Cpf:</label>
				      <input type="text" name="cpf" value="<?php echo $Professo['cpf']; ?>" required  pattern = ".{14}" >
				      <input type="hidden" name="cpfatual" value="<?php echo $Professo['cpf']; ?>">
				</div><!--form-group-->

				<div class="form-group">
					  <label>Cep:</label>
				      <input type="text" name="cep" value="<?php echo $Professo['cep']; ?>"required  pattern = ".{9}" >
				</div><!--form-group-->

				<div class="form-group">
					  <label>Data de Nacimento:</label>
				      <input type="date" name="data_de_nacimento" min="1900-01-01" max="2019-01-01" value="<?php echo $Professo['data_de_nacimento']; ?>" required  >
				</div><!--form-group-->

				<div class="form-group">
					  <label>Telefone:</label>
				      <input type="text" name="telefone"  value="<?php echo $Professo['telefone']; ?>" required pattern = ".{15}">
				      <input type="hidden" name="telefoneatual" value="<?php echo $Professo['telefone']; ?>">
				</div><!--form-group-->

				<div class="form-group">
					  <label>Email:</label>
				      <input type="text" name="email"  value="<?php echo $Professo['email']; ?>"  required pattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" >
				      <input type="hidden" name="emailatual"  value="<?php echo $Professo['email']; ?>" >
				</div><!--form-group-->

					 <div class="form-group">
					  <label>Estado Civil</label>
					  <select name="estado_civil" value="<?php echo $Professo['estado_civil']; ?>">
					  	    <option value="<?php echo $Professo['estado_civil']; ?>" selected="selected"><?php echo  $Professo['estado_civil'];; ?></option>
						    <option value="Solteiro">Solteiro</option>
						    <option value="Viuvo">Viuvo</option>
						    <option value="Casado">Casado</option>
			         </select>
				</div><!--form-group-->	

		        <div class="form-group">
					  <label>Sexo:</label>
					  <select name="sexo" value="<?php echo $Professo['sexo']; ?>">
					  	    <option value="<?php echo $Professo['sexo']; ?>" selected="selected"><?php echo  $Professo['sexo']; ?></option>
						    <option value="Masculino">Masculino</option>
						    <option value="Femenino">Femenino</option>
			         </select>
				</div><!--form-group-->	

				<div class="form-group">
					  <label>Senha:</label>
					  <input type="hidden" name="senhaatual"  value="<?php echo $Professo['senha']; ?>">
					  <input type="text" name="novasenha"  placeholder="nova senha (minimo 8 digitos, e obrigatorio o uso se numeros e letas minusculas e minusculass)" pattern="[0-9a-zA-Z]{8,}">
				</div><!--form-group-->	

				<div class="form-group">
					  <label>Estado:</label>
					  <?php 
							 if($Professo['estado'] == 1)
							  $aux = "Ativo";  
							 else
							  $aux = "Desativado";
					  ?>
					  <select name="estado" value="<?php echo $Professo['estado']; ?>">
					  	    <option value="<?php echo $Professo['estado']; ?>" selected="selected"><?php echo $aux ?></option>
						    <option value="1">Ativo</option>
						    <option value="0">Desativado</option>
			         </select>
				</div><!--form-group-->	

			    <div class="form-group">
					  <label> Nova Imagem:</label>
					  <input type="file" name="imagem" >
					  <input type="hidden" name="imagematual"  value="<?php echo $Professo['imagem']; ?>">
			    </div><!--form-group-->
                
              
				<div class="form-group">
			         <input type="submit" name="Editar-professo" value="Atualiza!">
		        </div><!--form-group-->
     </form>
</section><!--box-content-->
</div><!-- content -- >