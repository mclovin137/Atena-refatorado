package br.com.atena.escolar.matricula.dto;

/** Projeção pública explícita: campos privados nunca participam da serialização. */
public record MatriculaRespostaDTO(Long id, Long turmaId, Long alunoId, boolean dependencia) {}
