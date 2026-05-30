package dev.fer.library.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.fer.library.dto.request.FloorRequest;
import dev.fer.library.dto.response.FloorResponse;
import dev.fer.library.entity.Floor;
import dev.fer.library.entity.Library;

class FloorMapperTest {
  private FloorMapper floorMapper;

  @BeforeEach
  void setUp() {
    floorMapper = new FloorMapper();
  }

  @Test
  void shouldConvertRequestToEntity() {
    FloorRequest request = new FloorRequest(1L, "1", "Description");
    Library library = new Library(1L, "Library");
    Floor floor = floorMapper.toEntity(request, library);

    assertThat(floor).isNotNull();
    assertThat(floor.getId()).isNull();
    assertThat(floor.getCode()).isEqualTo("1");
    assertThat(floor.getDescription()).isEqualTo("Description");
    assertThat(floor.getLibrary().getId()).isEqualTo(1L);

  }

  @Test
  void shouldThrowWhenLibraryIsNotEqualToRequest() {
    FloorRequest request = new FloorRequest(1L, "1", "Description");
    Library library = new Library(2L, "Library");

    assertThrows(IllegalArgumentException.class, () -> floorMapper.toEntity(request, library));
  }

  @Test
  void shouldConvertEntityToResponse() {
    Library lib = new Library(1L ,"Library");
    Floor floor = new Floor(1L, lib, "1", "Description");

    FloorResponse response = floorMapper.toResponse(floor);

    assertThat(response).isNotNull();
    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.libraryId()).isEqualTo(1L);
    assertThat(response.code()).isEqualTo("1");
    assertThat(response.description()).isEqualTo("Description");

  }

  @Test
  void shouldConvertoToResponseList() {
    List<Floor> floors = new ArrayList<>();
    floors.add(new Floor(1L ,new Library(1L, "Library 1"), "1", "null"));
    floors.add(new Floor(2L ,new Library(2L, "Library 2"), "2", "null"));

    List<FloorResponse> responseList = floorMapper.toResponseList(floors);

    assertThat(responseList).hasSize(2);
    assertThat(responseList.getFirst().id()).isEqualTo(1L);
    assertThat(responseList.getFirst().code()).isEqualTo("1");
    assertThat(responseList.getFirst().libraryId()).isEqualTo(1L);
    assertThat(responseList.getLast().id()).isEqualTo(2L);
    assertThat(responseList.getLast().code()).isEqualTo("2");
    assertThat(responseList.getLast().libraryId()).isEqualTo(2L);
  }
}
