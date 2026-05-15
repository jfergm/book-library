package dev.fer.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.fer.library.dto.request.BookcaseRequest;
import dev.fer.library.dto.response.BookcaseResponse;
import dev.fer.library.service.BookcaseService;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/bookcases")
public class BookcaseController {

  private BookcaseService bookcaseService;

  BookcaseController(BookcaseService bookcaseService) {
    this.bookcaseService = bookcaseService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<BookcaseResponse> getBookcase(@PathVariable Long id) {
    BookcaseResponse res = bookcaseService.getBookcase(id);
    return ResponseEntity.ok(res);
  }

  @GetMapping("")
  public ResponseEntity<List<BookcaseResponse>> getBookcases() {
    return ResponseEntity.ok(bookcaseService.getBookcases());
  }

  @PostMapping("")
  public ResponseEntity<Void> createBookcase(@RequestBody BookcaseRequest request) {
    BookcaseResponse created = bookcaseService.createBookcase(request);

    URI location = ServletUriComponentsBuilder
      .fromCurrentRequestUri()
      .path("/{id}")
      .buildAndExpand(created.id())
      .toUri();
      
    return ResponseEntity.created(location).build();
  }
  
  
}
