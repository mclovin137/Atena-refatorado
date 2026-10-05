package br.com.atena.escolar.matricula.actor;

import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.disciplina.dao.IDisciplinaDAO;
import br.com.atena.escolar.matricula.dao.IMatriculaDAO;
import br.com.atena.escolar.pessoa.dao.IPessoaDAO;

import org.springframework.stereotype.Component;

@Component
public class ValidarVinculoEnsinoActor {
    private final IPessoaDAO pessoas;
    private final IDisciplinaDAO disciplinas;
    private final IMatriculaDAO matriculas;

    public ValidarVinculoEnsinoActor(
            IPessoaDAO pessoas, IDisciplinaDAO disciplinas, IMatriculaDAO matriculas) {
        this.pessoas = pessoas;
        this.disciplinas = disciplinas;
        this.matriculas = matriculas;
    }

    public void exigirPerfil(Long pessoaId, String perfil) {
        var pessoa =
                pessoas.buscar(pessoaId)
                        .orElseThrow(() -> ErroAplicacao.entrada("Pessoa não encontrada"));
        if (!pessoa.ativo() || !perfil.equals(pessoa.perfil())) {
            throw ErroAplicacao.entrada("Perfil incompatível ou pessoa inativa");
        }
    }

    public void exigirMatricula(Long disciplinaId, Long alunoId) {
        exigirPerfil(alunoId, "ALUNO");
        var disciplina =
                disciplinas
                        .buscar(disciplinaId)
                        .orElseThrow(() -> ErroAplicacao.entrada("Disciplina não encontrada"));
        if (!matriculas.alunoNaTurma(disciplina.turmaId(), alunoId)) {
            throw ErroAplicacao.entrada("Aluno não matriculado na turma");
        }
    }
}
