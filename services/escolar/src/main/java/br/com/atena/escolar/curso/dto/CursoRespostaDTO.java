package br.com.atena.escolar.curso.dto;

public record CursoRespostaDTO(
        Long id, String nome, Long faculdadeId, boolean vinculoInstitucionalPendente) {}
