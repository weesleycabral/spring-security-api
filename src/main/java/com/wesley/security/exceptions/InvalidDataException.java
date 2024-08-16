package com.wesley.security.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class InvalidDataException extends RuntimeException {
  private final HttpStatus status = HttpStatus.BAD_REQUEST;

  public InvalidDataException() {
    super("Invalid data");
  }

}
