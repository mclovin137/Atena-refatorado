package br.com.atena.escolar.horario.dto;

import java.time.LocalTime;

/** Projeção pública explícita: campos privados nunca participam da serialização. */
public record HorarioRespostaDTO(
        Long id, Long disciplinaId, int diaSemana, LocalTime inicio, LocalTime fim) {}
