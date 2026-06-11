package dev.fer.library.dto.request;

import dev.fer.library.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(
  @NotNull @Email String email,
  @NotNull @Size(min = 6) String password,
  @NotNull UserRole role
) {}
