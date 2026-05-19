package dev.fer.library.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import dev.fer.library.dto.response.BookResponse;
import dev.fer.library.entity.Author;
import dev.fer.library.entity.Book;

public class BookMapperTest {
  BookMapper mapper = new BookMapper();

  @Test
  void shouldConvertToResponse() {
    Book book = new Book(1L, "Eleanor & Park", "ISBN123", new Author(1L, null));
    
    BookResponse mapped = mapper.toResponse(book);
    
    assertThat(mapped.id()).isEqualTo(1L);
    assertThat(mapped.title()).isEqualTo("Eleanor & Park");
    assertThat(mapped.isbn()).isEqualTo("ISBN123");
    assertThat(mapped.authorId()).isEqualTo(1L);

  }
}
