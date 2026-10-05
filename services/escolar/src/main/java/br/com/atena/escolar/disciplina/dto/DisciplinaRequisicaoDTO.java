package br.com.atena.escolar.disciplina.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** Contrato de entrada explícito, independente da entidade JPA. */
public class DisciplinaRequisicaoDTO {
    public Long id;

    @NotNull public Long turmaId;

    @NotNull public Long materiaId;

    public Long professorId;

    @Min(1)
    @Max(2000)
    public int cargaHoraria;
}
