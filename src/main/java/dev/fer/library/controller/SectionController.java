package dev.fer.library.controller;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.fer.library.dto.request.SectionRequest;
import dev.fer.library.dto.request.SectionUpdateRequest;
import dev.fer.library.dto.response.SectionResponse;
import dev.fer.library.service.SectionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "Sections", description = "CRUD Operation for Sections")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/sections")
public class SectionController {

  private SectionService sectionService;

  SectionController(SectionService sectionService) {
    this.sectionService = sectionService;
  }

  @Operation(summary = "Get Section", description = "Get Section by ID")
  @ApiResponses( value = {
    @ApiResponse(responseCode = "200", description = "Section fetched successfully"),
    @ApiResponse(responseCode = "404", description = "Section not found", content = @Content)
  })
  @GetMapping("/{id}")
  public ResponseEntity<SectionResponse> getSection(@PathVariable Long id) {
    SectionResponse response = sectionService.getSection(id);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Get Sections", description = "Get all sections")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Sections fetched successfully")
  })
  @GetMapping("")
  public ResponseEntity<List<SectionResponse>> getSections(Pageable pageable) {
    return ResponseEntity.ok(sectionService.getSections(pageable));
  }

  @Operation(summary = "Add Section", description = "Add new Section")
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201", 
      description = "Section created sucessfully", 
      headers = @Header(
        name = "Location",
        description = "Location with the resource created"
      )
    ),
    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
  })
  @PostMapping("")
  public ResponseEntity<Void> createSection(@RequestBody @Valid SectionRequest request) {
    SectionResponse response = sectionService.createSection(request);
    URI location = ServletUriComponentsBuilder
      .fromCurrentRequestUri()
      .path("/{id}")
      .buildAndExpand(response.id())
      .toUri();

    return ResponseEntity.created(location).build();
  }
  
  @Operation(summary = "Update Section")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Section updated successfully"),
    @ApiResponse(responseCode = "400", description = "Bad request data", content = @Content),
    @ApiResponse(responseCode = "404", description = "Section not found", content = @Content)
  })
  @PutMapping("/{id}")
  public ResponseEntity<Void> updateSection(
    @PathVariable Long id, 
    @RequestBody @Valid SectionUpdateRequest update
  ) {
    sectionService.updateSection(id, update);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Delete Section")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Section deleted successfully"),
    @ApiResponse(responseCode = "404", description = "Section not found", content = @Content)
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteSection(@PathVariable Long id) {
    sectionService.deleteSection(id);
    return ResponseEntity.ok(null);
  }
}
