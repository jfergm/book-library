package dev.fer.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.fer.library.dto.response.BookCopyResponse;
import dev.fer.library.entity.Book;
import dev.fer.library.entity.BookCopy;
import dev.fer.library.entity.Shelf;
import dev.fer.library.enums.BookCopyStatus;
import dev.fer.library.exception.BookCopyNotFoundException;
import dev.fer.library.mapper.BookCopyMapper;
import dev.fer.library.repository.BookCopyRepository;

@ExtendWith(MockitoExtension.class)
public class BookCopyServiceTest {
  
  @Mock
  BookCopyRepository bookCopyRepository;

  BookCopyMapper bookCopyMapper = new BookCopyMapper();
  
  private BookCopyService bookCopyService;

  @BeforeEach
  void setUp() {
    bookCopyService = new BookCopyService(bookCopyRepository, bookCopyMapper);
  }

  @Test
  void shouldReturnBookCopy() {
    when(bookCopyRepository.findById(1L)).thenReturn(
      Optional.of(new BookCopy(
        1L,
        new Book(1L, null, null, null),
        new Shelf(1L, null, null, null),
        "BK123",
        BookCopyStatus.AVAILABLE
      ))
    );

    BookCopyResponse bookCopy = bookCopyService.getBookCopy(1L);

    assertThat(bookCopy.id()).isEqualTo(1L);
    assertThat(bookCopy.bookId()).isEqualTo(1L);
    assertThat(bookCopy.shelfId()).isEqualTo(1L);
    assertThat(bookCopy.code()).isEqualTo("BK123");
    assertThat(bookCopy.status()).isEqualTo("AVAILABLE");

    verify(bookCopyRepository).findById(1L);
  }

  @Test
  void shouldThrowWhenInvalidBookCopy() {
    when(bookCopyRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(BookCopyNotFoundException.class, () -> bookCopyService.getBookCopy(1L));

    verify(bookCopyRepository).findById(1L);
  }
}
