package br.com.atena.escolar.evento.dto;

import java.time.LocalDate;

/** Projeção pública explícita: campos privados nunca participam da serialização. */
public record EventoRespostaDTO(
        Long id, String titulo, LocalDate inicio, LocalDate fim, String descricao) {}
