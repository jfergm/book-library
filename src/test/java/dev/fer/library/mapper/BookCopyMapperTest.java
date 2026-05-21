package dev.fer.library.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import dev.fer.library.dto.response.BookCopyResponse;
import dev.fer.library.entity.Book;
import dev.fer.library.entity.BookCopy;
import dev.fer.library.entity.Shelf;
import dev.fer.library.enums.BookCopyStatus;

public class BookCopyMapperTest {
  BookCopyMapper mapper = new BookCopyMapper();

  @Test
  void shouldConvertToResponse() {
    BookCopy bookCopy = new BookCopy(
      1L, 
      new Book(1L, null, null, null),
      new Shelf(1L, null, null, null), 
      "BK123", 
      BookCopyStatus.AVAILABLE
    );

    BookCopyResponse response = mapper.toResponse(bookCopy);

    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.bookId()).isEqualTo(1L);
    assertThat(response.shelfId()).isEqualTo(1L);
    assertThat(response.code()).isEqualTo("BK123");
    assertThat(response.status()).isEqualTo(BookCopyStatus.AVAILABLE.name());
  }
}
