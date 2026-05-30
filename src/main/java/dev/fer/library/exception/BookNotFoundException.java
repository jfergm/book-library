package dev.fer.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class BookNotFoundException extends RuntimeException {
  public BookNotFoundException() {
    super("Book not found");
  }

  public BookNotFoundException(String message) {
    super(message);
  }
}
