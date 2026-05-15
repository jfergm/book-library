package dev.fer.library.dto.request;

public record BookcaseRequest(
  Long sectionId,
  String code,
  String label
) {}