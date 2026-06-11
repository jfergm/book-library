package dev.fer.library.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BookcaseRequest(
  @NotNull Long sectionId,
  @NotNull @Size(min = 2)  String code,
  @NotNull @Size(min = 2) String label
) {}