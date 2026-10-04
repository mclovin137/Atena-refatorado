$(function(){

	$('[name=matricula]').mask('999999');
	$('[name=cpf]').mask('999.999.999-99');
    $('[class=cpf]').mask('999.999.999-99');
	$('[name=cep]').mask('99999-999'); 
    $('[class=telefone]').mask('(99) 99999-9999');
    $('[name=telefone]').mask('(99) 99999-9999');
    $('[name=telefone_responsavel]').mask('(99) 99999-9999');
	$('[class=nota]').mask('999');
    $('[class=mudar-nota-input]').mask('999');
    $('[name=duracao]').mask('999');
    $('[name=cnpj]').mask('99.999.999/9999-99');

   
    $('#btn-cadastra-notas').on('click', function () {
        $('.listagem-notas').css('display','none');
        $('.cadastro-notas').css('display','block');
        $('.select-bm1').css('display','block');
        $('.select-bm2').remove();
        $('.select-bm3').remove();
        $('.select-bm4').remove();
       

    })

     $('#btn-cadastra-notas1').on('click', function () {
        $('.listagem-notas').css('display','none');
        $('.cadastro-notas').css('display','block');
        $('.select-bm1').css('display','block');
        $('.select-bm2').remove();
        $('.select-bm3').remove();
        $('.select-bm4').remove();
       

    })


    $('#btn-cadastra-notass').on('click', function () {
        $('.listagem-notas').css('display','none');
        $('.cadastro-notas').css('display','block');
        $('.select-bm2').css('display','block');
        $('.select-bm1').remove();
        $('.select-bm3').remove();
        $('.select-bm4').remove();
     

    })

  
     $('#btn-cadastra-notass2').on('click', function () {
        $('.listagem-notas').css('display','none');
        $('.cadastro-notas').css('display','block');
        $('.select-bm2').css('display','block');
        $('.select-bm3').remove();
        $('.select-bm1').remove();
        $('.select-bm4').remove();
      

    })

      $('#btn-cadastra-notasss').on('click', function () {
        $('.listagem-notas').css('display','none');
        $('.cadastro-notas').css('display','block');
        $('.select-bm3').css('display','block');
        $('.select-bm2').remove();
        $('.select-bm1').remove();
        $('.select-bm4').remove();
      

    })

      $('#btn-cadastra-notasss3').on('click', function () {
        $('.listagem-notas').css('display','none');
        $('.cadastro-notas').css('display','block');
        $('.select-bm3').css('display','block');
        $('.select-bm2').remove();
        $('.select-bm1').remove();
        $('.select-bm4').remove();
      

    })
    


    $('#btn-cadastra-notaas').on('click', function () {
        $('.listagem-notas').css('display','none');
        $('.cadastro-notas').css('display','block');
        $('.select-bm4').css('display','block');
        $('.select-bm2').remove();
        $('.select-bm3').remove();
        $('.select-bm1').remove();
        
    })

     $('#btn-cadastra-notaas4').on('click', function () {
        $('.listagem-notas').css('display','none');
        $('.cadastro-notas').css('display','block');
        $('.select-bm4').css('display','block');
        $('.select-bm2').remove();
        $('.select-bm3').remove();
        $('.select-bm1').remove();
        
    })


     $('#btn-voltar').on('click', function () {
        $('.listagem-notas').css('display','block');
        $('.cadastro-notas').css('display','none');

    })

     $('[actionBtn=enviar]').click(function(){
            var txt;
            var r = confirm("Deseja enviar as notas ? (o diario ira fecha ate segunda orden) ");
            if (r == true) 
            {
                return true;
            } 
            else 
            {
                return false;
            }
    })

     $('[actionBtn=enviar-boletim]').click(function(){
                var txt;
                var r = confirm("Deseja enviar as notas ? (se existir mais de uma avaliaçao, eleas iram ser somadas) ");
                if (r == true) 
                {
                    return true;
                }
                else 
                {
                    return false;
                }
        })

     $('[actionBtn=salvar]').click(function(){
            var txt;
            var r = confirm("Deseja salvar  as notas ? (so salver as notas se todas notas ja foram inseridas) ");
            if (r == true) 
            {
                return true;
            } 
            else
            {
                return false;
            }
    })

   $('[actionBtn=liberar]').click(function(){
            var txt;
            var r = confirm("Deseja liberar as notas ?  (o diario ira ficar aberto ate segunda orden)");
            if (r == true)
            {
                return true;
            }
            else 
            {
                return false;
            }
    })

     $('[actionBtn=exluir-atividade-qualificativa]').click(function(){
            var txt;
            var r = confirm("Deseja Excluir a ativiade ?");
            if (r == true)
            {
                return true;
            }
            else 
            {
                return false;
            }
    })

   
	
	
})