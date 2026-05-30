package dev.fer.library.dto.response;

public record FloorResponse(
  Long id,
  Long libraryId,
  String code,
  String description
) {}
