package com.wesley.security.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EmailAlreadyExistsException extends RuntimeException {
  private final HttpStatus status = HttpStatus.CONFLICT;

  public EmailAlreadyExistsException() {
    super("Email already exists");
  }

}
