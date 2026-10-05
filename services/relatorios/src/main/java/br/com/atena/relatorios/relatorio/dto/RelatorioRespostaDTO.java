package br.com.atena.relatorios.relatorio.dto;

public record RelatorioRespostaDTO(
        Long id, Long ownerId, Long alunoId, String tipo, String status, Long executionId) {}
