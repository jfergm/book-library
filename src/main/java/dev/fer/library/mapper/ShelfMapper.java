package dev.fer.library.mapper;

import org.springframework.stereotype.Component;

import dev.fer.library.dto.response.ShelfResponse;
import dev.fer.library.entity.Shelf;

@Component
public class ShelfMapper {

  public ShelfResponse toResponse(Shelf shelf) {
    return new ShelfResponse(shelf.getId(), shelf.getCode(), shelf.getLabel(), shelf.getBookcase().getId());
  }
}
