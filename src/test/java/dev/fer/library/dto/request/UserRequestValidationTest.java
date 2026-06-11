package dev.fer.library.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import dev.fer.library.enums.UserRole;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class UserRequestValidationTest {
  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void shouldBeValidWithAllValues() {
    UserRequest request = new UserRequest("email@example.com", "password123", UserRole.USER);

    Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);

    assertThat(violations).isEmpty();
  }

  @ParameterizedTest
  @MethodSource("invalidRequests")
  void shouldNotBeValidWithInvalidValues(String email, String password, UserRole role, String expectedProperty) {
    UserRequest request = new UserRequest(email, password, role);

    Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);

    assertThat(violations).hasSize(1);
    assertThat(violations)
      .extracting(ConstraintViolation::getPropertyPath)
      .anyMatch(path -> path.toString().equals(expectedProperty));
  }

  private static Stream<Arguments> invalidRequests() {
    return Stream.of(
      Arguments.of(null,               "password123",  UserRole.USER,  "email"),
      Arguments.of("notemail",         "password123",  UserRole.USER,  "email"),
      Arguments.of("email@email.com",  null,           UserRole.USER,  "password"),
      Arguments.of("email@email.com",  "short",        UserRole.USER,  "password"),
      Arguments.of("email@email.com",  "password123",  null,           "role")
    );
  }
}
