package dev.fer.library.dto.response;

public record BookResponse(
  Long id,
  String title,
  String isbn,
  Long authorId
) {}
