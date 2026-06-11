package dev.fer.library.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ShelfRequest(
  @NotNull @Size(min = 2) String code,
  @NotNull @Size(min = 2) String label,
  @NotNull Long bookcaseId
) {}
