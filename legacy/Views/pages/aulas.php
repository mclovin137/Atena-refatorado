<?php 
   use \Models\MainMolde;
   $url = explode('/',$_GET['url']);
   $ano = date('Y',time());
   $curso = MainMolde::select('matriculados','cod_aluno = ? AND cod_ano = ?',array($_SESSION['matricula'],$ano));
   $cursoAluno = $curso[1];
   $serie = $curso[2];
   $materia = $url[2];
   $materias = MainMolde::select('diciplina','cod_curso = ? AND cod_ano = ? AND cod_serie = ? AND nome = ?',array($cursoAluno,$ano,$serie,$materia));
   $cod = $materias[5];
   $atividades = \MySql::conectar()->prepare("SELECT * FROM `presenca` WHERE cod_aluno = ? AND cod_professo = ? AND cod_serie = ? AND cod_ano = ?  AND cod_curso = ? AND cod_materia = ? ORDER BY data");
   $atividades->execute(array($_SESSION['matricula'],$cod,$serie,$ano,$cursoAluno,$materia));
   $atividades = $atividades->fetchAll();

    
?>

<div class="content">
<div class="box-content-diario">
      <h2 class="text-center"> Aulas Ministradas</h2><br>
      <div class="wraper-table">            
         <table>    
            <tr>
                  <td class="coluna-tabela text-center">Data</td>
                  <td class="coluna-tabela text-center">Diciplina</td>
                  <td class="coluna-tabela text-center">Conteudo</td> 
                  <td class="coluna-tabela text-center">Presença</td>                    
            </tr>

         <?php foreach ($atividades as $key => $value) { 
              $date = DateTime::createFromFormat('Y-m-d',$value['data']);
              $date = $date->format('d/m/Y');     
              $conteudo = MainMolde::select('aulas','cod_professo = ? AND cod_serie = ? AND cod_ano = ?  AND cod_curso = ? AND cod_materia = ? AND data = ?',array($cod,$serie,$ano,$cursoAluno,$materia,$date));
              $faltas = $value['faltas'];
              $falta = ($faltas != 0 ? "Ausente" : "Presente" );
         ?>            
               <tr>  
                     <td class="coluna-tabela text-center"> <?php echo $date ?></td>
                     <td class="coluna-tabela text-center"> <?php echo $value['cod_materia'] ?></td>
                     <td class="coluna-tabela text-center"> <?php echo $conteudo[7] ?></td>
                     <td class="coluna-tabela text-center"> <?php echo $falta ?> </td>                                                     
               </tr>
          <?php }?>

         </table>        
      </div><!--wraper-table-->
</div><!--box-content-->
</div><!-- content -- >