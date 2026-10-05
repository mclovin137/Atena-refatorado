package br.com.atena.escolar.lote.actor;

import br.com.atena.escolar.disciplina.dao.IDisciplinaDAO;
import br.com.atena.escolar.matricula.dominio.Matricula;
import br.com.atena.escolar.nota.dao.INotaDAO;
import br.com.atena.escolar.nota.dominio.Nota;

import org.springframework.stereotype.Component;

@Component
public class PrepararNotasMatriculaActor {
    private final IDisciplinaDAO disciplinas;
    private final INotaDAO notas;

    public PrepararNotasMatriculaActor(IDisciplinaDAO disciplinas, INotaDAO notas) {
        this.disciplinas = disciplinas;
        this.notas = notas;
    }

    public void executar(Matricula matricula) {
        for (var disciplina : disciplinas.daTurma(matricula.turmaId())) {
            for (int bimestre = 1; bimestre <= 4; bimestre++) {
                notas.salvarAusente(
                        new Nota(
                                null,
                                disciplina.id(),
                                matricula.alunoId(),
                                bimestre,
                                null,
                                null,
                                null,
                                false));
            }
        }
    }
}
