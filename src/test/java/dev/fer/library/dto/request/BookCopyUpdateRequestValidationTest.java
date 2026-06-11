package dev.fer.library.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class BookCopyUpdateRequestValidationTest {
  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void shouldBeValidWithAllValues() {
    BookCopyUpdateRequest request = new BookCopyUpdateRequest("code");

    Set<ConstraintViolation<BookCopyUpdateRequest>> violations = validator.validate(request);

    assertThat(violations).isEmpty();
  }

  @Test
  void shouldNotBeValidWithNullCode() {
    BookCopyUpdateRequest request = new BookCopyUpdateRequest(null);

    Set<ConstraintViolation<BookCopyUpdateRequest>> violations = validator.validate(request);

    assertThat(violations).hasSize(1);
    assertThat(violations)
      .extracting(ConstraintViolation::getPropertyPath)
      .anyMatch(path -> path.toString().equals("code"));
  }

  @Test
  void shouldNotBeValidWithShortCode() {
    BookCopyUpdateRequest request = new BookCopyUpdateRequest("c");

    Set<ConstraintViolation<BookCopyUpdateRequest>> violations = validator.validate(request);

    assertThat(violations).hasSize(1);
    assertThat(violations)
      .extracting(ConstraintViolation::getPropertyPath)
      .anyMatch(path -> path.toString().equals("code"));
  }
}
