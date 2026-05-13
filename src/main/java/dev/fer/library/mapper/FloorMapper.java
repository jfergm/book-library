package dev.fer.library.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.fer.library.dto.request.FloorRequest;
import dev.fer.library.dto.response.FloorResponse;
import dev.fer.library.entity.Floor;
import dev.fer.library.entity.Library;

@Component
public class FloorMapper {
  public Floor toEntity(FloorRequest request, Library library) {

    if (!library.getId().equals(request.libraryId())) {
      throw new IllegalArgumentException();
    }

    Floor floor = new Floor(null, library, request.code(), request.description());

    return floor;
  }

  public FloorResponse toResponse(Floor floor) {
    return new FloorResponse(floor.getId(), floor.getLibrary().getId(), floor.getCode(), floor.getDescription());
  }

  public List<FloorResponse> toResponseList(List<Floor> floors) {
    return floors.stream().map(this::toResponse).toList();
  }
}
