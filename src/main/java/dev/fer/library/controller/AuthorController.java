package dev.fer.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.fer.library.dto.request.AuthorRequest;
import dev.fer.library.dto.response.AuthorResponse;
import dev.fer.library.service.AuthorService;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



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

  @GetMapping("")
  public ResponseEntity<List<AuthorResponse>> getAuthors() {
    return ResponseEntity.ok(authorService.getAuthors());
  }

  @PostMapping("")
  public ResponseEntity<Void> createAuthor(@RequestBody AuthorRequest request) {

    AuthorResponse created = authorService.createAuthor(request);

    URI location = ServletUriComponentsBuilder
      .fromCurrentRequestUri()
      .path("/{id}")
      .buildAndExpand(created.id())
      .toUri();
    return ResponseEntity.created(location).build();
  }
  

  @PutMapping("/{id}")
  public ResponseEntity<Void> updateAuthor(@PathVariable Long id, @RequestBody AuthorRequest request) {
    authorService.updateAuthor(id, request);

    return ResponseEntity.noContent().build();
  }
}
