package net.bifewagyu.authenticator.globalhandler.exeptionHandler;

import net.bifewagyu.authenticator.globalhandler.EmailCadastradoExeption;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExeptionHandler {

    @ExceptionHandler(EmailCadastradoExeption.class)
    public ResponseEntity<Map<String, String>> handleEmailDuplicadoExeption(EmailCadastradoExeption e)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erro ", e.getMessage()));
    }


    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleIntegridadeExeption(DataIntegrityViolationException e)
    {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erro", "Violação de integridade de dados (registro duplicado)"));

    }


}
