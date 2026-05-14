package dev.fer.library.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.fer.library.dto.request.SectionRequest;
import dev.fer.library.dto.response.SectionResponse;
import dev.fer.library.entity.Floor;
import dev.fer.library.entity.Section;
import dev.fer.library.exception.BadRequestException;

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

  public List<SectionResponse> toResponseList(List<Section> sections) {
    return sections.stream().map(this::toResponse).toList();
  }

  public Section toEntity(SectionRequest request, Floor floor) {
    if (floor.getId() != request.floorId()) {
      throw new BadRequestException();
    }

    return new Section(
      null, 
      floor, 
      request.code(), 
      request.label(), 
      request.description()
    );
  }
}
