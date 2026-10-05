package br.com.atena.relatorios.snapshot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SnapshotDTO(
        Long alunoId,
        String nome,
        String matricula,
        String documento,
        UnidadeSnapshotDTO unidade,
        List<LinhaSnapshotDTO> linhas) {}
