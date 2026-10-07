package br.com.atena.escolar.importacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record VincularPessoaRequisicaoDTO(
        @NotBlank @Size(max = 150) String identificadorLegado, @NotNull @Positive Long pessoaId) {}
