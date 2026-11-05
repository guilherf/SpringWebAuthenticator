package net.bifewagyu.authenticator.globalhandler.exeptionHandler;

import jakarta.validation.executable.ValidateOnExecution;
import net.bifewagyu.authenticator.globalhandler.EmailCadastradoExeption;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExeptionHandler {

    @ExceptionHandler(EmailCadastradoExeption.class)
    public ResponseEntity<Map<String, String>> handleEmailDuplicadoExeption(EmailCadastradoExeption e)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erro ", e.getMessage()));
    }


    @ExceptionHandler(StackOverflowError.class)
    public String handleStackOverflowExeption(StackOverflowError e)
    {
        return System.out.printf("erro: " + e.getMessage()).toString();
    }


    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleIntegridadeExeption(DataIntegrityViolationException e)
    {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erro", "Violação de integridade de dados (registro duplicado)"));

    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((e) -> {

           erros.putIfAbsent(e.getField(), e.getDefaultMessage());

        });


        Map<String, Object> erro = new LinkedHashMap<>();

        erro.put("status", 400);
        erro.put("erros", erros);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);


    }


}
