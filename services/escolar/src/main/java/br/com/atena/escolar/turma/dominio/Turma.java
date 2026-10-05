package br.com.atena.escolar.turma.dominio;

import br.com.atena.escolar.comum.dominio.ValidacoesDominio;

/** Estado escolar imutável; persistência e transporte pertencem aos adaptadores. */
public record Turma(Long id, Long cursoId, Long serieId, int ano, String nome) {

    public Turma {
        ValidacoesDominio.obrigatorio(cursoId, "cursoId");
        ValidacoesDominio.obrigatorio(serieId, "serieId");
        ValidacoesDominio.intervalo(ano, 1900, 2200, "ano");
        ValidacoesDominio.textoObrigatorio(nome, "nome");
        ValidacoesDominio.tamanho(nome, 150, "nome");
    }
}
