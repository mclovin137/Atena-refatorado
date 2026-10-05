package br.com.atena.relatorios.relatorio.dominio;

public record Relatorio(
        Long id,
        SolicitacaoRelatorio solicitacao,
        Long executionId,
        String nome,
        String snapshot) {}
