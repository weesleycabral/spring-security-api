package com.wesley.security.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ErrorCreatingTokenException extends RuntimeException {
  private final HttpStatus status = HttpStatus.CONFLICT;

  public ErrorCreatingTokenException() {
    super("rror creating token");
  }

}
