package br.com.atena.escolar.notaavaliacao.dto;

/** Projeção pública explícita: campos privados nunca participam da serialização. */
public record NotaAvaliacaoRespostaDTO(Long id, Long avaliacaoId, Long alunoId, Double valor) {}
