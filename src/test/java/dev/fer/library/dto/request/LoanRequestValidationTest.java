package dev.fer.library.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class LoanRequestValidationTest {
  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void shouldBeValidWithAllFields() {
    LoanRequest request = new LoanRequest(1L, 1L, new Date(), new Date(), "notes");

    Set<ConstraintViolation<LoanRequest>> violations = validator.validate(request);

    assertThat(violations).isEmpty();
  }

  @Test
  void shouldNotBeValidWithNullUserId() {
    LoanRequest request = new LoanRequest(null, 1L, new Date(), new Date(), "notes");

    Set<ConstraintViolation<LoanRequest>> violations = validator.validate(request);

    assertThat(violations).hasSize(1);
    assertThat(violations)
      .extracting(ConstraintViolation::getPropertyPath)
      .anyMatch(path -> path.toString().equals("userId"));
  }

  @Test
  void shouldNotBeValidWithNullBookCopyId() {
    LoanRequest request = new LoanRequest(1L, null, new Date(), new Date(), "notes");

    Set<ConstraintViolation<LoanRequest>> violations = validator.validate(request);

    assertThat(violations).hasSize(1);
    assertThat(violations)
      .extracting(ConstraintViolation::getPropertyPath)
      .anyMatch(path -> path.toString().equals("bookCopyId"));
  }

  @Test
  void shouldNotBeValidWithNullLoanDate() {
    LoanRequest request = new LoanRequest(1L, 1L, null, new Date(), "notes");

    Set<ConstraintViolation<LoanRequest>> violations = validator.validate(request);

    assertThat(violations).hasSize(1);
    assertThat(violations)
      .extracting(ConstraintViolation::getPropertyPath)
      .anyMatch(path -> path.toString().equals("loanDate"));
  }

  @Test
  void shouldNotBeValidWithNullDueDate() {
    LoanRequest request = new LoanRequest(1L, 1L, new Date(), null, "notes");

    Set<ConstraintViolation<LoanRequest>> violations = validator.validate(request);

    assertThat(violations).hasSize(1);
    assertThat(violations)
      .extracting(ConstraintViolation::getPropertyPath)
      .anyMatch(path -> path.toString().equals("dueDate"));
  }
}
