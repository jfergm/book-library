package dev.fer.library.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class FloorUpdateRequestValidationTest {
  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void shoudlBeValid() {
    FloorUpdateRequest request = new FloorUpdateRequest("123", "desc");

    Set<ConstraintViolation<FloorUpdateRequest>> violations = validator.validate(request);

    assertThat(violations).isEmpty();
  }

  @Test
  void shoudlNotBeValidWithNullCode() {
    FloorUpdateRequest request = new FloorUpdateRequest(null, "desc");

    Set<ConstraintViolation<FloorUpdateRequest>> violations = validator.validate(request);

    assertThat(violations).hasSize(1);
    assertThat(violations)
      .extracting(ConstraintViolation::getPropertyPath)
      .anyMatch(path -> path.toString().equals("code"));
  }

  @Test
  void shoudlNotBeValidWithShortCode() {
    FloorUpdateRequest request = new FloorUpdateRequest("c", "desc");

    Set<ConstraintViolation<FloorUpdateRequest>> violations = validator.validate(request);

    assertThat(violations).hasSize(1);
    assertThat(violations)
      .extracting(ConstraintViolation::getPropertyPath)
      .anyMatch(path -> path.toString().equals("code"));
  }
}
