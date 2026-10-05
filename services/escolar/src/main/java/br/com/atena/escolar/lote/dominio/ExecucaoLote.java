package br.com.atena.escolar.lote.dominio;

public record ExecucaoLote(
        Long identificador,
        String estado,
        String codigoSaida,
        long quantidadeLida,
        long quantidadeEscrita) {}
