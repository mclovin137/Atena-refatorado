package br.com.atena.escolar.lote.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record InicializarNotasRequisicaoDTO(
        @NotNull Long turmaId, @NotBlank @Size(max = 100) String requestId) {}
