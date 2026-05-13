package dev.fer.library.mapper;

import org.springframework.stereotype.Component;

import dev.fer.library.dto.response.SectionResponse;
import dev.fer.library.entity.Section;

@Component
public class SectionMapper {

  public SectionResponse toResponse(Section section) {
    return new SectionResponse(
      section.getId(), 
      section.getFloor().getId(), 
      section.getCode(), 
      section.getLabel(), 
      section.getDescription()
    );
  }
}
