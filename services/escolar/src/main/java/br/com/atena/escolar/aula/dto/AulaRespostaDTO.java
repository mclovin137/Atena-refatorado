package br.com.atena.escolar.aula.dto;

import java.time.LocalDate;

/** Projeção pública explícita: campos privados nunca participam da serialização. */
public record AulaRespostaDTO(
        Long id,
        Long disciplinaId,
        LocalDate data,
        String conteudo,
        String planejamento,
        int horas) {}
