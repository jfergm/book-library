package dev.fer.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.fer.library.dto.response.BookResponse;
import dev.fer.library.entity.Author;
import dev.fer.library.entity.Book;
import dev.fer.library.exception.BookNotFoundException;
import dev.fer.library.mapper.BookMapper;
import dev.fer.library.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
  BookService bookService;

  @Mock
  BookRepository bookRepository;

  BookMapper bookMapper = new BookMapper();

  private List<Book> books = new ArrayList<>();

  @BeforeEach
  void setUp() {
    bookService = new BookService(bookRepository, bookMapper);

    books = new ArrayList<>();

    books.add(new Book(1L, "Eleanor & Park", "ISBN123", new Author(1L, null)));
    books.add(new Book(2L, "Kafka On The Shore", "ISBN456", new Author(2L, null)));
    books.add(new Book(3L, "Another Book", "ISBN789", new Author(3L, null)));
  }

  @Test
  void shouldReturnBook() {
    when(bookRepository.findById(1L)).thenReturn(Optional.of(books.getFirst()));

    BookResponse book = bookService.getBook(1L);

    assertThat(book.id()).isEqualTo(1L);
    assertThat(book.title()).isEqualTo("Eleanor & Park");
    assertThat(book.isbn()).isEqualTo("ISBN123");
    assertThat(book.authorId()).isEqualTo(1L);

    verify(bookRepository).findById(1L);
  }

  @Test
  void shouldThrowWhenInvalidBook() {
    when(bookRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(BookNotFoundException.class, () -> bookService.getBook(1L));

    verify(bookRepository).findById(1L);
  }

  @Test
  void shouldReturnBookList() {
    when(bookRepository.findAll()).thenReturn(books);

    List<BookResponse> books = bookService.getBooks();

    assertThat(books.size()).isEqualTo(3);

    verify(bookRepository).findAll();
  }

}
