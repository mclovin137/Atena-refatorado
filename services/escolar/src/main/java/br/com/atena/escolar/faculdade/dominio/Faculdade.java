package br.com.atena.escolar.faculdade.dominio;

import br.com.atena.escolar.comum.dominio.ValidacoesDominio;

public record Faculdade(Long id, String nome) {
    public Faculdade {
        ValidacoesDominio.textoObrigatorio(nome, "nome");
        ValidacoesDominio.tamanho(nome, 150, "nome");
    }
}
