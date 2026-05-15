package dev.fer.library.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.fer.library.dto.response.BookcaseResponse;
import dev.fer.library.entity.Bookcase;

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
}
