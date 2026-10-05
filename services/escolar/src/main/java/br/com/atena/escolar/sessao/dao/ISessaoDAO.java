package br.com.atena.escolar.sessao.dao;

/** Porta de sessão: regras/casos de uso não carregam Servlet ou SecurityContext. */
public interface ISessaoDAO {
    String tokenCsrf();

    String emailAutenticado();

    String autenticar(String email, String senha);

    void encerrar();
}
