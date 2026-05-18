package dev.fer.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.fer.library.dto.response.AuthorResponse;
import dev.fer.library.service.AuthorService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/authors")
public class AuthorController {
  private AuthorService authorService;

  protected AuthorController(AuthorService authorService) {
    this.authorService = authorService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<AuthorResponse> getBook(@PathVariable Long id) {
    AuthorResponse author = authorService.getAuthor(id);
    return ResponseEntity.ok(author);
  }
  
}
