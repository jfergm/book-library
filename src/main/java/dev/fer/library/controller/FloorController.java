package dev.fer.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.fer.library.dto.request.FloorRequest;
import dev.fer.library.dto.request.FloorUpdateRequest;
import dev.fer.library.dto.response.FloorResponse;
import dev.fer.library.service.FloorService;

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

@Tag(name = "Floors", description = "CRUD Operations for Floors")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/floors")
public class FloorController {

  private FloorService floorService;

  FloorController(FloorService floorService) {
    this.floorService = floorService;
  }

  @Operation(summary = "Get Floor", description = "Get specific Floor by ID")
  @ApiResponses( value = {
    @ApiResponse(responseCode = "200", description = "Floor fetched successfully"),
    @ApiResponse(responseCode = "404", description = "Floor not found", content = @Content)
  })
  @GetMapping("/{id}")
  public ResponseEntity<FloorResponse> getFloor(@PathVariable Long id) {
    FloorResponse floor = floorService.getFloor(id);
    return ResponseEntity.ok(floor);
  }

  @Operation(summary = "Get Floors", description = "Get all Floors")
  @ApiResponses( value = {
    @ApiResponse(responseCode = "200", description = "Floors fetched successfully")
  })
  @GetMapping("")
  public ResponseEntity<List<FloorResponse>> getFloors() {
    List<FloorResponse> floors = floorService.getFloors();

    return ResponseEntity.ok(floors);
  }
  
  @Operation(summary = "Add Floor", description = "Add a new floor")
  @ApiResponses( value = {
    @ApiResponse(
      responseCode = "201", 
      description = "Floor added successfully", 
      headers = @Header(
        name = "Location",
        description = "Location with the resource created"
      )
    ),
    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
  })
  @PostMapping("")
  public ResponseEntity<Void> createFloor(@RequestBody @Valid FloorRequest request) {
    FloorResponse created = floorService.createFloor(request);

    URI location = ServletUriComponentsBuilder
      .fromCurrentRequestUri()
      .path("/{id}")
      .build(created.id());

    return ResponseEntity.created(location).build();
  }
  
  @Operation(summary = "Update Floor")
  @ApiResponses( value = {
    @ApiResponse(responseCode = "204", description = "Floor updated successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
    @ApiResponse(responseCode = "404", description = "Floor not found", content = @Content)
  })
  @PutMapping("/{id}")
  public ResponseEntity<Void> updateFloor(
    @PathVariable Long id, 
    @RequestBody FloorUpdateRequest update
  ) {
      
    floorService.updateFloor(id, update);

    return ResponseEntity.noContent().build();
  }
  @Operation(summary = "Delete Floor")
  @ApiResponses( value = {
    @ApiResponse(responseCode = "200", description = "Floor deleted successfully"),
    @ApiResponse(responseCode = "404", description = "Floor not found", content = @Content)
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteFloor(@PathVariable Long id) {
    floorService.deleteFloor(id);

    return ResponseEntity.ok(null);
  }
}
