package dev.fer.library.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class AuthorRequestValidationTest {
  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void shouldBeValidWithAllValues() {
    AuthorRequest request = new AuthorRequest("Author");

    Set<ConstraintViolation<AuthorRequest>> violations = validator.validate(request);

    assertThat(violations).isEmpty();
  }

  @Test
  void shouldBeValidWithNullAuthor() {
    AuthorRequest request = new AuthorRequest(null);

    Set<ConstraintViolation<AuthorRequest>> violations = validator.validate(request);

    assertThat(violations).hasSize(1);
    assertThat(violations)
      .extracting(ConstraintViolation::getPropertyPath)
      .anyMatch(path -> path.toString().equals("name"));
  }

  @Test
  void shouldBeValidWithShortAuthor() {
    AuthorRequest request = new AuthorRequest("a");

    Set<ConstraintViolation<AuthorRequest>> violations = validator.validate(request);

    assertThat(violations).hasSize(1);
    assertThat(violations)
      .extracting(ConstraintViolation::getPropertyPath)
      .anyMatch(path -> path.toString().equals("name"));
  }
}
