<?php 
        use \Models\MainMolde;
		$notas = MainMolde::selectAll('notas',' cod_professo = ? AND cod_ano = "2020"',array($_SESSION['matricula']));

        foreach ($notas as $key => $value)
        {
        	 @$nota_1_bm_final += $value['n1'] > $value['r1'] ? $value['n1'] : $value['r1'];
        	 @$nota_2_bm_final += $value['n2'] > $value['r2'] ? $value['n2'] : $value['r2'];
        	 @$nota_3_bm_final += $value['n3'] > $value['r3'] ? $value['n3'] : $value['r3'];
        	 @$nota_4_bm_final += $value['n4'] > $value['r4'] ? $value['n4'] : $value['r4'];
             @$quandidade_avaliacoes_1bm = $value['n1'] != "" ? $quandidade_avaliacoes_1bm + 1:  $quandidade_avaliacoes_1bm  + 0;
             @$quandidade_avaliacoes_2bm = $value['n2'] != "" ? $quandidade_avaliacoes_2bm + 1:  $quandidade_avaliacoes_2bm  + 0;
             @$quandidade_avaliacoes_3bm = $value['n3'] != "" ? $quandidade_avaliacoes_3bm + 1:  $quandidade_avaliacoes_3bm  + 0;
             @$quandidade_avaliacoes_4bm = $value['n4'] != "" ? $quandidade_avaliacoes_4bm + 1:  $quandidade_avaliacoes_4bm  + 0;

        }

        @$quandidade_avaliacoes_1bm = @$quandidade_avaliacoes_1bm == 0 ? @$quandidade_avaliacoes_1bm + 1 : $quandidade_avaliacoes_1bm;
        @$quandidade_avaliacoes_2bm = @$quandidade_avaliacoes_2bm == 0 ? @$quandidade_avaliacoes_2bm + 1 : $quandidade_avaliacoes_2bm;
        @$quandidade_avaliacoes_3bm = @$quandidade_avaliacoes_3bm == 0 ? @$quandidade_avaliacoes_3bm + 1 : $quandidade_avaliacoes_3bm;
        @$quandidade_avaliacoes_4bm = @$quandidade_avaliacoes_4bm == 0 ? @$quandidade_avaliacoes_4bm + 1 : $quandidade_avaliacoes_4bm;
        @$nota_1_bm_final = @$nota_1_bm_final == "" ? @$nota_1_bm_final = 0 : $nota_1_bm_final;
        @$nota_2_bm_final = @$nota_2_bm_final == "" ? @$nota_2_bm_final = 0 : $nota_2_bm_final;
        @$nota_3_bm_final = @$nota_3_bm_final == "" ? @$nota_3_bm_final = 0 : $nota_3_bm_final;
        @$nota_4_bm_final = @$nota_4_bm_final == "" ? @$nota_4_bm_final = 0 : $nota_4_bm_final;
       
        $nota_1_bm = $nota_1_bm_final / $quandidade_avaliacoes_1bm;
        $nota_2_bm = $nota_2_bm_final / $quandidade_avaliacoes_2bm;
        $nota_3_bm = $nota_3_bm_final / $quandidade_avaliacoes_3bm;
        $nota_4_bm = $nota_4_bm_final / $quandidade_avaliacoes_4bm;
     
?>
<div class="content">
	 <canvas id="myChart"  max-width="40O" max-height="400" ></canvas>
	 <input type="hidden" id="bm1" value="<?php echo $nota_1_bm ?> ">
	 <input type="hidden" id="bm2" value="<?php echo $nota_2_bm ?>">
	 <input type="hidden" id="bm3" value="<?php echo $nota_3_bm ?>">
	 <input type="hidden" id="bm4" value="<?php echo $nota_4_bm ?>">
	 <!--<canvas id="myChart2" max-width="400" max-height="400"></canvas>-->
</div><!--content-->