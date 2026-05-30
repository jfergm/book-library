package dev.fer.library.dto.response;

public record SectionResponse(
  Long id,
  Long floorId,
  String code,
  String label,
  String description
) {}
