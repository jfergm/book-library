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

class SectionUpdateRequestValidationTest {
  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void shouldBeValidWithAllValues() {
    SectionUpdateRequest request = new SectionUpdateRequest(1L, "code", "label", "desc");

    Set<ConstraintViolation<SectionUpdateRequest>> violations = validator.validate(request);

    assertThat(violations).isEmpty();
  }

  @ParameterizedTest
  @MethodSource("invalidRequests")
  void shouldNotBeValidWithInvalidFields(Long floorId, String code, String label, String expectedProperty) {
    SectionUpdateRequest request = new SectionUpdateRequest(floorId, code, label, "desc");

    Set<ConstraintViolation<SectionUpdateRequest>> violations = validator.validate(request);

    assertThat(violations).hasSize(1);
    assertThat(violations)
      .extracting(ConstraintViolation::getPropertyPath)
      .anyMatch(path -> path.toString().equals(expectedProperty));
  }

  private static Stream<Arguments> invalidRequests() {
    return Stream.of(
      Arguments.of(null,  "code",  "label",  "floorId"),
      Arguments.of(1L,    null,    "label",  "code"),
      Arguments.of(1L,    "c",     "label",  "code"),
      Arguments.of(1L,    "code",  null,     "label"),
      Arguments.of(1L,    "code",  "l",      "label")
    );
  }
}
