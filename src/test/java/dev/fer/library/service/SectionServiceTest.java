package dev.fer.library.service;

import static org.assertj.core.api.Assertions.assertThat;
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

import dev.fer.library.dto.request.SectionRequest;
import dev.fer.library.dto.request.SectionUpdateRequest;
import dev.fer.library.dto.response.SectionResponse;
import dev.fer.library.entity.Floor;
import dev.fer.library.entity.Library;
import dev.fer.library.entity.Section;
import dev.fer.library.exception.BadRequestException;
import dev.fer.library.exception.SectionNotFoundException;
import dev.fer.library.mapper.SectionMapper;
import dev.fer.library.repository.FloorRepository;
import dev.fer.library.repository.SectionRepository;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {
  @Mock
  private SectionRepository sectionRepository;

  @Mock
  private FloorRepository floorRepository;

  private SectionMapper sectionMapper = new SectionMapper();

  private SectionService sectionService;

  private List<Section> sections;

  @BeforeEach
  void setUp() {
    sectionService = new SectionService(sectionRepository, sectionMapper, floorRepository);
    sections = new ArrayList<>();
    Floor floor = new Floor(1L, new Library(1L, "Library"), "f1", "Description");

    sections.add(new Section(1L, floor, "lit", "Literature", "Description"));
    sections.add(new Section(2L, floor, "sc", "Science", "Description"));
    sections.add(new Section(3L, floor, "horr", "Horror", "Description"));
  }

  @Test
  void shouldReturnSection() {
    when(sectionRepository.findById(1L)).thenReturn(
      Optional.of(
        sections.getFirst()
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

  @Test
  void shouldReturnSectionList() {
    when(sectionRepository.findAll()).thenReturn(sections);

    List<SectionResponse> secs = sectionService.getSections();

    assertThat(secs).hasSize(3);
    verify(sectionRepository).findAll();
  }

  @Test
  void shouldCreateSection() {
    when(sectionRepository.save(any())).thenReturn(sections.getFirst());
    when(floorRepository.findById(1L)).thenReturn(
      Optional.of(new Floor(1L, null, null, null))
    );

    SectionResponse res = sectionService.createSection(
      new SectionRequest(1L, "lit", "Literature", "Description"));

    assertThat(res).isNotNull();
    assertThat(res.id()).isEqualTo(1L);
    assertThat(res.floorId()).isEqualTo(1L);
    assertThat(res.code()).isEqualTo("lit");
    assertThat(res.label()).isEqualTo("Literature");
    assertThat(res.description()).isEqualTo("Description");

    verify(sectionRepository).save(any(Section.class));
  }

  @Test
  void shouldThrowWhenCreateWithInvlidLibrary() {
    when(floorRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(BadRequestException.class, () -> sectionService.createSection(
      new SectionRequest(1L, "lit", "Literature", "")));

    verify(floorRepository).findById(1L);
    verify(sectionRepository, times(0)).save(any(Section.class));
    
  }

  @Test
  void shouldUpdateSection() {
    when(sectionRepository.findById(1L)).thenReturn(Optional.of(sections.getFirst()));
    when(sectionRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

    SectionResponse res = sectionService.updateSection(
      1L, 
      new SectionUpdateRequest(1L, "sc", "Science", "Description")
    );

    assertThat(res.id()).isEqualTo(1L);
    assertThat(res.floorId()).isEqualTo(1L);
    assertThat(res.code()).isEqualTo("sc");
    assertThat(res.label()).isEqualTo("Science");
    assertThat(res.description()).isEqualTo("Description");

    verify(sectionRepository).findById(1L);
    verify(sectionRepository).save(any(Section.class));
    verify(floorRepository, times(0)).findById(anyLong());
  }

  @Test
  void shouldFindByIdFloorWhenUpdateToNewFloor() {
    when(sectionRepository.findById(1L)).thenReturn(Optional.of(sections.getFirst()));
    when(floorRepository.findById(2L)).thenReturn(
      Optional.of(new Floor(2L, null, null, null))
    );
    when(sectionRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

    SectionResponse res = sectionService.updateSection(
      1L, 
      new SectionUpdateRequest(2L, "sc", "Science", "Description")
    );

    assertThat(res.floorId()).isEqualTo(2L);

    verify(sectionRepository).findById(1L);
    verify(sectionRepository).save(any(Section.class));
    verify(floorRepository).findById(anyLong());
  }

  @Test
  void shouldThrowNotFoundWhenInvalidSection() {
    when(sectionRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(
      SectionNotFoundException.class, 
      () -> sectionService.updateSection(
        1L, 
        new SectionUpdateRequest(2L, "sc", "Science", "Description")
      )
    );

    verify(sectionRepository).findById(1L);
    verify(sectionRepository, times(0)).save(any(Section.class));
    verify(floorRepository, times(0)).findById(anyLong());
  }

    @Test
  void shouldBadRequestWhenInvalidFloor() {
    when(sectionRepository.findById(1L)).thenReturn(Optional.of(sections.getFirst()));
    when(floorRepository.findById(2L)).thenReturn(Optional.empty());

    assertThrows(BadRequestException.class, () -> sectionService.updateSection(1L, new SectionUpdateRequest(2L, "sc", "Science", "Description")));

    verify(sectionRepository).findById(1L);
    verify(floorRepository).findById(anyLong());
    verify(sectionRepository, times(0)).save(any(Section.class));
  }

  @Test
  void shouldDeleteSection() {
    when(sectionRepository.existsById(1L)).thenReturn(true);

    sectionService.deleteSection(1L);

    verify(sectionRepository).existsById(1L);
    verify(sectionRepository).deleteById(1L);
  }

  @Test
  void shouldThrowWhenDeleteInvalidSection() {
    when(sectionRepository.existsById(1L)).thenReturn(false);

    assertThrows(SectionNotFoundException.class, () -> sectionService.deleteSection(1L));

    verify(sectionRepository).existsById(1L);
    verify(sectionRepository, times(0)).deleteById(1L);
  }
}
