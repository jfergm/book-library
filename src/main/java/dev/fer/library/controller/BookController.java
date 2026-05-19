package dev.fer.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.fer.library.dto.response.BookResponse;
import dev.fer.library.service.BookService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


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
}
