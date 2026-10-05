package br.com.atena.escolar.boletim.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/** Ausência de unidade serializa exatamente {}, preservando o contrato entre contextos. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UnidadeBoletimDTO(String nome, String endereco, String telefone, String cnpj) {}
