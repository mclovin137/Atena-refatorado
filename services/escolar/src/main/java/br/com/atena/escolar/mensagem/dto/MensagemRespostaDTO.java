package br.com.atena.escolar.mensagem.dto;

import java.time.LocalDateTime;

/** Projeção pública explícita: campos privados nunca participam da serialização. */
public record MensagemRespostaDTO(
        Long id, Long remetenteId, Long destinatarioId, String texto, LocalDateTime enviadaEm) {}
