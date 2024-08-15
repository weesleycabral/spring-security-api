package com.wesley.security.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ErrorCreatingTokenException extends RuntimeException {
  private final HttpStatus status = HttpStatus.BAD_REQUEST;

  public ErrorCreatingTokenException() {
    super("Error creating token");
  }

}
