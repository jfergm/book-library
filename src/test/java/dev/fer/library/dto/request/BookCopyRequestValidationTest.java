package dev.fer.library.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class BookCopyRequestValidationTest {
  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void shouldBeValidWithAllValues() {
    BookCopyRequest request = new BookCopyRequest(1L);

    Set<ConstraintViolation<BookCopyRequest>> violations = validator.validate(request);

    assertThat(violations).isEmpty();
  }

  @Test
  void shouldNotBeValidWithNullBookId() {
    BookCopyRequest request = new BookCopyRequest(null);

    Set<ConstraintViolation<BookCopyRequest>> violations = validator.validate(request);

    assertThat(violations).hasSize(1);
    assertThat(violations)
      .extracting(ConstraintViolation::getPropertyPath)
      .anyMatch(path -> path.toString().equals("bookId"));
  }
}
