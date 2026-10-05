<?php
    include('../../config.php');
   
    use \Models\MySql;
    use \Models\MainMolde;
	$alunos = MainMolde::selectAll('professo2','',array());
	$escolar = MainMolde::select('unidade_escolar','',array());
   
?>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="../../Views/estilo/pdf.css" />
    </head>
 
    <body>
	   
	<h2> <?php echo $escolar[1]; ?> </h2>

		<table> 
			<tr >
				<th>TELEFONE</th> 
				<th>ENDEREÇO</th>
				<th>CNPJ</th>
			</tr>
			<tr >
				<td> <?php echo $escolar[6]; ?> </td> 
				<td> <?php echo $escolar[2]; ?> </td>
				<td> <?php echo $escolar[7]; ?> </td> 
			</tr>
		</table> 
       
        <h2>  Professores </h2>
   
        <table>  		
				<tr >
					<th >Nome</th>
					<th >E-mail</th>
					<th >Matricula</th>				
				</tr>

				<?php
							
							foreach ($alunos as $key => $value) {
                                

				?>          
            
				<tr>	
									<td ><?php echo $value['nome']; ?></td>						
									<td ><?php echo $value['email']; ?></td>
									<td ><?php echo $value['matricula']; ?></td>
									
				</tr>
        
			<?php  } ?>	
	    </table>  
	</div><!--wraper-table-->

        </body>
</html>