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

import dev.fer.library.dto.response.BookcaseResponse;
import dev.fer.library.exception.BookcaseNotFoundException;
import dev.fer.library.service.BookcaseService;

@WebMvcTest(BookcaseController.class)
public class BookcaseControllerTest {
  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  BookcaseService bookcaseService;

  @Test
  void shouldReturnShelf() throws Exception {
    when(bookcaseService.getBookcase(1L)).thenReturn(
      new BookcaseResponse(1L, "1A", "Bookcase 1A", 1L)
    );
    mockMvc.perform(get("/bookcases/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value("1"))
      .andExpect(jsonPath("$.code").value("1A"))
      .andExpect(jsonPath("$.label").value("Bookcase 1A"))
      .andExpect(jsonPath("$.sectionId").value("1"));

    verify(bookcaseService).getBookcase(1L);
  }

  @Test
  void shouldReturnNotFoundWhenInvalidId() throws Exception {
    when(bookcaseService.getBookcase(999L)).thenThrow(BookcaseNotFoundException.class);
    mockMvc.perform(get("/bookcases/999"))
      .andExpect(status().isNotFound());
    
    verify(bookcaseService).getBookcase(999L);
  }
}
