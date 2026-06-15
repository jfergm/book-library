package dev.fer.library.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.fer.library.dto.request.SectionRequest;
import dev.fer.library.dto.request.SectionUpdateRequest;
import dev.fer.library.dto.response.SectionResponse;
import dev.fer.library.entity.Floor;
import dev.fer.library.entity.Section;
import dev.fer.library.exception.BadRequestException;
import dev.fer.library.exception.SectionNotFoundException;
import dev.fer.library.mapper.SectionMapper;
import dev.fer.library.repository.FloorRepository;
import dev.fer.library.repository.SectionRepository;

@Service
public class SectionService {

  private SectionRepository sectionRepository;

  private SectionMapper sectionMapper;

  private FloorRepository floorRepository;

  public SectionService(
    SectionRepository sectionRepository, 
    SectionMapper sectionMapper, 
    FloorRepository floorRepository
  ) {
    this.sectionRepository = sectionRepository;
    this.sectionMapper = sectionMapper;
    this.floorRepository = floorRepository;
  }

  public SectionResponse getSection(Long id) {
    Optional<Section> section = sectionRepository.findById(id);

    if (section.isEmpty()) {
      throw new SectionNotFoundException();
    }

    return sectionMapper.toResponse(section.get());
  }

  public List<SectionResponse> getSections(Pageable pageable) {
    return sectionMapper.toResponseList(sectionRepository.findAll(pageable).getContent());
  }

  public SectionResponse createSection(SectionRequest request) {
    Optional<Floor> floor = floorRepository.findById(request.floorId());

    if (floor.isEmpty()) {
      throw new BadRequestException();
    }

    Section section = sectionMapper.toEntity(request, floor.get());

    return sectionMapper.toResponse(sectionRepository.save(section));
  }

  public SectionResponse updateSection(Long id, SectionUpdateRequest update) {
    Optional<Section> sectionOptional = sectionRepository.findById(id);

    if (sectionOptional.isEmpty()) {
      throw new SectionNotFoundException();
    }

    Section section = sectionOptional.get();
    Optional<Floor> floor;

    if (section.getFloor().getId() != update.floorId()) {
      floor = floorRepository.findById(update.floorId());
    } else {
      floor = Optional.of(section.getFloor());
    }

    if (floor.isEmpty()) {
      throw new BadRequestException();
    }

    Section updated = new Section(
      section.getId(),
      floor.get(),
      update.code(),
      update.label(),
      update.description()
    );
    
    return sectionMapper.toResponse(sectionRepository.save(updated));
  }

  public void deleteSection(Long id) {
    if (sectionRepository.existsById(id)) {
      sectionRepository.deleteById(id);
      return;
    }

    throw new SectionNotFoundException();
  }
}
