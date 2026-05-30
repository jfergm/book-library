package dev.fer.library.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.fer.library.dto.request.AuthorRequest;
import dev.fer.library.dto.response.AuthorResponse;
import dev.fer.library.entity.Author;

@Component
public class AuthorMapper {

  public AuthorResponse toResponse(Author author) {
    return new AuthorResponse(author.getId(), author.getName());
  }

  public List<AuthorResponse> toResponseList(List<Author> authors) {
    return authors.stream().map(this::toResponse).toList();
  }

  public Author toEntity(AuthorRequest request) {
    return new Author(null, request.name());
  }
}
