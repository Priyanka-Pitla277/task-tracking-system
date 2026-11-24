package com.tasktracker.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handle(ApiException ex){return ResponseEntity.status(ex.getStatus()).body(Map.of("error",ex.getMessage()));}
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> badEnum(IllegalArgumentException ex){return ResponseEntity.badRequest().body(java.util.Map.of("error","Invalid status or priority value"));}
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validation(MethodArgumentNotValidException ex){
        var field=ex.getBindingResult().getFieldErrors().stream().findFirst();
        return ResponseEntity.badRequest().body(Map.of("error",field.isPresent()?field.get().getDefaultMessage():"Validation failed"));
    }
}
