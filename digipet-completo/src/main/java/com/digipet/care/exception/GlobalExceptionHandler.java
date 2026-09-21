package com.digipet.care.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<Map<String, String>> notFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    ResponseEntity<Map<String, String>> invalid(BusinessException ex) {
        return ResponseEntity.badRequest().body(Map.of("erro", ex.getMessage()));
    }

    // Disparado quando um campo anotado com @Valid/@NotNull/@NotBlank etc. falha a validação
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, Object>> validacao(MethodArgumentNotValidException ex) {
        Map<String, String> camposComErro = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(erro ->
                camposComErro.put(erro.getField(), erro.getDefaultMessage()));
        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("erro", "Dados inválidos.");
        corpo.put("campos", camposComErro);
        return ResponseEntity.badRequest().body(corpo);
    }

    // JSON malformado ou com tipo incompatível no corpo da requisição
    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<Map<String, String>> corpoInvalido(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(Map.of("erro", "Corpo da requisição ausente ou em formato inválido."));
    }

    // Ex.: passar "abc" onde a rota espera um Long no @PathVariable
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<Map<String, String>> parametroInvalido(MethodArgumentTypeMismatchException ex) {
        String mensagem = "O parâmetro '" + ex.getName() + "' recebeu um valor em formato inválido.";
        return ResponseEntity.badRequest().body(Map.of("erro", mensagem));
    }
}
