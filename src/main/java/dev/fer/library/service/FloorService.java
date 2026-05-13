package dev.fer.library.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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

@Service
public class FloorService {

  private FloorRepository floorRepository;

  private LibraryRepository libraryRepository;

  private FloorMapper floorMapper;

  protected FloorService(
    FloorRepository floorRepository, 
    FloorMapper floorMapper, 
    LibraryRepository libraryRepository
  ) {
    this.floorRepository = floorRepository;
    this.floorMapper = floorMapper;
    this.libraryRepository = libraryRepository;
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

  public FloorResponse createFloor(FloorRequest request) {
    Optional<Library> library = libraryRepository.findById(request.libraryId());
   
    if (library.isEmpty()) {
      throw new BadRequestException();
    }

    Floor floor = floorMapper.toEntity(request, library.get());

    return floorMapper.toResponse(floorRepository.save(floor));
  }

  public FloorResponse updateFloor(Long id, FloorUpdateRequest update) {
    Optional<Floor> floorOpt = floorRepository.findById(id);

    if (floorOpt.isEmpty()) {
      throw new FloorNotFoundException();
    }

    Floor floor = floorOpt.get();

    Floor updated = new Floor(floor.getId(), floor.getLibrary(), update.code(), update.description());

    return floorMapper.toResponse(floorRepository.save(updated));
  }
}
