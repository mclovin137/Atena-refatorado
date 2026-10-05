package br.com.atena.escolar.sessao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SenhaRequisicaoDTO(
        @NotBlank String atual, @NotBlank @Size(min = 12, max = 100) String nova) {}
