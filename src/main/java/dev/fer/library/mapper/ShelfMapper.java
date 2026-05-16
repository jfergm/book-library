package dev.fer.library.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.fer.library.dto.response.ShelfResponse;
import dev.fer.library.entity.Shelf;

@Component
public class ShelfMapper {

  public ShelfResponse toResponse(Shelf shelf) {
    return new ShelfResponse(shelf.getId(), shelf.getCode(), shelf.getLabel(), shelf.getBookcase().getId());
  }

  public List<ShelfResponse> toResponseList(List<Shelf> shelves) {
    return shelves.stream().map(this::toResponse).toList();
  }
}
