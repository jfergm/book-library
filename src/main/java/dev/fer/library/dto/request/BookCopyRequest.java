package dev.fer.library.dto.request;

import jakarta.validation.constraints.NotNull;

public record BookCopyRequest (@NotNull Long bookId) {}
