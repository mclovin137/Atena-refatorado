package br.com.atena.escolar.autorizacao.dominio;

/** Capacidades básicas de um perfil; regras de nota/presença ficam nos respectivos recursos. */
public interface IAutorizacaoPerfil {
    boolean administrar();

    boolean consultarPessoa(boolean propria, boolean escrita);

    boolean acessarEnsino(boolean leciona, boolean matriculado, boolean escrita);

    boolean acessarTurma(boolean leciona, boolean matriculado, boolean escrita);
}
