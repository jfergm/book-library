package dev.fer.library.dto.response;

import dev.fer.library.enums.UserRole;

public record UserResponse(
  String email,
  UserRole role
) {}
