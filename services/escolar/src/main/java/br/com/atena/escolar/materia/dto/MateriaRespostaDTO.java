package br.com.atena.escolar.materia.dto;

/** Projeção pública explícita: campos privados nunca participam da serialização. */
public record MateriaRespostaDTO(Long id, String nome) {}
