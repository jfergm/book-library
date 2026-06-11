package dev.fer.library.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AuthorRequest (@NotNull @Size(min = 2) String name) {}
