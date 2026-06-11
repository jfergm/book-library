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

class BookRquestValidationTest {
  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void shouldBeValidWithAllValues() {
    BookRequest request = new BookRequest("title", "isbn", 1L);

    Set<ConstraintViolation<BookRequest>> violations = validator.validate(request);

    assertThat(violations).isEmpty();
  }

  @ParameterizedTest
  @MethodSource("invalidRequests")
  void shouldNotBeValidWithInvalidFields(String title, String isbn, Long authorId, String expectedProperty) {
    BookRequest request = new BookRequest(title, isbn, authorId);

    Set<ConstraintViolation<BookRequest>> violations = validator.validate(request);

    assertThat(violations).hasSize(1);
    assertThat(violations)
      .extracting(ConstraintViolation::getPropertyPath)
      .anyMatch(path -> path.toString().equals(expectedProperty));
  } 

  private static Stream<Arguments> invalidRequests() {
    return Stream.of(
      Arguments.of(null,     "isbn",  1L,   "title"),
      Arguments.of("t",      "isbn",  1L,   "title"),
      Arguments.of("title",  null,    1L,   "isbn"),
      Arguments.of("title",  "i",     1L,   "isbn"),
      Arguments.of("title",  "isbn",  null, "authorId")
    );
  }
}
