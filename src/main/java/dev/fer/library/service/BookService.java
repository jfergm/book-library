package dev.fer.library.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.fer.library.dto.response.BookResponse;
import dev.fer.library.entity.Book;
import dev.fer.library.exception.BookNotFoundException;
import dev.fer.library.mapper.BookMapper;
import dev.fer.library.repository.BookRepository;

@Service
public class BookService {

  private BookRepository bookRepository;

  private BookMapper bookMapper;

  public BookService(BookRepository bookRepository, BookMapper bookMapper) {
    this.bookRepository = bookRepository;
    this.bookMapper = bookMapper;
  }
  
  public BookResponse getBook(Long id) {
    Optional<Book> book = bookRepository.findById(id);

    if (book.isEmpty()) {
      throw new BookNotFoundException();
    }

    return bookMapper.toResponse(book.get());
  }
}
