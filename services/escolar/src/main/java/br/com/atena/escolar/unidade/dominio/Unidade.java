package br.com.atena.escolar.unidade.dominio;

import br.com.atena.escolar.comum.dominio.ValidacoesDominio;

/** Estado escolar imutável; persistência e transporte pertencem aos adaptadores. */
public record Unidade(Long id, String nome, String endereco, String telefone, String cnpj) {

    public Unidade {
        ValidacoesDominio.textoObrigatorio(nome, "nome");
        ValidacoesDominio.tamanho(nome, 150, "nome");
        ValidacoesDominio.tamanho(endereco, 500, "endereco");
        ValidacoesDominio.tamanho(telefone, 30, "telefone");
        ValidacoesDominio.tamanho(cnpj, 18, "cnpj");
    }
}
