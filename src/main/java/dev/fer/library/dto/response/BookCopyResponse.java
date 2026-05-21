package dev.fer.library.dto.response;

public record BookCopyResponse(
  Long id,
  Long bookId,
  Long shelfId,
  String code,
  String status
) {}
