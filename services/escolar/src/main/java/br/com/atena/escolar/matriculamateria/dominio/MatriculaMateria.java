package br.com.atena.escolar.matriculamateria.dominio;

import br.com.atena.escolar.comum.dominio.ValidacoesDominio;

public record MatriculaMateria(Long matriculaId, Long disciplinaId) {
    public MatriculaMateria {
        ValidacoesDominio.obrigatorio(matriculaId, "matriculaId");
        ValidacoesDominio.obrigatorio(disciplinaId, "disciplinaId");
    }
}
