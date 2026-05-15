package dev.fer.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.fer.library.dto.response.BookcaseResponse;
import dev.fer.library.entity.Bookcase;
import dev.fer.library.entity.Section;
import dev.fer.library.exception.BookcaseNotFoundException;
import dev.fer.library.repository.BookcaseRepository;

@ExtendWith(MockitoExtension.class)
public class BookcaseServiceTest {
  @Mock
  BookcaseRepository bookcaseRepository;
  
  @InjectMocks
  private BookcaseService bookcaseService;
  
  @Test
  void shouldReturnBookcase() {
    when(bookcaseRepository.findById(1L)).thenReturn(
      Optional.of(new Bookcase(
        1L,
        "A1",
        "Bookcase A1",
        new Section(1L, null, null, null, null)
      ))
    );

    BookcaseResponse bookcase = bookcaseService.getBookcase(1L);

    assertThat(bookcase.id()).isEqualTo(1L);
    assertThat(bookcase.code()).isEqualTo("A1");
    assertThat(bookcase.label()).isEqualTo("Bookcase A1");
    assertThat(bookcase.sectionId()).isEqualTo(1L);

    verify(bookcaseRepository).findById(1L);
  }

  @Test
  void shouldThrowBookcaseNotFoundWhenInvalidId() {
    when(bookcaseRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(BookcaseNotFoundException.class, () -> bookcaseService.getBookcase(1L));

    verify(bookcaseRepository).findById(1L);
  }
}
