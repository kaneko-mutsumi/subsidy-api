package org.example.subsidyapi.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.example.subsidyapi.staff.EmailAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiErrorResponse> handleNotReadable(HttpMessageNotReadableException ex) {
    Optional<InvalidFormatException> invalidFormat = extractInvalidFormatException(ex);
    String message = "request body is invalid";
    List<ApiErrorResponse.FieldError> fieldErrors = Collections.emptyList();
    if (invalidFormat.isPresent()) {
      InvalidFormatException formatException = invalidFormat.get();
      String field = formatException.getPath().stream()
          .map(JsonMappingException.Reference::getFieldName)
          .filter(name -> name != null && !name.isBlank())
          .reduce((first, second) -> first + "." + second)
          .orElse("body");
      String value = String.valueOf(formatException.getValue());
      message = field + " の形式が不正です: " + value;
      fieldErrors = List.of(new ApiErrorResponse.FieldError(field, message));
    }
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

  private Optional<InvalidFormatException> extractInvalidFormatException(Throwable ex) {
    Throwable current = ex;
    while (current != null) {
      if (current instanceof InvalidFormatException invalidFormat) {
        return Optional.of(invalidFormat);
      }
      current = current.getCause();
    }
    return Optional.empty();
  }
}