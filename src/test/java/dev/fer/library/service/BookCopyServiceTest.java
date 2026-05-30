package dev.fer.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.fer.library.dto.request.BookCopyRequest;
import dev.fer.library.dto.request.BookCopyUpdateRequest;
import dev.fer.library.dto.request.BookCopyUpdateShelfRequest;
import dev.fer.library.dto.response.BookCopyResponse;
import dev.fer.library.entity.Book;
import dev.fer.library.entity.BookCopy;
import dev.fer.library.entity.Shelf;
import dev.fer.library.enums.BookCopyStatus;
import dev.fer.library.exception.BadRequestException;
import dev.fer.library.exception.BookCopyNotFoundException;
import dev.fer.library.mapper.BookCopyMapper;
import dev.fer.library.repository.BookCopyRepository;
import dev.fer.library.repository.BookRepository;
import dev.fer.library.repository.ShelfRepository;

@ExtendWith(MockitoExtension.class)
class BookCopyServiceTest {
  
  @Mock
  BookCopyRepository bookCopyRepository;

  @Mock
  BookRepository bookRepository;

  @Mock
  ShelfRepository shelfRepository;

  BookCopyMapper bookCopyMapper = new BookCopyMapper();
  
  private BookCopyService bookCopyService;

  private BookCopy bookCopy = new BookCopy(
    1L,
    new Book(1L, null, null, null),
    new Shelf(1L, null, null, null),
    "BK123",
    BookCopyStatus.AVAILABLE
  );

  @BeforeEach
  void setUp() {
    bookCopyService = new BookCopyService(
      bookCopyRepository, bookCopyMapper, bookRepository, shelfRepository);
  }

  @Test
  void shouldReturnBookCopy() {
    when(bookCopyRepository.findById(1L)).thenReturn(
      Optional.of(bookCopy)
    );

    BookCopyResponse bookCopyResponse = bookCopyService.getBookCopy(1L);

    assertThat(bookCopyResponse.id()).isEqualTo(1L);
    assertThat(bookCopyResponse.bookId()).isEqualTo(1L);
    assertThat(bookCopyResponse.shelfId()).isEqualTo(1L);
    assertThat(bookCopyResponse.code()).isEqualTo("BK123");
    assertThat(bookCopyResponse.status()).isEqualTo("AVAILABLE");

    verify(bookCopyRepository).findById(1L);
  }

