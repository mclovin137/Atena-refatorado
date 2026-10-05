package br.com.atena.escolar.material.dto;

/** Projeção pública explícita: campos privados nunca participam da serialização. */
public record MaterialRespostaDTO(
        Long id, Long disciplinaId, String nome, String tipo, Long autorId) {}
