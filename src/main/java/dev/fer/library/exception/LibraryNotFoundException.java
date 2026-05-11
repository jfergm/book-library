package dev.fer.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class LibraryNotFoundException extends RuntimeException {

  public LibraryNotFoundException() {
    super("Library not found");  
  }

  public LibraryNotFoundException(String message) {
    super(message);
  }
}
