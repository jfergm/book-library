package dev.fer.library.mapper;

import org.springframework.stereotype.Component;

import dev.fer.library.dto.request.BookCopyRequest;
import dev.fer.library.dto.request.BookCopyUpdateRequest;
import dev.fer.library.dto.request.BookCopyUpdateShelfRequest;
import dev.fer.library.dto.response.BookCopyResponse;
import dev.fer.library.entity.Book;
import dev.fer.library.entity.BookCopy;
import dev.fer.library.entity.Shelf;
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

  public BookCopy toUpdateEntity(BookCopy bookCopy, BookCopyUpdateRequest request) {
    return new BookCopy(
      bookCopy.getId(), 
      bookCopy.getBook(), 
      bookCopy.getShelf(), 
      request.code(), 
      bookCopy.getStatus()
    );
  }

  public BookCopy toUpdateShelfEntity(BookCopyUpdateShelfRequest request, BookCopy bookCopy, Shelf shelf) {

    if (
      (request.shelfId() == null && shelf != null) ||
      (request.shelfId() != null && shelf == null)
    ) {
      throw new BadRequestException();
    }

    if (shelf != null && request.shelfId() != shelf.getId()) {
      throw new BadRequestException();
    }

    return new BookCopy(
      bookCopy.getId(), 
      bookCopy.getBook(), 
      shelf, 
      bookCopy.getCode(), 
      bookCopy.getStatus()
    );
  }

  public BookCopy toCheckedOutEntity(BookCopy bookCopy) {
    return new BookCopy(
      bookCopy.getId(),
      bookCopy.getBook(),
      bookCopy.getShelf(),
      bookCopy.getCode(), 
      BookCopyStatus.CHECKED_OUT
    );
  }

  public BookCopy toProcessingEntity(BookCopy bookCopy) {
    return new BookCopy(
      bookCopy.getId(),
      bookCopy.getBook(),
      bookCopy.getShelf(),
      bookCopy.getCode(), 
      BookCopyStatus.PROCESSING
    );
  }
}

