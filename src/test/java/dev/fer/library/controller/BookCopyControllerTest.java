package dev.fer.library.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.fer.library.dto.request.BookCopyRequest;
import dev.fer.library.dto.request.BookCopyUpdateRequest;
import dev.fer.library.dto.response.BookCopyResponse;
import dev.fer.library.enums.BookCopyStatus;
import dev.fer.library.exception.BadRequestException;
import dev.fer.library.exception.BookCopyNotFoundException;
import dev.fer.library.service.BookCopyService;
import dev.fer.library.utils.TestUtils;

@WebMvcTest(BookCopyController.class)
public class BookCopyControllerTest {
  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  BookCopyService bookCopyService;

  private
  BookCopyResponse bookCopy = new BookCopyResponse(1L, 1L, 1L, "BK123", BookCopyStatus.PROCESSING.name());


  @Test
  void shouldReturnBookCopy() throws Exception {
    when(bookCopyService.getBookCopy(1L)).thenReturn(bookCopy); 

    mockMvc.perform(get("/book-copies/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value("1"))
      .andExpect(jsonPath("$.bookId").value("1"))
      .andExpect(jsonPath("$.shelfId").value("1"))
      .andExpect(jsonPath("$.code").value("BK123"))
      .andExpect(jsonPath("$.status").value("PROCESSING"));

    verify(bookCopyService).getBookCopy(1L);
  }

  @Test
  void shouldReturnNotFoundWhenInvalidBookCopy() throws Exception {
    when(bookCopyService.getBookCopy(1L)).thenThrow(BookCopyNotFoundException.class); 

    mockMvc.perform(get("/book-copies/1"))
      .andExpect(status().isNotFound());

    verify(bookCopyService).getBookCopy(1L);
  }

  @Test
  void shouldReturnCreatendAndLocation() throws Exception {
    when(bookCopyService.createBookCopy(any())).thenReturn(bookCopy);

    BookCopyRequest request = new BookCopyRequest(1L);

    mockMvc
      .perform(
        post("/book-copies")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(request)))
      .andExpect(status().isCreated())
      .andExpect(header().string("Location", containsString("/book-copies/1")));
  }

  @Test
  void shouldReturnNotFoundWhenCreateBookCopyWithInvalidBook() throws Exception {
    when(bookCopyService.createBookCopy(any())).thenThrow(BadRequestException.class);

    BookCopyRequest request = new BookCopyRequest(1L);

    mockMvc
      .perform(
        post("/book-copies")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(request)))
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnNoContentWhenUpdateBookCopy() throws Exception {
    when(bookCopyService.updateBookCopy(any(), any())).thenReturn(bookCopy);

    BookCopyUpdateRequest request = new BookCopyUpdateRequest("BKNWCODE123");

    mockMvc
      .perform(
        patch("/book-copies/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(request)))
      .andExpect(status().isNoContent());

    verify(bookCopyService).updateBookCopy(anyLong(), any());
  }

  @Test
  void shouldReturnNotFoundWhenUpdateInvalidBookCopy() throws Exception {
    when(bookCopyService.updateBookCopy(any(), any())).thenThrow(BookCopyNotFoundException.class);

    BookCopyUpdateRequest request = new BookCopyUpdateRequest("BKNWCODE123");

    mockMvc
      .perform(
        patch("/book-copies/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(request)))
      .andExpect(status().isNotFound());

    verify(bookCopyService).updateBookCopy(anyLong(), any());
  }
}
