package dev.fer.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.fer.library.dto.request.LibraryRequest;
import dev.fer.library.dto.response.LibraryResponse;
import dev.fer.library.service.LibraryService;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/libraries")
public class LibraryController {
  private LibraryService libraryService;

  protected LibraryController(LibraryService libraryService) {
    this.libraryService = libraryService;
  }

  @GetMapping("")
  public ResponseEntity<List<LibraryResponse>> getLibraries() {
    List<LibraryResponse> libraries = libraryService.getLibraries();

    return ResponseEntity.ok(libraries);
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<LibraryResponse> getLibary(@PathVariable Long id) {
    LibraryResponse libary = libraryService.getLibaryByID(id);
    return ResponseEntity.ok(libary);
  }

  @PostMapping("")
  public ResponseEntity<Void> createLibrary(@RequestBody LibraryRequest library) {
    LibraryResponse added = libraryService.createLibrary(library);

    URI location = ServletUriComponentsBuilder
      .fromCurrentRequestUri()
			.path("/{id}").buildAndExpand(added.id())
			.toUri();
    
      return ResponseEntity.created(location).build();
  }
  
  @PutMapping("/{id}")
  public ResponseEntity<Void> updateLibrary(@PathVariable Long id, @RequestBody LibraryRequest update) {   
    libraryService.updateLibrary(id, update);   
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteLibrary(@PathVariable Long id) {
    libraryService.deleteLibrary(id);
    return ResponseEntity.ok(null);
  }
  
}
