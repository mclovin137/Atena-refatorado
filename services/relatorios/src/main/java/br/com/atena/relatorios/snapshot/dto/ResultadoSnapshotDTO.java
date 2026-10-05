package br.com.atena.relatorios.snapshot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResultadoSnapshotDTO(
        Double media, Double parcial, Double frequencia, String situacao) {}
