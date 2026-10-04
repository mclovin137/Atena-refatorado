<?php
	namespace Views;


	class mainview
	{   
		public static function render($fileName, $arr = [],$header = 'header.php',$footer = 'footer.php')
		{
			include('Views/includes/'.$header);
			include('Views/pages/'.$fileName);
			include('Views/includes/'.$footer);		
			die();
		}
			
	}
	
?>