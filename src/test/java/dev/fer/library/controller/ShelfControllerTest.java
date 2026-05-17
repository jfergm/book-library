package dev.fer.library.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.fer.library.dto.request.ShelfRequest;
import dev.fer.library.dto.response.ShelfResponse;
import dev.fer.library.exception.BadRequestException;
import dev.fer.library.exception.ShelfNotFoundException;
import dev.fer.library.service.ShelfService;
import dev.fer.library.utils.TestUtils;

@WebMvcTest(ShelfController.class)
public class ShelfControllerTest {
  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  ShelfService shelfService;

  private List<ShelfResponse> shelves;

  @BeforeEach
  void setUp() {
    shelves = new ArrayList<>();

    shelves.add(new ShelfResponse(1L, "A1", "Shelf A1", 1L));
    shelves.add(new ShelfResponse(2L, "A2", "Shelf A2", 1L));
    shelves.add(new ShelfResponse(3L, "A3", "Shelf A3", 1L));
  }

  @Test
  void shouldReturnShelf() throws Exception {
    when(shelfService.getShelf(1L)).thenReturn(shelves.getFirst());

    mockMvc.perform(get("/shelves/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value("1"))
      .andExpect(jsonPath("$.code").value("A1"))
      .andExpect(jsonPath("$.label").value("Shelf A1"))
      .andExpect(jsonPath("$.bookcaseId").value("1"));

    verify(shelfService).getShelf(1L);
  }

  @Test
  void shouldReturnNotFoundWhenInvalidShelf() throws Exception {
    when(shelfService.getShelf(1L)).thenThrow(ShelfNotFoundException.class);

    mockMvc.perform(get("/shelves/1"))
      .andExpect(status().isNotFound());

    verify(shelfService).getShelf(1L);
  }

  @Test
  void shouldReturnShelvesList() throws Exception {
    when(shelfService.getShelves()).thenReturn(shelves);

    mockMvc.perform(get("/shelves"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(3)));

    verify(shelfService).getShelves();
  }

  @Test
  void shouldReturnCreatedAndLocation() throws Exception {
    when(shelfService.createShelf(any())).thenReturn(shelves.getFirst());

    ShelfRequest request = new ShelfRequest("A1", "Shelf A1", 1L);

    mockMvc.perform(post("/shelves").contentType(MediaType.APPLICATION_JSON).content(TestUtils.objectAsJson(request)))
      .andExpect(status().isCreated())
      .andExpect(header().string("Location", containsString("/shelves/1")));
    
    verify(shelfService).createShelf(any());
  }

  @Test
  void shouldReturnBadRequest() throws Exception {
    when(shelfService.createShelf(any())).thenThrow(BadRequestException.class);

    ShelfRequest request = new ShelfRequest("A1", "Shelf A1", 1L);

    mockMvc.perform(post("/shelves").contentType(MediaType.APPLICATION_JSON).content(TestUtils.objectAsJson(request)))
      .andExpect(status().isBadRequest());
    
    verify(shelfService).createShelf(any());
  }
}
