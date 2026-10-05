package br.com.atena.escolar.relatorio.dominio;

public record SolicitacaoRelatorio(
        Long alunoId,
        String tipo,
        String requestId,
        Long ownerId,
        Long professorId,
        boolean releasedOnly) {}
