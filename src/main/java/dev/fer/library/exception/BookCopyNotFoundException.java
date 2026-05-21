package dev.fer.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class BookCopyNotFoundException extends RuntimeException {

  public BookCopyNotFoundException() {
    super("Book copy not found");
  }

  public BookCopyNotFoundException(String message) {
    super(message);
  }
  
}
