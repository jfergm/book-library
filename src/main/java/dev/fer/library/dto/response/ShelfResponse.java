package dev.fer.library.dto.response;

public record ShelfResponse(
  Long id,
  String code,
  String label,
  Long bookcaseId
) {}
