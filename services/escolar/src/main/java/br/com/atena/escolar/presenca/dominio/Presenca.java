package br.com.atena.escolar.presenca.dominio;

import br.com.atena.escolar.comum.dominio.ValidacoesDominio;

/** Estado escolar imutável; persistência e transporte pertencem aos adaptadores. */
public record Presenca(Long id, Long aulaId, Long alunoId, int faltas) {

    public Presenca {
        ValidacoesDominio.obrigatorio(aulaId, "aulaId");
        ValidacoesDominio.obrigatorio(alunoId, "alunoId");
        ValidacoesDominio.intervalo(faltas, 0, 12, "faltas");
    }
}
