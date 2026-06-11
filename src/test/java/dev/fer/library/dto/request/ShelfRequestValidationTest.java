package dev.fer.library.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class ShelfRequestValidationTest {
  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void shouldBeValidWithAllValues() {
    ShelfRequest request = new ShelfRequest("code", "label", 1L);

    Set<ConstraintViolation<ShelfRequest>> violations = validator.validate(request);

    assertThat(violations).isEmpty();
  }

  @ParameterizedTest
  @MethodSource("invalidRequests")
  void shouldNotBeValidWithInvalidValues(String code, String label, Long bookcaseId, String expectedPorperty) {
    ShelfRequest request = new ShelfRequest(code, label, bookcaseId);

    Set<ConstraintViolation<ShelfRequest>> violations = validator.validate(request);

    assertThat(violations).hasSize(1);
    assertThat(violations)
      .extracting(ConstraintViolation::getPropertyPath)
      .anyMatch(path -> path.toString().equals(expectedPorperty));
  }

  private static Stream<Arguments> invalidRequests() {
    return Stream.of(
      Arguments.of(null,    "label",  1L,    "code"),
      Arguments.of("c",     "label",  1L,    "code"),
      Arguments.of("code",  null,     1L,    "label"),
      Arguments.of("code",  "l",      1L,    "label"),
      Arguments.of("code",  "label",  null,  "bookcaseId")
    );
  }
}
