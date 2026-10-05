<div class="content">
<section class="box-content center">
<h2><i class="fa fa-pencil"></i> Adicionar Padroes de grade curricular</h2>
     <form  method="post" enctype="multipart/form-data">

		        <div class="form-group">
			          <label>Nome :</label>
					  <input type="text" name="nome" required pattern=".{4,}" >
			    </div><!--form-group-->
                
			 
				
        <h2 class="text-center"> Materias</h2>
		        <div class="wraper-table ">				
					<table>

						<tr>
							<td class="coluna-tabela text-center">Adicionar</td>
							<td class="coluna-tabela text-center">Materia</td>
						    <td class="coluna-tabela text-center">Carga Horaria</td>														
						</tr>

						<?php  
							use \Models\MainMolde;
							$materiais = MainMolde::selectAll('materia','',array());
							foreach ($materiais as $key => $value) { 
						?>		     
						
							<tr>
							    <td class="coluna-tabela text-center"><input type="checkbox" name="materias[]" value="<?php echo $value['id']?>"> </td>	
							    <td class="coluna-tabela text-center"><?php echo $value['nome'] ?></td>
								<td class="coluna-tabela text-center"><input  type="number" step="1" min="1"  name="ch<?php echo $value['id'] ?>" > </td>		
								<input type="hidden" name = "id<?php echo $value['id']?>" value = "<?php echo $value['nome']?>" >																
							</tr>
		  
					   <?php } ?>

				    </table>	
			    </div><!--wraper-table-->
                	
            <div class="form-group">
			    <input type="submit"  name="Cadastre" value="Cadastrar!">
		    </div><!--form-group-->

    </form>

	<div class="wraper-table w80">				
		<table>
				<tr>
					<td class="coluna-tabela text-center">Grades Curricular Existentes</td>													
				</tr>
				    <?php  
						$grades = MainMolde::selectAll('grades_curricules','',array());
						foreach ($grades as $key => $value) { 
					?>									
				<tr>
					<td class = "text-center"> <a href = "editar-grade-curricular?id=<?php echo $value['id']?>"> <?php echo $value['nome'] ?> </a></td>		    															
		        </tr>
				   <?php } ?>
		 </table>	
	</div><!--wraper-table-->

</section><!--box-principal-->
</div><!-- content -- >