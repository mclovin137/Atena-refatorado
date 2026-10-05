package br.com.atena.escolar.presenca.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** Contrato de entrada explícito, independente da entidade JPA. */
public class PresencaRequisicaoDTO {
    public Long id;

    @NotNull public Long aulaId;

    @NotNull public Long alunoId;

    @Min(0)
    @Max(12)
    public int faltas;
}
