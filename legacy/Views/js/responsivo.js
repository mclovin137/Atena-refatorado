$(function(){

	var open  = true;
	var windowSize = $(window)[0].innerWidth;

	var targetSizeMenu = 250;

   if(windowSize < 1280)
   {
   	        var  targetSizeMenu = 500;
   	        $('.content,header').css('zoom','175%');
			$('.menu').css({'width':0,'padding':0},function(){
				open = false;
			});
			$('.content,header').css('width','100%');

			if(windowSize < 1280)
		    {
		    	$('.box-single-wraper').css('width','50%','	min-height','0px');
			    $('.box-metrica-single').css('width','50%');
		    }

			$('.content,header').css({'left':0},function(){
				open = false;
			});

			
   }    
    
   


	$('.menu-btn').click(function(){
      
		if(open == true)
		{
			//O menu está aberto, precisamos fechar e adaptar nosso conteudo geral do painel
			$('.menu').animate({'width':0,'padding':0},function(){
				open = false;
			});
			$('.content,header').css('width','100%');

			if(windowSize < 1280)
		    {
		    	$('.box-single-wraper').css('width','50%','	min-height','0px');
			    $('.box-metrica-single').css('width','50%');
			    $("input").prop('disabled', false);
				$("button").prop('disabled', false);
				$('.content').css('opacity','1');
				$('.content').css('position','relative');

		    }

			$('.content,header').animate({'left':0},function(){
				open = false;
			});
		}
		else
		{
			//O menu está fechado
			$('.content,header').css('width','100%');
			$('.menu').animate({'width':targetSizeMenu+'px','padding':'10px 0'},function(){
				open = true;
			});
			if(windowSize != 0)
			    if(windowSize < 1280)
			    {
			    	$('.box-single-wraper').css('width','calc(100%-250px)','min-height','0px');
				    $('.box-metrica-single').css('width','calc(100%-250px');
				    $('.menu').css('display','block');
				    $("input").prop('disabled', true);
				    $("button").prop('disabled', true);
				    $('input[type=submit]').removeAttr('disabled');
				    $('.content').css('opacity','0.4');
				    $('.content').css('position','fixed');
				   
			    }
			   
				$('header,.content').animate({'left':targetSizeMenu+'px'},function(){
				open = true;
			});
		}
	})

	
	
})