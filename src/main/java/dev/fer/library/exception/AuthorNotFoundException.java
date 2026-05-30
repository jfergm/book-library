package dev.fer.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class AuthorNotFoundException extends RuntimeException {
  public AuthorNotFoundException() {
    super("Author not found");
  }

  public AuthorNotFoundException(String message) {
    super(message);
  }
}
