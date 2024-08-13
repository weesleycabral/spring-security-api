package com.wesley.security.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class InvalidCredentialsException extends RuntimeException {

  private final HttpStatus status = HttpStatus.UNAUTHORIZED;

  public InvalidCredentialsException() {
    super("Invalid credentials");
  }

}
