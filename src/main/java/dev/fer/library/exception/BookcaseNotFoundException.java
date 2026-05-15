package dev.fer.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class BookcaseNotFoundException extends RuntimeException {
  public BookcaseNotFoundException() {
    super("Bookcase not found");
  }

  public BookcaseNotFoundException(String message) {
    super(message);
  }
}
