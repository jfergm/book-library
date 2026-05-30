package dev.fer.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class SectionNotFoundException extends RuntimeException {
  
  public SectionNotFoundException() {
    super("Section not found");
  }

  public SectionNotFoundException(String message) {
    super(message);
  } 
}
