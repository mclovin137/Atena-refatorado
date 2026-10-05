
<div class="content">
<div class="box-content">
	<h2><i class="fa fa-id-card-o" aria-hidden="true"></i> Alunos Cadastrados</h2>
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
			$query = " WHERE nome LIKE '%$busca%' OR matricula LIKE '%$busca%'  OR email LIKE '%$busca%'  OR cpf  LIKE '%$busca%' ";
		}

		$aluno = MySql::conectar()->prepare("SELECT * FROM `alunos2` $query");
		$aluno->execute();
		$aluno = $aluno->fetchAll();

		if(isset($_POST['pesquisa']))
		{
			echo '<div style="width:100%;" class="busca-result"><p>Foram encontrados <b>'.count($aluno).'</b> resultado(s)</p></div>';
		}
		
		foreach($aluno as $value){
	?>
		<div class="box-single-wraper">
			<div class="box-single">
				<div class="topo-box">
					<?php
						if($value['imagem'] == ''){
					?>
					<h2><i class="fa fa-user"></i></h2>
					<?php }else{ ?>
						<img src="<?php echo INCLUDE_PATH?>Views/uploads/<?php echo $value['imagem']; ?>" />
					<?php } ?>
				</div><!--topo-box-->
				<div class="body-box">
					    <p><b><i class="fa fa-pencil"></i> Nome:</b>          <?php echo $value['nome']; ?></p>
						<p><b><i class="fa fa-pencil"></i> Matricula:</b>          <?php echo $value['matricula']; ?></p>
						<p><b><i class="fa fa-pencil"></i> Email:</b>     <?php echo $value['email']; ?></p>
						<p><b><i class="fa fa-pencil"></i> Cpf:</b>       <?php echo $value['cpf']; ?></p>
						<p><b><i class="fa fa-pencil"></i> Telefone:</b>  <?php echo $value['telefone']; ?></p>
					    <div class="group-btn">
						    <a class="btn edit w100 " href="<?php echo INCLUDE_PATH_MAIN ?>editar-aluno?id=<?php echo $value['id']; ?>"><i class="fa fa-pencil"></i> Editar</a>
					    </div><!--group-btn-->
						<div class="group-btn ">
						    <a class="btn btn-primary w100 " href="<?php echo INCLUDE_PATH?>Models/pdf/HistoricoCompleto.php?id=<?php echo $value['matricula'] ?>"><i class="fa fa-card"></i> Historico</a>
					    </div><!--group-btn-->
					
				</div><!--body-box-->
			</div><!--box-single-->
		</div><!--box-single-wraper-->



	<?php } ?>

	 <div class="clear"></div>
    </div><!--boxes-->
</div><!--box-content-->	
</div><!-- content -- >