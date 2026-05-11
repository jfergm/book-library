package dev.fer.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.fer.library.entity.Library;
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
  public ResponseEntity<List<Library>> getLibraries() {
    List<Library> libraries = libraryService.getLibraries();

    return ResponseEntity.ok(libraries);
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<Library> getLibary(@PathVariable Long id) {
    Library libary = libraryService.getLibaryByID(id);
    return ResponseEntity.ok(libary);
  }

  @PostMapping("")
  public ResponseEntity<Void> createLibrary(@RequestBody Library library) {
    Library added = libraryService.createLibrary(library);

    URI location = ServletUriComponentsBuilder
      .fromCurrentRequestUri()
			.path("/{id}").buildAndExpand(added.getId())
			.toUri();
    
      return ResponseEntity.created(location).build();
  }
  
  @PutMapping("/{id}")
  public ResponseEntity<Void> updateLibrary(@PathVariable Long id, @RequestBody Library update) {   
    libraryService.updateLibrary(id, update);   
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteLibrary(@PathVariable Long id) {
    libraryService.deleteLibrary(id);
    return ResponseEntity.ok(null);
  }
  
}
