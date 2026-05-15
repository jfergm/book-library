package dev.fer.library.mapper;

import dev.fer.library.dto.response.BookcaseResponse;
import dev.fer.library.entity.Bookcase;

public class BookcaseMapper {
  public BookcaseResponse toResponse(Bookcase bookcase) {
    return new BookcaseResponse(
      bookcase.getId(), 
      bookcase.getCode(), 
      bookcase.getLabel(), 
      bookcase.getSection().getId()
    );
  }
}
