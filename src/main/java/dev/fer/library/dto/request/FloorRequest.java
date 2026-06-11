package dev.fer.library.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FloorRequest(
  @NotNull Long libraryId,
  @NotNull @Size(min = 2) String code,
  String description
) {}
