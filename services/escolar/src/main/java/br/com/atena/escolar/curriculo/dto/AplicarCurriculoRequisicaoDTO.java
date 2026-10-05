package br.com.atena.escolar.curriculo.dto;

import jakarta.validation.constraints.NotBlank;

public record AplicarCurriculoRequisicaoDTO(@NotBlank String nome) {}
