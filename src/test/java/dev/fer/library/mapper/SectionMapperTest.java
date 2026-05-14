package dev.fer.library.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.fer.library.dto.request.SectionRequest;
import dev.fer.library.dto.response.SectionResponse;
import dev.fer.library.entity.Floor;
import dev.fer.library.entity.Library;
import dev.fer.library.entity.Section;
import dev.fer.library.exception.BadRequestException;

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

  @Test
  void shouldConvertoToResponseList() {
    List<Section> sections = new ArrayList<>();
    Floor floor = new Floor(1L, new Library(1L, "Library"), "f1", "");

    sections.add(new Section(1L, floor, "lit", "Literature", ""));
    sections.add(new Section(2L, floor, "sc", "Science", ""));
    sections.add(new Section(3L, floor, "horr", "Horror", ""));

    List<SectionResponse> responseList = sectionMapper.toResponseList(sections);

    assertThat(responseList).hasSize(3);
    assertThat(responseList.getFirst().id()).isEqualTo(1L);
    assertThat(responseList.getFirst().code()).isEqualTo("lit");
    assertThat(responseList.getFirst().floorId()).isEqualTo(1L);
    assertThat(responseList.getLast().id()).isEqualTo(3L);
    assertThat(responseList.getLast().code()).isEqualTo("horr");
    assertThat(responseList.getLast().floorId()).isEqualTo(1L);
  }

  @Test
  void shouldConvertRequestToEntity() {
    SectionRequest request = new SectionRequest(1L, "lit", "Literature", "Description");
    Floor floor = new Floor(1L, new Library(1L, ""), "1", "");
    
    Section section = sectionMapper.toEntity(request, floor);

    assertThat(section).isNotNull();
    assertThat(section.getId()).isNull();
    assertThat(section.getCode()).isEqualTo("lit");
    assertThat(section.getLabel()).isEqualTo("Literature");
    assertThat(section.getDescription()).isEqualTo("Description");
    assertThat(section.getFloor().getId()).isEqualTo(1L);

  }

  @Test
  void shouldThrowWhenLibraryIsNotEqualToRequest() {
    SectionRequest request = new SectionRequest(1L, "lit", "Literature", "Description");
    Floor floor = new Floor(2L, null, null, null);

    assertThrows(BadRequestException.class, () -> sectionMapper.toEntity(request, floor));
  }
}
