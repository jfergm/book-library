package dev.fer.library.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import dev.fer.library.dto.response.BookcaseResponse;
import dev.fer.library.entity.Bookcase;
import dev.fer.library.entity.Section;

public class BookcaseMapperTest {
  private BookcaseMapper mapper = new BookcaseMapper();

  @Test
  void shouldconvertEntityToResponse() {
    Bookcase bookcase = new Bookcase(
      1L, 
      "A1", 
      "Bookcase A1", 
      new Section(1L, null, null, null, null)
    );

    BookcaseResponse response = mapper.toResponse(bookcase);

    assertThat(response.id()).isEqualTo(bookcase.getId());
  }
}
