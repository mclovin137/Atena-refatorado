<?php
        use \Models\MainMolde;
        $prof = $_SESSION['matricula'];
        $ano = date('Y',time());



                $horariosegunda01 = MySql::conectar()->prepare("SELECT * FROM horario WHERE dia = 'segunda' AND horario = '07:00'  AND cod_professo = ? AND cod_ano = ?");
                $horariosegunda01->execute([$prof, $ano]);
                $horariosegunda01 = $horariosegunda01->fetch();

                $horariosegunda02 = MySql::conectar()->prepare("SELECT * FROM horario WHERE dia = 'segunda' AND horario = '08:55' AND cod_professo = ? AND cod_ano = ?");
                $horariosegunda02->execute([$prof, $ano]);
                $horariosegunda02 = $horariosegunda02->fetch();

                $horariosegunda03 = MySql::conectar()->prepare("SELECT * FROM horario WHERE dia = 'segunda' AND horario = '09:10' AND cod_professo = ? AND cod_ano = ?");
                $horariosegunda03->execute([$prof, $ano]);
                $horariosegunda03 = $horariosegunda03->fetch();

                $horariosegunda04 = MySql::conectar()->prepare("SELECT * FROM horario WHERE dia = 'segunda' AND horario = '10:05' AND cod_professo = ? AND cod_ano = ?");
                $horariosegunda04->execute([$prof, $ano]);
                $horariosegunda04 = $horariosegunda04->fetch();


                $horarioterca01 = MySql::conectar()->prepare("SELECT * FROM horario WHERE dia = 'terca' AND horario = '07:00'   AND cod_professo = ? AND cod_ano = ?");
                $horarioterca01->execute([$prof, $ano]);
                $horarioterca01 = $horarioterca01->fetch();

                $horarioterca02 = MySql::conectar()->prepare("SELECT * FROM horario WHERE dia = 'terca' AND horario = '08:55'   AND cod_professo = ? AND cod_ano = ?");
                $horarioterca02->execute([$prof, $ano]);
                $horarioterca02 = $horarioterca02->fetch();

                $horarioterca03 = MySql::conectar()->prepare("SELECT * FROM horario WHERE dia = 'terca' AND horario = '09:10'   AND cod_professo = ? AND cod_ano = ?");
                $horarioterca03->execute([$prof, $ano]);
                $horarioterca03 = $horarioterca03->fetch();

                $horarioterca04 = MySql::conectar()->prepare("SELECT * FROM horario WHERE dia = 'terca' AND horario = '10:05'   AND cod_professo = ? AND cod_ano = ?");
                $horarioterca04->execute([$prof, $ano]);
                $horarioterca04 = $horarioterca04->fetch();


                $horarioquarta01 = MySql::conectar()->prepare("SELECT * FROM horario WHERE dia = 'quarta' AND horario = '07:00'  AND cod_professo = ? AND cod_ano = ?");
                $horarioquarta01->execute([$prof, $ano]);
                $horarioquarta01 = $horarioquarta01->fetch();

                $horarioquarta02 = MySql::conectar()->prepare("SELECT * FROM horario WHERE dia = 'quarta' AND horario = '08:55'   AND cod_professo = ? AND cod_ano = ?");
                $horarioquarta02->execute([$prof, $ano]);
                $horarioquarta02 = $horarioquarta02->fetch();

                $horarioquarta03 = MySql::conectar()->prepare("SELECT * FROM horario WHERE dia = 'quarta' AND horario = '09:10'   AND cod_professo = ? AND cod_ano = ?");
                $horarioquarta03->execute([$prof, $ano]);
                $horarioquarta03 = $horarioquarta03->fetch();

                $horarioquarta04 = MySql::conectar()->prepare("SELECT * FROM horario WHERE dia = 'quarta' AND horario = '10:05'   AND cod_professo = ? AND cod_ano = ?");
                $horarioquarta04->execute([$prof, $ano]);
                $horarioquarta04 = $horarioquarta04->fetch();


                $horarioquinta01 = MySql::conectar()->prepare("SELECT * FROM horario WHERE dia = 'quinta' AND horario = '07:00'   AND cod_professo = ? AND cod_ano = ?");
                $horarioquinta01->execute([$prof, $ano]);
                $horarioquinta01 = $horarioquinta01->fetch();

                $horarioquinta02 = MySql::conectar()->prepare("SELECT * FROM horario WHERE dia = 'quinta' AND horario = '08:55'   AND cod_professo = ? AND cod_ano = ?");
                $horarioquinta02->execute([$prof, $ano]);
                $horarioquinta02 = $horarioquinta02->fetch();

                $horarioquinta03 = MySql::conectar()->prepare("SELECT * FROM horario WHERE dia = 'quinta' AND horario = '09:10'   AND cod_professo = ? AND cod_ano = ?");
                $horarioquinta03->execute([$prof, $ano]);
                $horarioquinta03 = $horarioquinta03->fetch();

                $horarioquinta04 = MySql::conectar()->prepare("SELECT * FROM horario WHERE dia = 'quinta' AND horario = '10:05'   AND cod_professo = ? AND cod_ano = ?");
                $horarioquinta04->execute([$prof, $ano]);
                $horarioquinta04 = $horarioquinta04->fetch();


                $horariosexta01 = MySql::conectar()->prepare("SELECT * FROM horario WHERE  dia = 'sexta' AND horario = '07:00'   AND cod_professo = ? AND cod_ano = ?");
                $horariosexta01->execute([$prof, $ano]);
                $horariosexta01 = $horariosexta01->fetch();

                $horariosexta02 = MySql::conectar()->prepare("SELECT * FROM horario WHERE  dia = 'sexta' AND horario = '08:55'   AND cod_professo = ? AND cod_ano = ?");
                $horariosexta02->execute([$prof, $ano]);
                $horariosexta02 = $horariosexta02->fetch();

                $horariosexta03 = MySql::conectar()->prepare("SELECT * FROM horario WHERE  dia = 'sexta' AND horario = '09:10'   AND cod_professo = ? AND cod_ano = ?");
                $horariosexta03->execute([$prof, $ano]);
                $horariosexta03 = $horariosexta03->fetch();

                $horariosexta04 = MySql::conectar()->prepare("SELECT * FROM horario WHERE  dia = 'sexta' AND horario = '10:05'   AND cod_professo = ? AND cod_ano = ?");
                $horariosexta04->execute([$prof, $ano]);
                $horariosexta04 = $horariosexta04->fetch();



