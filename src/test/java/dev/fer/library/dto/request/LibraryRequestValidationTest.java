package dev.fer.library.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class LibraryRequestValidationTest {
  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void shouldBeValid() {
    LibraryRequest request = new LibraryRequest("Library name");

    Set<ConstraintViolation<LibraryRequest>> violations = validator.validate(request);

    assertThat(violations).isEmpty();
  }

  @Test
  void shouldBeValidWhenNameIsAtLeastSize2() {
    LibraryRequest request = new LibraryRequest("LB");

    Set<ConstraintViolation<LibraryRequest>> violations = validator.validate(request);

    assertThat(violations).isEmpty();
  }

  @Test
  void shouldNotBeValidWhenNullName() {
    LibraryRequest request = new LibraryRequest(null);

    Set<ConstraintViolation<LibraryRequest>> violations = validator.validate(request);

    assertThat(violations).hasSize(1);
    assertThat(violations)
      .extracting(ConstraintViolation::getPropertyPath)
      .anyMatch(path -> path.toString().equals("name"));
  }

  @Test
  void shouldNotBeValidWhenShortName() {
    LibraryRequest request = new LibraryRequest("n");

    Set<ConstraintViolation<LibraryRequest>> violations = validator.validate(request);

    assertThat(violations).hasSize(1);
    assertThat(violations)
      .extracting(ConstraintViolation::getPropertyPath)
      .anyMatch(path -> path.toString().equals("name"));
  }
}
