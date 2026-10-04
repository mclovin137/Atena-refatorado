<div class="content">
<section class="box-content center">
<h2><i class="fa fa-pencil"></i> Adicionar Aluno</h2>
     <form class="ajax"  action="<?php echo INCLUDE_PATH?>Models/ajax/CadastroAluno.php" method="post" enctype="multipart/form-data" >
		       
		        <div class="form-group">
			          <label>Nome:</label>
					  <input type="text" class ="required" name="nome" id="nome" required  pattern = ".{4,}" >
			    </div><!--form-group-->
				

				<div class="form-group">
					  <label>Cpf do aluno ou responsavel:</label>
				      <input type="text" class ="required" name="cpf" required  pattern = ".{14}" >
				</div><!--form-group-->

				<div class="form-group">
					  <label>Cep:</label>
				      <input type="text" class ="required" name="cep" required  pattern = ".{9}" >
				</div><!--form-group-->

				<div class="form-group">
					  <label>Endereço:</label>
				      <input type="text"  class ="required" name = "endereço" required  >
				</div><!--form-group-->

				<div class="form-group">
					  <label>Nome Do Pai: (opcional)</label>
				      <input type="text" name="pai"  pattern = ".{4,}">
				</div><!--form-group-->

				<div class="form-group">
					  <label>Nome Da Mae ou Responsavel: </label>
				      <input type="text"  class ="required" name="mae" required pattern = ".{4,}">
				</div><!--form-group-->

				<div class="form-group">
					  <label>Telefone do Responsavel: </label>
				      <input type="text" class = "required" name="telefone_responsavel" required pattern = ".{15}">
				</div><!--form-group-->

				<div class="form-group">
					  <label>E-mail do Responsavel: </label>
				      <input type="text" name="email_responsavel"  class ="required" required pattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$">
				</div><!--form-group-->

				<div class="form-group">
					  <label>Data de Nacimento:</label>
				      <input type="date"   class ="required" name="data_de_nacimento" min="2000-01-01" max="2019-01-01" required  >
				</div><!--form-group-->

				<div class="form-group">
					  <label>Telefone do aluno (opcional):</label>
				      <input type="text" name="telefone" class = "telefone" pattern = ".{15}">
				</div><!--form-group-->

				<div class="form-group">
					  <label>Email do aluno (opcional):</label>
				      <input type="text" name="email" pattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" >
				</div><!--form-group-->

				 <div class="form-group">
					  <label>Estado Civil</label>
					  <select name="estado_civil">
						    <option value="Solteiro">Solteiro</option>
						    <option value="Viuvo">Viuvo</option>
						    <option value="Casado">Casado</option>
			         </select>
				</div><!--form-group-->	

		        <div class="form-group">
					  <label>Sexo:</label>
					  <select name="sexo">
						    <option value="Masculino">Masculino</option>
						    <option value="Femenino">Femenino</option>
			         </select>
				</div><!--form-group-->	

				<div class="form-group">
					  <label>Senha:</label>
					  <input type="password"  class ="required" name="senha" required pattern = ".{4,}">
				</div><!--form-group-->	

			    <div class="form-group">
					  <label>Imagem (opcional) :</label>
					  <input type="file" name="imagem">
			    </div><!--form-group-->
                
              
				<div class="form-group">
			         <input type="submit" name="Cadastro-aluno" value="Cadastrar!">
		        </div><!--form-group-->
     </form>
</section><!--box-principal-->
</div><!-- content -- >