?>
<div class="content">
<div class="box-content-diario">
<h2 class="text-center"><i class="fa fa-id-card-o" aria-hidden="true"></i> Horario </h2><br>
<br>

<div class="content-turmas ">
     <div class="wraper-table">
                <table>

                        <tr>
                            <td class="coluna-tabela text-center">Horario</td>
                            <td class="coluna-tabela text-center">Segunda</td>
                            <td class="coluna-tabela text-center">Terça</td>
                            <td class="coluna-tabela text-center">Quarta</td>
                            <td class="coluna-tabela text-center">Quinta</td>
                            <td class="coluna-tabela text-center">Sexta</td>

                        </tr>


                        <tr>
                            <td class="coluna-tabela text-center">07:00 - 07:55</td>
                            <td class="coluna-tabela text-center"><?php echo \Seguranca::texto(@$horariosegunda01[2]); ?> <?php echo \Seguranca::texto(@$horariosegunda01[3]); ?> - <?php echo \Seguranca::texto(@$horariosegunda01[5]); ?></td>
                            <td class="coluna-tabela text-center"><?php echo \Seguranca::texto(@$horarioterca01[2]); ?>   <?php echo \Seguranca::texto(@$horarioterca01[3]); ?> - <?php echo \Seguranca::texto(@$horarioterca01[5]); ?></td>
                            <td class="coluna-tabela text-center"><?php echo \Seguranca::texto(@$horarioquarta01[2]); ?>  <?php echo \Seguranca::texto(@$horarioquarta01[3]); ?> - <?php echo \Seguranca::texto(@$horarioquarta01[5]); ?></td>
                            <td class="coluna-tabela text-center"><?php echo \Seguranca::texto(@$horarioquinta01[2]); ?>  <?php echo \Seguranca::texto(@$horarioquinta01[3]); ?> - <?php echo \Seguranca::texto(@$horarioquinta01[5]); ?></td>
                            <td class="coluna-tabela text-center"><?php echo \Seguranca::texto(@$horariosexta01[2]); ?> <?php echo \Seguranca::texto(@$horariosexta01[3]); ?> - <?php echo \Seguranca::texto(@$horariosexta01[5]); ?></td>
                        </tr>

                        <tr>
                            <td class="coluna-tabela text-center">07:55 - 08:50</td>
                            <td class="coluna-tabela text-center"><?php echo \Seguranca::texto(@$horariosegunda02[2]); ?> <?php echo \Seguranca::texto(@$horariosegunda02[3]); ?> - <?php echo \Seguranca::texto(@$horariosegunda02[5]); ?></td>
                            <td class="coluna-tabela text-center"><?php echo \Seguranca::texto(@$horarioterca02[2]); ?>   <?php echo \Seguranca::texto(@$horarioterca02[3]); ?> - <?php echo \Seguranca::texto(@$horarioterca02[5]); ?></td>
                            <td class="coluna-tabela text-center"><?php echo \Seguranca::texto(@$horarioquarta02[2]); ?>  <?php echo \Seguranca::texto(@$horarioquarta02[3]); ?> - <?php echo \Seguranca::texto(@$horarioquarta02[5]); ?></td>
                            <td class="coluna-tabela text-center"><?php echo \Seguranca::texto(@$horarioquinta02[2]); ?>  <?php echo \Seguranca::texto(@$horarioquinta02[3]); ?> - <?php echo \Seguranca::texto(@$horarioquinta02[5]); ?></td>
                            <td class="coluna-tabela text-center"><?php echo \Seguranca::texto(@$horariosexta02[2]); ?> <?php echo \Seguranca::texto(@$horariosexta02[3]); ?> - <?php echo \Seguranca::texto(@$horariosexta02[5]); ?></td>
                        </tr>

                        <tr>
                            <td class="coluna-tabela text-center">09:10 - 10:05</td>
                            <td class="coluna-tabela text-center"><?php echo \Seguranca::texto(@$horariosegunda03[2]); ?> <?php echo \Seguranca::texto(@$horariosegunda03[3]); ?> - <?php echo \Seguranca::texto(@$horariosegunda03[5]); ?></td>
                            <td class="coluna-tabela text-center"><?php echo \Seguranca::texto(@$horarioterca03[2]); ?>   <?php echo \Seguranca::texto(@$horarioterca03[3]); ?> - <?php echo \Seguranca::texto(@$horarioterca03[5]); ?></td>
                            <td class="coluna-tabela text-center"><?php echo \Seguranca::texto(@$horarioquarta03[2]); ?>  <?php echo \Seguranca::texto(@$horarioquarta03[3]); ?> - <?php echo \Seguranca::texto(@$horarioquarta03[5]); ?></td>
                            <td class="coluna-tabela text-center"><?php echo \Seguranca::texto(@$horarioquinta03[2]); ?>  <?php echo \Seguranca::texto(@$horarioquinta03[3]); ?> - <?php echo \Seguranca::texto(@$horarioquinta03[5]); ?></td>
                            <td class="coluna-tabela text-center"><?php echo \Seguranca::texto(@$horariosexta03[2]); ?> <?php echo \Seguranca::texto(@$horariosexta03[3]); ?> - <?php echo \Seguranca::texto(@$horariosexta03[5]); ?></td>
                        </tr>

                        <tr>
                            <td class="coluna-tabela text-center">10:05 - 11:00</td>
                           <td class="coluna-tabela text-center"><?php echo \Seguranca::texto(@$horariosegunda04[2]); ?> <?php echo \Seguranca::texto(@$horariosegunda04[3]); ?> - <?php echo \Seguranca::texto(@$horariosegunda04[5]); ?></td>
                            <td class="coluna-tabela text-center"><?php echo \Seguranca::texto(@$horarioterca04[2]); ?>   <?php echo \Seguranca::texto(@$horarioterca04[3]); ?> - <?php echo \Seguranca::texto(@$horarioterca04[5]); ?></td>
                            <td class="coluna-tabela text-center"><?php echo \Seguranca::texto(@$horarioquarta04[2]); ?>  <?php echo \Seguranca::texto(@$horarioquarta04[3]); ?> - <?php echo \Seguranca::texto(@$horarioquarta04[5]); ?></td>
                            <td class="coluna-tabela text-center"><?php echo \Seguranca::texto(@$horarioquinta04[2]); ?>  <?php echo \Seguranca::texto(@$horarioquinta04[3]); ?> - <?php echo \Seguranca::texto(@$horarioquinta04[5]); ?></td>
                            <td class="coluna-tabela text-center"><?php echo \Seguranca::texto(@$horariosexta04[2]); ?> <?php echo \Seguranca::texto(@$horariosexta04[3]); ?> - <?php echo \Seguranca::texto(@$horariosexta04[5]); ?></td>
                        </tr>


                </table>
    </div><!--wraper-table-->
</div><!--content-turmas-->
</div><!-- content-diario-->
</div><!--content -- >
