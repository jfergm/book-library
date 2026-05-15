package dev.fer.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

import dev.fer.library.dto.request.BookcaseRequest;
import dev.fer.library.dto.response.BookcaseResponse;
import dev.fer.library.entity.Bookcase;
import dev.fer.library.entity.Section;
import dev.fer.library.exception.BadRequestException;
import dev.fer.library.exception.BookcaseNotFoundException;
import dev.fer.library.mapper.BookcaseMapper;
import dev.fer.library.repository.BookcaseRepository;
import dev.fer.library.repository.SectionRepository;

@ExtendWith(MockitoExtension.class)
public class BookcaseServiceTest {
  @Mock
  BookcaseRepository bookcaseRepository;

  @Mock
  SectionRepository sectionRepository;

  private BookcaseMapper bookcaseMapper = new BookcaseMapper();
  
  private BookcaseService bookcaseService;

  List<Bookcase> bookcases;

  @BeforeEach
  void setUp() {
    bookcaseService = new BookcaseService(bookcaseRepository, bookcaseMapper, sectionRepository);

    bookcases = new ArrayList<>();

    Section section = new Section(1L, null, null, null, null);
    bookcases.add(new Bookcase(1L, "1A", "Bookcase 1A", section));
    bookcases.add(new Bookcase(2L, "2A", "Bookcase 2A", section));
    bookcases.add(new Bookcase(3L, "3A", "Bookcase 3A", section));
  }
  
  @Test
  void shouldReturnBookcase() {
    when(bookcaseRepository.findById(1L)).thenReturn(Optional.of(bookcases.getFirst()));

    BookcaseResponse bookcase = bookcaseService.getBookcase(1L);

    assertThat(bookcase.id()).isEqualTo(1L);
    assertThat(bookcase.code()).isEqualTo("1A");
    assertThat(bookcase.label()).isEqualTo("Bookcase 1A");
    assertThat(bookcase.sectionId()).isEqualTo(1L);

    verify(bookcaseRepository).findById(1L);
  }

  @Test
  void shouldThrowBookcaseNotFoundWhenInvalidId() {
    when(bookcaseRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(BookcaseNotFoundException.class, () -> bookcaseService.getBookcase(1L));

    verify(bookcaseRepository).findById(1L);
  }

  @Test
  void shouldReturnBookcaseList() {
    when(bookcaseRepository.findAll()).thenReturn(bookcases);

    List<BookcaseResponse> bookcasesResponse = bookcaseService.getBookcases();

    assertThat(bookcasesResponse.size()).isEqualTo(3);

    verify(bookcaseRepository).findAll();
  }

  @Test
  void shouldCreateBookcase() {
    when(bookcaseRepository.save(any())).thenReturn(bookcases.getFirst());
    when(sectionRepository.findById(1L)).thenReturn(
      Optional.of(bookcases.getFirst().getSection())
    );

    BookcaseRequest request = new BookcaseRequest(1L, "1A", "Bookcase 1A");

    BookcaseResponse bookcase = bookcaseService.createBookcase(request);

    assertThat(bookcase.id()).isNotNull();
    assertThat(bookcase.sectionId()).isEqualTo(1L);
  
    verify(sectionRepository).findById(anyLong());
    verify(bookcaseRepository).save(any(Bookcase.class));
  }

  @Test
  void shouldThrowWhenInvalidSection() {
    when(sectionRepository.findById(1L)).thenReturn(Optional.empty());

    BookcaseRequest request = new BookcaseRequest(1L, "1A", "Bookcase 1A");

    assertThrows(BadRequestException.class, () -> bookcaseService.createBookcase(request));
  
    verify(sectionRepository).findById(anyLong());
    verify(bookcaseRepository, times(0)).save(any(Bookcase.class));
  }
}
