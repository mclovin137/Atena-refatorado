package br.com.atena.escolar.requisitomateria.dominio;

import br.com.atena.escolar.comum.dominio.ValidacoesDominio;

public record RequisitoMateria(Long requisitoId, Long materiaId) {
    public RequisitoMateria {
        ValidacoesDominio.obrigatorio(requisitoId, "requisitoId");
        ValidacoesDominio.obrigatorio(materiaId, "materiaId");
    }
}
