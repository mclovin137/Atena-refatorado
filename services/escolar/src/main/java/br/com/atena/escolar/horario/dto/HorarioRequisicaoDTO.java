package br.com.atena.escolar.horario.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

/** Contrato de entrada explícito, independente da entidade JPA. */
public class HorarioRequisicaoDTO {
    public Long id;

    @NotNull public Long disciplinaId;

    @Min(1)
    @Max(7)
    public int diaSemana;

    @NotNull public LocalTime inicio;

    @NotNull public LocalTime fim;
}
