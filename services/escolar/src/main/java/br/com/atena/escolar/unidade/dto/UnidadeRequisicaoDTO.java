package br.com.atena.escolar.unidade.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Contrato de entrada explícito, independente da entidade JPA. */
public class UnidadeRequisicaoDTO {
    public Long id;

    @NotBlank
    @Size(max = 150)
    public String nome;

    @Size(max = 500)
    public String endereco;

    @Size(max = 30)
    public String telefone;

    @Size(max = 18)
    public String cnpj;
}
