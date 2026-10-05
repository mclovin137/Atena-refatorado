package br.com.atena.escolar.disciplina.dto;

/** Projeção pública explícita: campos privados nunca participam da serialização. */
public record DisciplinaRespostaDTO(
        Long id, Long turmaId, Long materiaId, Long professorId, int cargaHoraria) {}
