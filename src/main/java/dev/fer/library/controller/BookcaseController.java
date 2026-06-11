package dev.fer.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.fer.library.dto.request.BookcaseRequest;
import dev.fer.library.dto.response.BookcaseResponse;
import dev.fer.library.service.BookcaseService;

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

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "Bookcases", description = "CRUD operation for Bookcases")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/bookcases")
public class BookcaseController {

  private BookcaseService bookcaseService;

  BookcaseController(BookcaseService bookcaseService) {
    this.bookcaseService = bookcaseService;
  }

  @Operation(summary = "Get Bookcase", description = "Get Bookcase by ID")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Bookcase fetched successfully"),
    @ApiResponse(responseCode = "404", description = "Bookcase not found", content = @Content)
  })
  @GetMapping("/{id}")
  public ResponseEntity<BookcaseResponse> getBookcase(@PathVariable Long id) {
    BookcaseResponse res = bookcaseService.getBookcase(id);
    return ResponseEntity.ok(res);
  }

  @Operation(summary = "Get Bookcases", description = "Get all Bookcases")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Bookcases fetched successfully"),
  })
  @GetMapping("")
  public ResponseEntity<List<BookcaseResponse>> getBookcases() {
    return ResponseEntity.ok(bookcaseService.getBookcases());
  }

  @Operation(summary = "Add Bookcase", description = "Add new Bookcase")
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201", 
      description = "Bookcase created sucessfully", 
      headers = @Header(
        name = "Location",
        description = "Location with the resource created"
      )
    ),
    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
  })
  @PostMapping("")
  public ResponseEntity<Void> createBookcase(@RequestBody @Valid BookcaseRequest request) {
    BookcaseResponse created = bookcaseService.createBookcase(request);

    URI location = ServletUriComponentsBuilder
      .fromCurrentRequestUri()
      .path("/{id}")
      .buildAndExpand(created.id())
      .toUri();
      
    return ResponseEntity.created(location).build();
  }

  @Operation(summary = "Update Bookcase")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Bookcase updated successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
    @ApiResponse(responseCode = "404", description = "Bookcase not found", content = @Content)
  })
  @PutMapping("/{id}")
  public ResponseEntity<Void> updateBookcase(
    @PathVariable long id, 
    @RequestBody @Valid BookcaseRequest request
  ) {
    bookcaseService.updateBookcase(id, request);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Delete Bookcase")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Bookcase deleted successfully"),
    @ApiResponse(responseCode = "404", description = "Bookcase not found", content = @Content)
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBookcase(@PathVariable Long id) {
    bookcaseService.deleteBookcase(id);
    return ResponseEntity.ok(null);
  }
}
