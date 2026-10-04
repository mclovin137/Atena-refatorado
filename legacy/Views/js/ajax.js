

$(function(){
	$('.ajax').ajaxForm({
		dataType:'json',
		beforeSend:function()
		{
			$('.ajax').animate({'opacity':'0.6'});
			$('.ajax').find('input[type=submit]').attr('disabled','true');
		},
		success: function(data)
		{
			$('.ajax').animate({'opacity':'1'}); 
			$('.ajax').find('input[type=submit]').removeAttr('disabled');
			$('.erro-box').remove();
		    $('.box-sucesso').remove();
			if(data.sucesso)
			{
				$('.ajax').prepend('<div class="box-sucesso"><i class="fa fa-check"></i> '+data.mensagem+'</div>');
				$('.erro-box').remove();
				$('#nome').focus();
				$('.ajax').find('input[class=required]').val("");

			}
			else
			{
				$('.ajax').prepend('<div class="erro-box"><i class="fa fa-times"></i> '+data.mensagem+'</div>');
				$('.box-sucesso').remove();
				$('#nome').focus();
				
			}
			
		}
	})
	
	$('.ajaxx').ajaxForm({
		dataType:'json',
		beforeSend:function()
		{
			$('.ajaxx').animate({'opacity':'0.6'});
			$('.ajaxx').find('input[type=submit]').attr('disabled','true');
		},
		success: function(data)
		{
			$('.ajaxx').animate({'opacity':'1'});
			$('.ajaxx').find('input[type=submit]').removeAttr('disabled');
			$('.erro-box').remove();
		    $('.box-sucesso').remove();
			if(data.sucesso)
			{
				$('.ajaxx').prepend('<div class="box-sucesso"><i class="fa fa-check"></i> '+data.mensagem+'</div>');
				$('.erro-box').remove();
				$('#nome').focus();
			}
			else
			{
				$('.ajaxx').prepend('<div class="erro-box"><i class="fa fa-times"></i> '+data.mensagem+'</div>');
				$('.box-sucesso').remove();
				$('#nome').focus();
				
			}
			
		}
    })

    $('.formch').ajaxForm({
		dataType:'json',
		beforeSend:function()
		{
			$('.formch').animate({'opacity':'0.6'});
			$('.formch').find('input[type=submit]').attr('disabled','true');
		},
		success: function(data)
		{
			$('.formch').animate({'opacity':'1'});
			$('.formch').find('input[type=submit]').removeAttr('disabled');
			$('.erro-box').remove();
		    $('.box-sucesso').remove();
			if(data.sucesso)
			{
				$('.formch').prepend('<div class="box-sucesso"><i class="fa fa-check"></i> '+data.mensagem+'</div>');
				$('.erro-box').remove();
				$('html, header').animate({ scrollTop: $('.formch').offset().top }, 'slow');
				
			}
			else
			{
				$('.formch').prepend('<div class="erro-box"><i class="fa fa-times"></i> '+data.mensagem+'</div>');
				$('.box-sucesso').remove();
				$('html, header').animate({ scrollTop: $('.formch').offset().top }, 'slow');
				
			}
			
		}
    })

	$('.ajax-cadastro').ajaxForm({
		dataType:'json',
		beforeSend:function()
		{
			$('.ajax-cadastro').animate({'opacity':'0.6'});
			$('.ajax-cadastro').find('input[type=submit]').attr('disabled','true');
		},
		success: function(data)
		{
			$('.ajax-cadastro').animate({'opacity':'1'});
			$('.ajax-cadastro').find('input[type=submit]').removeAttr('disabled');
			$('.erro-box').remove();
		    $('.box-sucesso').remove();
			if(data.sucesso)
			{
			    $('html, header').animate({ scrollTop: $('.ajax-cadastro').offset().top }, 'slow');
				$('.ajax-cadastro').prepend('<div class="box-sucesso"><i class="fa fa-check"></i> '+data.mensagem+'</div>');
				$('.erro-box').remove();
							
			}
			else
			{
			    $('html, header').animate({ scrollTop: $('.ajax-cadastro').offset().top }, 'slow');
				$('.ajax-cadastro').prepend('<div class="erro-box"><i class="fa fa-times"></i> '+data.mensagem+'</div>');
				$('.box-sucesso').remove();
				
				
			}
			
		}
	})

	$('.ajax-bm1').ajaxForm({
		dataType:'json',
		beforeSend:function()
		{
			$('.ajax-bm1').animate({'opacity':'0.6'});
			$('.ajax-bm1').find('input[type=submit]').attr('disabled','true');
		},
		success: function(data)
		{
			$('.ajax-bm1').animate({'opacity':'1'});
			$('.ajax-bm1').find('input[type=submit]').removeAttr('disabled');
			$('.erro-box').remove();
		    $('.box-sucesso').remove();
			if(data.sucesso)
			{
			    $('html, header').animate({ scrollTop: $('.ajax-bm1').offset().top }, 'slow');
				$('.ajax-bm1').prepend('<div class="box-sucesso"><i class="fa fa-check"></i> '+data.mensagem+'</div>');
				$('.erro-box').remove();				
				
			}
			else
			{
				$('html, header').animate({ scrollTop: $('.ajax-bm1').offset().top }, 'slow');
				$('.ajax-bm1').prepend('<div class="erro-box"><i class="fa fa-times"></i> '+data.mensagem+'</div>');
				$('.box-sucesso').remove();
								
			}
			
		}
	})
     	$('.ajax-rec1').ajaxForm({
		dataType:'json',
		beforeSend:function()
		{
			$('.ajax-rec1').animate({'opacity':'0.6'});
			$('.ajax-rec1').find('input[type=submit]').attr('disabled','true');
		},
		success: function(data)
		{
			$('.ajax-rec1').animate({'opacity':'1'});
			$('.ajax-rec1').find('input[type=submit]').removeAttr('disabled');
			$('.erro-box').remove();
		    $('.box-sucesso').remove();
			if(data.sucesso)
			{
			    $('html, header').animate({ scrollTop: $('.ajax-rec1').offset().top }, 'slow');
				$('.ajax-rec1').prepend('<div class="box-sucesso"><i class="fa fa-check"></i> '+data.mensagem+'</div>');
				$('.erro-box').remove();				
				
			}
			else
			{
				$('html, header').animate({ scrollTop: $('.ajax-rec1').offset().top }, 'slow');
				$('.ajax-rec1').prepend('<div class="erro-box"><i class="fa fa-times"></i> '+data.mensagem+'</div>');
				$('.box-sucesso').remove();
								
			}
			
		}
	})


	$('.ajax-bm2').ajaxForm({
		dataType:'json',
		beforeSend:function()
		{
			$('.ajax-bm2').animate({'opacity':'0.6'});
			$('.ajax-bm2').find('input[type=submit]').attr('disabled','true');
		},
		success: function(data)
		{
			$('.ajax-bm2').animate({'opacity':'1'});
			$('.ajax-bm2').find('input[type=submit]').removeAttr('disabled');
			$('.erro-box').remove();
		    $('.box-sucesso').remove();
			if(data.sucesso)
			{
			    $('html, header').animate({ scrollTop: $('.ajax-bm2').offset().top }, 'slow');
				$('.ajax-bm2').prepend('<div class="box-sucesso"><i class="fa fa-check"></i> '+data.mensagem+'</div>');
				$('.erro-box').remove();				
				
			}
			else
			{
				$('html, header').animate({ scrollTop: $('.ajax-bm2').offset().top }, 'slow');
				$('.ajax-bm2').prepend('<div class="erro-box"><i class="fa fa-times"></i> '+data.mensagem+'</div>');
				$('.box-sucesso').remove();
								
			}
			
		}
	})

	$('.ajax-rec2').ajaxForm({
		dataType:'json',
		beforeSend:function()
		{
			$('.ajax-rec2').animate({'opacity':'0.6'});
			$('.ajax-rec2').find('input[type=submit]').attr('disabled','true');
		},
		success: function(data)
		{
			$('.ajax-rec2').animate({'opacity':'1'});
			$('.ajax-rec2').find('input[type=submit]').removeAttr('disabled');
			$('.erro-box').remove();
		    $('.box-sucesso').remove();
			if(data.sucesso)
			{
			    $('html, header').animate({ scrollTop: $('.ajax-rec2').offset().top }, 'slow');
				$('.ajax-rec2').prepend('<div class="box-sucesso"><i class="fa fa-check"></i> '+data.mensagem+'</div>');
				$('.erro-box').remove();				
				
			}
			else
			{
				$('html, header').animate({ scrollTop: $('.ajax-rec2').offset().top }, 'slow');
				$('.ajax-rec2').prepend('<div class="erro-box"><i class="fa fa-times"></i> '+data.mensagem+'</div>');
				$('.box-sucesso').remove();
								
			}
			
		}
	})


	$('.ajax-bm3').ajaxForm({
		dataType:'json',
		beforeSend:function()
		{
			$('.ajax-bm3').animate({'opacity':'0.6'});
			$('.ajax-bm3').find('input[type=submit]').attr('disabled','true');
		},
		success: function(data)
		{
			$('.ajax-bm3').animate({'opacity':'1'});
			$('.ajax-bm3').find('input[type=submit]').removeAttr('disabled');
			$('.erro-box').remove();
		    $('.box-sucesso').remove();
			if(data.sucesso)
			{
				$('html, header').animate({ scrollTop: $('.ajax-bm3').offset().top }, 'slow');
				$('.ajax-bm3').prepend('<div class="box-sucesso"><i class="fa fa-check"></i> '+data.mensagem+'</div>');
				$('.erro-box').remove();								
			}
			else
			{
				$('html, header').animate({ scrollTop: $('.ajax-bm3').offset().top }, 'slow');
				$('.ajax-bm3').prepend('<div class="erro-box"><i class="fa fa-times"></i> '+data.mensagem+'</div>');
				$('.box-sucesso').remove();
								
			}
			
		}
	})

	$('.ajax-rec3').ajaxForm({
		dataType:'json',
		beforeSend:function()
		{
			$('.ajax-rec3').animate({'opacity':'0.6'});
			$('.ajax-rec3').find('input[type=submit]').attr('disabled','true');
		},
		success: function(data)
		{
			$('.ajax-rec3').animate({'opacity':'1'});
			$('.ajax-rec3').find('input[type=submit]').removeAttr('disabled');
			$('.erro-box').remove();
		    $('.box-sucesso').remove();
			if(data.sucesso)
			{
			    $('html, header').animate({ scrollTop: $('.ajax-rec3').offset().top }, 'slow');
				$('.ajax-rec3').prepend('<div class="box-sucesso"><i class="fa fa-check"></i> '+data.mensagem+'</div>');
				$('.erro-box').remove();				
				
			}
			else
			{
				$('html, header').animate({ scrollTop: $('.ajax-rec3').offset().top }, 'slow');
				$('.ajax-rec3').prepend('<div class="erro-box"><i class="fa fa-times"></i> '+data.mensagem+'</div>');
				$('.box-sucesso').remove();
								
			}
			
		}
	})


	$('.ajax-bm4').ajaxForm({
		dataType:'json',
		beforeSend:function()
		{
			$('.ajax-bm4').animate({'opacity':'0.6'});
			$('.ajax-bm4').find('input[type=submit]').attr('disabled','true');
		},
		success: function(data)
		{
			$('.ajax-bm4').animate({'opacity':'1'});
			$('.ajax-bm4').find('input[type=submit]').removeAttr('disabled');
			$('.erro-box').remove();
		    $('.box-sucesso').remove();
			if(data.sucesso)
			{
			    $('html, header').animate({ scrollTop: $('.ajax-bm4').offset().top }, 'slow');
				$('.ajax-bm4').prepend('<div class="box-sucesso"><i class="fa fa-check"></i> '+data.mensagem+'</div>');
				$('.erro-box').remove();
							
			}
			else{

				$('html, header').animate({ scrollTop: $('.ajax-bm4').offset().top }, 'slow');
				$('.ajax-bm4').prepend('<div class="erro-box"><i class="fa fa-times"></i> '+data.mensagem+'</div>');
				$('.box-sucesso').remove();
								
			}
			
		}
	})

	$('.ajax-rec4').ajaxForm({
		dataType:'json',
		beforeSend:function()
		{
			$('.ajax-rec4').animate({'opacity':'0.6'});
			$('.ajax-rec4').find('input[type=submit]').attr('disabled','true');
		},
		success: function(data)
		{
			$('.ajax-rec4').animate({'opacity':'1'});
			$('.ajax-rec4').find('input[type=submit]').removeAttr('disabled');
			$('.erro-box').remove();
		    $('.box-sucesso').remove();
			if(data.sucesso)
			{
			    $('html, header').animate({ scrollTop: $('.ajax-rec4').offset().top }, 'slow');
				$('.ajax-rec4').prepend('<div class="box-sucesso"><i class="fa fa-check"></i> '+data.mensagem+'</div>');
				$('.erro-box').remove();				
				
			}
			else
			{
				$('html, header').animate({ scrollTop: $('.ajax-rec4').offset().top }, 'slow');
				$('.ajax-rec4').prepend('<div class="erro-box"><i class="fa fa-times"></i> '+data.mensagem+'</div>');
				$('.box-sucesso').remove();
								
			}
			
		}
	})

	$('.msn-confima').ajaxForm({
		dataType:'json',
		beforeSend:function(data)
		{
			$('.erro-box').remove();
			$('.box-sucesso').remove();
			$('.materias').remove();
			$('.modal-body').prepend('<div class="materias"></div>');
			$('#exampleModal').modal('show');
		},
		success: function(data)
		{		
			if(data.sucesso)
			{			
				$('.modal-body').prepend('<div class="box-sucesso"><i class="fa fa-check"></i> '+data.mensagem+'</div>');
				$('.materias').prepend(data.msn);
				// $('html, header').animate({ scrollTop: $('.ajax_modal').offset().top }, 'slow');
			}
			else
			{
				$('.modal-body').prepend('<div class="erro-box"><i class="fa fa-times"></i> '+data.mensagem+'</div>');
				$('.materias').prepend(data.msn);
				
			}
		}
	})


	$('.ajax-frequencia').ajaxForm({
		dataType:'json',
		beforeSend:function()
		{
			$('.ajax-frequencia').animate({'opacity':'0.6'});
			$('.ajax-frequencia').find('input[type=submit]').attr('disabled','true');
		},
		success: function(data)
		{
			$('.ajax-frequencia').animate({'opacity':'1'});
			$('.ajax-frequencia').find('input[type=submit]').removeAttr('disabled');
			$('.erro-box').remove();
		    $('.box-sucesso').remove();
			if(data.sucesso)
			{
			    $('html, header').animate({ scrollTop: $('.ajax-frequencia').offset().top }, 'slow');
				$('.ajax-frequencia').prepend('<div class="box-sucesso"><i class="fa fa-check"></i> '+data.mensagem+'</div>');
				$('.erro-box').remove();
							
			}
			else{

				$('html, header').animate({ scrollTop: $('.ajax-frequencia').offset().top }, 'slow');
				$('.ajax-frequencia').prepend('<div class="erro-box"><i class="fa fa-times"></i> '+data.mensagem+'</div>');
				$('.box-sucesso').remove();
								
			}
			
		}
	})



	$("select").change(function () {
		sendRequest();
	});

	 $('input[type=radio]').change(function () {
		sendRequest1();
	});

	
	$(":input[class=input-busca]").bind('keyup change input', function () {
		sendRequest3();
	});

	function sendRequest3(){

		$('.msn-confima').ajaxSubmit({
            success:function(data)
            {
                $('.top-letest-product-section').html(data);
                console.log("olaa mundo");
			}
		})
	}

	function sendRequest(){

		$('.form').ajaxSubmit({
            
            //data:{:$('.from').val()},
			success:function(data)
			{		
		 	  $('.content-turmas').html(data);
			}
		})
	}

	function sendRequest1(){

		$('.formch').ajaxSubmit({
         
			success:function(data)
			{		
		 	  $('.materia').html(data);
			}
		})
	}

	





})
