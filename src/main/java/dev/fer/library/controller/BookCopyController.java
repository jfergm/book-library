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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "Book Copies", description = "Operations for managing Book Copies")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/book-copies")
public class BookCopyController {
  private BookCopyService bookCopyService;

  protected BookCopyController(BookCopyService bookCopyService) {
    this.bookCopyService = bookCopyService;
  }

  @Operation(summary = "Get Book Copy")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Book Copy fetched successfully"),
    @ApiResponse(responseCode = "404", description = "Book Copy not found", content = @Content),
  })
  @GetMapping("/{id}")
  public ResponseEntity<BookCopyResponse> getBookCopy(@PathVariable Long id) {
    BookCopyResponse bookCopy = bookCopyService.getBookCopy(id);
    return ResponseEntity.ok(bookCopy);
  }

  @Operation(summary = "Add Book Copy")
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201", 
      description = "Book Copy created sucessfully", 
      headers = @Header(
        name = "Location",
        description = "Location with the resource created"
      )
    ),
    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
  })
  @PostMapping("")
  public ResponseEntity<Void> createBookCopy(@RequestBody @Valid BookCopyRequest request) {
    BookCopyResponse response = bookCopyService.createBookCopy(request);

    URI location = ServletUriComponentsBuilder
      .fromCurrentRequestUri()
      .path("/{id}")
      .buildAndExpand(response.id())
      .toUri();

    return ResponseEntity.created(location).build();
  }

  @Operation(summary = "Update Book Copy")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Book Copy updated successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
    @ApiResponse(responseCode = "404", description = "Book Copy not found", content = @Content),
  })
  @PatchMapping("/{id}")
  public ResponseEntity<Void> updateBookCopy(
    @PathVariable Long id, 
    @RequestBody @Valid BookCopyUpdateRequest request
  ) {
    bookCopyService.updateBookCopy(id, request);

    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Update Book Copy Shelf", description = "Update the shelf where is located the Book Copy")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Book Copy shelf updated successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
    @ApiResponse(responseCode = "404", description = "Book Copy not found", content = @Content),
  })
  @PutMapping("/{id}/shelf")
  public ResponseEntity<Void> updateBookCopyShelf(
    @PathVariable Long id, 
    @RequestBody @Valid BookCopyUpdateShelfRequest request
  ) {

    bookCopyService.updateBookCopyShelf(id, request);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Delete Book Copy Shelf")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Book Copy deleted successfully"),
    @ApiResponse(responseCode = "404", description = "Book Copy not found", content = @Content),
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBookCopy(@PathVariable Long id) {
    bookCopyService.deleteBookCopy(id);

    return ResponseEntity.ok(null);
  } 
  
}
