package dev.fer.library.service;

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

  private List<Library> libraries;

  @BeforeEach
  void setUp() {
    libraries = new ArrayList<>();
    libraries.add(new Library(1L, "Library 1"));
    libraries.add(new Library(2L, "Library 2"));
    libraries.add(new Library(3L, "Library 3"));
  }

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

  @Test
  void shouldReturnLibraries() {
    when(libraryRepository.findAll()).thenReturn(libraries);

    List<Library> libs = libraryService.getLibraries();


    assertThat(libs.size()).isEqualTo(3);
    assertThat(libs.getFirst().getId()).isEqualTo(libraries.getFirst().getId());
    verify(libraryRepository).findAll();
  }

  @Test
  void shouldAddLibrary() {
    when(libraryRepository.save(any(Library.class))).thenReturn(libraries.getFirst());

    Library library = libraryService.createLibrary(new Library(null, "Library 1"));

    assertThat(library.getId()).isNotNull();
    verify(libraryRepository).save(any(Library.class));
  }

  @Test
  void shouldUpdateLibrary() {
    Library existing = libraries.getFirst();
    Library update = new Library(1L, "Library updated");

    when(libraryRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(libraryRepository.save(any(Library.class))).thenAnswer(i -> i.getArguments()[0]);

    Library updated = libraryService.updateLibrary(1L, update);

    assertThat(updated.getName()).isEqualTo("Library updated");
    verify(libraryRepository).findById(1L);
    verify(libraryRepository).save(any(Library.class));
  }

  @Test
  void shouldThrowWhenUpdateInvalidLibraryID() {
    when(libraryRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(LibraryNotFoundException.class, 
      () -> libraryService.updateLibrary(1L, libraries.getFirst()));
    
    verify(libraryRepository).findById(anyLong());
    verify(libraryRepository, times(0)).save(any(Library.class));
  }
}
