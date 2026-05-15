package dev.fer.library.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.fer.library.dto.response.BookcaseResponse;
import dev.fer.library.entity.Bookcase;
import dev.fer.library.exception.BookcaseNotFoundException;
import dev.fer.library.mapper.BookcaseMapper;
import dev.fer.library.repository.BookcaseRepository;

@Service
public class BookcaseService {

  private BookcaseRepository bookcaseRepository;

  private BookcaseMapper bookcaseMapper;

  BookcaseService(BookcaseRepository bookcaseRepository, BookcaseMapper bookcaseMapper) {
    this.bookcaseRepository = bookcaseRepository;
    this.bookcaseMapper = bookcaseMapper;
  }

  public BookcaseResponse getBookcase(Long id) {
    Optional<Bookcase> bookcase = bookcaseRepository.findById(id);

    if (bookcase.isEmpty()) {
      throw new BookcaseNotFoundException();
    }

    return bookcaseMapper.toResponse(bookcase.get());
  }
}
