package br.com.atena.escolar.turma.dto;

/** Projeção pública explícita: campos privados nunca participam da serialização. */
public record TurmaRespostaDTO(Long id, Long cursoId, Long serieId, int ano, String nome) {}
