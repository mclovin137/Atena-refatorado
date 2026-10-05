package br.com.atena.escolar.comum.rest;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class FiltroCredencialServico extends OncePerRequestFilter {
    private final byte[] credencial;

    public FiltroCredencialServico(String token) {
        if (token == null || token.length() < 32)
            throw new IllegalStateException(
                    "Credencial de serviço deve ter pelo menos 32 caracteres");
        credencial = token.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if (request.getRequestURI().startsWith("/api/internal/")) {
            String fornecida = request.getHeader("X-Service-Token");
            if (fornecida == null
                    || !MessageDigest.isEqual(
                            credencial, fornecida.getBytes(StandardCharsets.UTF_8))) {
                response.setStatus(401);
                response.setContentType("application/json");
                response.getWriter().write("{\"message\":\"Credencial de serviço inválida\"}");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
