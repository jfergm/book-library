package dev.fer.library.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.fer.library.dto.request.BookCopyRequest;
import dev.fer.library.dto.response.BookCopyResponse;
import dev.fer.library.entity.Book;
import dev.fer.library.entity.BookCopy;
import dev.fer.library.exception.BadRequestException;
import dev.fer.library.exception.BookCopyNotFoundException;
import dev.fer.library.mapper.BookCopyMapper;
import dev.fer.library.repository.BookCopyRepository;
import dev.fer.library.repository.BookRepository;

@Service
public class BookCopyService {

  private BookCopyRepository bookCopyRepository;

  private BookCopyMapper bookCopyMapper;

  private BookRepository bookRepository;

  public BookCopyService(BookCopyRepository bookCopyRepository, BookCopyMapper bookCopyMapper, BookRepository bookRepository) {
    this.bookCopyRepository = bookCopyRepository;
    this.bookCopyMapper = bookCopyMapper;
    this.bookRepository = bookRepository;
  }

  public BookCopyResponse getBookCopy(Long id) {
    Optional<BookCopy> bookCopy = bookCopyRepository.findById(id);

    if (bookCopy.isEmpty()) {
      throw new BookCopyNotFoundException();
    }

    return bookCopyMapper.toResponse(bookCopy.get());
  }

  public BookCopyResponse createBookCopy(BookCopyRequest request) {
    Optional<Book> book = bookRepository.findById(request.bookId());

    if (book.isEmpty()) {
      throw new BadRequestException();
    }

    BookCopy bookCopy = bookCopyMapper.toEntity(request, book.get());

    return bookCopyMapper.toResponse(bookCopyRepository.save(bookCopy));
  }
}
