package br.com.atena.escolar.curso.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/** Contrato de entrada explícito, independente da entidade JPA. */
public class CursoRequisicaoDTO {
    public Long id;

    @NotBlank
    @Size(max = 150)
    public String nome;

    @Positive public Long faculdadeId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Boolean vinculoInstitucionalPendente;

    private boolean faculdadeInformada;

    @JsonSetter("faculdadeId")
    public void definirFaculdade(Long faculdadeId) {
        this.faculdadeId = faculdadeId;
        this.faculdadeInformada = true;
    }

    public boolean temFaculdadeInformada() {
        return faculdadeInformada;
    }
}
