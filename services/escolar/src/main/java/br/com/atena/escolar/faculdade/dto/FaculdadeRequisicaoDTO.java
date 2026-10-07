package br.com.atena.escolar.faculdade.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class FaculdadeRequisicaoDTO {
    public Long id;

    @NotBlank
    @Size(max = 150)
    public String nome;
}
