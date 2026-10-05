<?php
    use \Models\MainMolde;
	if(isset($_GET['user']))
	{
		$usuario = $_GET['user'];
		$remetente = $_SESSION['matricula'];
		$verifica = MainMolde::selectAll('professo2','matricula = ?',array($usuario));
		$aux = ceil(count($verifica));
		$verifica = MainMolde::selectAll('secretaria2','matricula = ?',array($usuario));
		$aux1 = ceil(count($verifica));
		$verifica = MainMolde::selectAll('alunos2','matricula = ?',array($usuario));
		$aux2 = ceil(count($verifica));
		if ($usuario == $remetente)
		{
			\Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
		}
		else if ($aux + $aux1 + $aux2 == 0)
		{
			\Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
	    }
	    if ($aux != 0) 
	    {
	    	 $use1 = MainMolde::select('professo2','matricula = ?',array($usuario));
	    }
	    else if ($aux1 != 0)
	    {
	    	 $use1 = MainMolde::select('secretaria2','matricula = ?',array($usuario));
	    }
	    else if ($aux2 != 0)
	    {
	    	 $use1 = MainMolde::select('alunos2','matricula = ?',array($usuario));
	    }
	}
	else
	{
		\Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
	}
	

?>
<div class="content">
<section class="box-content center">
	 <div class="box-chat-online">
		<?php
			$mensagens = MySql::conectar()->prepare("SELECT * FROM `chat` WHERE cod_remetente = ? AND cod_destino = ?  OR cod_remetente = ? AND cod_destino = ? ORDER BY id DESC LIMIT 20");
			$mensagens->execute(array($remetente,$usuario,$usuario,$remetente));
			$mensagens = $mensagens->fetchAll();
			$mensagens = array_reverse($mensagens);		
				
			foreach ($mensagens as $key => $value) {  ?>
			<?php if ($value['cod_remetente'] == $remetente) { ?>
	            <div class="container-msn">
					  <img src="<?php echo INCLUDE_PATH?>Views/uploads/<?php echo $_SESSION['imagem'] ?>" />
					  <p><?php echo $value['mensagem']; ?></p>
					  <span class="time-right"><?php echo $value['data'];?> </span>
				</div>
			<?php } ?>
			<?php if ($value['cod_remetente'] != $remetente) { ?>
	            <div class="container-msn darker">
					  <img class="right" src="<?php echo INCLUDE_PATH?>Views/uploads/<?php echo $use1[6] ?>" />
					  <p><?php echo $value['mensagem']; ?></p>
					  <span class="time-left"><?php echo $value['data'];?> </span>
				</div>
			<?php } ?>
			
			<?php } ?>	



	 </div><!--box-chat-online-->

	 <form method="post" enctype="multipart/form-data">
	     <textarea type="txt" name="mensagem" class="campo_de_texto_msn" required></textarea>  
	     <input type="hidden" name="remetente"    value="<?php echo $remetente ?>" >
		 <input type="hidden" name="destino"      value="<?php echo $usuario   ?>" >
		 <input type="hidden" name="visualizado"  value="<?php echo 1          ?>" >
		 <input type="submit" name="enviar-msn"   value="Enviar">           
     </form>
</section><!--box-content-->
</div><!-- content -- >