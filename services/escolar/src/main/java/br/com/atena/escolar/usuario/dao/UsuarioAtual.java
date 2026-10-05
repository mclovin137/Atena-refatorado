package br.com.atena.escolar.usuario.dao;

import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.pessoa.dao.IPessoaDAO;
import br.com.atena.escolar.usuario.dominio.IUsuarioAtual;
import br.com.atena.escolar.usuario.dominio.PerfilEnum;
import br.com.atena.escolar.usuario.dominio.UsuarioAutenticado;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioAtual implements IUsuarioAtual {
    private final IPessoaDAO pessoas;

    public UsuarioAtual(IPessoaDAO pessoas) {
        this.pessoas = pessoas;
    }

    @Override
    public UsuarioAutenticado obter() {
        var autenticacao = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacao == null || !autenticacao.isAuthenticated()) {
            throw new ErroAplicacao(
                    ErroAplicacao.CategoriaEnum.NAO_AUTENTICADO, "Autenticação necessária");
        }
        var pessoa =
                pessoas.buscarPorEmail(autenticacao.getName())
                        .filter(registro -> registro.ativo())
                        .orElseThrow(
                                () ->
                                        new ErroAplicacao(
                                                ErroAplicacao.CategoriaEnum.NAO_AUTENTICADO,
                                                "Conta inativa"));
        return new UsuarioAutenticado(
                pessoa.id(), pessoa.email(), PerfilEnum.valueOf(pessoa.perfil()));
    }
}
