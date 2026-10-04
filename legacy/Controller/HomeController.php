<?php
namespace Controller;
use \Views\mainview;

    
class HomeController
{   
	public function index()
	{
		
	   	if(isset($_POST['acao']))
		{		    
			\Models\HomeMolde::login();
	    }


		 mainview::render('Home.php');

	}
}
?>