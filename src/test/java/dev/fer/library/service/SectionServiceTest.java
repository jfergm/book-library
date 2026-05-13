package dev.fer.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.fer.library.dto.response.SectionResponse;
import dev.fer.library.entity.Floor;
import dev.fer.library.entity.Library;
import dev.fer.library.entity.Section;
import dev.fer.library.exception.SectionNotFoundException;
import dev.fer.library.mapper.SectionMapper;
import dev.fer.library.repository.SectionRepository;

@ExtendWith(MockitoExtension.class)
public class SectionServiceTest {
  @Mock
  private SectionRepository sectionRepository;

  private SectionMapper sectionMapper = new SectionMapper();

  private SectionService sectionService;

  @BeforeEach
  void setUp() {
    sectionService = new SectionService(sectionRepository, sectionMapper);
  }

  @Test
  void shouldReturnSection() {
    when(sectionRepository.findById(1L)).thenReturn(
      Optional.of(
        new Section(
          1L, 
          new Floor(1L, new Library(1L, "Library"), "f1", ""), 
          "lit", 
          "Literature", 
          "Description"
        )
      )
    );

    SectionResponse sectionResponse = sectionService.getSection(1L);

    assertThat(sectionResponse.id()).isEqualTo(1L);
    assertThat(sectionResponse.floorId()).isEqualTo(1L);
    assertThat(sectionResponse.code()).isEqualTo("lit");
    assertThat(sectionResponse.label()).isEqualTo("Literature");
    assertThat(sectionResponse.description()).isEqualTo("Description");
    
    verify(sectionRepository).findById(1L);
  }

  @Test
  void shouldThrowWhenInvalidSectionId() {
    when(sectionRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(SectionNotFoundException.class, () -> sectionService.getSection(1L));
    
    verify(sectionRepository).findById(1L);
  }
}
