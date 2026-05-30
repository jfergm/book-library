package dev.fer.library.dto.request;

public record SectionRequest(
  Long floorId,
  String code,
  String label,
  String description
) {}
