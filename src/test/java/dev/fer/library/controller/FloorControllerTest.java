package dev.fer.library.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.fer.library.dto.request.FloorRequest;
import dev.fer.library.dto.request.FloorUpdateRequest;
import dev.fer.library.dto.response.FloorResponse;
import dev.fer.library.exception.BadRequestException;
import dev.fer.library.exception.FloorNotFoundException;
import dev.fer.library.security.CustomUserDetailsService;
import dev.fer.library.security.SecurityConfig;
import dev.fer.library.service.FloorService;
import dev.fer.library.service.JwtService;
import dev.fer.library.utils.TestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@WebMvcTest(FloorController.class)
@Import(SecurityConfig.class)
@WithMockUser
class FloorControllerTest {
  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  FloorService floorService;

  @MockitoBean
  CustomUserDetailsService customUserDetailsService;

  @MockitoBean
  JwtService jwtService;

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

  @Test
  void shouldReturnFloorList() throws Exception {
    when(floorService.getFloors(any(Pageable.class))).thenReturn(floors);
    
    mockMvc.perform(get("/floors"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(4)));

    verify(floorService).getFloors(any(Pageable.class));
  }

  @Test
  void shouldPassPagination() throws Exception {
    when(floorService.getFloors(any(Pageable.class))).thenReturn(floors);
    
    mockMvc
      .perform(get("/floors")
        .param("page", "1")
        .param("size", "10"))
      .andExpect(status().isOk());
    
    ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);

    verify(floorService).getFloors(captor.capture());

    Pageable pageable = captor.getValue();

    assertThat(pageable.getPageNumber()).isEqualTo(1);
    assertThat(pageable.getPageSize()).isEqualTo(10);

    verify(floorService).getFloors(any(Pageable.class));
  }

  @Test
  void shouldPassPaginationWitDefaults() throws Exception {
    when(floorService.getFloors(any(Pageable.class))).thenReturn(floors);
    
    mockMvc
      .perform(get("/floors"))
      .andExpect(status().isOk());
    
    ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);

    verify(floorService).getFloors(captor.capture());

    Pageable pageable = captor.getValue();

    assertThat(pageable.getPageNumber()).isEqualTo(0);
    assertThat(pageable.getPageSize()).isEqualTo(20);

    verify(floorService).getFloors(any(Pageable.class));
  }

  @Test
  void shouldReturnCreatedAndLocation() throws Exception {
    FloorRequest floorReq = new FloorRequest(1L, "1w", "Description");

    when(floorService.createFloor(any())).thenReturn(floors.getFirst());

    mockMvc
      .perform(
        post("/floors")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(floorReq)))
      .andExpect(status().isCreated())
      .andExpect(header().string("Location", containsString("/floors/1")));

    verify(floorService).createFloor(any());
  }

  @Test
  void shouldReturnBadRequestCreatingWithInvalidRequest() throws Exception {
    FloorRequest floorReq = new FloorRequest(null, "1", "Description");

    when(floorService.createFloor(any())).thenReturn(floors.getFirst());

    mockMvc
      .perform(
        post("/floors")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(floorReq)))
      .andExpect(status().isBadRequest());

  }

  @Test
  void shouldReturn400WhenCreateWithInvalidLibrary() throws Exception {
    when(floorService.createFloor(any())).thenThrow(BadRequestException.class);
    FloorRequest floorReq = new FloorRequest(1L, "1", "Description");

    mockMvc
      .perform(
        post("/floors")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(floorReq)))
      .andExpect(status().isBadRequest());

  }

  @Test
  void shouldReturnNoContentWhenUpdate() throws Exception {
    when(floorService.updateFloor(anyLong(), any(FloorUpdateRequest.class))).thenReturn(floors.getFirst());
    mockMvc
      .perform(
        put("/floors/1").contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(new FloorUpdateRequest("1A", "Description"))))
      .andExpect(status().isNoContent());

    verify(floorService).updateFloor(anyLong(), any());
  }

  @Test
  void shouldReturnBadRequestUpdatingWithInvalidRequest() throws Exception {
    FloorUpdateRequest floorReq = new FloorUpdateRequest(null, "Description");

    when(floorService.createFloor(any())).thenReturn(floors.getFirst());

    mockMvc
      .perform(
        post("/floors")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(floorReq)))
      .andExpect(status().isBadRequest());

  }

  @Test
  void shouldReturnNotFoundWhenUpdateInvalidFloor() throws Exception {
    when(floorService.updateFloor(anyLong(), any(FloorUpdateRequest.class))).thenThrow(FloorNotFoundException.class);

    mockMvc
      .perform(
        put("/floors/1").contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(new FloorUpdateRequest("1A", "Description"))))
      .andExpect(status().isNotFound());

    verify(floorService).updateFloor(anyLong(), any());
  }

  @Test
  void shouldReturnOkWhenDeleteFloor() throws Exception {
    doNothing().when(floorService).deleteFloor(1L);

    mockMvc.perform(delete("/floors/1"))
      .andExpect(status().isOk());

    verify(floorService).deleteFloor(1L);
  }

  @Test
  void shouldReturnNotFoundWhenDeleteInvalidFloor() throws Exception {
    doThrow(FloorNotFoundException.class).when(floorService).deleteFloor(1L);

    mockMvc.perform(delete("/floors/1"))
      .andExpect(status().isNotFound());
    
    verify(floorService).deleteFloor(1L);
  }

  @Test
  @WithAnonymousUser
  void shouldReturnForbiddenWhenNoAuth() throws Exception {
    mockMvc.perform(get("/floors"))
      .andExpect(status().isForbidden());
  }
}
