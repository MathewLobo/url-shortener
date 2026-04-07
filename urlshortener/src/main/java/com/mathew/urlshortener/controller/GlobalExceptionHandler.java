package com.mathew.urlshortener.controller;

import com.mathew.urlshortener.exception.UrlExpiredException;
import com.mathew.urlshortener.exception.UrlNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<String> handleNotFound(UrlNotFoundException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler(UrlExpiredException.class)
    public ResponseEntity<String> handleExpired(UrlExpiredException e) {
        return ResponseEntity.status(410).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidation(MethodArgumentNotValidException e) {
        String errors = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(err -> err.getDefaultMessage())
            .collect(Collectors.joining(", "));
        return ResponseEntity.status(400).body(errors);
    }
}