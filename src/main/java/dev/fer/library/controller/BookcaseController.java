package dev.fer.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.fer.library.dto.request.BookcaseRequest;
import dev.fer.library.dto.response.BookcaseResponse;
import dev.fer.library.service.BookcaseService;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



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
  public ResponseEntity<Void> createBookcase(@RequestBody @Valid BookcaseRequest request) {
    BookcaseResponse created = bookcaseService.createBookcase(request);

    URI location = ServletUriComponentsBuilder
      .fromCurrentRequestUri()
      .path("/{id}")
      .buildAndExpand(created.id())
      .toUri();
      
    return ResponseEntity.created(location).build();
  }
  
  @PutMapping("/{id}")
  public ResponseEntity<Void> updateBookcase(
    @PathVariable long id, 
    @RequestBody @Valid BookcaseRequest request
  ) {
    bookcaseService.updateBookcase(id, request);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBookcase(@PathVariable Long id) {
    bookcaseService.deleteBookcase(id);
    return ResponseEntity.ok(null);
  }
}
