package br.com.atena.escolar.sessao.dto;

import br.com.atena.escolar.pessoa.dto.PessoaRespostaDTO;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SessaoRespostaDTO(String csrfToken, PessoaRespostaDTO user) {}
