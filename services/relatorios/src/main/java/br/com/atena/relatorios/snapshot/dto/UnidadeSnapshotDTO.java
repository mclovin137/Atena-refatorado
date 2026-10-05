package br.com.atena.relatorios.snapshot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UnidadeSnapshotDTO(String nome, String endereco, String telefone, String cnpj) {}
