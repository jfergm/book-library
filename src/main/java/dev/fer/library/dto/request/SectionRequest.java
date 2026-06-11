package dev.fer.library.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SectionRequest(
  @NotNull Long floorId,
  @NotNull @Size(min = 2) String code,
  @NotNull @Size(min = 2) String label,
  String description
) {}
