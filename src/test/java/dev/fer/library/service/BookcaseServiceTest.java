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
class BookcaseServiceTest {
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

  @Test
  void shouldUpdateBookcase() {
    when(bookcaseRepository.findById(1L)).thenReturn(Optional.of(bookcases.getFirst()));
    when(bookcaseRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

    BookcaseRequest update = new BookcaseRequest(1L, "new code", "new label");

    BookcaseResponse updated = bookcaseService.updateBookcase(1L, update);

    assertThat(updated.id()).isEqualTo(1L);
    assertThat(updated.sectionId()).isEqualTo(1L);
    assertThat(updated.code()).isEqualTo("new code");
    assertThat(updated.label()).isEqualTo("new label");

    verify(bookcaseRepository).findById(1L);
    verify(bookcaseRepository).save(any(Bookcase.class));
    verify(sectionRepository, times(0)).findById(anyLong());
  }

  @Test
  void shouldFindSectionWhenUpdate() {
    when(bookcaseRepository.findById(1L)).thenReturn(Optional.of(bookcases.getFirst()));
    when(sectionRepository.findById(2L)).thenReturn(
      Optional.of(new Section(2L, null, null, null, null))
    );
    when(bookcaseRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

    BookcaseRequest update = new BookcaseRequest(2L, "new code", "new label");

    BookcaseResponse updated = bookcaseService.updateBookcase(1L, update);

    assertThat(updated.id()).isEqualTo(1L);
    assertThat(updated.sectionId()).isEqualTo(2L);
    assertThat(updated.code()).isEqualTo("new code");
    assertThat(updated.label()).isEqualTo("new label");

    verify(bookcaseRepository).findById(1L);
    verify(bookcaseRepository).save(any(Bookcase.class));
    verify(sectionRepository).findById(2L);
  }

  @Test
  void shouldThrowNotFoundWhenInvalidBookcase() {
    when(bookcaseRepository.findById(999L)).thenReturn(Optional.empty());

    BookcaseRequest update = new BookcaseRequest(2L, "new code", "new label");

    assertThrows(BookcaseNotFoundException.class, () -> bookcaseService.updateBookcase(999L, update));

    verify(bookcaseRepository).findById(999L);
    verify(bookcaseRepository, times(0)).save(any(Bookcase.class));
    verify(sectionRepository, times(0)).findById(2L);
  }

  @Test
  void shouldThrowBadRequestWhenInvalidSection() {
    when(bookcaseRepository.findById(1L)).thenReturn(Optional.of(bookcases.getFirst()));
    when(sectionRepository.findById(99L)).thenReturn(Optional.empty());

    BookcaseRequest update = new BookcaseRequest(99L, "new code", "new label");

    assertThrows(BadRequestException.class, () -> bookcaseService.updateBookcase(1L, update));

    verify(bookcaseRepository).findById(1L);
    verify(bookcaseRepository, times(0)).save(any(Bookcase.class));
    verify(sectionRepository).findById(99L);
  }

  @Test
  void shouldDeleteBookcase() {
    when(bookcaseRepository.existsById(1L)).thenReturn(true);
    doNothing().when(bookcaseRepository).deleteById(1L);

    bookcaseService.deleteBookcase(1L);

    verify(bookcaseRepository).existsById(1L);
    verify(bookcaseRepository).deleteById(1L);
  }

  @Test
  void shouldThrowWhenDeleteInvalidBookcase() {
    when(bookcaseRepository.existsById(1L)).thenReturn(false);

    assertThrows(BookcaseNotFoundException.class, () -> bookcaseService.deleteBookcase(1L));

    verify(bookcaseRepository).existsById(1L);
    verify(bookcaseRepository, times(0)).deleteById(1L);
  }

}
