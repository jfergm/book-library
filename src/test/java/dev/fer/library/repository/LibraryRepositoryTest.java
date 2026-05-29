package dev.fer.library.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import dev.fer.library.entity.Library;

@DataJpaTest
class LibraryRepositoryTest {
  @Autowired
  private LibraryRepository libraryRepository;

  private Long libraryID;

  @BeforeEach
  void setUp() {
    Library library = new Library(null, "Library test");
    libraryRepository.save(library);

    libraryID = library.getId();
  }

  @Test
  void shouldInsertLibrary() {
    Library library = new Library(null, "Library");
    
    libraryRepository.save(library);
    assertThat(library.getId()).isNotNull();
    assertThat(library.getName()).isEqualTo("Library");
  }

  @Test
  void shouldReturnLibraryById() {
    Library library = libraryRepository.findById(libraryID).get();

    assertThat(library.getId()).isEqualTo(libraryID);
  }

  @Test
  void shouldReturnNullWhenNotExist() {
    Optional<Library> library = libraryRepository.findById(999L);
    
    assertThat(library.isEmpty()).isTrue();
  }

  @Test
  void shouldReturnTrueWhenLibraryExist() {
    assertThat(libraryRepository.existsById(libraryID)).isTrue();
  }

  @Test
  void shouldReturnFalseWhenLibraryNotExist() {
    assertThat(libraryRepository.existsById(99L)).isFalse();
  }

  @Test
  void shouldUpdateLibrary() {
    Library library = libraryRepository.findById(libraryID).get();
    
    Library updated = new Library(library.getId(), "Updated");
    
    libraryRepository.save(updated);

    assertThat(library.getName()).isEqualTo(updated.getName());
    assertThat(library.getId()).isEqualTo(updated.getId());
  }
}
