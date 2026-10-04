<?php 
	use \Models\MainMolde;
	@$dados = MainMolde::select('unidade_escolar','',array());
	if(@$_SESSION['bk'] == false) { 	
?>
<section class="main ">
<div class="layer"></div>
			<header>
				<div class="logo left">
					<?php echo @$dados[1] ?>
				</div><!--logo-->
			<div class="clear"></div>
			</header>
			<div class="container-banner">
				<div class="form right">
					<h2>Preencha o formulário</h2>
					<form method="post">
						<div class="input-container">
							<span>E-mail*</span>
							<input type="text" name="nome" >
						</div><!--input-container-->

						<div class="input-container">
							<span>Matricula*</span>
							<input type="text" name="matricula" maxlength="50" required>
						</div><!--input-container-->

						<div class="input-container">
							<span>Senha*</span>
							<span class="btn-show-pass">
								<i class="zmdi zmdi-eye"></i>
							</span>
							<input type="password" name="senha" maxlength="32" required >						
						</div><!--input-container-->

						<p class="warning">*Campos obrigatórios</p>

						<div class="input-submit-container">
							<input type="submit" name="acao" value="Enviar">
						</div><!--input-submit-container-->
					</form>
				</div><!--form-->
	        <div class="clear"></div>
	        </div><!--container-banner-->		    
</section>
<?php }
 else {
 ?>
<section class="main ">
<div class="layer"></div>
			<header>
				<div class="logo left">
					IFG - Campus Urutai
				</div><!--logo-->
			<div class="clear"></div>
			</header>
			<div class="container-banner">
				<div class="msg-bk">
					<div class="header-msg-bk"><p>ALERTA</p></div><!--header-->
					<div class="body-msg-bk">
                       <p>Seu acesso foi bloqueado devido o grande numero de tentativas</p>
                       <p>feche o navegador e tente novamente</p>
                    </div><!--body-msg-bk-->
                    <div class="footer-msg-bk"><p>°</p></div><!--footer-->
				</div><!--msg-bk-->	       
	        </div><!--container-banner-->		    
</section>
<?php }
