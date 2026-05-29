package dev.fer.library.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.fer.library.dto.request.AuthorRequest;
import dev.fer.library.dto.response.AuthorResponse;
import dev.fer.library.entity.Author;

class AuthorMapperTest {
  AuthorMapper mapper = new AuthorMapper();

  private List<Author> authors;
  @BeforeEach
  void setUp() {
    authors = new ArrayList<>();

    authors.add(new Author(1L, "Rainbow Rowell"));
    authors.add(new Author(2L, "Haruki Murakami"));
    authors.add(new Author(3L, "Julio Cortazar"));
  }

  @Test
  void shouldConvertEntityToResponse() {
    Author author = authors.getFirst();

    AuthorResponse response = mapper.toResponse(author);

    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.name()).isEqualTo("Rainbow Rowell");
  }

  @Test
  void shouldConvertToResponseList() {
    List<AuthorResponse> responseList = mapper.toResponseList(authors);

    assertThat(responseList).hasSize(3);
    assertThat(responseList.get(0).id()).isEqualTo(1L);
    assertThat(responseList.get(1).id()).isEqualTo(2L);
    assertThat(responseList.get(2).id()).isEqualTo(3L);
  }

  @Test
  void shouldConvertRequestToEntity() {
    AuthorRequest request = new AuthorRequest("Author name");

    Author author = mapper.toEntity(request);

    assertThat(author.getId()).isNull();
    assertThat(author.getName()).isEqualTo("Author name");
  }
}
