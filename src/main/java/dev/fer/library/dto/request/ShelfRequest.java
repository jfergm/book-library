package dev.fer.library.dto.request;

public record ShelfRequest(
  String code,
  String label,
  Long bookcaseId
) {}
