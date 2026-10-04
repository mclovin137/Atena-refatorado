<!DOCTYPE html>
<html>
<head>
	<!--===============================================================================================-->	
	<title>Area do Aluno</title>
	<!--===============================================================================================-->	
	<link rel="icon" href="<?php echo INCLUDE_PATH; ?>Views/imagens/images.png" type="image/x-icon" />
	<!--======================================= META TAGS =============================================-->	
	<meta name="author" content="jovenlegolas" />
	<!--===============================================================================================-->	
	<meta name="keywords" content="sistema escolar,educaçao">
	<!--===============================================================================================-->	
	<meta name="description" content="sistema escolar">
	<!--===============================================================================================-->	
	<meta name="google" value="notranslate">
	<!--===============================================================================================-->	
	<meta name="title" value="SDGE">
	<!--===============================================================================================-->	
	<meta http-equiv="X-UA-Compatible" content="IE=Edge">
	<!--===============================================================================================-->
	<meta charset="viewport" content="width=device-width;initial-scale=1.0;maximum-scale=1.0">
	<!--===============================================================================================-->	
	
	<!--========================================= FONTES ==============================================-->
	<link href="https://fonts.googleapis.com/css?family=Open+Sans:300,400" rel="stylesheet">
	<!--===============================================================================================-->	
	<link rel="stylesheet" href="<?php echo INCLUDE_PATH ?>Views/fonts/font-awesome.css">
	<!--===============================================================================================-->	
	<link rel="stylesheet" href="<?php echo INCLUDE_PATH ?>Views/fonts/iconic/css/material-design-iconic-font.min.css">
	<!--===============================================================================================-->
	<link rel="stylesheet" href="<?php echo INCLUDE_PATH ?>Views/fonts/fontawesome-5.8.2/css/fontawesome.css">
	<!--===============================================================================================-->	

    <!--========================================== BOOTSTRAP ==========================================-->	
	<link rel="stylesheet" href="<?php echo INCLUDE_PATH ?>Views/estilo/bootstrap.css">
	<!--===============================================================================================-->	

	<!--===========================================  ESTILO ===========================================-->	
    <link rel="stylesheet" href="<?php echo INCLUDE_PATH ?>Views/estilo/main.css">
    <!--===============================================================================================-->

   

</head>
<body>
<header>

<div class="center">
		<div class="menu-btn left">
			<i class="fa fa-bars"></i>
		</div><!--menu-btn-->
<div class="clear"></div>
</div><!-- centre -->

<div class="botoes right">
	<ul>         
		<a href="<?php echo INCLUDE_PATH_MAIN_HOME ?>"><i class="fa fa-home"></i> <span>Página Inicial</span></a>
		<a href="<?php echo INCLUDE_PATH_MAIN_HOME ?>?loggout"><i class="fa fa-sign-out"></i> <span>Sair</span></a>
    </ul>
<div class="clear"></div>
</div><!--botoes-->
		     		
<div class="menu left">
	<h2 class="text-center titulo">IFG</h2>
	<div class="menu-wraper">
	    <div class="box-usuario">
		    <?php
				if(@$_SESSION['imagem'] == ''){
			?>
				<div class="avatar-usuario ">
					<i class="fa fa-user"></i>
				</div><!--avatar-usuario-->
			<?php }else{ ?>
				<div class="imagem-usuario">
					<img src="<?php echo INCLUDE_PATH?>Views/uploads/<?php echo $_SESSION['imagem']; ?>" />
				</div><!--avatar-usuario-->
			<?php } ?>	
		        <div class="nome-usuario">
				    <p><?php echo @$_SESSION['nome']; ?></p>		
			    </div><!--nome-usuario-->
	   </div><!--box-usuario-->
	   <div class="items-menu">
		   <h2 class="text-center centre">Atividades</h2>
		      <a href="<?php echo INCLUDE_PATH_MAIN ?>diario"><i class="fa fa-list-alt"></i><span class="iten-menu-txt">Diario </span></a>
		      <a href="<?php echo INCLUDE_PATH_MAIN ?>boletim"><i class="fa fa-list-alt"></i><span class="iten-menu-txt">Boletim</span></a>

		   <h2 class="text-center centre">Materiais</h2>
		      <a href="<?php echo INCLUDE_PATH_MAIN?>cadastrar-arquivo"><i class="fa fa-file-archive"></i><span class="iten-menu-txt">Materias</span></a>
		      <a href="<?php echo INCLUDE_PATH_MAIN?>listagem-horario"><i class="fa fa-calendar-alt"></i><span class="iten-menu-txt">horarios</span></a>

		   <h2 class="text-center centre">Outros</h2>
              <a href="<?php echo INCLUDE_PATH_MAIN?>chat"><i class="fa fa-comments-o" aria-hidden="true"></i><span class="iten-menu-txt">Chat</span></a>	
		      <a href="<?php echo INCLUDE_PATH_MAIN?>calendario"><i class="fa fa-calendar-alt"></i><span class="iten-menu-txt">Calendario Academico</span></a>
		      <a href="<?php echo INCLUDE_PATH_MAIN?>atualiza-dados"><i class="fa fa-id-card-o" ></i><span class="iten-menu-txt">Atualiza Meus Dados</span></a>

	   </div><!--items-menu-->
   </div><!--menu-wraper-->
<div class="clear"></div>
</div><!--menu-->

</header>



	