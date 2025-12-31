package org.example.subsidyapi.controller;

import java.util.List;

public record ApiErrorResponse(
    String error,
    String message,
    String detail,
    List<FieldError> errors
) {

  public record FieldError(
      String field,
      String message
  ) {}
}