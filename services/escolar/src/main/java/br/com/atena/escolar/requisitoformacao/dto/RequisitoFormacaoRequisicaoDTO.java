package br.com.atena.escolar.requisitoformacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class RequisitoFormacaoRequisicaoDTO {
    public Long id;

    @NotBlank
    @Size(max = 150)
    public String nome;

    @NotNull @Positive public Long gradeId;

    @Size(max = 4000)
    public String descricao;
}
