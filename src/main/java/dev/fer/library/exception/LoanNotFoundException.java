package dev.fer.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class LoanNotFoundException extends RuntimeException {
  public LoanNotFoundException() {
    super("Loan not found");
  }

  public LoanNotFoundException(String message) {
    super(message);
  }
}
