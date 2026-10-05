<div class="content">
<section class="box-content center">
<h2><i class="fa fa-pencil"></i> Adicionar Sectario</h2>
     <form class="ajax" action="<?php echo INCLUDE_PATH?>Models/ajax/CadastroSectario.php" method="post" enctype="multipart/form-data"   autofocus>
		        <div class="form-group">
			          <label>Nome:</label>
					  <input type="text" name="nome" class ="required" id="nome" required  pattern = "{4,}" >
			    </div><!--form-group-->

				<div class="form-group">
					  <label>Cpf:</label>
				      <input type="text" name="cpf"  class ="required" required  pattern = ".{14}" >
				</div><!--form-group-->

				<div class="form-group">
					  <label>Cep:</label>
				      <input type="text" name="cep" class ="required" required  pattern = ".{9}" >
				</div><!--form-group-->
			

				<div class="form-group">
					  <label>Data de Nacimento:</label>
				      <input type="date" class ="required"  name="data_de_nacimento" min="1900-01-01" max="2019-01-01" required  >
				</div><!--form-group-->

				<div class="form-group">
					  <label>Telefone:</label>
				      <input type="text" class ="required" name="telefone" required pattern = ".{15}">
				</div><!--form-group-->

				<div class="form-group">
					  <label>Email:</label>
				      <input type="text" class ="required" name="email" required pattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" >
				</div><!--form-group-->

				 <div class="form-group">
					  <label>Estado Civil</label>
					  <select name="estado_civil" class ="required">
						    <option value="Solteiro">Solteiro</option>
						    <option value="Viuvo">Viuvo</option>
						    <option value="Casado">Casado</option>
			         </select>
				</div><!--form-group-->	

		        <div class="form-group">
					  <label>Sexo:</label>
					  <select name="sexo" class ="required">
						    <option value="Masculino">Masculino</option>
						    <option value="Femenino">Femenino</option>
			         </select>
				</div><!--form-group-->	

				<div class="form-group">
					  <label>Senha:</label>
					  <input type="password" class ="required" name="senha" placeholder = "minimo 8 digitos, e obrigatorio o uso se numeros e letas minusculas e minusculass" required pattern="[0-9a-zA-Z]{8,}">
				</div><!--form-group-->	

			    <div class="form-group">
					  <label>Imagem (opcional) :</label>
					  <input type="file" name="imagem"/>
			    </div><!--form-group-->
                
              
				<div class="form-group">
			         <input type="submit" name="Cadastro-professor" value="Cadastrar!">
		        </div><!--form-group-->
     </form>
</section><!--box-principal-->
</div><!-- content -- >