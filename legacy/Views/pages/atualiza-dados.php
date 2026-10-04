<?php
    use \Models\MainMolde;
	
		$matricula = $_SESSION['matricula'];
		$dados = MainMolde::select('secretaria2','matricula = ?',array($matricula));		
	
?>
<div class="content">
<section class="box-content center">
	<h2><i class="fa fa-pencil"></i> Editar Dados</h2>
	 <form class="ajaxx"  action="<?php echo INCLUDE_PATH?>Models/ajax/Atualiza.php" method="post" enctype="multipart/form-data" >
	           <div class="form-group">
			          <label>Nome:</label>
					  <input type="text" name="nome" id="nome" value="<?php echo $dados['nome']; ?>" required  pattern = ".{4,}" >
					  <input type="hidden" name="nomeatual" value="<?php echo $dados['nome']; ?>">
			    </div><!--form-group-->

				<div class="form-group">
					 <input type="hidden" name="matriculaatual" value="<?php echo $dados['matricula']; ?>">
			    </div><!--form-group-->

				<div class="form-group">
					  <label>Cpf:</label>
				      <input type="text" name="cpf" value="<?php echo $dados['cpf']; ?>" required  pattern = ".{14}" >
				      <input type="hidden" name="cpfatual" value="<?php echo $dados['cpf']; ?>">
				</div><!--form-group-->

				<div class="form-group">
					  <label>Cep:</label>
				      <input type="text" name="cep" value="<?php echo $dados['cep']; ?>"required  pattern = ".{9}" >
				</div><!--form-group-->


				<div class="form-group">
					  <label>Data de Nacimento:</label>
				      <input type="date" name="data_de_nacimento" min="2000-01-01" max="2019-01-01" value="<?php echo $dados['data_de_nacimento']; ?>" required  >
				</div><!--form-group-->

				<div class="form-group">
					  <label>Telefone:</label>
				      <input type="text" name="telefone"  value="<?php echo $dados['telefone']; ?>" required pattern = ".{15}">
				      <input type="hidden" name="telefoneatual" value="<?php echo $dados['telefone']; ?>">
				</div><!--form-group-->

				<div class="form-group">
					  <label>Email:</label>
				      <input type="text" name="email"  value="<?php echo $dados['email']; ?>"  required pattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" >
				      <input type="hidden" name="emailatual"  value="<?php echo $dados['email']; ?>" >
				</div><!--form-group-->

					 <div class="form-group">
					  <label>Estado Civil</label>
					  <select name="estado_civil" value="<?php echo $dados['estado_civil']; ?>">
					  	    <option value="<?php echo $dados['estado_civil']; ?>" selected="selected"><?php echo  $dados['estado_civil']; ?></option>
						    <option value="Solteiro">Solteiro</option>
						    <option value="Viuvo">Viuvo</option>
						    <option value="Casado">Casado</option>
			         </select>
				</div><!--form-group-->	

		        <div class="form-group">
					  <label>Sexo:</label>
					  <select name="sexo" value="<?php echo $dados['sexo']; ?>">
					  	    <option value="<?php echo $dados['sexo']; ?>" selected="selected"><?php echo  $dados['sexo']; ?></option>
						    <option value="Masculino">Masculino</option>
						    <option value="Femenino">Femenino</option>
			         </select>
				</div><!--form-group-->	

				<div class="form-group">
					  <label>Senha:</label>
					  <input type="hidden" name="senhaatual"  value="<?php echo $dados['senha']; ?>">
					  <input type="text" name="novasenha"  placeholder = "nova senha (minimo 8 digitos, e obrigatorio o uso se numeros e letas minusculas e minusculass)" pattern="[0-9a-zA-Z]{8,}">
				</div><!--form-group-->	

			    <div class="form-group">
					  <label> Nova Imagem:</label>
					  <input type="file" name="imagem" >
					  <input type="hidden" name="imagematual"  value="<?php echo $dados['imagem']; ?>">
			    </div><!--form-group-->
                
              
				<div class="form-group">
			         <input type="submit" name="atualiza-adm" value="Atualiza!">
		        </div><!--form-group-->
     </form>
</section><!--box-content-->
</div><!-- content -- >