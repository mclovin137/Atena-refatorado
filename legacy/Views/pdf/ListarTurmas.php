<?php
    include('../../config.php');
   
    use \Models\MySql;
    use \Models\MainMolde;

    $turmas = MainMolde::selectAll('turmas','',array());
    $escolar = MainMolde::select('unidade_escolar','',array());

?>
<html>
<head>
		<link rel="stylesheet" href="<?php echo INCLUDE_PATH ?>Views/estilo/pdf.css">
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
    <h2>Turmas</h2>

    <?php foreach($turmas as $key => $value) {?>
	    <table>  		
				<tr >
					<th >Ano</th>
					<th >Curso</th>
					<th >Serie</th>		
                    <th >Aluno</th>	
                    <th >Situaçao</th>					
				</tr>

				
                <?php 
                    
                    $alunos = MainMolde::selectAll('matriculados','cod_ano = ? AND cod_serie = ? AND cod_curso = ?',array($value['ano'],$value['serie'],$value['curso']));
                    foreach($alunos as $key => $valuee) {

                     @$NotasExistentes = 0;
                     @$Situacao = "";
                     @$estados = 0;
                     @$estados1 = 0;
                     @$estados2 = 0;
                     @$estado = "";
                     @$MateriasTurma = "";
                     $MateriasFinalizadas = "";
                     @$MateriasExistentes = 0;
                     @$MateriasFinalizadasExistentes = 0;


                     @$NotasAluno = MainMolde::selectAll('notas','cod_ano = ? AND cod_aluno = ? ',array($value['ano'],$valuee['cod_aluno']));
                     $NotasExistentes = ceil( count($NotasAluno) );

                     @$MateriasTurma = MainMolde::selectAll('grade_curricular','cod_ano = ? AND cod_serie = ? AND cod_curso = ?'
                     ,array($value['ano'],$value['serie'],$value['curso']));
                     $MateriasExistentes = ceil( count($MateriasTurma) );

                     $MateriasFinalizadas = MainMolde::selectAll('notas','cod_ano = ? AND cod_serie = ? AND cod_curso = ? AND aprovado != 0  AND cod_aluno = ?'
                     ,array($value['ano'],$value['serie'],$value['curso'],$valuee['cod_aluno']));
                     $MateriasFinalizadasExistentes = ceil( count( $MateriasFinalizadas ) );
                     
                     if( ($NotasExistentes != 0) && ($MateriasFinalizadasExistentes == $MateriasExistentes) )
                     {
                       
                        $Situacao = MainMolde::selectAll('notas','cod_ano = ? AND cod_aluno = ? AND NOT aprovado = 1 ',
                        array($value['ano'],$valuee['cod_aluno']));
                        $estados1 = ceil ( count( $Situacao ) );

                        $Situacaoo = MainMolde::selectAll('notas','cod_ano = ? AND cod_aluno = ? AND NOT aprovado = 1 ',
                        array("(dp) ".$value['ano'],$valuee['cod_aluno']));
                        $estados2 = ceil ( count( $Situacaoo ) );

                        $estados = $estados1 + $estados2;

                        if($estados == 0)
                            $estado = "Aprovado";
                        else if ( $estados > 2)
                           $estado = "Reprovado";
                        else
                        {
                          @$materias = "";
                          foreach ($Situacao as $key => $materia) 
                          {
                              $materias .= $materia['cod_diciplina'].". ";
                          }
                          foreach ($Situacaoo as $key => $materia) 
                          {
                              $materias .= $materia['cod_diciplina'].". ";
                          }
                           $estado .= "Depedencia( ".$materias." )";
                        }
                      
                     }
                     else
                     {
                         $estado = "Cursando";
                     }

                ?>
                    <tr>	
                            <td ><?php echo $value['ano'] ?></td>						
                            <td ><?php echo $value['curso'] ?></td>
                            <td ><?php echo $value['serie'] ?></td>
                            <td ><?php echo $valuee['nome'] ?></td>	
                            <td ><?php echo @$estado ?></td>							
                    </tr>
                <?php }?>

	    </table> <br><br>
    <?php }?>

</body>
</html>


