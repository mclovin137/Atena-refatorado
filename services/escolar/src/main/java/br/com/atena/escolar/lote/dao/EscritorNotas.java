package br.com.atena.escolar.lote.dao;

import br.com.atena.escolar.lote.actor.PrepararNotasMatriculaActor;
import br.com.atena.escolar.matricula.dominio.Matricula;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class EscritorNotas implements ItemWriter<Matricula> {
    private final PrepararNotasMatriculaActor preparar;

    public EscritorNotas(PrepararNotasMatriculaActor preparar) {
        this.preparar = preparar;
    }

    @Override
    public void write(Chunk<? extends Matricula> matriculas) {
        for (var matricula : matriculas) {
            preparar.executar(matricula);
        }
    }
}
