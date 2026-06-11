package dev.fer.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.fer.library.dto.request.LibraryRequest;
import dev.fer.library.dto.response.LibraryResponse;
import dev.fer.library.service.LibraryService;

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



@Tag(name = "Libraries", description = "CRUD Operations for managing Libraires")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/libraries")
public class LibraryController {
  private LibraryService libraryService;

  protected LibraryController(LibraryService libraryService) {
    this.libraryService = libraryService;
  }

  @Operation(summary = "Get libraries", description = "Get all Libraries")
  @ApiResponses(
    @ApiResponse(responseCode = "200", description = "Libraries fetched successfully")
  )
  @GetMapping("")
  public ResponseEntity<List<LibraryResponse>> getLibraries() {
    List<LibraryResponse> libraries = libraryService.getLibraries();

    return ResponseEntity.ok(libraries);
  }
  
  @Operation(summary = "Get Library", description = "Get specific Library by ID")
  @ApiResponses( value = {
    @ApiResponse(responseCode = "200", description = "Library fetched successfully"),
    @ApiResponse(responseCode = "404", description = "Library not found", content = @Content)
  })
  @GetMapping("/{id}")
  public ResponseEntity<LibraryResponse> getLibary(@PathVariable Long id) {
    LibraryResponse libary = libraryService.getLibaryByID(id);
    return ResponseEntity.ok(libary);
  }

  @Operation(summary = "Add Library", description = "Add a new Library")
  @ApiResponses( value = {
    @ApiResponse(responseCode = "201", description = "Library created sucessfully", headers = @Header(
      name = "Location",
      description = "Location with the resource created"
    )),
    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
  })
  @PostMapping("")
  public ResponseEntity<Void> createLibrary(@RequestBody @Valid LibraryRequest library) {
    LibraryResponse added = libraryService.createLibrary(library);

    URI location = ServletUriComponentsBuilder
      .fromCurrentRequestUri()
			.path("/{id}").buildAndExpand(added.id())
			.toUri();
    
      return ResponseEntity.created(location).build();
  }
  
  @Operation(summary = "Update Library")
  @ApiResponses( value = {
    @ApiResponse(responseCode = "204", description = "Library updated successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
    @ApiResponse(responseCode = "404", description = "Library not found", content = @Content)
  })
  @PutMapping("/{id}")
  public ResponseEntity<Void> updateLibrary(
    @PathVariable Long id, 
    @RequestBody @Valid LibraryRequest update
  ) {   
    libraryService.updateLibrary(id, update);   
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Delete Library")
  @ApiResponses( value = {
    @ApiResponse(responseCode = "200", description = "Library deleted successfully"),
    @ApiResponse(responseCode = "404", description = "Library not found", content = @Content)
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteLibrary(@PathVariable Long id) {
    libraryService.deleteLibrary(id);
    return ResponseEntity.ok(null);
  }
  
}
