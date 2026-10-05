package br.com.atena.escolar.autorizacao.service;

import br.com.atena.escolar.autorizacao.dominio.IAutorizacaoPerfil;

final class AutorizacaoProfessorService implements IAutorizacaoPerfil {

    @Override
    public boolean administrar() {
        return false;
    }

    @Override
    public boolean consultarPessoa(boolean propria, boolean escrita) {
        return propria && !escrita;
    }

    @Override
    public boolean acessarEnsino(boolean leciona, boolean matriculado, boolean escrita) {
        return leciona;
    }

    @Override
    public boolean acessarTurma(boolean leciona, boolean matriculado, boolean escrita) {
        return leciona && !escrita;
    }
}
