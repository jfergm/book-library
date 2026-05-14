package dev.fer.library.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.fer.library.dto.request.SectionRequest;
import dev.fer.library.dto.request.SectionUpdateRequest;
import dev.fer.library.dto.response.SectionResponse;
import dev.fer.library.service.SectionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/sections")
public class SectionController {

  private SectionService sectionService;

  SectionController(SectionService sectionService) {
    this.sectionService = sectionService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<SectionResponse> getSection(@PathVariable Long id) {
    SectionResponse response = sectionService.getSection(id);
    return ResponseEntity.ok(response);
  }

  @GetMapping("")
  public ResponseEntity<List<SectionResponse>> getSections() {
    return ResponseEntity.ok(sectionService.getSections());
  }

  @PostMapping("")
  public ResponseEntity<Void> createSection(@RequestBody SectionRequest request) {
    SectionResponse response = sectionService.createSection(request);
    URI location = ServletUriComponentsBuilder
      .fromCurrentRequestUri()
      .path("/{id}")
      .buildAndExpand(response.id())
      .toUri();

    return ResponseEntity.created(location).build();
  }
  
  @PutMapping("/{id}")
  public ResponseEntity<Void> updateSection(@PathVariable Long id, @RequestBody SectionUpdateRequest update) {
    sectionService.updateSection(id, update);
    return ResponseEntity.noContent().build();
  }
}
