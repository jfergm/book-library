package dev.fer.library.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.fer.library.entity.Library;
import dev.fer.library.exception.LibraryNotFoundException;
import dev.fer.library.repository.LibraryRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class LibraryServiceTest {

  @Mock
  LibraryRepository libraryRepository;

  @InjectMocks
  LibraryService libraryService;

  @Test
  void shouldGetLibrary() {

    when(libraryRepository.findById(1L)).thenReturn(
      Optional.of(new Library(1L, "Library"))
    );

    Library library = libraryService.getLibaryByID(1L);

    assertThat(library.getId()).isNotNull();
    assertThat(library.getName()).isEqualTo("Library");
    verify(libraryRepository).findById(1L);
  }

  @Test
  void shouldThrowWhenInvalidLibraryID() {
    when(libraryRepository.findById(999L)).thenReturn(Optional.empty());

    assertThrows(LibraryNotFoundException.class, () -> libraryService.getLibaryByID(999L));
    verify(libraryRepository).findById(999L);
  }
}
