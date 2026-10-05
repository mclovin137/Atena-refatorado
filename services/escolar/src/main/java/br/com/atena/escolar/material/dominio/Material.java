package br.com.atena.escolar.material.dominio;

import br.com.atena.escolar.comum.dominio.ValidacoesDominio;

/** Estado escolar imutável; persistência e transporte pertencem aos adaptadores. */
public record Material(
        Long id,
        Long disciplinaId,
        String nome,
        String tipo,
        String arquivo,
        Long autorId,
        byte[] conteudo) {

    public Material {
        conteudo = conteudo == null ? null : conteudo.clone();
        ValidacoesDominio.obrigatorio(disciplinaId, "disciplinaId");
        ValidacoesDominio.textoObrigatorio(nome, "nome");
        ValidacoesDominio.tamanho(nome, 150, "nome");
    }

    @Override
    public byte[] conteudo() {
        return conteudo == null ? null : conteudo.clone();
    }
}
