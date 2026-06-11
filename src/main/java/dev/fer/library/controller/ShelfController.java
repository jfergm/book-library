package dev.fer.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.fer.library.dto.request.ShelfRequest;
import dev.fer.library.dto.response.ShelfResponse;
import dev.fer.library.service.ShelfService;

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

@Tag(name = "Shelves", description = "CRUD operation for Shelves")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/shelves")
public class ShelfController {

  private ShelfService shelfService;

  protected ShelfController(ShelfService shelfService) {
    this.shelfService = shelfService;
  }

  @Operation(summary = "Get Shelf")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Shelf fetched successfully"),
    @ApiResponse(responseCode = "404", description = "Shelf not found", content = @Content)
  })
  @GetMapping("/{id}")
  public ResponseEntity<ShelfResponse> getShelf(@PathVariable Long id) {
    ShelfResponse response = shelfService.getShelf(id);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Get Shelves")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Shelves fetched successfully"),
  })
  @GetMapping("")
  public ResponseEntity<List<ShelfResponse>> getShelves() {
    return ResponseEntity.ok(shelfService.getShelves());
  }
  
  @Operation(summary = "Add Shelf")
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201", 
      description = "Shelf created sucessfully", 
      headers = @Header(
        name = "Location",
        description = "Location with the resource created"
      )
    ),
    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
  })
  @PostMapping("")
  public ResponseEntity<Void> createShelf(@RequestBody @Valid ShelfRequest request) {
    ShelfResponse created = shelfService.createShelf(request);
    URI location = ServletUriComponentsBuilder
      .fromCurrentRequestUri()
      .path("/{id}")
      .buildAndExpand(created.id())
      .toUri();
    return ResponseEntity.created(location).build();
  }

  @Operation(summary = "Update Shelf")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Shelf updated successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
    @ApiResponse(responseCode = "404", description = "Shelf not found", content = @Content)
  })
  @PutMapping("/{id}")
  public ResponseEntity<Void> updateShelf(
    @PathVariable Long id, 
    @RequestBody @Valid ShelfRequest request
  ) {
    shelfService.updateShelf(id, request);

    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Delete Shelf")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Shelf deleted successfully"),
    @ApiResponse(responseCode = "404", description = "Shelf not found", content = @Content)
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteShelf(@PathVariable Long id) {
    shelfService.deleteShelf(id);
    return ResponseEntity.ok(null);
  } 
}
