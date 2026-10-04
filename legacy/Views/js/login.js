
(function ($) {
    "use strict";

    /*==================================================================
    [ erro ]*/

     $('.erro-box').fadeOut(2500,function(){
        
             $('.btn-show-pass').css('top','62.4%');
             $('.fa-home').css('padding','4px');
             $('.fa-sign-out').css('padding','4px');
             $('.fa-bars').css('padding','4px');
     });
  
     
     $('.box-sucesso').fadeOut(3000,function(){

             $('.fa-home').css('padding','4px');
             $('.fa-sign-out').css('padding','4px');
             $('.fa-bars').css('padding','4px');
        
    });

    

    /*==================================================================*/
    $('.input100').each(function(){
        $(this).on('blur', function(){
            if($(this).val().trim() != "") {
                $(this).addClass('has-val');
            }
            else {
                $(this).removeClass('has-val');
            }
        })    
    })
  
  
   
    /*==================================================================
    [ Show pass ]*/
    var showPass = 0;
    $('.btn-show-pass').on('click', function(){
        if(showPass == 0) {
            $(this).next('input').attr('type','text');
            $(this).find('i').removeClass('zmdi-eye');
            $(this).find('i').addClass('zmdi-eye-off');
            showPass = 1;
        }
        else {
            $(this).next('input').attr('type','password');
            $(this).find('i').addClass('zmdi-eye');
            $(this).find('i').removeClass('zmdi-eye-off');
            showPass = 0;
        }
        
    });
    

    $(".pesquisa-form").click(function(){
        $(".pesquisa-form-icone").css('display','none');
    });

    $('#ano').on('change', function() {
        var text = $(this).val();
       
        
     });

     $('#curso').on('change', function() {
        var text = $(this).val();
       
     });

     $('#serie').on('change', function() {
        var text = $(this).val();
      
     });

   


})(jQuery);