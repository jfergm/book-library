package dev.fer.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ShelfNotFoundException extends RuntimeException {
  public ShelfNotFoundException() {
    super("Shelf not found");
  }

  public ShelfNotFoundException(String message) {
    super(message);
  }
}
