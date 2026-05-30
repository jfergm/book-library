package dev.fer.library.dto.response;

public record BookcaseResponse(
  Long id,
  String code,
  String label,
  Long sectionId
) {}
