<?php 
    use \Models\MainMolde;
     $secetario = MainMolde::select('secretaria2','matricula = ?',array( @$_SESSION['matricula'] ) );
?>
<!DOCTYPE html>
<html>
<head>
	<!--===============================================================================================-->	
	<title>Area da Secretaria </title>
	<!--===============================================================================================-->	
	<link rel="icon" href="<?php echo INCLUDE_PATH; ?>Views/imagens/images.png" type="image/x-icon" />
	<!--======================================= META TAGS =============================================-->	
	<meta name="author" content="jovenlegolas" />
	<!--===============================================================================================-->	
	<meta name="keywords" content="sistema escolar,educaçao">
	<!--===============================================================================================-->	
	<meta name="description" content="sistema escolar">
	<!--===============================================================================================-->	
	<meta http-equiv="X-UA-Compatible" content="IE=Edge">
	<!--===============================================================================================-->
	<meta name="google" value="notranslate">
	<!--===============================================================================================-->	
	<meta name="title" value="SDGE">
	<!--===============================================================================================-->	
	<meta charset="viewport" content="width=device-width;initial-scale=1.0;maximum-scale=1.0">
	
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

    <!--============================================= CAROUCEL ========================================-->	
	<link rel="stylesheet" href="<?php echo INCLUDE_PATH ?>Lib/carousel/css/flaticon.css"/>
	<!--===============================================================================================-->	
	<link rel="stylesheet" href="<?php echo INCLUDE_PATH ?>Lib/carousel/css/slicknav.min.css"/>
	<!--===============================================================================================-->	
	<link rel="stylesheet" href="<?php echo INCLUDE_PATH ?>Lib/carousel/css/jquery-ui.min.css"/>
	<!--===============================================================================================-->	
	<link rel="stylesheet" href="<?php echo INCLUDE_PATH ?>Lib/carousel/css/owl.carousel.min.css"/>
	<!--===============================================================================================-->	
	<link rel="stylesheet" href="<?php echo INCLUDE_PATH ?>Lib/carousel/css/animate.css"/>
	<!--===============================================================================================-->	
	<link rel="stylesheet" href="<?php echo INCLUDE_PATH ?>Lib/carousel/css/style.css"/>
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
				<div class="avatar-usuario">
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
		   <h2 class="text-center">Cadastro de usuarios</h2>
		      <a href="<?php echo INCLUDE_PATH_MAIN?>cadastrar-alunos"><i class="fa fa-user-plus"></i><span class="iten-menu-txt ">Cadastrar Alunos</span></a>
		      <a href="<?php echo INCLUDE_PATH_MAIN?>cadastrar-professores"><i class="fa fa-user-plus"></i><span class="iten-menu-txt">Cadastrar Professor</span></a>
			  <?php if($secetario['diretor'] == 1) {?>
				<a href="<?php echo INCLUDE_PATH_MAIN?>cadastrar-sectario"><i class="fa fa-user-plus"></i><span class="iten-menu-txt">Cadastrar Funcionario</span></a>
			  <?php } ?>
		   <h2 class="text-center">Gestao de usuarios</h2>
		      <a href="<?php echo INCLUDE_PATH_MAIN?>listagem-alunos"><i class="fa fa-id-card-o" ></i><span class="iten-menu-txt">Listar Alunos</span></a>
		      <a href="<?php echo INCLUDE_PATH_MAIN?>listagem-professores"><i class="fa fa-id-card-o" ></i><span class="iten-menu-txt">Listar Professor</span></a>
			  <?php if($secetario['diretor'] == 1) {?>
				<a href="<?php echo INCLUDE_PATH_MAIN?>listagem-sectarios"><i class="fa fa-id-card-o"></i><span class="iten-menu-txt">Listar Funcionarios</span></a>
			  <?php } ?>
		   <h2 class="text-center">Cadastro</h2>	    
		      <a href="<?php echo INCLUDE_PATH_MAIN?>cadastrar-serie"><i class="fa fa-calendar-plus"></i><span class="iten-menu-txt">Cadastrar Serie</span></a>
 	          <a href="<?php echo INCLUDE_PATH_MAIN?>cadastrar-curso"><i class="fa fa-calendar-plus"></i><span class="iten-menu-txt">Cadastrar Curso</span></a>
 	          <a href="<?php echo INCLUDE_PATH_MAIN?>cadastrar-materia"><i class="fa fa-calendar-plus"></i><span class="iten-menu-txt">Cadastrar Materia</span></a>
			<h2 class="text-center">Cadastro de Turmas</h2>
			  <a href="<?php echo INCLUDE_PATH_MAIN?>cadastrar-diciplina"><i class="fa fa-calendar-plus"></i><span class="iten-menu-txt">Cadastrar Diciplina</span></a>
			  <a href="<?php echo INCLUDE_PATH_MAIN?>cadastrar-grade-curricular"><i class="fa fa-calendar-plus"></i><span class="iten-menu-txt">Cadastrar Grade curricular</span></a>
			  <a href="<?php echo INCLUDE_PATH_MAIN?>cadastrar-dependecia"><i class="fa fa-calendar-plus"></i><span class="iten-menu-txt">Cadastrar Dependecias </span></a>
			  <a href="<?php echo INCLUDE_PATH_MAIN?>cadastrar-turmas"><i class="fa fa-calendar-plus"></i><span class="iten-menu-txt">Cadastrar Turmas</span></a>
 	          <a href="<?php echo INCLUDE_PATH_MAIN?>cadastrar-horario"><i class="fa fa-calendar-plus"></i><span class="iten-menu-txt">Cadastra Horario	</span></a>
		   <h2 class="text-center">Gestao de turmas</h2>   
 	           <a href="<?php echo INCLUDE_PATH_MAIN?>gerir-turmas"><i class="fa fa-calendar-alt"></i><span class="iten-menu-txt">Turmas</span></a>
 	           <a href="<?php echo INCLUDE_PATH_MAIN?>diario"><i class="fa fa-calendar-alt"></i><span class="iten-menu-txt">Diciplinas</span></a>
 	           <a href="<?php echo INCLUDE_PATH_MAIN?>listagem-horario"><i class="fa fa-calendar-alt"></i><span class="iten-menu-txt">Ver horarios</span></a>
 	       <h2 class="text-center">Outros</h2>   
 	           <a href="<?php echo INCLUDE_PATH_MAIN?>chat"><i class="fa fa-comments-o" aria-hidden="true"></i><span class="iten-menu-txt">Chat</span></a>		
			   <a href="<?php echo INCLUDE_PATH_MAIN?>calendario"><i class="fa fa-calendar-alt"></i><span class="iten-menu-txt">Calendario Academico</span></a>
			   <a href="<?php echo INCLUDE_PATH_MAIN?>lancamento-notas"><i class="fa fa-hourglass"></i><span class="iten-menu-txt">Limita tempo do diario</span></a>
			   <a href="<?php echo INCLUDE_PATH_MAIN?>atualiza-dados"><i class="fa fa-id-card-o" ></i><span class="iten-menu-txt">Atualiza Meus Dados</span></a>
			   <a href="<?php echo INCLUDE_PATH_MAIN?>unidade_escolar"><i class="fa fa-id-card-o" ></i><span class="iten-menu-txt">Unidade Escolar</span></a>
 	          
 	          
	   </div><!--items-menu-->
   </div><!--menu-wraper-->
<div class="clear"></div>
</div><!--menu-->

</header>



	
	