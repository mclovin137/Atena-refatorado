package br.com.atena.escolar.avaliacao.dto;

import br.com.atena.escolar.avaliacao.dominio.ModoConsolidacaoEnum;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ConsolidarAvaliacoesRequisicaoDTO(
        @Min(1) @Max(4) int bimestre, @NotNull ModoConsolidacaoEnum modo) {}
