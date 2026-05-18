package dev.fer.library.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.fer.library.dto.response.AuthorResponse;
import dev.fer.library.exception.AuthorNotFoundException;
import dev.fer.library.service.AuthorService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {
  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  AuthorService authorService;

  @Test
  void shouldReturnAuthor() throws Exception {
    when(authorService.getAuthor(1L)).thenReturn(new AuthorResponse(1L, "Rainbow Rowell"));

    mockMvc.perform(get("/authors/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value("1"))
      .andExpect(jsonPath("$.name").value("Rainbow Rowell"));

    verify(authorService).getAuthor(1L);
  }

  @Test
  void shouldReturnNotFoundWhenInvalidAuthor() throws Exception {
    when(authorService.getAuthor(1L)).thenThrow(AuthorNotFoundException.class);

    mockMvc.perform(get("/authors/1"))
      .andExpect(status().isNotFound());

    verify(authorService).getAuthor(1L);
  }
}
