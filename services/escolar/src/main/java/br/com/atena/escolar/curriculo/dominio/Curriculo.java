package br.com.atena.escolar.curriculo.dominio;

import br.com.atena.escolar.comum.dominio.ValidacoesDominio;

/** Estado escolar imutável; persistência e transporte pertencem aos adaptadores. */
public record Curriculo(Long id, String nome, Long materiaId, int cargaHoraria) {

    public Curriculo {
        ValidacoesDominio.textoObrigatorio(nome, "nome");
        ValidacoesDominio.tamanho(nome, 150, "nome");
        ValidacoesDominio.obrigatorio(materiaId, "materiaId");
        ValidacoesDominio.intervalo(cargaHoraria, 1, 2000, "cargaHoraria");
    }
}
