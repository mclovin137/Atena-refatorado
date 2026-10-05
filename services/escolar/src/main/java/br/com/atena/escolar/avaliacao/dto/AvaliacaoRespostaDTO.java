package br.com.atena.escolar.avaliacao.dto;

import java.time.LocalDate;

/** Projeção pública explícita: campos privados nunca participam da serialização. */
public record AvaliacaoRespostaDTO(
        Long id,
        Long disciplinaId,
        String nome,
        int bimestre,
        Double valor,
        LocalDate data,
        String tipo) {}
