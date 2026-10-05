package br.com.atena.escolar.matricula.dto;

import jakarta.validation.constraints.NotNull;

/** Contrato de entrada explícito, independente da entidade JPA. */
public class MatriculaRequisicaoDTO {
    public Long id;

    @NotNull public Long turmaId;

    @NotNull public Long alunoId;

    public boolean dependencia;
}
