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

class LoginRequestValidationTest {
  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void shouldBeValidWithAllValues() {
    LoginRequest request = new LoginRequest("email@email.com", "password123");

    Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

    assertThat(violations).isEmpty();
  }

  @ParameterizedTest
  @MethodSource("invalidRequests")
  void shouldNotBeValidWithInvalidFields(String email, String password, String expectedProperty) {
    LoginRequest request = new LoginRequest(email, password);

    Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

    assertThat(violations).hasSize(1);
    assertThat(violations)
      .extracting(ConstraintViolation::getPropertyPath)
      .anyMatch(path -> path.toString().equals(expectedProperty));
  }

  private static Stream<Arguments> invalidRequests() {
    return Stream.of(
      Arguments.of(null,               "password123",  "email"),
      Arguments.of("notemail",         "password123",  "email"),
      Arguments.of("email@email.com",  null,           "password"),
      Arguments.of("email@email.com",  "short",        "password")
    );
  }
}
