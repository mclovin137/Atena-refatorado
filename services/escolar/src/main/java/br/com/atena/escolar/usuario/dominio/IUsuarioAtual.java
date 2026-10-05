package br.com.atena.escolar.usuario.dominio;

/** Porta de identidade: casos de uso não conhecem Authentication/Servlet. */
public interface IUsuarioAtual {
    UsuarioAutenticado obter();
}
