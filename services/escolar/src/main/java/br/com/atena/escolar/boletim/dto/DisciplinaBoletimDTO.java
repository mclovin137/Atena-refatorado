package br.com.atena.escolar.boletim.dto;

import java.util.List;

public record DisciplinaBoletimDTO(
        String turma,
        int ano,
        String disciplina,
        Long disciplinaId,
        int cargaHoraria,
        boolean dependencia,
        List<Double> notas,
        List<Double> recuperacoes,
        int faltas,
        ResultadoBoletimDTO resultado) {}
