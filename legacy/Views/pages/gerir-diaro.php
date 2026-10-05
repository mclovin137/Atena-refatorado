<?php
     use \Models\MainMolde;
    $Data = MainMolde::select('datas','',array());	
    @$data = date('Y-m-d');
    if (@$Data[0] == "" || @$Data[0] == 0000-00-00) 
    {
           $Data[0] = $data;
    }
    if (@$Data[1] == "" || @$Data[1] == 0000-00-00) 
    {
           $Data[1] = $Data;
    }
    if (@$Data[2] == "" || @$Data[2] == 0000-00-00) 
    {
            $Data[2] = $Data;
    }
    if (@$Data[3] == "" || @$Data[3] == 0000-00-00) 
    {
            $Data[3] = $data;
    }
?>

<div class="content">
    <div class="box-content-diario">  
        <form  method="post" enctype="multipart/form-data">
            <div class="wraper-table">				
				<table>                  
					 <tr>
                        <td class="coluna-tabela text-center">#</td>
						<td class="coluna-tabela text-center">1° Bimestre</td>
                        <td class="coluna-tabela text-center">2° Bimestre</td>
                        <td class="coluna-tabela text-center">3° Bimestre</td>
                        <td class="coluna-tabela text-center">4° Bimestre</td>														
					 </tr>

				     <tr>
                        <td class="coluna-tabela text-center"> Estado </td>
					    <td class="coluna-tabela text-center">
                            <?php 
                                if($data <= $Data[0])
                                    echo "ativado";
                                else
                                    echo "desativado";
                            ?>
                        </td>
                        <td class="coluna-tabela text-center"> 
                            <?php 
                                if($data <= $Data[1])
                                    echo "ativado";
                                else
                                    echo "desativado";
                            ?>
                        </td>	
                        <td class="coluna-tabela text-center"> 
                            <?php 
                                if($data <= $Data[2])
                                    echo "ativado";
                                else
                                    echo "desativado";
                            ?>
                        </td>
                        <td class="coluna-tabela text-center"> 
                            <?php 
                                if($data <= $Data[3])
                                    echo "ativado";
                                else
                                    echo "desativado";
                            ?>
                        </td>									
					 </tr>

                     <tr>
                        <td class="coluna-tabela text-center">  Data de fechamento </td>
					    <td class="coluna-tabela "> 
                            <input type="date" class ="input-date" name="data1" min="2020-01-01" max="2020-12-31" value = "<?php echo $Data['datanota01'] ?>">
                        </td>
						<td class="coluna-tabela "> 
                            <input type="date" class ="input-date" name="data2" min="2020-01-01" max="2020-12-31" value = "<?php echo $Data['datanota02'] ?>"> 
                        </td>
						<td class="coluna-tabela "> 
                            <input type="date" class ="input-date" name="data3" min="2020-01-01" max="2020-12-31" value = "<?php echo $Data['datanota03'] ?>"> 
                        </td>
						<td class="coluna-tabela "> 
                            <input type="date" class ="input-date" name="data4" min="2020-01-01" max="2020-12-31" value = "<?php echo $Data['datanota04'] ?>"> 
                        </td>														
					 </tr>
			    </table>	
		    </div><!--wraper-table-->

            <div class="form-group">
			         <input type="submit" class = "btn btn-primary" name="limit-tempo" value="Enviar">
		    </div><!--form-group-->

        </form>    
    </div>
</div><!--content -- >
