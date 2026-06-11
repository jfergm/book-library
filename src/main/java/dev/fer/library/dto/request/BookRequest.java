package dev.fer.library.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BookRequest(
  @NotNull @Size(min = 2) String title,
  @NotNull @Size(min = 2) String isbn,
  @NotNull Long authorId
) {}
