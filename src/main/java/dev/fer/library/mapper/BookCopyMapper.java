package dev.fer.library.mapper;

import org.springframework.stereotype.Component;

import dev.fer.library.dto.response.BookCopyResponse;
import dev.fer.library.entity.BookCopy;

@Component
public class BookCopyMapper {

  public BookCopyResponse toResponse(BookCopy bookCopy) {
    return new BookCopyResponse(
      bookCopy.getId(), 
      bookCopy.getBook().getId(), 
      bookCopy.getShelf().getId(), 
      bookCopy.getCode(), 
      bookCopy.getStatus().name()
    );
  }
}
