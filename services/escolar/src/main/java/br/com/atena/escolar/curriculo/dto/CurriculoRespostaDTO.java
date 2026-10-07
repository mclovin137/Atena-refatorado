package br.com.atena.escolar.curriculo.dto;

/** Projeção pública explícita: campos privados nunca participam da serialização. */
public record CurriculoRespostaDTO(
        Long id, String nome, Long materiaId, int cargaHoraria, Long gradeId) {}
