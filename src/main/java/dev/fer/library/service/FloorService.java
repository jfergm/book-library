package dev.fer.library.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.fer.library.dto.response.FloorResponse;
import dev.fer.library.entity.Floor;
import dev.fer.library.exception.FloorNotFoundException;
import dev.fer.library.mapper.FloorMapper;
import dev.fer.library.repository.FloorRepository;

@Service
public class FloorService {

  private FloorRepository floorRepository;

  private FloorMapper floorMapper;

  protected FloorService(FloorRepository floorRepository, FloorMapper floorMapper) {
    this.floorRepository = floorRepository;
    this.floorMapper = floorMapper;
  }

  public FloorResponse getFloor(Long id) {
    Optional<Floor> floor = floorRepository.findById(id);

    if (floor.isEmpty()) {
      throw new FloorNotFoundException();
    }

    return floorMapper.toResponse(floor.get());
  }

  public List<FloorResponse> getFloors() {
    return floorMapper.toResponseList((List<Floor>)floorRepository.findAll());
  }
}
