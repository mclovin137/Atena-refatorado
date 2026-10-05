$(function(){


	$('td[dia]').click(function(){
		$('td').removeClass('day-selected');
		$(this).addClass('day-selected');
		var novoDia = $(this).attr('dia').split('-');
		var novoDia = novoDia[2]+ '/' + novoDia[1]+'/' + novoDia[0];
		trocarDatas($(this).attr('dia'),novoDia);

		aplicarEventos($(this).attr('dia'));
	})


	
	function trocarDatas(nformatado,formatado){
		$('input[name=data]').attr('value',nformatado);
		$('form .card-title').html('Adicionar Tarefa para: '+ formatado);
		$('.box-tarefas .card-title').html('Adicionar Tarefa para: '+ formatado);
	}

	

})