package br.com.atena.escolar.listagem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LinhaListaDTO(Long id, String nome, String matricula, Integer ano) {}
