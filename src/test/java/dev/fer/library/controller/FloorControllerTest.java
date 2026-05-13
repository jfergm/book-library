package dev.fer.library.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.fer.library.dto.response.FloorResponse;
import dev.fer.library.exception.FloorNotFoundException;
import dev.fer.library.service.FloorService;

@WebMvcTest(FloorController.class)
public class FloorControllerTest {
  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  FloorService floorService;

  private List<FloorResponse> floors;

  @BeforeEach
  void setUp() {
    floors = new ArrayList<>();
    floors.add(new FloorResponse(1L, 1L, "1", "Floor description"));
    floors.add(new FloorResponse(2L, 1L, "2", "Floor description"));
    floors.add(new FloorResponse(3L, 1L, "3", "Floor description"));
    floors.add(new FloorResponse(3L, 2L, "1", "Floor description"));
  }


  @Test
  void shouldReturnFloor()throws Exception {
    when(floorService.getFloor(1L)).thenReturn(floors.getFirst());

    mockMvc.perform(get("/floors/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value("1"))
      .andExpect(jsonPath("$.code").value("1"))
      .andExpect(jsonPath("$.libraryId").value("1"))
      .andExpect(jsonPath("$.description").value("Floor description"));

    verify(floorService).getFloor(1L);
  }

  @Test
  void shouldReturnNotFoundWhenInvalidFloorId() throws Exception {
    when(floorService.getFloor(1L)).thenThrow(FloorNotFoundException.class);

    mockMvc.perform(get("/floors/1"))
      .andExpect(status().isNotFound());
  }
}
