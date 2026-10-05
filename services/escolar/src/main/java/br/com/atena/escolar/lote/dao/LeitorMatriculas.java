package br.com.atena.escolar.lote.dao;

import br.com.atena.escolar.matricula.dao.IMatriculaDAO;
import br.com.atena.escolar.matricula.dominio.Matricula;

import org.springframework.batch.item.ItemReader;

import java.util.Collections;
import java.util.Iterator;

/** Paginação determinística; reinício pode reler porque a escrita é idempotente. */
public class LeitorMatriculas implements ItemReader<Matricula> {
    private final IMatriculaDAO matriculas;
    private final Long turmaId;
    private int pagina;
    private Iterator<Matricula> paginaAtual = Collections.emptyIterator();
    private boolean concluido;

    public LeitorMatriculas(IMatriculaDAO matriculas, Long turmaId) {
        this.matriculas = matriculas;
        this.turmaId = turmaId;
    }

    @Override
    public Matricula read() {
        if (concluido) {
            return null;
        }
        if (!paginaAtual.hasNext()) {
            var registros = matriculas.paginaDaTurma(turmaId, pagina++, 50);
            if (registros.isEmpty()) {
                concluido = true;
                return null;
            }
            paginaAtual = registros.iterator();
        }
        return paginaAtual.next();
    }
}
