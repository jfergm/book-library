package dev.fer.library.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import dev.fer.library.entity.Bookcase;
import dev.fer.library.entity.Floor;
import dev.fer.library.entity.Library;
import dev.fer.library.entity.Section;
import dev.fer.library.entity.Shelf;

@DataJpaTest
class ShelfRepositoryTest {
  @Autowired
  ShelfRepository shelfRepository;

  @Autowired
  BookcaseRepository bookcaseRepository;

  @Autowired
  SectionRepository sectionRepository;

  @Autowired
  FloorRepository floorRepository;

  @Autowired
  LibraryRepository libraryRepository;

  private Shelf shelf;

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

    Bookcase bookcase = bookcaseRepository.save(
      new Bookcase(null, "c", "l", section)
    );

    shelf = shelfRepository.save(
      new Shelf(null, "A1", "Shelf A1", bookcase)
    );
  }

  @Test
  void shouldInsertShelf() {
    Shelf newShelf = shelfRepository.save(
      new Shelf(null, "A1", "Shelf A1", shelf.getBookcase())
    );

    assertThat(newShelf.getId()).isNotNull();
    assertThat(newShelf.getCode()).isEqualTo("A1");
    assertThat(newShelf.getLabel()).isEqualTo("Shelf A1");
    assertThat(newShelf.getBookcase().getId()).isEqualTo(shelf.getBookcase().getId());
  }

  @Test
  void shouldReturnShelfById() {
    Optional<Shelf> returned = shelfRepository.findById(shelf.getId());

    assertThat(returned).isNotEmpty();
    assertThat(returned.get().getId()).isEqualTo(shelf.getId());
  }

  @Test
  void shouldUpdateShelf() {
    Shelf toUpdate = new Shelf(shelf.getId(), "nc", "New code", shelf.getBookcase());
    shelfRepository.save(toUpdate);

    Shelf updated = shelfRepository.findById(shelf.getId()).get();

    assertThat(updated.getId()).isEqualTo(shelf.getId());
    assertThat(updated.getCode()).isEqualTo("nc");
    assertThat(updated.getLabel()).isEqualTo("New code");
  }

  @Test
  void shouldReturnTrueWhenExist() {

    assertThat(shelfRepository.existsById(shelf.getId())).isTrue();
  }

  @Test
  void shouldReturnFalseWhenNotExist() {

    assertThat(shelfRepository.existsById(9L)).isFalse();
  }

  @Test
  void shouldDeleteShelf() {
    assertThat(shelfRepository.existsById(shelf.getId())).isTrue();
    shelfRepository.deleteById(shelf.getId());
    assertThat(shelfRepository.existsById(shelf.getId())).isFalse();

  }

  @Test
  void shouldReturnLibrariesPaginated() {
    shelfRepository.save(new Shelf(null, "SHLF_1", "SHELF 1", shelf.getBookcase()));
    shelfRepository.save(new Shelf(null, "SHLF_2", "SHELF 2", shelf.getBookcase()));
    shelfRepository.save(new Shelf(null, "SHLF_3", "SHELF 3", shelf.getBookcase()));

    Page<Shelf> shelvesPage = shelfRepository.findAll(PageRequest.of(0, 1));
    assertThat(shelvesPage.getContent()).hasSize(1);
    assertThat(shelvesPage.getTotalElements()).isEqualTo(4);
    assertThat(shelvesPage.getTotalPages()).isEqualTo(4);
    assertThat(shelvesPage.getNumber()).isEqualTo(0);
    assertThat(shelvesPage.isFirst()).isTrue();
    assertThat(shelvesPage.isLast()).isFalse();
  }
}
