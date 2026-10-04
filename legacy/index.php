<?php 
include('config.php');

$Home = new Controller\HomeController();
$main = new Controller\MainController();
$CadastroAluno = new Controller\CadastroAlunoController();
$VerAluno = new Controller\VerAlunoController();
$VerProfesso = new Controller\VerProfessorController();
$CadastroProfesso = new Controller\CadastroProfessoController();
$cadastrocurso = new Controller\CadastroCursoController();
$cadastroDiciplina = new Controller\CadastroDiciplinaController();
$EditarAluno = new Controller\EditarAlunoController();
$EditarProfessor = new Controller\EditarProfessorController();
$cadastromateria = new Controller\CadastroMateriaController();
$cadastrarTurmas = new Controller\CadastroTurmasController();
$boletim = new Controller\BoletimController();
$lacamentoNotas = new Controller\LancaNotasController();
$chat = new Controller\ChatController();
$chatbox = new Controller\ChatBoxController();
$AtualizaDados = new Controller\AtualizaDadosController();
$calendario = new Controller\CalendarioController();
$addarquivo = new Controller\AdicionarArquivoController();
$diario = new Controller\DiarioController();
$diariobox = new Controller\DiarioBoxController();
$panejaAula = new Controller\PanejaAulaController();
$panejaAulaBox = new Controller\PanejaAulaBoxController();
$avaliacoes = new Controller\AvaliacoesController();
$aulas = new Controller\AulascoesController();
$cadastroserie = new Controller\CadastroSerieController();
$horario = new Controller\CadastroHorarioController();
$listahorario = new Controller\ListaHorarioController();
$gerirturmas = new Controller\GestaoDeTurmasController();
$gerirturmasbox = new Controller\GestaoDeTurmasBoxController();
$gradecurricular = new Controller\GradeCurricularController();
$historico = new Controller\HistoricoController();
$trocaProfessor = new Controller\TrocaDeProfessorController();
$dp = new Controller\CadastroDependeciaController();
$cgc = new Controller\CriaGradeCurricularController();
$egc = new Controller\EditarCurricularController();
$CadastroSectario = new Controller\CadastroSectarioController();
$ue = new Controller\UnidadeEscolarController();
$editarSectario = new Controller\EditarSectarioController();
$editar = new Controller\EditarController();
// erro 404 //

if(isset($_GET['url']))
{
	$url = explode('/',$_GET['url']);
	if($url[0] != 'main')	
	{ 
		\Models\HomeMolde::redirect(INCLUDE_PATH);
			if($url[0] == 'main') 
			{
				if(isset($url[1]))
				{
					
						if(file_exists('Views/pages/'.$url[1].'.php'))
						{	
						   \Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
						}

						else
						{
							
							\Models\HomeMolde::redirect(INCLUDE_PATH);
						}
				}
				else
				{
					
				   		if(@$_SESSION['login_Aluno'] != true && @$_SESSION['login_Professo'] != true && @$_SESSION['login_secretaria'] != true)
					    {
			               \Models\HomeMolde::redirect(INCLUDE_PATH);
					    }
					    else
					    {
                           \Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);
					    }
					    
				}
			}
		
	}
	elseif(isset($url[1]))
	{ 
		
		if(!file_exists('Views/pages/'.$url[1].'.php'))
		{			  
		  
             \Models\HomeMolde::redirect(INCLUDE_PATH_MAIN_HOME);          
		} 
	  
		
	}
	
} 

// erro 404 fim //
	

Router::get('/',function() use ($Home){
	$Home->index();
});

Router::get('/main/Home',function() use ($main){
	$main->index();
});

Router::get('/main/Home/',function() use ($main){
	$main->index();
});

Router::get('/main/home',function() use ($main){
	$main->index();
});

Router::get('/main/home/',function() use ($main){
	$main->index();
});

Router::get('/main/',function() use ($main){
	$main->index();
});

Router::get('/main/Home/?',function() use ($main){
	$main->index();
});

