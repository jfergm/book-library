package dev.fer.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class FloorNotFoundException extends RuntimeException {
  public FloorNotFoundException() {
    super("Floor not found");  
  }

  public FloorNotFoundException(String message) {
    super(message);
  }
}
