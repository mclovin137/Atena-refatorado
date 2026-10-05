package br.com.atena.escolar.aula.dominio;

import br.com.atena.escolar.comum.dominio.ValidacoesDominio;

import java.time.LocalDate;

/** Estado escolar imutável; persistência e transporte pertencem aos adaptadores. */
public record Aula(
        Long id,
        Long disciplinaId,
        LocalDate data,
        String conteudo,
        String planejamento,
        int horas) {

    public Aula {
        ValidacoesDominio.obrigatorio(disciplinaId, "disciplinaId");
        ValidacoesDominio.obrigatorio(data, "data");
        ValidacoesDominio.textoObrigatorio(conteudo, "conteudo");
        ValidacoesDominio.tamanho(conteudo, 4000, "conteudo");
        ValidacoesDominio.tamanho(planejamento, 4000, "planejamento");
        ValidacoesDominio.intervalo(horas, 1, 12, "horas");
    }
}
