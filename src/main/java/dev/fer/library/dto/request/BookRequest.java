package dev.fer.library.dto.request;

public record BookRequest(
  String title,
  String isbn,
  Long authorId
) {}
