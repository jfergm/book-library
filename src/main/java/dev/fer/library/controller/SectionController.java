package dev.fer.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.fer.library.dto.response.SectionResponse;
import dev.fer.library.service.SectionService;

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
}
