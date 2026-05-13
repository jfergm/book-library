package dev.fer.library.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.fer.library.dto.response.SectionResponse;
import dev.fer.library.entity.Floor;
import dev.fer.library.entity.Library;
import dev.fer.library.entity.Section;

public class SectionMapperTest {
  private SectionMapper sectionMapper;

  @BeforeEach
  void setUp() {
    sectionMapper = new SectionMapper();
  }

  @Test
  void shouldConvertEntityToResponse() {
    Section section = new Section(
      1L, 
      new Floor(1L, new Library(1L, "Library"), "f1", ""), 
      "lit", 
      "Literature", 
      "Description"
    );

    SectionResponse response = sectionMapper.toResponse(section);

    assertThat(response).isNotNull();
    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.floorId()).isEqualTo(1L);
    assertThat(response.code()).isEqualTo("lit");
    assertThat(response.label()).isEqualTo("Literature");
    assertThat(response.description()).isEqualTo("Description");
  }
}
