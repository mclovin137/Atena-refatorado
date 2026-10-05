package br.com.atena.escolar.mensagem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/** Contrato de entrada explícito, independente da entidade JPA. */
public class MensagemRequisicaoDTO {
    public Long id;

    public Long remetenteId;

    @NotNull public Long destinatarioId;

    @NotBlank
    @Size(max = 4000)
    public String texto;

    public LocalDateTime enviadaEm;
}
