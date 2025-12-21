package org.example.subsidyapi.controller;

public class InvalidRequestParameterException extends RuntimeException {
  public InvalidRequestParameterException(String message) {
    super(message);
  }
}
