package dev.fer.library.dto.request;

public record SectionUpdateRequest(
  Long floorId,
  String code,
  String label,
  String description
) {}
