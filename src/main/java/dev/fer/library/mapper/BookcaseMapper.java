package dev.fer.library.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.fer.library.dto.request.BookcaseRequest;
import dev.fer.library.dto.response.BookcaseResponse;
import dev.fer.library.entity.Bookcase;
import dev.fer.library.entity.Section;
import dev.fer.library.exception.BadRequestException;

@Component
public class BookcaseMapper {
  public BookcaseResponse toResponse(Bookcase bookcase) {
    return new BookcaseResponse(
      bookcase.getId(), 
      bookcase.getCode(), 
      bookcase.getLabel(), 
      bookcase.getSection().getId()
    );
  }

  public List<BookcaseResponse> toResponseList(List<Bookcase> bookcases) {
    return bookcases.stream().map(this::toResponse).toList();
  } 

  public Bookcase toEntity(BookcaseRequest request, Section section) {
    if (section.getId() != request.sectionId()) {
      throw new BadRequestException();
    }
    return new Bookcase(null, request.code(), request.label(), section);
  }
}
