package br.com.atena.escolar.evento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/** Contrato de entrada explícito, independente da entidade JPA. */
public class EventoRequisicaoDTO {
    public Long id;

    @NotBlank
    @Size(max = 150)
    public String titulo;

    @NotNull public LocalDate inicio;

    @NotNull public LocalDate fim;

    @Size(max = 4000)
    public String descricao;
}
