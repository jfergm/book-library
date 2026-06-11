package dev.fer.library.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class FloorRequestValidationTest {
  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void shouldBeValidWithAllValues() {
    FloorRequest request = new FloorRequest(1L, "CODE1", "Description");

    Set<ConstraintViolation<FloorRequest>> violations = validator.validate(request);

    assertThat(violations).isEmpty();
  }

  @Test
  void shouldBeValidWithEmptyDescription() {
    FloorRequest request = new FloorRequest(1L, "CODE1", "");

    Set<ConstraintViolation<FloorRequest>> violations = validator.validate(request);

    assertThat(violations).isEmpty();
  }

  @Test
  void shouldNotBeValidWithNullLibraryId() {
    FloorRequest request = new FloorRequest(null, "CODE1", "");

    Set<ConstraintViolation<FloorRequest>> violations = validator.validate(request);

    assertThat(violations).hasSize(1);

    assertThat(violations)
      .extracting(ConstraintViolation::getPropertyPath)
      .anyMatch(path -> path.toString().equals("libraryId"));
  }

  @Test
  void shouldNotBeValidWithNullCode() {
    FloorRequest request = new FloorRequest(1L, null, "");

    Set<ConstraintViolation<FloorRequest>> violations = validator.validate(request);

    assertThat(violations).hasSize(1);

    assertThat(violations)
      .extracting(ConstraintViolation::getPropertyPath)
      .anyMatch(path -> path.toString().equals("code"));
  }

  @Test
  void shouldNotBeValidWithShortCode() {
    FloorRequest request = new FloorRequest(1L, "c", "");

    Set<ConstraintViolation<FloorRequest>> violations = validator.validate(request);

    assertThat(violations).hasSize(1);

    assertThat(violations)
      .extracting(ConstraintViolation::getPropertyPath)
      .anyMatch(path -> path.toString().equals("code"));
  }
}
