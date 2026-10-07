package br.com.atena.escolar.importacao.dto;

import java.util.Map;

/** Staging interno privado; não é um DTO REST. */
public record RegistroImportacaoDTO(
        String tabela,
        int ordinal,
        String origemId,
        String status,
        String motivo,
        Map<String, String> dados,
        Map<String, Long> destinos) {}
