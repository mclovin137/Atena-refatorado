<?php
    use \Models\MainMolde;
    $dados = MainMolde::select('unidade_escolar','',array());
?>
<div class="content">
<section class="box-content center">
    <form class="ajax-cadastro" action="<?php echo INCLUDE_PATH?>Models/ajax/CadastroUnidadeEscolar.php" method="post" enctype="multipart/form-data"  ><br><br>
        <div >
            <input id='selecao-arquivo' class="input-img" type="file" name="img"/>
            <label class = "img-logo" for='selecao-arquivo'><img scr = ../uploads/user.jpg /></label> 
        </div>
        <h2 class = "text-center"><?php echo $dados[1] ?><h2>

        <div class ="textt"> 
            <p class = "linerr"></p>
            <h5 class = "text-titulo-descriçao">Documentos</h5>
            <div class="wraper-table-pdf">			
                <table>
                        <tr>
                            <td class="coluna-tabela text-center">Relatorios</td>				
                        </tr>
                    
                        <tr>	
                            <td class="coluna-tabela text-center"> <a href="<?php echo INCLUDE_PATH?>Models/pdf/ListaAlunos.php">Alunos Cadastrados</a> </td>														
                        </tr>

                        <tr>	
                            <td class="coluna-tabela text-center"> <a href="<?php echo INCLUDE_PATH?>Models/pdf/ListaProfessores.php">Professores Cadastrados</a> </td>														
                        </tr>

                        <tr>	
                            <td class="coluna-tabela text-center"> <a href="<?php echo INCLUDE_PATH?>Models/pdf/ListaTurmas.php">Turmas</a> </td>														
                        </tr>
                    
                </table>	
            </div><!--wraper-table-->
        </div><br>

        <div class ="text"> 
            <p class = "liner"></p>
            <h5 class = "text-titulo-descriçao">Dados</h5>
                <ul>
                    <li class = "text-center">Nome : 
                        <input class = "input-text" type="text" value = "<?php echo $dados[1] ?>" name = "nome" require/>
                    </li>
                    <li class = "text-center">Cnpj : 
                        <input class = "input-text " type="text" value = "<?php echo $dados[7] ?>" name = "cnpj" require />
                    </li>
                    <li class = "text-center">Diretor : 
                        <input class = "input-text" type="text" value = "<?php echo $dados[3] ?>" name = "diretor" require />
                    </li>
                    <li class = "text-center">Endereço :
                        <input class = "input-text" type="text" value = "<?php echo $dados[2] ?>" name = "endereco" require />
                    </li>
                    <li class = "text-center">Telefone : 
                        <input class = "input-text" type="text" value = "<?php echo $dados[6] ?>" name = "telefone" require />
                    </li>
                    <li class = "text-center">Duraçao da aula :
                        <input class = "input-text " type="text" value = "<?php echo $dados[5] ?>" name = "duracao" require/>
                    </li>
                    <li class = "text-center">Quantidade de aulas : 
                        <input class = "input-text numb" type="text" value = "<?php echo $dados[4] ?>" name = "quantidade_aulas" require />
                    </li>
                    
                </ul>
        </div><br>
       
        <div class = "center-btn">
            <input type="submit" value = "Cadastra" name = "Cadastra" />
        </div>
    </form>

</section>
</div> <!-- content -- >