package dev.fer.library.dto.response;

import java.util.Date;

import dev.fer.library.enums.LoanStatus;

public record LoanResponse(
  Long id,
  Long userId,
  Long bookCopyId,
  Date loanDate,
  Date dueDate,
  Date returnDate,
  LoanStatus status,
  String notes
) {}
