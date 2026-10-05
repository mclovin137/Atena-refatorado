package br.com.atena.escolar.sessao.dao;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Repository
public class SessaoDAO implements ISessaoDAO {
    private final AuthenticationManager autenticacoes;

    public SessaoDAO(AuthenticationManager autenticacoes) {
        this.autenticacoes = autenticacoes;
    }

    private ServletRequestAttributes requisicaoAtual() {
        return (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    }

    @Override
    public String tokenCsrf() {
        var requisicao = requisicaoAtual().getRequest();
        var token = (CsrfToken) requisicao.getAttribute(CsrfToken.class.getName());
        return token.getToken();
    }

    @Override
    public String emailAutenticado() {
        var autenticacao = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacao == null
                || !autenticacao.isAuthenticated()
                || autenticacao instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return autenticacao.getName();
    }

    @Override
    public String autenticar(String email, String senha) {
        var autenticacao =
                autenticacoes.authenticate(
                        UsernamePasswordAuthenticationToken.unauthenticated(email, senha));
        var atributos = requisicaoAtual();
        HttpServletRequest requisicao = atributos.getRequest();
        HttpServletResponse resposta = atributos.getResponse();
        if (requisicao.getSession(false) != null) {
            requisicao.changeSessionId();
        }
        var contexto = SecurityContextHolder.createEmptyContext();
        contexto.setAuthentication(autenticacao);
        SecurityContextHolder.setContext(contexto);
        new HttpSessionSecurityContextRepository().saveContext(contexto, requisicao, resposta);
        return autenticacao.getName();
    }

    @Override
    public void encerrar() {
        var requisicao = requisicaoAtual().getRequest();
        if (requisicao.getSession(false) != null) {
            requisicao.getSession(false).invalidate();
        }
        SecurityContextHolder.clearContext();
    }
}
