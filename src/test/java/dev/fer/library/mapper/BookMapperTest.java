package dev.fer.library.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import dev.fer.library.dto.request.BookRequest;
import dev.fer.library.dto.response.BookResponse;
import dev.fer.library.entity.Author;
import dev.fer.library.entity.Book;
import dev.fer.library.exception.BadRequestException;

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

  @Test
  void shoouldConvertToResponseList() {
    List<Book> books = new ArrayList<>();

    books.add(new Book(1L, "Eleanor & Park", "ISBN123", new Author(1L, null)));
    books.add(new Book(2L, "Kafka On The Shore", "ISBN456", new Author(2L, null)));
    books.add(new Book(3L, "Another Book", "ISBN789", new Author(3L, null)));

    List<BookResponse> responseList = mapper.toResponseList(books);

    assertThat(responseList.get(0).title()).isEqualTo("Eleanor & Park");
    assertThat(responseList.get(1).title()).isEqualTo("Kafka On The Shore");
    assertThat(responseList.get(2).title()).isEqualTo("Another Book");
  }

  @Test
  void shouldConvertToEntity() {
    BookRequest bookRequest = new BookRequest("Book", "ISBN", 1L);
    Author author = new Author(1L, null);

    Book book = mapper.toEntity(bookRequest, author);

    assertThat(book.getTitle()).isEqualTo("Book");
    assertThat(book.getIsbn()).isEqualTo("ISBN");
    assertThat(book.getAuthor().getId()).isEqualTo(1L);
  }

  @Test
  void shouldThrowWhenConvertToEntityWithInvalidAuthor() {
    BookRequest bookRequest = new BookRequest("Book", "ISBN", 1L);
    Author author = new Author(2L, null);

    assertThrows(BadRequestException.class, () -> mapper.toEntity(bookRequest, author));
  }
}
