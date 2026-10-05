package br.com.atena.escolar.boletim.dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record DisciplinaPreparada(
        String turma,
        int ano,
        String disciplina,
        Long disciplinaId,
        int cargaHoraria,
        boolean dependencia,
        List<Double> notas,
        List<Double> recuperacoes,
        int faltas,
        ResultadoEscolar resultado) {
    public DisciplinaPreparada {
        notas = Collections.unmodifiableList(new ArrayList<>(notas));
        recuperacoes = Collections.unmodifiableList(new ArrayList<>(recuperacoes));
    }
}
