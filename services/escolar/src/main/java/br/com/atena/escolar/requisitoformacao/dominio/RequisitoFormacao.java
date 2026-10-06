package br.com.atena.escolar.requisitoformacao.dominio;

import br.com.atena.escolar.comum.dominio.ValidacoesDominio;

public record RequisitoFormacao(Long id, String nome, Long gradeId, String descricao) {
    public RequisitoFormacao {
        ValidacoesDominio.textoObrigatorio(nome, "nome");
        ValidacoesDominio.tamanho(nome, 150, "nome");
        ValidacoesDominio.obrigatorio(gradeId, "gradeId");
        ValidacoesDominio.intervalo(gradeId, 1, Long.MAX_VALUE, "gradeId");
        ValidacoesDominio.tamanho(descricao, 4000, "descricao");
    }
}
