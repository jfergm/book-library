package dev.fer.library.dto.request;

import dev.fer.library.enums.UserRole;

public record UserRequest(
  String email,
  String password,
  UserRole role
) {}
