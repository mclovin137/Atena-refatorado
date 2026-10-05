package br.com.atena.escolar.lote.dto;

public record ExecucaoLoteRespostaDTO(
        Long id, String status, String exitCode, long readCount, long writeCount) {}
