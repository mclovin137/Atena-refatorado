package br.com.atena.escolar.curso.dto;

/** Projeção pública explícita: campos privados nunca participam da serialização. */
public record CursoRespostaDTO(Long id, String nome) {}
