package br.com.atena.escolar.comum.rest;

import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.relatorio.dominio.ErroIntegracaoRelatorio;

import jakarta.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestControllerAdvice
public class ErroControllerAdvice {
    @ExceptionHandler(ErroIntegracaoRelatorio.class)
    ResponseEntity<?> integracao(ErroIntegracaoRelatorio erro) {
        return ResponseEntity.status(erro.codigoResposta())
                .body(Map.of("message", erro.getMessage()));
    }

    @ExceptionHandler(ErroAplicacao.class)
    ResponseEntity<?> dominio(ErroAplicacao e) {
        int status =
                switch (e.categoria()) {
                    case ENTRADA_INVALIDA -> 400;
                    case NAO_AUTENTICADO -> 401;
                    case ACESSO_NEGADO -> 403;
                    case NAO_ENCONTRADO -> 404;
                    case CONFLITO -> 409;
                    case FALHA_INTERNA -> 500;
                };
        return ResponseEntity.status(status).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<?> status(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(
                        Map.of(
                                "message",
                                e.getReason() == null ? "Requisição inválida" : e.getReason()));
    }

    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        HandlerMethodValidationException.class,
        HttpMessageNotReadableException.class,
        ConstraintViolationException.class,
        IllegalArgumentException.class
    })
    ResponseEntity<?> bad(Exception e) {
        return ResponseEntity.badRequest()
                .body(
                        Map.of(
                                "message",
                                "Dados inválidos; confira os campos obrigatórios e seus limites"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<?> conflict(Exception e) {
        return ResponseEntity.status(409)
                .body(Map.of("message", "Registro duplicado ou vínculo impede a operação"));
    }

    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<?> credentials(Exception e) {
        return ResponseEntity.status(401).body(Map.of("message", "Credenciais inválidas"));
    }

    @ExceptionHandler(RestClientResponseException.class)
    ResponseEntity<?> upstream(RestClientResponseException e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(Map.of("message", "Serviço de relatórios recusou a operação"));
    }

    @ExceptionHandler(ResourceAccessException.class)
    ResponseEntity<?> unavailable(Exception e) {
        return ResponseEntity.status(503)
                .body(Map.of("message", "Serviço de relatórios indisponível"));
    }
}
