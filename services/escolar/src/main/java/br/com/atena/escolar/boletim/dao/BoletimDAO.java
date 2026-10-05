package br.com.atena.escolar.boletim.dao;

import br.com.atena.escolar.aula.dao.IAulaDAO;
import br.com.atena.escolar.boletim.dominio.DadosBoletim;
import br.com.atena.escolar.boletim.dominio.DadosDisciplinaBoletim;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.disciplina.dao.IDisciplinaDAO;
import br.com.atena.escolar.materia.dao.IMateriaDAO;
import br.com.atena.escolar.matricula.dao.IMatriculaDAO;
import br.com.atena.escolar.nota.dao.INotaDAO;
import br.com.atena.escolar.pessoa.dao.IPessoaDAO;
import br.com.atena.escolar.presenca.dao.IPresencaDAO;
import br.com.atena.escolar.turma.dao.ITurmaDAO;
import br.com.atena.escolar.unidade.dao.IUnidadeDAO;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BoletimDAO implements IBoletimDAO {
    private final IPessoaDAO pessoas;
    private final IMatriculaDAO matriculas;
    private final IDisciplinaDAO disciplinas;
    private final ITurmaDAO turmas;
    private final IMateriaDAO materias;
    private final INotaDAO notas;
    private final IAulaDAO aulas;
    private final IPresencaDAO presencas;
    private final IUnidadeDAO unidades;

    public BoletimDAO(
            IPessoaDAO pessoas,
            IMatriculaDAO matriculas,
            IDisciplinaDAO disciplinas,
            ITurmaDAO turmas,
            IMateriaDAO materias,
            INotaDAO notas,
            IAulaDAO aulas,
            IPresencaDAO presencas,
            IUnidadeDAO unidades) {
        this.pessoas = pessoas;
        this.matriculas = matriculas;
        this.disciplinas = disciplinas;
        this.turmas = turmas;
        this.materias = materias;
        this.notas = notas;
        this.aulas = aulas;
        this.presencas = presencas;
        this.unidades = unidades;
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public DadosBoletim carregar(Long alunoId) {
        var aluno =
                pessoas.buscar(alunoId)
                        .filter(pessoa -> "ALUNO".equals(pessoa.perfil()))
                        .orElseThrow(() -> ErroAplicacao.ausente("Aluno não encontrado"));
        List<DadosDisciplinaBoletim> registros = new ArrayList<>();
        for (var matricula : matriculas.doAluno(alunoId)) {
            var turma = turmas.buscar(matricula.turmaId()).orElseThrow();
            for (var disciplina : disciplinas.daTurma(matricula.turmaId())) {
                int faltas =
                        aulas.daDisciplina(disciplina.id()).stream()
                                .flatMap(aula -> presencas.daAula(aula.id()).stream())
                                .filter(presenca -> presenca.alunoId().equals(alunoId))
                                .mapToInt(presenca -> presenca.faltas())
                                .sum();
                registros.add(
                        new DadosDisciplinaBoletim(
                                turma.nome(),
                                turma.ano(),
                                materias.buscar(disciplina.materiaId()).orElseThrow().nome(),
                                disciplina.id(),
                                disciplina.cargaHoraria(),
                                matricula.dependencia(),
                                disciplina.professorId(),
                                notas.doAlunoNaDisciplina(disciplina.id(), alunoId),
                                faltas));
            }
        }
        var unidade = unidades.listar().stream().findFirst().orElse(null);
        return new DadosBoletim(aluno, unidade, registros);
    }
}
