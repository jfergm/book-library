package dev.fer.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.fer.library.dto.response.BookcaseResponse;
import dev.fer.library.service.BookcaseService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


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
  
}
