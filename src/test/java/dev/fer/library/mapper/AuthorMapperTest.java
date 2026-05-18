package dev.fer.library.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import dev.fer.library.dto.response.AuthorResponse;
import dev.fer.library.entity.Author;

public class AuthorMapperTest {
  AuthorMapper mapper = new AuthorMapper();

  @Test
  void shouldConvertEntityToResponse() {
    Author author = new Author(1L, "Rainbow Rowell");

    AuthorResponse response = mapper.toResponse(author);

    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.name()).isEqualTo("Rainbow Rowell");
  }
}
