package br.com.atena.escolar.presenca.dto;

/** Projeção pública explícita: campos privados nunca participam da serialização. */
public record PresencaRespostaDTO(Long id, Long aulaId, Long alunoId, int faltas) {}
