package dev.fer.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.fer.library.dto.response.BookCopyResponse;
import dev.fer.library.service.BookCopyService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
}
