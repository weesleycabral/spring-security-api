package com.wesley.security.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.wesley.security.exceptions.EmailAlreadyExistsException;
import com.wesley.security.exceptions.ErrorCreatingTokenException;
import com.wesley.security.exceptions.ErrorMessage;
import com.wesley.security.exceptions.InvalidCredentialsException;
import com.wesley.security.exceptions.InvalidDataException;
import com.wesley.security.exceptions.UserNotFoundException;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({ UserNotFoundException.class })
  public ResponseEntity<ErrorMessage> handleUserNotFound(UserNotFoundException exception) {
    return ResponseEntity.status(exception.getStatus())
        .body(new ErrorMessage(exception.getMessage(), exception.getStatus()));
  }

  @ExceptionHandler({ InvalidCredentialsException.class })
  public ResponseEntity<ErrorMessage> handleInvalidCredentialsException(InvalidCredentialsException exception) {
    return ResponseEntity.status(exception.getStatus())
        .body(new ErrorMessage(exception.getMessage(), exception.getStatus()));
  }

  @ExceptionHandler({ EmailAlreadyExistsException.class })
  public ResponseEntity<ErrorMessage> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception) {
    return ResponseEntity.status(exception.getStatus())
        .body(new ErrorMessage(exception.getMessage(), exception.getStatus()));
  }

  @ExceptionHandler({ InvalidDataException.class })
  public ResponseEntity<ErrorMessage> handleInvalidDataException(InvalidDataException exception) {
    return ResponseEntity.status(exception.getStatus())
        .body(new ErrorMessage(exception.getMessage(), exception.getStatus()));
  }

  @ExceptionHandler({ ErrorCreatingTokenException.class })
  public ResponseEntity<ErrorMessage> handleErrorCreatingTokenException(ErrorCreatingTokenException exception) {
    return ResponseEntity.status(exception.getStatus())
        .body(new ErrorMessage(exception.getMessage(), exception.getStatus()));
  }
}
