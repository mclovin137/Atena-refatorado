<div class="content">
<section class="box-content center">
<h2><i class="fa fa-pencil"></i> Adicionar Curso</h2>
     <form  method="post" enctype="multipart/form-data">

		        <div class="form-group">
			          <label>Nome:</label>
					  <input type="text" name="nome" required  pattern = ".{4,}">
			    </div><!--form-group-->
                
			 
				<div class="form-group">
			         <input type="submit" name="Cadastre-curso" value="Cadastrar!">
	
		        </div><!--form-group-->
    </form>
    <h2 class="text-center"> Curso Existentes</h2>
    <div class="listagem-csm">	         
		        	<?php  
		        	   use \Models\MainMolde;
		        	   $materiais = MainMolde::selectAll('curso','',array());
		        	   foreach ($materiais as $key => $value) { 
		             ?>		        								
						    <form  method="post" enctype="multipart/form-data">
								<div class="box-tarefas-single">
								  <ul>							  							  
										  <input type="hidden" name="nomeatual"  value="<?php echo $value['nome'] ?>" >
										  <li class="list-group-item"><p class = "text-center"><?php echo $value['nome']?><p></li>       
								  </ul>
								</div><!--box-tarefas-sling-->
							</form>	

				      <?php }?>
    <div class="clear"></div>		        	
    </div>
	
</section><!--box-principal-->
</div><!-- content -- >