Router::get('/main/cadastrar-alunos',function() use ($CadastroAluno){
	$CadastroAluno->index();
});

Router::get('/main/cadastrar-professores',function() use ($CadastroProfesso){
	$CadastroProfesso->index();
});

Router::get('/main/cadastrar-sectario',function() use ($CadastroSectario){
	$CadastroSectario->index();
});


Router::get('/main/listagem-alunos',function() use ($VerAluno){
	$VerAluno->index();
});

Router::get('/main/listagem-professores',function() use ($VerProfesso){
	$VerProfesso->index();
});

Router::get('/main/cadastrar-curso',function() use ($cadastrocurso){
	$cadastrocurso->index();
});

Router::get('/main/cadastrar-materia',function() use ($cadastromateria){
	$cadastromateria->index();
});

Router::get('/main/cadastrar-serie',function() use ($cadastroserie){
	$cadastroserie->index();
});

Router::get('/main/cadastrar-diciplina',function() use ($cadastroDiciplina){
	$cadastroDiciplina->index();
});

Router::get('/main/cadastrar-turmas',function() use ($cadastrarTurmas){
	$cadastrarTurmas->index();
});


Router::get('/main/boletim',function() use ($boletim){
	$boletim->index();
});

Router::get('/main/editar-aluno',function() use ($EditarAluno){
	$EditarAluno->index();
});

Router::get('/main/editar-professor',function() use ($EditarProfessor){
	$EditarProfessor->index();
});

Router::get('/main/lancamento-notas',function() use ($lacamentoNotas){
	$lacamentoNotas->index();
});

Router::get('/main/chat',function() use ($chat){
	$chat->index();
});

Router::get('/main/chat-box',function() use ($chatbox){
	$chatbox->index();
});

Router::get('/main/atualiza-dados',function() use ($AtualizaDados){
	$AtualizaDados->index();
});

Router::get('/main/calendario',function() use ($calendario){
	$calendario->index();
});

Router::get('/main/cadastrar-arquivo',function() use ($addarquivo){
	$addarquivo->index();
});

Router::get('/main/diario',function() use ($diario){
	$diario->index();
});

Router::get('/main/diario-box',function() use ($diariobox){
	$diariobox->index();
});

Router::get('/main/planeja-aula',function() use ($panejaAula){
	$panejaAula->index();
});

Router::get('/main/planeja-aula-box',function() use ($panejaAulaBox){
	$panejaAulaBox->index();
});

Router::get('/main/troca-professor',function() use ($trocaProfessor){
	$trocaProfessor->index();
});


Router::get('/main/avaliacoes',function() use ($avaliacoes){
	$avaliacoes->index();
});

Router::get('/main/aulas/?',function() use ($aulas){
	$aulas->index();
});

Router::get('/main/aulas',function() use ($main){
	$main->index();
});

Router::get('/main/cadastrar-horario',function() use ($horario){
	$horario->index();
});

Router::get('/main/listagem-horario',function() use ($listahorario){
	$listahorario->index();
});

Router::get('/main/gerir-turmas',function() use ($gerirturmas){
	$gerirturmas->index();
});

Router::get('/main/gestaotumas-box',function() use ($gerirturmasbox){
	$gerirturmasbox->index();
});

Router::get('/main/grade-curricular',function() use ($gradecurricular){
	$gradecurricular->index();
});

Router::get('/main/historico',function() use ($historico){
	$historico->index();
});

Router::get('/main/cadastrar-dependecia',function() use ($dp){
	$dp->index();
});

Router::get('/main/cadastrar-grade-curricular',function() use ($cgc){
	$cgc->index();
});

Router::get('/main/editar-grade-curricular',function() use ($egc){
	$egc->index();
});

Router::get('/main/unidade_escolar',function() use ($ue){
	$ue->index();
});

Router::get('/main/listagem-sectarios',function() use ($editarSectario){
	$editarSectario->index();
});

Router::get('/main/editar-sectarios',function() use ($editar){
	$editar->index();
});





?>
