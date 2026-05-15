package dev.fer.library.controller;

import static org.hamcrest.Matchers.hasSize;
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

import dev.fer.library.dto.response.BookcaseResponse;
import dev.fer.library.exception.BookcaseNotFoundException;
import dev.fer.library.service.BookcaseService;

@WebMvcTest(BookcaseController.class)
public class BookcaseControllerTest {
  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  BookcaseService bookcaseService;

  private
  List<BookcaseResponse> bookcases;

  @BeforeEach
  void setUp() {
    bookcases = new ArrayList<>();

    bookcases.add(new BookcaseResponse(1L, "1A", "Bookcase 1A", 1L));
    bookcases.add(new BookcaseResponse(2L, "2A", "Bookcase 2A", 1L));
    bookcases.add(new BookcaseResponse(3L, "3A", "Bookcase 3A", 1L));
  }

  @Test
  void shouldReturnShelf() throws Exception {
    when(bookcaseService.getBookcase(1L)).thenReturn(bookcases.getFirst());
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

  @Test
  void shouldReturnList() throws Exception {
    when(bookcaseService.getBookcases()).thenReturn(bookcases);

    mockMvc.perform(get("/bookcases"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(3)));

    verify(bookcaseService).getBookcases();
  }
}
