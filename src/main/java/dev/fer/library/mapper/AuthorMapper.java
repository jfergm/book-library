package dev.fer.library.mapper;

import org.springframework.stereotype.Component;

import dev.fer.library.dto.response.AuthorResponse;
import dev.fer.library.entity.Author;

@Component
public class AuthorMapper {

  public AuthorResponse toResponse(Author author) {
    return new AuthorResponse(author.getId(), author.getName());
  }
}
