package br.com.atena.escolar.curso.dominio;

import br.com.atena.escolar.comum.dominio.ValidacoesDominio;

/** Estado escolar imutável; persistência e transporte pertencem aos adaptadores. */
public record Curso(Long id, String nome, Long faculdadeId) {

    public Curso(Long id, String nome) {
        this(id, nome, null);
    }

    public Curso {
        ValidacoesDominio.textoObrigatorio(nome, "nome");
        ValidacoesDominio.tamanho(nome, 150, "nome");
        ValidacoesDominio.intervalo(faculdadeId, 1, Long.MAX_VALUE, "faculdadeId");
    }
}
