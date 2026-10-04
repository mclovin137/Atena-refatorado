<?php   
    use \Models\MainMolde; 
    $matricula = $_SESSION['matricula'];
    $professo = MainMolde::selectAll('professo2','matricula != ?',array($matricula));
    $secretario = MainMolde::selectAll('secretaria2','matricula != ?',array($matricula));
    $alunos = MainMolde::selectAll('alunos2','matricula != ?',array($matricula));

 ?>
<div class="content">
<div class="box-content">
	<div class="busca">
			<h4><i class="fa fa-search"></i> Realizar uma busca</h4>
			<form method="post">
				<input placeholder="Procure por: Nome, Matricula, Email" type="text" name="busca">
				<input type="submit" name="pesquisa" value="Buscar!">
			</form>
	</div><!--busca-->  
	<?php  
	   if(isset($_POST['pesquisa']))
	   {
		    $busca = $_POST['busca'];
			$query = " WHERE nome LIKE '%$busca%' OR matricula LIKE '%$busca%'  OR email LIKE '%$busca%'  OR cpf  LIKE '%$busca%' ";	

			$professo = MySql::conectar()->prepare("SELECT * FROM `professo2` $query");
			$professo->execute();
			$professo = $professo->fetchAll();

			$secretario = MySql::conectar()->prepare("SELECT * FROM `secretaria2` $query");
			$secretario->execute();
			$secretario = $secretario->fetchAll();

		    $alunos = MySql::conectar()->prepare("SELECT * FROM `alunos2` $query");
			$alunos->execute();
			$alunos = $alunos->fetchAll();
			
			
	   }

	   if(isset($_POST['pesquisa']))
	   {
				echo '<div style="width:100%;" class="busca-result"><p>Foram encontrado(s)<p></div>';
	   }

    ?> 

     <h2 class="text-center"><i class="fa fa-envelope"></i>  Conversas</h2>
     <div class="boxes">
		    <?php            
		         foreach ($professo as $key => $value) {					 			   
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
							    <p><b><i class="fa fa-pencil"></i> Cargo:</b>      Professor</p>
								<p><b><i class="fa fa-pencil"></i> Nome:</b>      <?php echo $value['nome']; ?></p>
								<p><b><i class="fa fa-pencil"></i> Email:</b>     <?php echo $value['email']; ?></p>
							    <div class="group-btn text-center">
								    <a class="btn btn-primary msn" href="<?php echo INCLUDE_PATH_MAIN ?>chat-box?user=<?php echo $value['matricula']; ?>"> <i class="fa fa-envelope"></i>  Enviar Mensagem</a>
							    </div><!--group-btn-->
						</div><!--body-box-->
					</div><!--box-single-->
			</div><!--box-single-wraper-->
			<?php } ?>

		
			<?php 
			    foreach ($secretario as $key => $value) {					 			   
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
							    <p><b><i class="fa fa-pencil"></i> Cargo:</b>      Secretario</p>
								<p><b><i class="fa fa-pencil"></i> Nome:</b>      <?php echo $value['nome']; ?></p>
								<p><b><i class="fa fa-pencil"></i> Email:</b>     <?php echo $value['email']; ?></p>
							    <div class="group-btn text-center">
								    <a class="btn btn-primary msn" href="<?php echo INCLUDE_PATH_MAIN ?>chat-box?user=<?php echo $value['matricula']; ?>"> <i class="fa fa-envelope"></i>  Enviar Mensagem</a>
							    </div><!--group-btn-->
						</div><!--body-box-->
					</div><!--box-single-->
			</div><!--box-single-wraper-->
			<?php } ?>

			<?php 
			    
			    foreach ($alunos as $key => $value) {
			     		 			   
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
							    <p><b><i class="fa fa-pencil"></i> Cargo:</b>      Aluno</p>
								<p><b><i class="fa fa-pencil"></i> Nome:</b>      <?php echo $value['nome']; ?></p>
								<p><b><i class="fa fa-pencil"></i> Email:</b>     <?php echo $value['email']; ?></p>
							    <div class="group-btn text-center">
								    <a class="btn btn-primary msn" href="<?php echo INCLUDE_PATH_MAIN ?>chat-box?user=<?php echo $value['matricula']; ?>"> <i class="fa fa-envelope"></i>  Enviar Mensagem</a>
							    </div><!--group-btn-->
						</div><!--body-box-->
					</div><!--box-single-->
			</div><!--box-single-wraper-->
			<?php } ?>

			
			

	</div><!--boxes-->	
</div><!--box-principal-->
</div><!-- content -- >