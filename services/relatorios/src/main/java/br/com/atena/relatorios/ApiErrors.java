package br.com.atena.relatorios;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestControllerAdvice
public class ApiErrors {
 @ExceptionHandler(ResponseStatusException.class) ResponseEntity<?> status(ResponseStatusException e){return ResponseEntity.status(e.getStatusCode()).body(Map.of("message",e.getReason()==null?"Requisição inválida":e.getReason()));}
 @ExceptionHandler({org.springframework.web.bind.MethodArgumentNotValidException.class,org.springframework.web.method.annotation.HandlerMethodValidationException.class,org.springframework.http.converter.HttpMessageNotReadableException.class,jakarta.validation.ConstraintViolationException.class,IllegalArgumentException.class}) ResponseEntity<?> bad(Exception e){return ResponseEntity.badRequest().body(Map.of("message","Dados inválidos; confira os campos obrigatórios e seus limites"));}
 @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class) ResponseEntity<?> conflict(Exception e){return ResponseEntity.status(409).body(Map.of("message","Registro duplicado ou vínculo impede a operação"));}
}
