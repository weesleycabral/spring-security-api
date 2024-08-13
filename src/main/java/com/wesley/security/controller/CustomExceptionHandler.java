package com.wesley.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.wesley.security.exception.ErrorMessage;
import com.wesley.security.exception.UserNotFoundException;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler({ UserNotFoundException.class })
  public ResponseEntity<ErrorMessage> handleUserNotFound(UserNotFoundException exception) {
    return ResponseEntity.status(exception.getStatus())
        .body(new ErrorMessage(exception.getMessage(), exception.getStatus()));
  }
}
