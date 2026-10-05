package br.com.atena.relatorios.snapshot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/** Contrato de integração: histórico e listas possuem campos distintos opcionais. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record LinhaSnapshotDTO(
        Long id,
        String nome,
        String matricula,
        String turma,
        Integer ano,
        String disciplina,
        Long disciplinaId,
        Integer cargaHoraria,
        Boolean dependencia,
        List<Double> notas,
        List<Double> recuperacoes,
        Integer faltas,
        ResultadoSnapshotDTO resultado) {}