  @Test
  void shouldThrowWhenInvalidBookCopy() {
    when(bookCopyRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(BookCopyNotFoundException.class, () -> bookCopyService.getBookCopy(1L));

    verify(bookCopyRepository).findById(1L);
  }

  @Test
  void shouldCreateBookCopy() {
    when(bookCopyRepository.save(any())).thenReturn(
      new BookCopy(
        1L,
        new Book(1L, null, null, null),
        null,
        "",
        BookCopyStatus.PROCESSING
      )
    );
    when(bookRepository.findById(1L)).thenReturn(
      Optional.of(new Book(1L, null, null, null))
    );

    BookCopyRequest request = new BookCopyRequest(1L);
    BookCopyResponse response = bookCopyService.createBookCopy(request);

    assertThat(response.id()).isNotNull();
    assertThat(response.bookId()).isNotNull();
    assertThat(response.shelfId()).isNull();
    assertThat(response.code()).isEmpty();
    assertThat(response.status()).isEqualTo(BookCopyStatus.PROCESSING.name());
    
    verify(bookRepository).findById(1L);
    verify(bookCopyRepository).save(any());
  }

  @Test
  void shouldThrowWhenCreateBookCopyWithInvalidBook() {
    when(bookRepository.findById(1L)).thenReturn(Optional.empty());

    BookCopyRequest request = new BookCopyRequest(1L);

    assertThrows(BadRequestException.class, () -> bookCopyService.createBookCopy(request));
    
    verify(bookRepository).findById(1L);
    verify(bookCopyRepository, times(0)).save(any());
  }

  @Test
  void shouldUpdateBookCopy() {
    when(bookCopyRepository.findById(1L)).thenReturn(Optional.of(bookCopy));
    when(bookCopyRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
    
    BookCopyUpdateRequest request = new BookCopyUpdateRequest("NEWCODE");

    BookCopyResponse response = bookCopyService.updateBookCopy(1L, request);

    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.code()).isEqualTo("NEWCODE");

    verify(bookCopyRepository).findById(1L);
    verify(bookCopyRepository).save(any());

  }

  @Test
  void shouldThrowWhenUpdateInvalidBookCopy() {
    when(bookCopyRepository.findById(1L)).thenReturn(Optional.empty());
    
    BookCopyUpdateRequest request = new BookCopyUpdateRequest("NEWCODE");

    assertThrows(
      BookCopyNotFoundException.class, 
      () ->  bookCopyService.updateBookCopy(1L, request));

    verify(bookCopyRepository).findById(1L);
    verify(bookCopyRepository, times(0)).save(any());

  }

  @Test
  void shouldUpdateBookCopyShelf() {
    when(bookCopyRepository.findById(1L)).thenReturn(Optional.of(bookCopy));
    when(shelfRepository.findById(2L))
      .thenReturn(Optional.of(new Shelf(2L, null, null, null)));
    when(bookCopyRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
    
    BookCopyUpdateShelfRequest request = new BookCopyUpdateShelfRequest(2L);

    BookCopyResponse response = bookCopyService.updateBookCopyShelf(1L, request);

    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.shelfId()).isEqualTo(2L);

    verify(bookCopyRepository).findById(1L);
    verify(shelfRepository).findById(2L);
    verify(bookCopyRepository).save(any());
  }

  @Test
  void shouldUpdateBookCopyShelfWithNullValue() {
    when(bookCopyRepository.findById(1L)).thenReturn(Optional.of(bookCopy));
    when(bookCopyRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
    
    BookCopyUpdateShelfRequest request = new BookCopyUpdateShelfRequest(null);

    BookCopyResponse response = bookCopyService.updateBookCopyShelf(1L, request);

    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.shelfId()).isNull();

    verify(bookCopyRepository).findById(1L);
    verify(shelfRepository, times(0)).findById(anyLong());
    verify(bookCopyRepository).save(any());
  }

  @Test
  void shouldThrowWhenUpdateBookCopyShelfWithInvalidBookCopy() {
    when(bookCopyRepository.findById(1L)).thenReturn(Optional.empty());
    
    BookCopyUpdateShelfRequest request = new BookCopyUpdateShelfRequest(null);

    assertThrows(
      BookCopyNotFoundException.class, 
      () -> bookCopyService.updateBookCopyShelf(1L, request));

    verify(bookCopyRepository).findById(1L);
    verify(shelfRepository, times(0)).findById(any());
    verify(bookCopyRepository, times(0)).save(any());
  }

  @Test
  void shouldThrowWhenUpdateBookCopyShelfWithInvalidShelf() {
    when(bookCopyRepository.findById(1L)).thenReturn(Optional.of(bookCopy));
    when(shelfRepository.findById(anyLong())).thenReturn(Optional.empty());
    
    BookCopyUpdateShelfRequest request = new BookCopyUpdateShelfRequest(2L);

    assertThrows(
      BadRequestException.class, 
      () -> bookCopyService.updateBookCopyShelf(1L, request));

    verify(bookCopyRepository).findById(1L);
    verify(shelfRepository).findById(anyLong());
    verify(bookCopyRepository, times(0)).save(any());
  }

  @Test
  void shouldDeleteBookCopy() {
    when(bookCopyRepository.existsById(1L)).thenReturn(true);

    bookCopyService.deleteBookCopy(1L);

    verify(bookCopyRepository).existsById(1L);
    verify(bookCopyRepository).deleteById(1L);
  }

  @Test
  void shouldThrowWhenDeleteBookCopyWithInvalidBookCopy() {
    when(bookCopyRepository.existsById(1L)).thenReturn(false);

    assertThrows(
      BookCopyNotFoundException.class, 
      () -> bookCopyService.deleteBookCopy(1L));

    verify(bookCopyRepository).existsById(1L);
    verify(bookCopyRepository, times(0)).deleteById(1L);
  }

  @Test
  void shouldReturnBookCopyEntity() {
    when(bookCopyRepository.findById(1L)).thenReturn(
      Optional.of(bookCopy)
    );

    Optional<BookCopy> entity = bookCopyService.getEntity(1L);
    assertThat(entity)
      .isPresent()
      .contains(bookCopy);

    verify(bookCopyRepository).findById(1L);
  }

  @Test
  void shouldReturnEmptyBookCopyEntity() {
    when(bookCopyRepository.findById(1L)).thenReturn(Optional.empty());

    Optional<BookCopy> entity = bookCopyService.getEntity(1L);
    assertThat(entity).isEmpty();

    verify(bookCopyRepository).findById(1L);
  }
}
