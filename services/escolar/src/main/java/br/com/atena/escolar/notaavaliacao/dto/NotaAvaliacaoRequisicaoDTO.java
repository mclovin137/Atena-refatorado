package br.com.atena.escolar.notaavaliacao.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/** Contrato de entrada explícito, independente da entidade JPA. */
public class NotaAvaliacaoRequisicaoDTO {
    public Long id;

    @NotNull public Long avaliacaoId;

    @NotNull public Long alunoId;

    @NotNull
    @DecimalMin("0")
    @DecimalMax("100")
    public Double valor;
}
