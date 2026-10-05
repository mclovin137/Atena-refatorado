package br.com.atena.escolar.autorizacao.actor;

import br.com.atena.escolar.autorizacao.service.AutorizacaoPerfilFactory;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.disciplina.dao.IDisciplinaDAO;
import br.com.atena.escolar.matricula.dao.IMatriculaDAO;
import br.com.atena.escolar.usuario.dominio.IUsuarioAtual;
import br.com.atena.escolar.usuario.dominio.UsuarioAutenticado;

import org.springframework.stereotype.Component;

@Component
public class AutorizacaoEnsinoActor {

    private final IDisciplinaDAO disciplinas;
    private final IMatriculaDAO matriculas;
    private final IUsuarioAtual usuarioAtual;
    private final AutorizacaoPerfilFactory perfis = new AutorizacaoPerfilFactory();

    public AutorizacaoEnsinoActor(
            IDisciplinaDAO disciplinas, IMatriculaDAO matriculas, IUsuarioAtual usuarioAtual) {
        this.disciplinas = disciplinas;
        this.matriculas = matriculas;
        this.usuarioAtual = usuarioAtual;
    }

    public UsuarioAutenticado usuario() {
        return usuarioAtual.obter();
    }

    public void exigirSecretaria() {
        if (!perfis.selecionar(usuario().perfil()).administrar()) {
            ErroAplicacao.negar();
        }
    }

    public void exigirDisciplina(Long disciplinaId, boolean escrita) {
        var disciplina =
                disciplinas
                        .buscar(disciplinaId)
                        .orElseThrow(() -> ErroAplicacao.ausente("Disciplina não encontrada"));
        var usuario = usuario();
        boolean leciona = usuario.identificador().equals(disciplina.professorId());
        boolean matriculado =
                matriculas.alunoNaTurma(disciplina.turmaId(), usuario.identificador());
        if (!perfis.selecionar(usuario.perfil()).acessarEnsino(leciona, matriculado, escrita)) {
            ErroAplicacao.negar();
        }
    }

    public void exigirAluno(Long alunoId, Long disciplinaId, boolean escrita) {
        exigirDisciplina(disciplinaId, escrita);
        var usuario = usuario();
        if (usuario.aluno() && !usuario.identificador().equals(alunoId)) {
            ErroAplicacao.negar();
        }
        var disciplina = disciplinas.buscar(disciplinaId).orElseThrow();
        if (!matriculas.alunoNaTurma(disciplina.turmaId(), alunoId)) {
            throw ErroAplicacao.entrada("Aluno não matriculado na turma");
        }
    }

    public void exigirTurma(Long turmaId, boolean escrita) {
        var usuario = usuario();
        boolean leciona =
                disciplinas.daTurma(turmaId).stream()
                        .anyMatch(
                                disciplina ->
                                        usuario.identificador().equals(disciplina.professorId()));
        boolean matriculado = matriculas.alunoNaTurma(turmaId, usuario.identificador());
        if (!perfis.selecionar(usuario.perfil()).acessarTurma(leciona, matriculado, escrita)) {
            ErroAplicacao.negar();
        }
    }
}
