package dev.fer.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.fer.library.dto.response.FloorResponse;
import dev.fer.library.service.FloorService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



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
  
  
}
