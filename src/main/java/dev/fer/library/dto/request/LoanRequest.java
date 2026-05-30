package dev.fer.library.dto.request;

import java.util.Date;

public record LoanRequest(
  Long userId,
  Long bookCopyId,
  Date loanDate,
  Date dueDate,
  String notes
) {}
