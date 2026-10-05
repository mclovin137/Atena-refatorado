package br.com.atena.escolar.matricula.dominio;

import br.com.atena.escolar.comum.dominio.ValidacoesDominio;

/** Estado escolar imutável; persistência e transporte pertencem aos adaptadores. */
public record Matricula(Long id, Long turmaId, Long alunoId, boolean dependencia) {

    public Matricula {
        ValidacoesDominio.obrigatorio(turmaId, "turmaId");
        ValidacoesDominio.obrigatorio(alunoId, "alunoId");
    }
}
