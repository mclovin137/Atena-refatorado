package br.com.atena.escolar.material.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Contrato de entrada explícito, independente da entidade JPA. */
public class MaterialRequisicaoDTO {
    public Long id;

    @NotNull public Long disciplinaId;

    @NotBlank
    @Size(max = 150)
    public String nome;

    public String tipo;

    public Long autorId;
}
