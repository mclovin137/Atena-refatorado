package br.com.atena.escolar.aula.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/** Contrato de entrada explícito, independente da entidade JPA. */
public class AulaRequisicaoDTO {
    public Long id;

    @NotNull public Long disciplinaId;

    @NotNull public LocalDate data;

    @NotBlank
    @Size(max = 4000)
    public String conteudo;

    @Size(max = 4000)
    public String planejamento;

    @Min(1)
    @Max(12)
    public int horas;
}
