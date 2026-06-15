package dev.fer.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.fer.library.dto.request.AuthorRequest;
import dev.fer.library.dto.response.AuthorResponse;
import dev.fer.library.service.AuthorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "Authors", description = "CRUD operations for Authors")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/authors")
public class AuthorController {
  private AuthorService authorService;

  protected AuthorController(AuthorService authorService) {
    this.authorService = authorService;
  }

  @Operation(summary = "Get Author", description = "Get Author by ID")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Author fetched successfully"),
    @ApiResponse(responseCode = "404", description = "Author not found", content = @Content)
  })
  @GetMapping("/{id}")
  public ResponseEntity<AuthorResponse> getBook(@PathVariable Long id) {
    AuthorResponse author = authorService.getAuthor(id);
    return ResponseEntity.ok(author);
  }

  @Operation(summary = "Get Authors", description = "Get all Authors")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Authors fetched successfully"),
  })
  @GetMapping("")
  public ResponseEntity<List<AuthorResponse>> getAuthors(Pageable pageable) {
    return ResponseEntity.ok(authorService.getAuthors(pageable));
  }

  @Operation(summary = "Add Author", description = "Add new Author")
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201", 
      description = "Author created sucessfully", 
      headers = @Header(
        name = "Location",
        description = "Location with the resource created"
      )
    ),
    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
  })
  @PostMapping("")
  public ResponseEntity<Void> createAuthor(@RequestBody @Valid AuthorRequest request) {

    AuthorResponse created = authorService.createAuthor(request);

    URI location = ServletUriComponentsBuilder
      .fromCurrentRequestUri()
      .path("/{id}")
      .buildAndExpand(created.id())
      .toUri();
    return ResponseEntity.created(location).build();
  }
  
  @Operation(summary = "Update Author")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Author updated successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
    @ApiResponse(responseCode = "404", description = "Author not found", content = @Content),
  })
  @PutMapping("/{id}")
  public ResponseEntity<Void> updateAuthor(@PathVariable Long id, @RequestBody @Valid AuthorRequest request) {
    authorService.updateAuthor(id, request);

    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Delete Author")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Author deleted successfully"),
    @ApiResponse(responseCode = "404", description = "Author not found", content = @Content),
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
    authorService.deleteAuthor(id);
    return ResponseEntity.ok(null);
  }
}
