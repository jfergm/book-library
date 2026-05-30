package dev.fer.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
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

import dev.fer.library.dto.request.BookRequest;
import dev.fer.library.dto.response.BookResponse;
import dev.fer.library.entity.Author;
import dev.fer.library.entity.Book;
import dev.fer.library.exception.BadRequestException;
import dev.fer.library.exception.BookNotFoundException;
import dev.fer.library.mapper.BookMapper;
import dev.fer.library.repository.AuthorRepository;
import dev.fer.library.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
  BookService bookService;

  @Mock
  BookRepository bookRepository;

  @Mock
  AuthorRepository authorRepository;

  BookMapper bookMapper = new BookMapper();

  private List<Book> books = new ArrayList<>();

  @BeforeEach
  void setUp() {
    bookService = new BookService(bookRepository, bookMapper, authorRepository);

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

    List<BookResponse> booksResponse = bookService.getBooks();

    assertThat(booksResponse).hasSize(3);

    verify(bookRepository).findAll();
  }

  @Test
  void shouldCreateBoook() {
    when(bookRepository.save(any())).thenReturn(books.getFirst());
    when(authorRepository.findById(1L)).thenReturn(Optional.of(new Author(1L, null)));

    BookRequest request = new BookRequest("Eleanor & Park", "ISBN123", 1L);

    BookResponse created = bookService.createBook(request);

    assertThat(created.id()).isNotNull();
    assertThat(created.title()).isEqualTo(request.title());
    assertThat(created.isbn()).isEqualTo(request.isbn());
    assertThat(created.authorId()).isEqualTo(request.authorId());

    verify(bookRepository).save(any());
    verify(authorRepository).findById(1L);
  }

  @Test
  void shouldThrowWhenCreateBoookWithInvalidAuthor() {
    when(authorRepository.findById(1L)).thenReturn(Optional.empty());

    BookRequest request = new BookRequest("Eleanor & Park", "ISBN123", 1L);

    assertThrows(BadRequestException.class, () -> bookService.createBook(request));

    verify(bookRepository, times(0)).save(any());
    verify(authorRepository).findById(1L);
  }

  @Test
  void shouldUpdateBook() {
    when(bookRepository.findById(1L)).thenReturn(Optional.of(books.getFirst()));
    when(bookRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

    BookRequest request = new BookRequest("New title", "NEWISBN", 1L);

    BookResponse response = bookService.updateBook(1L, request);

    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.title()).isEqualTo("New title");
    assertThat(response.isbn()).isEqualTo("NEWISBN");
    assertThat(response.authorId()).isEqualTo(1L);

    verify(bookRepository).save(any());
    verify(bookRepository).findById(anyLong());
  }

  @Test
  void shouldUpdateBookWithDifferentAuthor() {
    when(bookRepository.findById(1L)).thenReturn(Optional.of(books.getFirst()));
    when(authorRepository.findById(2L)).thenReturn(
      Optional.of(new Author(2L, null))
    );
    when(bookRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

    BookRequest request = new BookRequest("New title", "NEWISBN", 2L);

    BookResponse response = bookService.updateBook(1L, request);

    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.title()).isEqualTo("New title");
    assertThat(response.isbn()).isEqualTo("NEWISBN");
    assertThat(response.authorId()).isEqualTo(2L);

    verify(bookRepository).save(any());
    verify(bookRepository).findById(anyLong());
    verify(authorRepository).findById(anyLong());
  }

  @Test
  void shouldThrowNotFoundWhenUpdateInvalidBook() {
    when(bookRepository.findById(1L)).thenReturn(Optional.empty());

    BookRequest request = new BookRequest("New title", "NEWISBN", 1L);

    assertThrows(BookNotFoundException.class, () ->  bookService.updateBook(1L, request));

    verify(bookRepository, times(0)).save(any());
    verify(bookRepository).findById(anyLong());
    verify(authorRepository, times(0)).findById(anyLong());
  }

  @Test
  void shouldThrowNotFoundWhenUpdateBookWithInvalidAuthor() {
    when(bookRepository.findById(1L)).thenReturn(Optional.of(books.getFirst()));
    when(authorRepository.findById(2L)).thenReturn(Optional.empty());

    BookRequest request = new BookRequest("New title", "NEWISBN", 2L);

    assertThrows(BadRequestException.class, () ->  bookService.updateBook(1L, request));

    verify(bookRepository, times(0)).save(any());
    verify(bookRepository).findById(anyLong());
    verify(authorRepository).findById(anyLong());
  }

  @Test
  void shouldDeleteBook() {
    when(bookRepository.existsById(1L)).thenReturn(true);
    doNothing().when(bookRepository).deleteById(anyLong());

    bookService.deleteBook(1L);
    
    verify(bookRepository).existsById(1L);
    verify(bookRepository).deleteById(1L);
  }

  @Test
  void shouldThrowWhenDeleteInvalidBook() {
    when(bookRepository.existsById(1L)).thenReturn(false);

   assertThrows(BookNotFoundException.class, () ->  bookService.deleteBook(1L));
    
    verify(bookRepository).existsById(1L);
    verify(bookRepository, times(0)).deleteById(1L);
  }

}
