package dev.fer.library.dto.request;

import java.util.Date;

import jakarta.validation.constraints.NotNull;

public record LoanRequest(
  @NotNull Long userId,
  @NotNull Long bookCopyId,
  @NotNull Date loanDate,
  @NotNull Date dueDate,
  String notes
) {}
