package dev.fer.library.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.fer.library.dto.request.BookRequest;
import dev.fer.library.dto.response.BookResponse;
import dev.fer.library.entity.Author;
import dev.fer.library.entity.Book;
import dev.fer.library.exception.BadRequestException;

@Component
public class BookMapper {
  public BookResponse toResponse(Book book) {
    return new BookResponse(book.getId(), book.getTitle(), book.getIsbn(), book.getAuthor().getId());
  }

  public List<BookResponse> toResponseList(List<Book> books) {
    return books.stream().map(this::toResponse).toList();
  }

  public Book toEntity(BookRequest request, Author author) {
    if (author.getId() != request.authorId()) {
      throw new BadRequestException();
    }
    return new Book(null, request.title(), request.isbn(), author);
  }
}
