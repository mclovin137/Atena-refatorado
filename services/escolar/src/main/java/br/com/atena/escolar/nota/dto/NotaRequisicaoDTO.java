package br.com.atena.escolar.nota.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/** Contrato de entrada explícito, independente da entidade JPA. */
public class NotaRequisicaoDTO {
    public Long id;

    @NotNull public Long disciplinaId;

    @NotNull public Long alunoId;

    @Min(1)
    @Max(4)
    public int bimestre;

    @DecimalMin("0")
    @DecimalMax("100")
    public Double valor;

    @DecimalMin("0")
    @DecimalMax("60")
    public Double recuperacao;

    public LocalDate prazo;

    public boolean liberada;
}
