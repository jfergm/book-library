package dev.fer.library.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.fer.library.dto.request.BookcaseRequest;
import dev.fer.library.dto.response.BookcaseResponse;
import dev.fer.library.exception.BadRequestException;
import dev.fer.library.exception.BookcaseNotFoundException;
import dev.fer.library.security.CustomUserDetailsService;
import dev.fer.library.security.SecurityConfig;
import dev.fer.library.service.BookcaseService;
import dev.fer.library.service.JwtService;
import dev.fer.library.utils.TestUtils;

@WebMvcTest(BookcaseController.class)
@Import(SecurityConfig.class)
@WithMockUser
class BookcaseControllerTest {
  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  BookcaseService bookcaseService;

  @MockitoBean
  CustomUserDetailsService customUserDetailsService;

  @MockitoBean
  JwtService jwtService;

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

  @Test
  void shouldReturnCreatedAndLocationHeader() throws Exception {
    when(bookcaseService.createBookcase(any())).thenReturn(bookcases.getFirst());

    BookcaseRequest req = new BookcaseRequest(1L, "1A", "Bookcase 1A");

    mockMvc
      .perform(
        post("/bookcases")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(req)))
      .andExpect(status().isCreated())
      .andExpect(header().string("Location", containsString("/bookcases/1")));

    verify(bookcaseService).createBookcase(any());
  }

  @Test
  void shouldReturnBadRequestWhenCreateWithInvalidData() throws Exception {
    BookcaseRequest req = new BookcaseRequest(null, "1A", "Bookcase 1A");

    mockMvc
      .perform(
        post("/bookcases")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(req)))
      .andExpect(status().isBadRequest());

  }

  @Test
  void shouldReturnBadRequestWhenInvalidSection() throws Exception {
    when(bookcaseService.createBookcase(any())).thenThrow(BadRequestException.class);

    BookcaseRequest req = new BookcaseRequest(1L, "1A", "Bookcase 1A");

    mockMvc
      .perform(
        post("/bookcases")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(req)))
      .andExpect(status().isBadRequest());

    verify(bookcaseService).createBookcase(any());
  }

  @Test
  void shouldReturnBadRequestWhenUpdateWithInvalidData() throws Exception {
    BookcaseRequest req = new BookcaseRequest(1L, "", "Bookcase 1A");

    mockMvc
      .perform(
        put("/bookcases/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(req)))
      .andExpect(status().isBadRequest());

  }

  @Test
  void shouldReturnNoContentWhenUpdate() throws Exception {
    when(bookcaseService.updateBookcase(anyLong(), any())).thenReturn(bookcases.getFirst());

    BookcaseRequest update = new BookcaseRequest(1L, "new", "new");
    mockMvc
      .perform(
        put("/bookcases/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(update)))
      .andExpect(status().isNoContent());

    verify(bookcaseService).updateBookcase(anyLong(), any());
  }

  @Test
  void shouldReturnNotFoundWhenUpdateWithInvalidBookcase() throws Exception {
    when(bookcaseService.updateBookcase(anyLong(), any())).thenThrow(BookcaseNotFoundException.class);

    BookcaseRequest update = new BookcaseRequest(1L, "new", "new");
    mockMvc
      .perform(
        put("/bookcases/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(update)))
      .andExpect(status().isNotFound());

    verify(bookcaseService).updateBookcase(anyLong(), any());
  }

  @Test
  void shouldReturnBadRequestWhenUpdateWithInvalidSection() throws Exception {
    when(bookcaseService.updateBookcase(anyLong(), any())).thenThrow(BadRequestException.class);

    BookcaseRequest update = new BookcaseRequest(1L, "new", "new");
    mockMvc
      .perform(
        put("/bookcases/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(update)))
      .andExpect(status().isBadRequest());

    verify(bookcaseService).updateBookcase(anyLong(), any());
  }

  @Test
  void shouldReturnOkWhenDeleteBookcase() throws Exception {
    doNothing().when(bookcaseService).deleteBookcase(1L);
    mockMvc.perform(delete("/bookcases/1"))
      .andExpect(status().isOk());

    verify(bookcaseService).deleteBookcase(1L);
  }

  @Test
  void shouldReturnNotFoundWhenDeleteInvalidBookcase() throws Exception {
    doThrow(BookcaseNotFoundException.class).when(bookcaseService).deleteBookcase(1L);
    mockMvc.perform(delete("/bookcases/1"))
      .andExpect(status().isNotFound());

    verify(bookcaseService).deleteBookcase(1L);
  }

  @Test
  @WithAnonymousUser
  void shouldReturnForbiddenWhenNoAuth() throws Exception {
    mockMvc.perform(get("/bookcases"))
      .andExpect(status().isForbidden());
  }

}
