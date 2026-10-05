package br.com.atena.escolar.comum.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;

@Configuration
public class ObservabilidadeConfig {
    @Bean
    OncePerRequestFilter requestLogs() {
        return new OncePerRequestFilter() {
            final Logger log = LoggerFactory.getLogger("atena.http");

            @Override
            protected void doFilterInternal(
                    HttpServletRequest request, HttpServletResponse response, FilterChain chain)
                    throws ServletException, IOException {
                try {
                    chain.doFilter(request, response);
                } finally {
                    Object route =
                            request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
                    log.info(
                            "HTTP {} {} status={}",
                            request.getMethod(),
                            route == null ? "sem-rota" : route,
                            response.getStatus());
                }
            }
        };
    }
}
