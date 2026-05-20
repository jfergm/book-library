package dev.fer.library.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.fer.library.dto.request.BookRequest;
import dev.fer.library.dto.response.BookResponse;
import dev.fer.library.service.BookService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/books")
public class BookController {

  private BookService bookService;

  protected BookController(BookService bookService) {
    this.bookService = bookService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<BookResponse> getBook(@PathVariable Long id) {
    BookResponse response = bookService.getBook(id);
    return ResponseEntity.ok(response);
  }

  @GetMapping("")
  public ResponseEntity<List<BookResponse>> getBooks() {
    return ResponseEntity.ok(bookService.getBooks());
  }

  @PostMapping("")
  public ResponseEntity<Void> createBook(@RequestBody BookRequest request) {
    BookResponse created = bookService.createBook(request);

    URI location = ServletUriComponentsBuilder
      .fromCurrentRequestUri()
      .path("/{id}")
      .buildAndExpand(created.id())
      .toUri();

    return ResponseEntity.created(location).build();
  }
  
  
}
