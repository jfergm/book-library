package dev.fer.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.fer.library.dto.request.FloorRequest;
import dev.fer.library.dto.request.FloorUpdateRequest;
import dev.fer.library.dto.response.FloorResponse;
import dev.fer.library.service.FloorService;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/floors")
public class FloorController {

  private FloorService floorService;

  FloorController(FloorService floorService) {
    this.floorService = floorService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<FloorResponse> getFloor(@PathVariable Long id) {
    FloorResponse floor = floorService.getFloor(id);
    return ResponseEntity.ok(floor);
  }

  @GetMapping("")
  public ResponseEntity<List<FloorResponse>> getFloors() {
    List<FloorResponse> floors = floorService.getFloors();

    return ResponseEntity.ok(floors);
  }
  
  @PostMapping("")
  public ResponseEntity<Void> createFloor(@RequestBody FloorRequest request) {
    FloorResponse created = floorService.createFloor(request);

    URI location = ServletUriComponentsBuilder
      .fromCurrentRequestUri()
      .path("/{id}")
      .build(created.id());

    return ResponseEntity.created(location).build();
  }
  
  @PutMapping("/{id}")
  public ResponseEntity<Void> updateFloor(@PathVariable Long id, @RequestBody FloorUpdateRequest update) {
      
    floorService.updateFloor(id, update);

    return ResponseEntity.noContent().build();
  }
}
