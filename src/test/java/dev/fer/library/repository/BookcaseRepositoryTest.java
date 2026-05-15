package dev.fer.library.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import dev.fer.library.entity.Bookcase;
import dev.fer.library.entity.Floor;
import dev.fer.library.entity.Library;
import dev.fer.library.entity.Section;

@DataJpaTest
public class BookcaseRepositoryTest {
  @Autowired
  BookcaseRepository bookcaseRepository;

  @Autowired
  SectionRepository sectionRepository;

  @Autowired
  FloorRepository floorRepository;

  @Autowired
  LibraryRepository libraryRepository;

  private Bookcase bookcase;

  @BeforeEach
  void setUp() {
    Library library = libraryRepository.save(
      new Library(null, "Library")
    );

    Floor floor = floorRepository.save(
      new Floor(null, library, "1", "")
    );

    Section section = sectionRepository.save(
      new Section(null, floor, "sc", "Science", "")
    );

    bookcase = bookcaseRepository.save(
      new Bookcase(null, "1A", "Bookcase 1A", section)
    );
  }

  @Test
  void shouldInsertBookcase() {
    Bookcase newBookcase = bookcaseRepository.save(
      new Bookcase(null, "ns", "Natural Science", bookcase.getSection())
    );

    assertThat(newBookcase.getId()).isNotNull();
    assertThat(newBookcase.getCode()).isEqualTo("ns");
    assertThat(newBookcase.getLabel()).isEqualTo("Natural Science");
    assertThat(newBookcase.getSection().getId()).isEqualTo(bookcase.getSection().getId());
  }

  @Test
  void shouldReturnBookcaseById() {
    Optional<Bookcase> returned = bookcaseRepository.findById(bookcase.getId());

    assertThat(returned).isNotEmpty();
    assertThat(returned.get().getId()).isEqualTo(bookcase.getId());
  }

  @Test
  void shouldUpdateBookcase() {
    Bookcase toUpdate = new Bookcase(bookcase.getId(), "nc", "New code", bookcase.getSection());
    bookcaseRepository.save(toUpdate);

    Bookcase updated = bookcaseRepository.findById(bookcase.getId()).get();

    assertThat(updated.getId()).isEqualTo(bookcase.getId());
    assertThat(updated.getCode()).isEqualTo("nc");
    assertThat(updated.getLabel()).isEqualTo("New code");
  }

  @Test
  void shouldReturnTrueWhenExist() {

    assertThat(bookcaseRepository.existsById(bookcase.getId())).isTrue();
  }

  @Test
  void shouldReturnFalseWhenNotExist() {

    assertThat(bookcaseRepository.existsById(9L)).isFalse();
  }

  @Test
  void shouldDeleteBookcase() {
    assertThat(bookcaseRepository.existsById(bookcase.getId())).isTrue();
    bookcaseRepository.deleteById(bookcase.getId());
    assertThat(bookcaseRepository.existsById(bookcase.getId())).isFalse();

  }

}
