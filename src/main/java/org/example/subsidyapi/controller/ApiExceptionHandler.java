package org.example.subsidyapi.controller;

import java.util.Map;
import org.example.subsidyapi.staff.EmailAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(InvalidRequestParameterException.class)
  public ResponseEntity<Map<String, Object>> handleInvalidParam(
      InvalidRequestParameterException ex) {
    Map<String, Object> body = Map.of(
        "error", "BAD_REQUEST",
        "message", ex.getMessage(),
        "detail", ex.getClass().getSimpleName()
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<Map<String, Object>> handleEmailDup(EmailAlreadyExistsException ex) {
    Map<String, Object> body = Map.of(
        "error", "CONFLICT",
        "message", ex.getMessage(),
        "detail", ex.getClass().getSimpleName()
    );
    return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
  }
}
