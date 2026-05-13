package dev.fer.library.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.fer.library.dto.response.SectionResponse;
import dev.fer.library.entity.Section;
import dev.fer.library.exception.SectionNotFoundException;
import dev.fer.library.mapper.SectionMapper;
import dev.fer.library.repository.SectionRepository;

@Service
public class SectionService {

  private SectionRepository sectionRepository;

  private SectionMapper sectionMapper;

  public SectionService(SectionRepository sectionRepository, SectionMapper sectionMapper) {
    this.sectionRepository = sectionRepository;
    this.sectionMapper = sectionMapper;
  }

  public SectionResponse getSection(Long id) {
    Optional<Section> section = sectionRepository.findById(id);

    if (section.isEmpty()) {
      throw new SectionNotFoundException();
    }

    return sectionMapper.toResponse(section.get());
  }
}
