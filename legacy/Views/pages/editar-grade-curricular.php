<?php 
    use \Models\MainMolde;
    if(isset($_GET['id']))
	{	
		$id = $_GET['id'];
		$grade = MainMolde::select('grades_curricules','id = ?',array($id));
		$nome = $grade[1];
		$grade_materias = MainMolde::selectAll('grade_curricular_padroes','grade = ?',array($nome));
		foreach($grade_materias as $key => $value)
		{
			$materias[] = $value['nome'];
		}
	}
	else
	{
		\Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
	}
?>
<div class="content">
<section class="box-content center">
	<form  method="post" enctype="multipart/form-data">

		<div class="form-group">
			<label>Nome :</label>
			<input type="text" name="novonome" value = "<?php echo $grade[1] ?>" >
			<input type="hidden" name="nome" value = "<?php echo $grade[1] ?>" >
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
							$materiais = MainMolde::selectAll('materia','',array());
							foreach ($materiais as $key => $value) { 
							 $aux = false;		
							 foreach ($materias as $key => $valuee) 
							 { 
								if($valuee == $value['nome'])
								{
									$aux = true;
									$gradee = MainMolde::select('grade_curricular_padroes','grade = ? AND nome = ?',array($nome,$valuee));
								}
							 }						
							
						?>		     
						
							<tr>
							    <td class="coluna-tabela text-center"><input type="checkbox" name="materias[]" value="<?php echo $value['id']?>"
								    <?php if(@$aux == true){ ?> checked <?php } ?> 
								> </td>	
							    <td class="coluna-tabela text-center"><?php echo $value['nome'] ?></td>
								<td class="coluna-tabela text-center"><input  type="number" step="1" min="1"  name="ch<?php echo $value['id'] ?>" 
									<?php if(@$aux == true){ ?> value = "<?php echo $gradee[2] ?>" <?php } ?> 
								> </td>	
								<input type="hidden" name = "id<?php echo $value['id']?>" value = "<?php echo $value['nome']?>" >																		
							</tr>
		  
					   <?php } ?>

				    </table>	
			    </div><!--wraper-table-->
                	
            <div class="form-group">
			    <input type="submit"  name="Cadastre" value="Atualizar!">
				<input type="submit"  name="Deleta" value="Deleta a grade!">
		    </div><!--form-group-->

    </form>
</section><!--box-principal-->
</div><!-- content -- >