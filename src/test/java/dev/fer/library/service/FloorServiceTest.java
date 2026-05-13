package dev.fer.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

import dev.fer.library.dto.request.FloorRequest;
import dev.fer.library.dto.request.FloorUpdateRequest;
import dev.fer.library.dto.response.FloorResponse;
import dev.fer.library.entity.Floor;
import dev.fer.library.entity.Library;
import dev.fer.library.exception.BadRequestException;
import dev.fer.library.exception.FloorNotFoundException;
import dev.fer.library.mapper.FloorMapper;
import dev.fer.library.repository.FloorRepository;
import dev.fer.library.repository.LibraryRepository;

@ExtendWith(MockitoExtension.class)
public class FloorServiceTest {

  @Mock
  private FloorRepository floorRepository;

  @Mock
  private LibraryRepository libraryRepository;

  private FloorService floorService;

  private FloorMapper floorMapper = new FloorMapper();

  private List<Floor> floors;

  private static Library library = new Library(1L, "Library");

  @BeforeEach
  void setUp() {
    floorService = new FloorService(floorRepository, floorMapper, libraryRepository);
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

  @Test
  void shouldCreateFloor() {
    when(floorRepository.save(any(Floor.class))).thenReturn(floors.getFirst());
    when(libraryRepository.findById(1L)).thenReturn(Optional.of(library));

    FloorResponse floor = floorService.createFloor(new FloorRequest(1L, "1A", "Description"));
    assertThat(floor.id()).isNotNull();
    verify(libraryRepository).findById(1L);
    verify(floorRepository).save(any(Floor.class));
  }

  @Test
  void shouldThrowWhenCreateWithInvlidLibrary() {
    when(libraryRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(BadRequestException.class, () -> floorService.createFloor(new FloorRequest(1L, "1A", "Description")));
    verify(libraryRepository).findById(1L);
    verify(floorRepository, times(0)).save(any(Floor.class));
    
  }

  @Test
  void shouldUpdateFloor() {
    when(floorRepository.findById(1L)).thenReturn(Optional.of(floors.getFirst()));
    when(floorRepository.save(any(Floor.class))).thenAnswer(i -> i.getArguments()[0]);

    FloorResponse floorResponse = floorService.updateFloor(1L, new FloorUpdateRequest("1A", "New Description"));
    
    assertThat(floorResponse.code()).isEqualTo("1A");
    assertThat(floorResponse.description()).isEqualTo("New Description");
    verify(floorRepository).findById(1L);
    verify(floorRepository).save(any(Floor.class));

  }

  @Test
  void shouldThrowWhenUpdateInvalidFloor() {
    when(floorRepository.findById(1L)).thenReturn(Optional.empty());

    
    assertThrows(
      FloorNotFoundException.class,
      () -> floorService.updateFloor(1L, new FloorUpdateRequest("1A", "New Description"))
    );

    verify(floorRepository).findById(1L);
    verify(floorRepository, times(0)).save(any(Floor.class));
  }

  @Test
  void shouldDeleteFloor() {
    when(floorRepository.existsById(1L)).thenReturn(true);

    floorService.deleteFloor(1L);

    verify(floorRepository).existsById(1L);
    verify(floorRepository).deleteById(1L);
  }

  @Test
  void shouldThrowWhenDeleteInvalidFloor() {
    when(floorRepository.existsById(1L)).thenReturn(false);

    assertThrows(FloorNotFoundException.class, () -> floorService.deleteFloor(1L));

    verify(floorRepository).existsById(1L);
    verify(floorRepository, times(0)).deleteById(1L);
  }
}
