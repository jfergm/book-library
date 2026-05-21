package dev.fer.library.mapper;

import org.springframework.stereotype.Component;

import dev.fer.library.dto.request.BookCopyRequest;
import dev.fer.library.dto.response.BookCopyResponse;
import dev.fer.library.entity.Book;
import dev.fer.library.entity.BookCopy;
import dev.fer.library.enums.BookCopyStatus;
import dev.fer.library.exception.BadRequestException;

@Component
public class BookCopyMapper {

  public BookCopyResponse toResponse(BookCopy bookCopy) {
    return new BookCopyResponse(
      bookCopy.getId(), 
      bookCopy.getBook().getId(), 
      (bookCopy.getShelf() != null) ? bookCopy.getShelf().getId() : null, 
      bookCopy.getCode(), 
      bookCopy.getStatus().name()
    );
  }

  public BookCopy toEntity(BookCopyRequest request, Book book) {
    if (request.bookId() != book.getId()) {
      throw new BadRequestException();
    }

    return new BookCopy(null, book, null, "", BookCopyStatus.PROCESSING);
  }
}

