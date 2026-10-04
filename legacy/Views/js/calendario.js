$(function(){


	$('.day').click(function(){
		$('td').removeClass('day-selected');	
		$('td').removeClass('day-tarefa-pendente-selected');	
		$('td').removeClass('day-tarefa-concluida-selected');		
		$(this).addClass('day-selected');
	    $('.day-tarefa-pendente-selected').remove('-selected');	
	    $('.day-tarefa-concluida-selected').remove('-selected');	
		var novoDia = $(this).attr('dia').split('-');
		// now =  new Date;
		// var mesAtual = now.getMonth();
		// var dia = now.getDay();	
        var novoDia = novoDia[2]+ '/' +novoDia[1]+'/' + novoDia[0];
		trocarDatas($(this).attr('dia'),novoDia);
		
	})

	$('.day-tarefa-pendente').click(function(){
		$('td').removeClass('day-tarefa-pendente-selected');
		$('td').removeClass('day-tarefa-concluida-selected');		
		$('td').removeClass('day-selected');	
		$(this).addClass('day-tarefa-pendente-selected');
		var novoDia = $(this).attr('dia').split('-');
		// now =  new Date;
		// var mesAtual = now.getMonth();
		// var dia = now.getDay();	
        var novoDia = novoDia[2]+ '/' +novoDia[1]+'/' + novoDia[0];
		trocarDatas($(this).attr('dia'),novoDia);
		
	})

	$('.day-tarefa-concluida').click(function(){
		$('td').removeClass('day-tarefa-pendente-selected');
		$('td').removeClass('day-tarefa-concluida-selected');		
		$('td').removeClass('day-selected');	
		$(this).addClass('day-tarefa-concluida-selected');
		var novoDia = $(this).attr('dia').split('-');
		// now =  new Date;
		// var mesAtual = now.getMonth();
		// var dia = now.getDay();	
        var novoDia = novoDia[2]+ '/' +novoDia[1]+'/' + novoDia[0];
		trocarDatas($(this).attr('dia'),novoDia);
		
	})


	$('.add-cale').ajaxForm({
		dataType:'json',
		success:function(data){
			$('.box-alert').remove();
			$('.card-data').after('<div class="box-alert sucesso">Evento foi adicionado com sucesso!</div>');
			$('.box-tarefas ').after('<div class="box-tarefas-single"><h2><i class="fa fa-pencil"></i> '+data.tarefa+'</h2></div>');
			$('.add-cale')[0].reset();
		}
	})


	function trocarDatas(nformatado,formatado){
		$('input[name=data]').attr('value',nformatado);
		$('.card-data').html('Adicionar Tarefa para: '+ formatado);
		$('.card-link').html('<a href="planeja-aula-box?dia='+formatado+'"> Planeja</a>')
	}

	function aplicarEventos(data){
		$.ajax({
			url: include_path+'Models/ajax/Calendario.php',
			method:'post',
			data:{'data':data,'acao':'puxar'}
		}).done(function(data){
		})
	}

})