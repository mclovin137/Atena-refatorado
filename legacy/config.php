<?php
	session_start();
	date_default_timezone_set('America/Sao_Paulo');
	include('Models/Router.php');
	include('Models/MySql.php');
	include('Models/MainMolde.php');
	include('Models/HomeMolde.php');
	include('Views/mainviw.php');

	


	$autoload = function($class)
	{				
		   include($class.'.php');		  
	};
	
	spl_autoload_register($autoload);

    // Diretorios
    define('BASE_DIR',__DIR__.'/');
	define('INCLUDE_PATH','http://localhost/Sistema Escolar/');
    //define('INCLUDE_PATH','//10.10.202.18/Sistema Escolar/');
	//define('INCLUDE_PATH','//192.168.1.13/Sistema Escolar/');
	define('INCLUDE_PATH_MAIN',INCLUDE_PATH.'main/');
	define('INCLUDE_PATH_MAIN_HOME',INCLUDE_PATH_MAIN.'Home/');
	define('INCLUDE_PATH_GOOGLE','https://www.google.com/');
	
	//Conectar com banco de dados!
	define('HOST','localhost');
	define('USER','root');
	define('PASSWORD','');
	define('DATABASE','SistemaEscolar');

	ob_end_flush();

	
	
?>