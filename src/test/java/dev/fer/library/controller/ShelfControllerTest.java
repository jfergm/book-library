package dev.fer.library.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.fer.library.dto.response.ShelfResponse;
import dev.fer.library.exception.ShelfNotFoundException;
import dev.fer.library.service.ShelfService;

@WebMvcTest(ShelfController.class)
public class ShelfControllerTest {
  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  ShelfService shelfService;

  @Test
  void shouldReturnShelf() throws Exception {
    when(shelfService.getShelf(1L)).thenReturn(
      new ShelfResponse(1L, "A1", "Shelf A1", 1L)
    );

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
}
