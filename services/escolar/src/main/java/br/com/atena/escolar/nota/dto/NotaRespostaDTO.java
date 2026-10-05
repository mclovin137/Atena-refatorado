package br.com.atena.escolar.nota.dto;

import java.time.LocalDate;

/** Projeção pública explícita: campos privados nunca participam da serialização. */
public record NotaRespostaDTO(
        Long id,
        Long disciplinaId,
        Long alunoId,
        int bimestre,
        Double valor,
        Double recuperacao,
        LocalDate prazo,
        boolean liberada) {}
