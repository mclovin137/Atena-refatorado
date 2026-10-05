<?php

include('../../config.php');
use \Models\MainMolde;
 
    $data['mensagem'] = "";
    $data['sucesso'] = true;
    $dom = array();
    $dom = "";
    if(isset($_POST['Cadastre-horario']))
    {
                $materia = strip_tags($_POST['materia']);
                $ano = strip_tags($_POST['ano']);
                $serie = strip_tags($_POST['serie']);
                $curso = strip_tags($_POST['curso']);
                $professor = strip_tags($_POST['professor']);
                $horario = strip_tags($_POST['horario']);
                $Y = $ano;
                $dataInvalida = false;
                for ($i=1; $i <= 12; $i++) 
                { 
                    $numeroDias = cal_days_in_month(CAL_GREGORIAN,$i,$Y);
                    for ($c=1; $c <= $numeroDias ; $c++) 
                    {  
                       
                     
                       
                        if($c < 10 && $i < 10)
                          $data_dia = $Y.'-'.'0'.$i.'-'.'0'.$c;

                        else if($c >= 10 && $i >= 10)
                          $data_dia = $Y.'-'.$i.'-'.$c;

                        else if($c >= 10 && $i < 10)
                          $data_dia = $ano.'-'.'0'.$i.'-'.$c;

                         else if($c < 10 && $i >= 10)
                          $data_dia = $Y.'-'.$i.'-'.'0'.$c;
                     
                        $diaSemana = date('w',strtotime($data_dia));

                        if ($diaSemana == 1) 
                        {
                            $segunda[] = $data_dia;
                        }
                        else if($diaSemana == 2)
                        {
                            $terca[] = $data_dia;
                        }
                        else if($diaSemana == 3)
                        {
                            $quarta[] = $data_dia;
                        }
                        else if($diaSemana == 4)
                        {
                            $quinta[] = $data_dia;
                        }
                        else if($diaSemana == 5)
                        {
                            $sexta[] = $data_dia;
                        }
                    
                    }

                    
                }
              
                if (isset($_POST['segunda'])) 
                {
                
      
                      
                        foreach ($segunda as $dia) 
                        {
                            $verifica = MainMolde::selectAll('horario','cod_curos = ? AND cod_serie = ? AND cod_ano = ? AND data = ? AND horario = ? AND cod_ano = ?',array($curso,$serie,$ano,$dia,$horario,$ano));
                            $nv = ceil(count($verifica));

                            $verifica1 = MainMolde::selectAll('horario','data = ? AND horario = ? AND cod_professo = ?',array($dia,$horario,$professor));
                            $nv1 = ceil(count($verifica1));

                            if ($nv == 0 && $nv1 == 0) 
                            {
                                $datas[] = $dia;
                              
                            }
                            else
                            {
                                $dataInvalida = true;
                                break;
                            }
                            
                        }                  
                
                } 

                if (isset($_POST['terca'])) 
                {
                    foreach ($terca as $dia) 
                    {
                           
                             $verifica = MainMolde::selectAll('horario','cod_curos = ? AND cod_serie = ? AND cod_ano = ? AND data = ? AND horario = ? AND cod_ano = ?',array($curso,$serie,$ano,$dia,$horario,$ano));
                            $nv = ceil(count($verifica));

                            $verifica1 = MainMolde::selectAll('horario','data = ? AND horario = ? AND cod_professo = ?',array($dia,$horario,$professor));
                            $nv1 = ceil(count($verifica1));

                            if ($nv == 0 && $nv1 == 0) 
                            {
                                $datas[] = $dia;
                                
                            }
                            else
                            {
                                $dataInvalida = true;
                                break;
                            }
                    }
                } 

                if (isset($_POST['quarta'])) 
                {
                    foreach ($quarta as $dia) 
                    {
                           
                           $verifica = MainMolde::selectAll('horario','cod_curos = ? AND cod_serie = ? AND cod_ano = ? AND data = ? AND horario = ? AND cod_ano = ?',array($curso,$serie,$ano,$dia,$horario,$ano));
                            $nv = ceil(count($verifica));

                            $verifica1 = MainMolde::selectAll('horario','data = ? AND horario = ? AND cod_professo = ?',array($dia,$horario,$professor));
                            $nv1 = ceil(count($verifica1));

                            if ($nv == 0 && $nv1 == 0) 
                            {
                                $datas[] = $dia;
                               
                            }
                            else
                            {
                                $dataInvalida = true;
                                break;
                            }

                    }
                } 

                if (isset($_POST['quinta'])) 
                {
                    foreach ($quinta as $dia) 
                    {
                           
                           $verifica = MainMolde::selectAll('horario','cod_curos = ? AND cod_serie = ? AND cod_ano = ? AND data = ? AND horario = ? AND cod_ano = ?',array($curso,$serie,$ano,$dia,$horario,$ano));
                            $nv = ceil(count($verifica));

                            $verifica1 = MainMolde::selectAll('horario','data = ? AND horario = ? AND cod_professo = ?',array($dia,$horario,$professor));
                            $nv1 = ceil(count($verifica1));

                            if ($nv == 0 && $nv1 == 0) 
                            {
                                $datas[] = $dia;
                              
                            }
                            else
                            {
                                $dataInvalida = true;
                                break;
                            }
                    }
                } 
                if (isset($_POST['sexta'])) 
                {
                    foreach ($sexta as $dia) 
                    {
                          
                            $verifica = MainMolde::selectAll('horario','cod_curos = ? AND cod_serie = ? AND cod_ano = ? AND data = ? AND horario = ? AND cod_ano = ?',array($curso,$serie,$ano,$dia,$horario,$ano));
                            $nv = ceil(count($verifica));

                            $verifica1 = MainMolde::selectAll('horario','data = ? AND horario = ? AND cod_professo = ?',array($dia,$horario,$professor));
                            $nv1 = ceil(count($verifica1));

                            if ($nv == 0 && $nv1 == 0) 
                            {
                                $datas[] = $dia;
                               
                            }
                            else
                            {
                                $dataInvalida = true;
                                break;
                            }
                    }
                } 
                 
                if(@$datas != "")   
                {
                        foreach (@$datas as $datas)
                        {
                               $dia_n = date('w',strtotime($datas));
                               switch ($dia_n) 
                               {
                                    case 1:
                                       $dia = "segunda";
                                       break;
                                    case 2:
                                       $dia = "terca";
                                       break;
                                    case 3:
                                       $dia = "quarta";
                                       break;
                                    case 4:
                                       $dia = "quinta";
                                       break;
                                    case 5:
                                       $dia = "sexta";
                                       break;
                                   
                                     default:
                                       break;
                               } 

                               $verifica = MainMolde::select('diciplina','cod_curso = ? AND cod_serie = ? AND cod_ano = ?  ',
                               array($curso,$serie,$ano));
                              
                               if($verifica != 0)
                               {
                                    $sql = \MySql::conectar()->prepare("INSERT INTO `horario` VALUES (null,?,?,?,?,?,?,?,?)");
                                    $sql->execute(array($professor,$materia,$curso,$ano,$serie,$datas,$horario,$dia));
                                    $data['mensagem'] =  "cadastro realizado com sucesso!";
                               }
                               else
                               {
                                    $data['sucesso'] = false;
                                    $data['mensagem'] =  "Diciplina nao encontrada!";
                               }
                              
                        }

                   
                }
                else
                {
                     if ($dataInvalida == true) 
                     {
                            $data['sucesso'] = false;
                            $data['mensagem'] =  "horario ocupado!";
                     }
                     else
                     {
                            $data['sucesso'] = false;
                            $data['mensagem'] =  "nehuma data foi selecionada!";
                     }

                   
                }

        die(json_encode($data));

    }

 
     @$professor = strip_tags($_POST['professor']);
     $AnoAtual = date('Y');
     $materia = MainMolde::selectAll('diciplina','cod_professo = ? AND cod_ano = ?',array($professor,$AnoAtual));
     
     $dom.='<label>Materia:</label>';
     $dom.='<select name="materia" required>';
    

        foreach ($materia as $key => $value) 
        {           
            $dom.=
            '<option value="'.$value['nome'].'">'.$value['nome'].'</option>';
                  
        }

      $dom.='</select>';

     echo $dom;

?>