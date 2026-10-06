package br.com.atena.escolar.importacao.dto;

public record TabelaImportacaoDTO(
        String nome, long lidos, long importados, long pendentes, long rejeitados) {}
