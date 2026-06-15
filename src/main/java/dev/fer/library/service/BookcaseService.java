package dev.fer.library.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.fer.library.dto.request.BookcaseRequest;
import dev.fer.library.dto.response.BookcaseResponse;
import dev.fer.library.entity.Bookcase;
import dev.fer.library.entity.Section;
import dev.fer.library.exception.BadRequestException;
import dev.fer.library.exception.BookcaseNotFoundException;
import dev.fer.library.mapper.BookcaseMapper;
import dev.fer.library.repository.BookcaseRepository;
import dev.fer.library.repository.SectionRepository;

@Service
public class BookcaseService {

  private BookcaseRepository bookcaseRepository;

  private SectionRepository sectionRepository;

  private BookcaseMapper bookcaseMapper;

  BookcaseService(
    BookcaseRepository bookcaseRepository, 
    BookcaseMapper bookcaseMapper, 
    SectionRepository sectionRepository
  ) {
    this.bookcaseRepository = bookcaseRepository;
    this.bookcaseMapper = bookcaseMapper;
    this.sectionRepository = sectionRepository;
  }

  public BookcaseResponse getBookcase(Long id) {
    Optional<Bookcase> bookcase = bookcaseRepository.findById(id);

    if (bookcase.isEmpty()) {
      throw new BookcaseNotFoundException();
    }

    return bookcaseMapper.toResponse(bookcase.get());
  }

  public List<BookcaseResponse> getBookcases(Pageable pageable) {
    return bookcaseMapper.toResponseList(
      (List<Bookcase>) bookcaseRepository.findAll(pageable).getContent());
  }

  public BookcaseResponse createBookcase(BookcaseRequest request) {
    Optional<Section> section = sectionRepository.findById(request.sectionId());

    if (section.isEmpty()) {
      throw new BadRequestException();
    }

    Bookcase bookcase = bookcaseMapper.toEntity(request, section.get());

    return bookcaseMapper.toResponse(bookcaseRepository.save(bookcase));
  }

  public BookcaseResponse updateBookcase(Long id, BookcaseRequest request) {
    Optional<Bookcase> bookcaseOptional = bookcaseRepository.findById(id);

    if (bookcaseOptional.isEmpty()) {
      throw new BookcaseNotFoundException();
    }

    Bookcase bookcase = bookcaseOptional.get();
    
    Optional<Section> section;

    if (request.sectionId() != bookcase.getSection().getId()) {
      section = sectionRepository.findById(request.sectionId());
    } else {
      section = Optional.of(bookcase.getSection());
    }

    if (section.isEmpty()) {
      throw new BadRequestException();
    }

    Bookcase updated = new Bookcase(
      bookcase.getId(), 
      request.code(), 
      request.label(), 
      section.get()
    );

    return bookcaseMapper.toResponse(bookcaseRepository.save(updated));
  }

  public void deleteBookcase(Long id) {
    if (bookcaseRepository.existsById(id)) {
      bookcaseRepository.deleteById(id);
      return;
    }

    throw new BookcaseNotFoundException();
  }
}
