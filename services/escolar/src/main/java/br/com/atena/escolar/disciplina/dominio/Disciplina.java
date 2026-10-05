package br.com.atena.escolar.disciplina.dominio;

import br.com.atena.escolar.comum.dominio.ValidacoesDominio;

/** Estado escolar imutável; persistência e transporte pertencem aos adaptadores. */
public record Disciplina(
        Long id, Long turmaId, Long materiaId, Long professorId, int cargaHoraria) {

    public Disciplina {
        ValidacoesDominio.obrigatorio(turmaId, "turmaId");
        ValidacoesDominio.obrigatorio(materiaId, "materiaId");
        ValidacoesDominio.intervalo(cargaHoraria, 1, 2000, "cargaHoraria");
    }
}
