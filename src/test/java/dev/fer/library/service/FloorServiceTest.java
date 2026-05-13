package dev.fer.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import dev.fer.library.dto.response.FloorResponse;
import dev.fer.library.entity.Floor;
import dev.fer.library.entity.Library;
import dev.fer.library.exception.FloorNotFoundException;
import dev.fer.library.mapper.FloorMapper;
import dev.fer.library.repository.FloorRepository;

@ExtendWith(MockitoExtension.class)
public class FloorServiceTest {

  @Mock
  private FloorRepository floorRepository;

  private FloorService floorService;

  private FloorMapper floorMapper = new FloorMapper();

  private List<Floor> floors;

  private static Library library = new Library(1L, "Library");

  @BeforeEach
  void setUp() {
    floorService = new FloorService(floorRepository, floorMapper);
    floors = new ArrayList<>();
    floors.add(new Floor(1L, library, "1A", "Description"));
    floors.add(new Floor(2L, library, "2A", "Description"));
    floors.add(new Floor(3L, library, "3A", "Description"));
    floors.add(new Floor(4L, new Library(2L, "Library 2"), "1", "Description"));
  }

  @Test
  void shouldReturnFloor() {
    
    when(floorRepository.findById(1L)).thenReturn(
      Optional.of(new Floor(1L, library, "1", ""))
    );

    FloorResponse floor = floorService.getFloor(1L);

    assertThat(floor.id()).isNotNull();
    assertThat(floor.code()).isEqualTo("1");
    assertThat(floor.libraryId()).isNotNull();
    verify(floorRepository).findById(1L);
  }

  @Test
  void shouldThrowWhenInvalidFloor() {
    when(floorRepository.findById(1L)).thenReturn(Optional.empty());
    
    assertThrows(FloorNotFoundException.class, () -> floorService.getFloor(1L) );
    verify(floorRepository).findById(1L);
  }

  @Test
  void shouldReturnFloorList() {
    when(floorRepository.findAll()).thenReturn(floors);

    List<FloorResponse> fs = floorService.getFloors();

    assertThat(fs.size()).isEqualTo(4);
    verify(floorRepository).findAll();
  }

}
