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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "Books", description = "CRUD operations for Books")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/books")
public class BookController {

  private BookService bookService;

  protected BookController(BookService bookService) {
    this.bookService = bookService;
  }

  @Operation(summary = "Get Book", description = "Get Book by ID")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Book fetched successfully"),
    @ApiResponse(responseCode = "404", description = "Book not found", content = @Content),
  })
  @GetMapping("/{id}")
  public ResponseEntity<BookResponse> getBook(@PathVariable Long id) {
    BookResponse response = bookService.getBook(id);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Get Books", description = "Get all Books")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Books fetched successfully"),
  })
  @GetMapping("")
  public ResponseEntity<List<BookResponse>> getBooks() {
    return ResponseEntity.ok(bookService.getBooks());
  }

  @Operation(summary = "Add Book", description = "Add new Book")
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201", 
      description = "Book created sucessfully", 
      headers = @Header(
        name = "Location",
        description = "Location with the resource created"
      )
    ),
    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
  })
  @PostMapping("")
  public ResponseEntity<Void> createBook(@RequestBody @Valid BookRequest request) {
    BookResponse created = bookService.createBook(request);

    URI location = ServletUriComponentsBuilder
      .fromCurrentRequestUri()
      .path("/{id}")
      .buildAndExpand(created.id())
      .toUri();

    return ResponseEntity.created(location).build();
  }
  
  @Operation(summary = "Update Book")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Book updated successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
    @ApiResponse(responseCode = "404", description = "Book not found", content = @Content),
  })
  @PutMapping("/{id}")
  public ResponseEntity<Void> updateBook(
    @PathVariable Long id, 
    @RequestBody @Valid BookRequest request
  ) {
    bookService.updateBook(id, request);

    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Delete Book")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Book deleted successfully"),
    @ApiResponse(responseCode = "404", description = "Book not found", content = @Content),
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> responseEntity(@PathVariable Long id) {
    bookService.deleteBook(id);

    return ResponseEntity.ok(null);
  }
  
}
