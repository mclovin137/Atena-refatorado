package br.com.atena.relatorios.comum.rest;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;

@Component
public class RegistroHttpFiltro extends OncePerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger("atena.http");

    @Override
    protected void doFilterInternal(
            HttpServletRequest requisicao, HttpServletResponse resposta, FilterChain cadeia)
            throws ServletException, IOException {
        try {
            cadeia.doFilter(requisicao, resposta);
        } finally {
            Object rota = requisicao.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
            LOG.info(
                    "HTTP {} {} status={}",
                    requisicao.getMethod(),
                    rota == null ? "sem-rota" : rota,
                    resposta.getStatus());
        }
    }
}
