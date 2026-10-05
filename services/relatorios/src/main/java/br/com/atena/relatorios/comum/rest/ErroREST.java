package br.com.atena.relatorios.comum.rest;

import br.com.atena.relatorios.comum.dto.ErroRespostaDTO;
import br.com.atena.relatorios.relatorio.dominio.ErroRelatorio;

import jakarta.validation.ConstraintViolationException;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class ErroREST {
    @ExceptionHandler(ErroRelatorio.class)
    public ResponseEntity<ErroRespostaDTO> relatorio(ErroRelatorio erro) {
        int status =
                switch (erro.categoria()) {
                    case INVALIDO -> 400;
                    case NAO_ENCONTRADO -> 404;
                    case NAO_CONCLUIDO, CONFLITO -> 409;
                };
        return ResponseEntity.status(status).body(new ErroRespostaDTO(erro.getMessage()));
    }

    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        HandlerMethodValidationException.class,
        HttpMessageNotReadableException.class,
        ConstraintViolationException.class,
        IllegalArgumentException.class
    })
    public ResponseEntity<ErroRespostaDTO> invalido(Exception erro) {
        return ResponseEntity.badRequest()
                .body(
                        new ErroRespostaDTO(
                                "Dados inválidos; confira os campos obrigatórios e seus limites"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroRespostaDTO> inesperado(Exception erro) {
        org.slf4j.LoggerFactory.getLogger(ErroREST.class)
                .error(
                        "Falha inesperada em relatório: categoria={}",
                        erro.getClass().getSimpleName());
        return ResponseEntity.internalServerError()
                .body(new ErroRespostaDTO("Não foi possível concluir a operação"));
    }
}
