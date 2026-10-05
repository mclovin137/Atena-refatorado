package br.com.atena.escolar.autorizacao.service;

import br.com.atena.escolar.autorizacao.dominio.IAutorizacaoPerfil;

final class AutorizacaoSecretariaService implements IAutorizacaoPerfil {

    @Override
    public boolean administrar() {
        return true;
    }

    @Override
    public boolean consultarPessoa(boolean propria, boolean escrita) {
        return true;
    }

    @Override
    public boolean acessarEnsino(boolean leciona, boolean matriculado, boolean escrita) {
        return true;
    }

    @Override
    public boolean acessarTurma(boolean leciona, boolean matriculado, boolean escrita) {
        return true;
    }
}
