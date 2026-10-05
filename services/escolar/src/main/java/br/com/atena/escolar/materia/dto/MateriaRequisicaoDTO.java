package br.com.atena.escolar.materia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Contrato de entrada explícito, independente da entidade JPA. */
public class MateriaRequisicaoDTO {
    public Long id;

    @NotBlank
    @Size(max = 150)
    public String nome;
}
