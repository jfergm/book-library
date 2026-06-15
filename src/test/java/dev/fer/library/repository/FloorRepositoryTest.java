package dev.fer.library.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import dev.fer.library.entity.Floor;
import dev.fer.library.entity.Library;

@DataJpaTest
class FloorRepositoryTest {
  @Autowired
  private FloorRepository floorRepository;

  @Autowired LibraryRepository libraryRepository;

  private Long floorID;

  @BeforeEach
  void setUp() {
    Library library = libraryRepository.save(new Library(null, "Library"));
    Floor floor = new Floor(floorID, library, "1", "null");
    floorRepository.save(floor);

    floorID = floor.getId();
  }

  @Test
  void shouldInsertFloor() {
    Floor floor = new Floor(floorID, new Library(null, "Library"), "1", "null");
    
    floorRepository.save(floor);
    assertThat(floor.getId()).isNotNull();
    assertThat(floor.getCode()).isEqualTo("1");
    assertThat(floor.getLibrary()).isNotNull();
  }

  @Test
  void shouldReturnFloorById() {
    Floor floor = floorRepository.findById(floorID).get();

    assertThat(floor.getId()).isEqualTo(floorID);
  }

  @Test
  void shouldReturnNullWhenNotExist() {
    Optional<Floor> floor = floorRepository.findById(999L);
    
    assertThat(floor).isEmpty();
  }

  @Test
  void shouldReturnTrueWhenFloorExist() {
    assertThat(floorRepository.existsById(floorID)).isTrue();
  }

  @Test
  void shouldReturnFalseWhenFloorNotExist() {
    assertThat(floorRepository.existsById(99L)).isFalse();
  }

  @Test
  void shouldUpdateFloor() {
    Floor floor = floorRepository.findById(floorID).get();
    
    Floor updated = new Floor(floor.getId(), floor.getLibrary(), floor.getCode(), "The new desc");
    
    floorRepository.save(updated);

    assertThat(floor.getDescription()).isEqualTo(updated.getDescription());
    assertThat(floor.getId()).isEqualTo(updated.getId());
  }

  @Test
  void shouldReturnLibrariesPaginated() {
    Floor floor = floorRepository.findById(floorID).get();

    floorRepository.save(new Floor(null, floor.getLibrary(), "FLOOR 1", "null"));
    floorRepository.save(new Floor(null, floor.getLibrary(), "FLOOR 2", "null"));
    floorRepository.save(new Floor(null, floor.getLibrary(), "FLOOR 3", "null"));

    Page<Floor> floorsPage = floorRepository.findAll(PageRequest.of(0, 1));
    assertThat(floorsPage.getContent()).hasSize(1);
    assertThat(floorsPage.getTotalElements()).isEqualTo(4);
    assertThat(floorsPage.getTotalPages()).isEqualTo(4);
    assertThat(floorsPage.getNumber()).isZero();
    assertThat(floorsPage.isFirst()).isTrue();
    assertThat(floorsPage.isLast()).isFalse();
  }
}
