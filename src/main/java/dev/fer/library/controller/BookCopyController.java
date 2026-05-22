package dev.fer.library.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.fer.library.dto.request.BookCopyRequest;
import dev.fer.library.dto.request.BookCopyUpdateRequest;
import dev.fer.library.dto.request.BookCopyUpdateShelfRequest;
import dev.fer.library.dto.response.BookCopyResponse;
import dev.fer.library.service.BookCopyService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/book-copies")
public class BookCopyController {
  private BookCopyService bookCopyService;

  protected BookCopyController(BookCopyService bookCopyService) {
    this.bookCopyService = bookCopyService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<BookCopyResponse> getBookCopy(@PathVariable Long id) {
    BookCopyResponse bookCopy = bookCopyService.getBookCopy(id);
    return ResponseEntity.ok(bookCopy);
  }

  @PostMapping("")
  public ResponseEntity<Void> createBookCopy(@RequestBody BookCopyRequest request) {
    BookCopyResponse response = bookCopyService.createBookCopy(request);

    URI location = ServletUriComponentsBuilder
      .fromCurrentRequestUri()
      .path("/{id}")
      .buildAndExpand(response.id())
      .toUri();

    return ResponseEntity.created(location).build();
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Void> updateBookCopy(@PathVariable Long id, @RequestBody BookCopyUpdateRequest request) {
    bookCopyService.updateBookCopy(id, request);

    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}/shelf")
  public ResponseEntity<Void> updateBookCopyShelf(
    @PathVariable Long id, 
    @RequestBody BookCopyUpdateShelfRequest request
  ) {

    bookCopyService.updateBookCopyShelf(id, request);
    return ResponseEntity.noContent().build();
  }
  
}
