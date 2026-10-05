package br.com.atena.escolar.unidade.dto;

/** Projeção pública explícita: campos privados nunca participam da serialização. */
public record UnidadeRespostaDTO(
        Long id, String nome, String endereco, String telefone, String cnpj) {}
