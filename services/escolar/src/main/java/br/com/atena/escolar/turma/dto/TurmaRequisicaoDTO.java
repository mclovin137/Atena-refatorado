package br.com.atena.escolar.turma.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Contrato de entrada explícito, independente da entidade JPA. */
public class TurmaRequisicaoDTO {
    public Long id;

    @NotNull public Long cursoId;

    @NotNull public Long serieId;

    @Min(1900)
    @Max(2200)
    public int ano;

    @NotBlank
    @Size(max = 150)
    public String nome;
}
