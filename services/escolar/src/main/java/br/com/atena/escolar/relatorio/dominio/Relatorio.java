package br.com.atena.escolar.relatorio.dominio;

public record Relatorio(
        Long id, Long ownerId, Long alunoId, String tipo, String status, Long executionId) {}
