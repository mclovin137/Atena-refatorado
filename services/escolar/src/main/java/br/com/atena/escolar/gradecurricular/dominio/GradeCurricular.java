package br.com.atena.escolar.gradecurricular.dominio;

import br.com.atena.escolar.comum.dominio.ValidacoesDominio;

public record GradeCurricular(Long id, String nome, Long cursoId) {
    public GradeCurricular {
        ValidacoesDominio.textoObrigatorio(nome, "nome");
        ValidacoesDominio.tamanho(nome, 150, "nome");
        ValidacoesDominio.obrigatorio(cursoId, "cursoId");
        ValidacoesDominio.intervalo(cursoId, 1, Long.MAX_VALUE, "cursoId");
    }
}
