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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.fer.library.dto.request.LibraryRequest;
import dev.fer.library.dto.response.LibraryResponse;
import dev.fer.library.entity.Library;
import dev.fer.library.exception.LibraryNotFoundException;
import dev.fer.library.mapper.LibraryMapper;
import dev.fer.library.repository.LibraryRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {

  @Mock
  LibraryRepository libraryRepository;

  LibraryMapper libraryMapper = new LibraryMapper();

  LibraryService libraryService;

  private List<LibraryResponse> librariesResponse;

  @BeforeEach
  void setUp() {
    librariesResponse = new ArrayList<>();
    librariesResponse.add(new LibraryResponse(1L, "Library 1"));
    librariesResponse.add(new LibraryResponse(2L, "Library 2"));
    librariesResponse.add(new LibraryResponse(3L, "Library 3"));

    libraryService = new LibraryService(libraryRepository, libraryMapper);
  }

  @Test
  void shouldGetLibrary() {

    when(libraryRepository.findById(1L)).thenReturn(
      Optional.of(new Library(1L, "Library"))
    );

    LibraryResponse library = libraryService.getLibaryByID(1L);

    assertThat(library.id()).isNotNull();
    assertThat(library.name()).isEqualTo("Library");
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
    List<Library> libraries = librariesResponse.stream().map(t -> new Library(t.id(), t.name()) ).toList();
    when(libraryRepository.findAll()).thenReturn(libraries);

    List<LibraryResponse> libs = libraryService.getLibraries();


    assertThat(libs).hasSize(3);
    assertThat(libs.getFirst().id()).isEqualTo(librariesResponse.getFirst().id());
    verify(libraryRepository).findAll();
  }

  @Test
  void shouldAddLibrary() {
    when(libraryRepository.save(any(Library.class))).thenReturn(new Library(1L , "Library 1"));

    LibraryResponse library = libraryService.createLibrary(new LibraryRequest("Library 1"));
    assertThat(library.id()).isNotNull();
    verify(libraryRepository).save(any(Library.class));
  }

  @Test
  void shouldUpdateLibrary() {
    Library existing = new Library(1L ,"Library");
    LibraryRequest update = new LibraryRequest("Library updated");

    when(libraryRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(libraryRepository.save(any(Library.class))).thenAnswer(i -> i.getArguments()[0]);

    LibraryResponse updated = libraryService.updateLibrary(1L, update);

    assertThat(updated.name()).isEqualTo("Library updated");
    verify(libraryRepository).findById(1L);
    verify(libraryRepository).save(any(Library.class));
  }

  @Test
  void shouldThrowWhenUpdateInvalidLibraryID() {
    when(libraryRepository.findById(1L)).thenReturn(Optional.empty());

    LibraryRequest request = new LibraryRequest("Library");
    assertThrows(LibraryNotFoundException.class, 
      () -> libraryService.updateLibrary(1L, request));
    
    verify(libraryRepository).findById(anyLong());
    verify(libraryRepository, times(0)).save(any(Library.class));
  }

  @Test
  void shouldDeleteLibrary() {
    when(libraryRepository.existsById(1L)).thenReturn(true);

    libraryService.deleteLibrary(1L);

    verify(libraryRepository).existsById(1L);
    verify(libraryRepository).deleteById(1L);
  }

  @Test
  void shouldThrowWhenDeleteInvalidLibrary() {
    when(libraryRepository.existsById(1L)).thenReturn(false);

    assertThrows(LibraryNotFoundException.class, () -> libraryService.deleteLibrary(1L));

    verify(libraryRepository).existsById(1L);
    verify(libraryRepository, times(0)).deleteById(1L);
  }
}
