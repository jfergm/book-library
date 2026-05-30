package dev.fer.library.dto.request;

public record FloorRequest(
  Long libraryId,
  String code,
  String description
) {}
