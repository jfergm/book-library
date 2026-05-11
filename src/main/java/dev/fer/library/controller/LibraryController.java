package dev.fer.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.fer.library.entity.Library;
import dev.fer.library.service.LibraryService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/libraries")
public class LibraryController {
  private LibraryService libraryService;

  protected LibraryController(LibraryService libraryService) {
    this.libraryService = libraryService;
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<Library> getLibary(@PathVariable Long id) {
    Library libary = libraryService.getLibaryByID(id);
    return ResponseEntity.ok(libary);
  }
  
}
