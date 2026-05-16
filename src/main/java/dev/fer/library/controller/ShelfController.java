package dev.fer.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.fer.library.dto.response.ShelfResponse;
import dev.fer.library.service.ShelfService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/shelves")
public class ShelfController {

  private ShelfService shelfService;

  protected ShelfController(ShelfService shelfService) {
    this.shelfService = shelfService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<ShelfResponse> getShelf(@PathVariable Long id) {
    ShelfResponse response = shelfService.getShelf(id);
    return ResponseEntity.ok(response);
  }

  @GetMapping("")
  public ResponseEntity<List<ShelfResponse>> getShelves() {
    return ResponseEntity.ok(shelfService.getShelves());
  }
  
  
}
