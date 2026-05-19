package dev.fer.library.mapper;

import org.springframework.stereotype.Component;

import dev.fer.library.dto.response.BookResponse;
import dev.fer.library.entity.Book;

@Component
public class BookMapper {
  public BookResponse toResponse(Book book) {
    return new BookResponse(book.getId(), book.getTitle(), book.getIsbn(), book.getAuthor().getId());
  }
}
