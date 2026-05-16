package dev.fer.library.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.fer.library.dto.response.ShelfResponse;
import dev.fer.library.entity.Shelf;
import dev.fer.library.exception.ShelfNotFoundException;
import dev.fer.library.mapper.ShelfMapper;
import dev.fer.library.repository.ShelfRepository;

@Service
public class ShelfService {

  private ShelfRepository shelfRepository;

  private ShelfMapper shelfMapper;

  public ShelfService(ShelfRepository shelfRepository, ShelfMapper shelfMapper) {
    this.shelfRepository = shelfRepository;
    this.shelfMapper = shelfMapper;
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
}
