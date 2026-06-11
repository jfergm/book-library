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

class BookcaseRequestValidationTest {
  private static Validator validator;

  @BeforeAll
  static void setUpdate() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    validator = factory.getValidator();
  }

  @Test
  void shouldBeValidWithAllValues() {
    BookcaseRequest request = new BookcaseRequest(1L, "code", "label");

    Set<ConstraintViolation<BookcaseRequest>> violations = validator.validate(request);

    assertThat(violations).isEmpty();
  }

  @ParameterizedTest
  @MethodSource("invalidRequests")
  void shouldNotBeValidWithInvalidFields(
    Long sectionId, 
    String code, 
    String label, 
    String expectedProperty
  ) {
    BookcaseRequest request = new BookcaseRequest(sectionId, code, label);

    Set<ConstraintViolation<BookcaseRequest>> violations = validator.validate(request);

    assertThat(violations).hasSize(1);
    assertThat(violations)
      .extracting(ConstraintViolation::getPropertyPath)
      .anyMatch(path -> path.toString().equals(expectedProperty));
  }

  private static Stream<Arguments> invalidRequests() {
    return Stream.of(
      Arguments.of(null, "code",  "label",  "sectionId"),
      Arguments.of(1L,   null,    "label",  "code"),
      Arguments.of(1L,   "c",     "label",  "code"),
      Arguments.of(1L,   "code",  null,     "label"),
      Arguments.of(1L,   "code",  "l",      "label")
    );
  }
}
