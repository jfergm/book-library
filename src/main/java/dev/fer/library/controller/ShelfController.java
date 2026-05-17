package dev.fer.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.fer.library.dto.request.ShelfRequest;
import dev.fer.library.dto.response.ShelfResponse;
import dev.fer.library.service.ShelfService;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




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
  
  @PostMapping("")
  public ResponseEntity<Void> createShelf(@RequestBody ShelfRequest request) {
    ShelfResponse created = shelfService.createShelf(request);
    URI location = ServletUriComponentsBuilder
      .fromCurrentRequestUri()
      .path("/{id}")
      .buildAndExpand(created.id())
      .toUri();
    return ResponseEntity.created(location).build();
  }
  
}
