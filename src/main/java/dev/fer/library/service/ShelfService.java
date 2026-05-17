package dev.fer.library.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.fer.library.dto.request.ShelfRequest;
import dev.fer.library.dto.response.ShelfResponse;
import dev.fer.library.entity.Bookcase;
import dev.fer.library.entity.Shelf;
import dev.fer.library.exception.BadRequestException;
import dev.fer.library.exception.ShelfNotFoundException;
import dev.fer.library.mapper.ShelfMapper;
import dev.fer.library.repository.BookcaseRepository;
import dev.fer.library.repository.ShelfRepository;

@Service
public class ShelfService {

  private ShelfRepository shelfRepository;

  private BookcaseRepository bookcaseRepository;

  private ShelfMapper shelfMapper;

  public ShelfService(ShelfRepository shelfRepository, ShelfMapper shelfMapper, BookcaseRepository bookcaseRepository) {
    this.shelfRepository = shelfRepository;
    this.shelfMapper = shelfMapper;
    this.bookcaseRepository = bookcaseRepository;
  }

  public ShelfResponse getShelf(Long id) {
    Optional<Shelf> shelf = shelfRepository.findById(id);

    if (shelf.isEmpty()) {
      throw new ShelfNotFoundException();
    }
    
    return shelfMapper.toResponse(shelf.get());
  }

  public List<ShelfResponse> getShelves() {
    return shelfMapper.toResponseList((List<Shelf>) shelfRepository.findAll());
  }

  public ShelfResponse createShelf(ShelfRequest request) {
    Optional<Bookcase> bookcase = bookcaseRepository.findById(request.bookcaseId());

    if (bookcase.isEmpty()) {
      throw new BadRequestException();
    }

    Shelf shelf = shelfMapper.toEntity(request, bookcase.get());

    return shelfMapper.toResponse(shelfRepository.save(shelf));
  }
}
