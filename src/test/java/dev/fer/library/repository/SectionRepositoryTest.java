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
import dev.fer.library.entity.Section;

@DataJpaTest
class SectionRepositoryTest {
  @Autowired
  private SectionRepository sectionRepository;

  @Autowired
  private LibraryRepository libraryRepository;

  @Autowired 
  private FloorRepository floorRepository;

  private Long sectionID;

  private Floor floor;

  @BeforeEach
  void setUp() {
    Library library = libraryRepository.save(new Library(null, "Library"));
    floor = floorRepository.save(new Floor(null, library, "1A", ""));

    Section section = new Section(null, floor, "lit", "Literature", "Description");
    sectionRepository.save(section);

    sectionID = section.getId();
  }

  @Test
  void shouldInsertSection() {
    Section section = new Section(null, floor, "sc", "Science", "");
    
    sectionRepository.save(section);
    assertThat(section.getId()).isNotNull();
    assertThat(section.getCode()).isEqualTo("sc");
    assertThat(section.getFloor()).isNotNull();
  }

  @Test
  void shouldReturnSectionById() {
    Section section = sectionRepository.findById(sectionID).get();

    assertThat(section.getId()).isEqualTo(sectionID);
  }

  @Test
  void shouldReturnNullWhenNotExist() {
    Optional<Section> section = sectionRepository.findById(999L);
    
    assertThat(section).isEmpty();
  }

  @Test
  void shouldReturnTrueWhenSectionExist() {
    assertThat(sectionRepository.existsById(sectionID)).isTrue();
  }

  @Test
  void shouldReturnFalseWhenSectionNotExist() {
    assertThat(sectionRepository.existsById(99L)).isFalse();
  }

  @Test
  void shouldUpdateSection() {
    Section section = sectionRepository.findById(sectionID).get();
    
    Section updated = new Section(section.getId(), floor, "lit", "Literature", "New description");
    
    sectionRepository.save(updated);

    assertThat(section.getDescription()).isEqualTo(updated.getDescription());
    assertThat(section.getId()).isEqualTo(updated.getId());
  }

  @Test
  void shouldReturnLibrariesPaginated() {
    sectionRepository.save(new Section(null, floor, "SECTION 1", "LABEL1", "null"));
    sectionRepository.save(new Section(null, floor, "SECTION 2", "LABEL2", "null"));
    sectionRepository.save(new Section(null, floor, "SECTION 3", "LABEL3", "null"));

    Page<Section> librariesPage = sectionRepository.findAll(PageRequest.of(0, 1));
    assertThat(librariesPage.getContent()).hasSize(1);
    assertThat(librariesPage.getTotalElements()).isEqualTo(4);
    assertThat(librariesPage.getTotalPages()).isEqualTo(4);
    assertThat(librariesPage.getNumber()).isZero();
    assertThat(librariesPage.isFirst()).isTrue();
    assertThat(librariesPage.isLast()).isFalse();
  }
}
