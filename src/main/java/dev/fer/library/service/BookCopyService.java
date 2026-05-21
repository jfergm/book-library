package dev.fer.library.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.fer.library.dto.response.BookCopyResponse;
import dev.fer.library.entity.BookCopy;
import dev.fer.library.exception.BookCopyNotFoundException;
import dev.fer.library.mapper.BookCopyMapper;
import dev.fer.library.repository.BookCopyRepository;

@Service
public class BookCopyService {

  private BookCopyRepository bookCopyRepository;

  private BookCopyMapper bookCopyMapper;

  public BookCopyService(BookCopyRepository bookCopyRepository, BookCopyMapper bookCopyMapper) {
    this.bookCopyRepository = bookCopyRepository;
    this.bookCopyMapper = bookCopyMapper;
  }

  public BookCopyResponse getBookCopy(Long id) {
    Optional<BookCopy> bookCopy = bookCopyRepository.findById(id);

    if (bookCopy.isEmpty()) {
      throw new BookCopyNotFoundException();
    }

    return bookCopyMapper.toResponse(bookCopy.get());
  }
}
