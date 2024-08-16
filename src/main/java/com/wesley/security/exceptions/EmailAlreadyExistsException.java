package com.wesley.security.exceptions;

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
