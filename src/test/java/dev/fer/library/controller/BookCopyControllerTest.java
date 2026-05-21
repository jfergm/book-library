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

import dev.fer.library.dto.response.BookCopyResponse;
import dev.fer.library.exception.BookCopyNotFoundException;
import dev.fer.library.service.BookCopyService;

@WebMvcTest(BookCopyController.class)
public class BookCopyControllerTest {
  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  BookCopyService bookCopyService;

  @Test
  void shouldReturnBookCopy() throws Exception {
    when(bookCopyService.getBookCopy(1L)).thenReturn(
      new BookCopyResponse(1L, 1L, 1L, "bk123", "AVAILABLE")
    ); 

    mockMvc.perform(get("/book-copies/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value("1"))
      .andExpect(jsonPath("$.bookId").value("1"))
      .andExpect(jsonPath("$.shelfId").value("1"))
      .andExpect(jsonPath("$.code").value("bk123"))
      .andExpect(jsonPath("$.status").value("AVAILABLE"));

    verify(bookCopyService).getBookCopy(1L);
  }

  @Test
  void shouldReturnNotFoundWhenInvalidBookCopy() throws Exception {
    when(bookCopyService.getBookCopy(1L)).thenThrow(BookCopyNotFoundException.class); 

    mockMvc.perform(get("/book-copies/1"))
      .andExpect(status().isNotFound());

    verify(bookCopyService).getBookCopy(1L);
  }
}
