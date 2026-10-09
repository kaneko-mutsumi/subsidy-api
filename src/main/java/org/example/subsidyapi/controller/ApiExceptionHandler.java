package org.example.subsidyapi.controller;

import java.util.List;
import org.example.subsidyapi.staff.EmailAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(InvalidRequestParameterException.class)
  public ResponseEntity<ApiErrorResponse> handleInvalidParam(
      InvalidRequestParameterException ex) {
    ApiErrorResponse body = new ApiErrorResponse(
        "BAD_REQUEST",
        ex.getMessage(),
        ex.getClass().getSimpleName(),
        List.of()
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
    var fieldErrors = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> new ApiErrorResponse.FieldError(
            error.getField(),
            error.getDefaultMessage()
        ))
        .toList();
    String message = fieldErrors.stream()
        .findFirst()
        .map(ApiErrorResponse.FieldError::message)
        .orElse("request validation failed");
    ApiErrorResponse body = new ApiErrorResponse(
        "BAD_REQUEST",
        message,
        ex.getClass().getSimpleName(),
        fieldErrors
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<ApiErrorResponse> handleEmailDup(EmailAlreadyExistsException ex) {
    ApiErrorResponse body = new ApiErrorResponse(
        "CONFLICT",
        ex.getMessage(),
        ex.getClass().getSimpleName(),
        List.of()
    );
    return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
  }
}
