package br.com.atena.escolar.sessao.rest;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.LongSupplier;

/** Limita o trabalho de autenticação na única instância; reinício reinicia as janelas. */
public final class LimitarLoginFiltro extends OncePerRequestFilter {
    private final Map<String, Janela> janelas = new HashMap<>();
    private final LongSupplier segundos;
    private final int capacidade;

    public LimitarLoginFiltro() {
        this(() -> System.nanoTime() / 1_000_000_000L, 1000);
    }

    LimitarLoginFiltro(LongSupplier segundos, int capacidade) {
        this.segundos = segundos;
        this.capacidade = capacidade;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !"POST".equals(request.getMethod())
                || !(request.getContextPath() + "/api/session/login")
                        .equals(request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        int espera = consumir(request.getRemoteAddr());
        if (espera > 0) {
            response.setStatus(429);
            response.setHeader("Retry-After", String.valueOf(espera));
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter()
                    .write("{\"message\":\"Muitas tentativas. Aguarde e tente novamente.\"}");
            return;
        }
        chain.doFilter(request, response);
    }

    private synchronized int consumir(String ip) {
        long agora = segundos.getAsLong();
        janelas.values().removeIf(janela -> agora >= janela.inicio + 60);
        if (!janelas.containsKey(ip) && janelas.size() >= capacidade) return 60;
        var janela = janelas.computeIfAbsent(ip, chave -> new Janela(agora));
        if (janela.tentativas >= 20) return (int) Math.max(1, janela.inicio + 60 - agora);
        janela.tentativas++;
        return 0;
    }

    private static final class Janela {
        private final long inicio;
        private int tentativas;

        private Janela(long inicio) {
            this.inicio = inicio;
        }
    }
}
