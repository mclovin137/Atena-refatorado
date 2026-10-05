package br.com.atena.relatorios.comum.rest;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class AutenticacaoServicoFiltro extends OncePerRequestFilter {
    private final byte[] token;

    public AutenticacaoServicoFiltro(@Value("${atena.service-token}") String token) {
        if (token.length() < 32) {
            throw new IllegalArgumentException("SERVICE_TOKEN deve ter pelo menos 32 caracteres");
        }
        this.token = token.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest requisicao, HttpServletResponse resposta, FilterChain cadeia)
            throws ServletException, IOException {
        if (requisicao.getRequestURI().equals("/actuator/health")) {
            cadeia.doFilter(requisicao, resposta);
            return;
        }
        String fornecido = requisicao.getHeader("X-Service-Token");
        if (fornecido == null
                || !MessageDigest.isEqual(token, fornecido.getBytes(StandardCharsets.UTF_8))) {
            resposta.setStatus(401);
            resposta.setContentType("application/json");
            resposta.setCharacterEncoding(StandardCharsets.UTF_8.name());
            resposta.getWriter().write("{\"message\":\"Credencial de serviço inválida\"}");
            return;
        }
        cadeia.doFilter(requisicao, resposta);
    }
}
