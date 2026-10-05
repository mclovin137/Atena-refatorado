package br.com.atena.escolar.curriculo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Contrato de entrada explícito, independente da entidade JPA. */
public class CurriculoRequisicaoDTO {
    public Long id;

    @NotBlank
    @Size(max = 150)
    public String nome;

    @NotNull public Long materiaId;

    @Min(1)
    @Max(2000)
    public int cargaHoraria;
}
