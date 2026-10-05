package br.com.atena.escolar.serie.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Contrato de entrada explícito, independente da entidade JPA. */
public class SerieRequisicaoDTO {
    public Long id;

    @NotBlank
    @Size(max = 150)
    public String nome;
}
