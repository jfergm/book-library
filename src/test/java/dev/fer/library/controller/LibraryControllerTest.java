package dev.fer.library.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.fer.library.dto.request.LibraryRequest;
import dev.fer.library.dto.response.LibraryResponse;
import dev.fer.library.exception.LibraryNotFoundException;
import dev.fer.library.security.CustomUserDetailsService;
import dev.fer.library.security.SecurityConfig;
import dev.fer.library.service.JwtService;
import dev.fer.library.service.LibraryService;
import dev.fer.library.utils.TestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(LibraryController.class)
@Import(SecurityConfig.class)
@WithMockUser
public class LibraryControllerTest {
  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  LibraryService libraryService;

  @MockitoBean
  CustomUserDetailsService customUserDetailsService;

  @MockitoBean
  JwtService jwtService;

  private List<LibraryResponse> libraries;

  @BeforeEach
  void setUp() {
    this.libraries = new ArrayList<>();

    libraries.add(new LibraryResponse(1L, "Library 1"));
    libraries.add(new LibraryResponse(2L, "Library 2"));
    libraries.add(new LibraryResponse(3L, "Library 3"));
  }

  @Test
  void shouldReturnLibrary() throws Exception {
    when(libraryService.getLibaryByID(1L)).thenReturn(libraries.getFirst());

    mockMvc.perform(get("/libraries/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value("1"))
      .andExpect(jsonPath("$.name").value("Library 1"));
  }

  @Test
  void shouldReturnNotFoundWhenInvalidLibraryID() throws Exception {
    when(libraryService.getLibaryByID(1L)).thenThrow(LibraryNotFoundException.class);

    mockMvc.perform(get("/libraries/1"))
      .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnListOfLibraries() throws Exception {
    when(libraryService.getLibraries()).thenReturn(libraries);

    mockMvc.perform(get("/libraries"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(3)));
  }

  @Test
  void shouldReturnCreatedAndLocation() throws Exception {
    LibraryRequest libReq = new LibraryRequest("Library");

    when(libraryService.createLibrary(any())).thenReturn(libraries.getFirst());

    mockMvc
      .perform(
        post("/libraries")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(libReq)))
      .andExpect(status().isCreated())
      .andExpect(header().string("Location", containsString("/libraries/1")));

    verify(libraryService).createLibrary(any());
  }

  @Test
  void shouldReturnNoContentWhenUpdateLibrary() throws Exception {
    when(libraryService.updateLibrary(anyLong(), any(LibraryRequest.class))).thenReturn(libraries.getFirst());
    
    mockMvc
      .perform(
        put("/libraries/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(libraries.getFirst())))
      .andExpect(status().isNoContent());

    verify(libraryService).updateLibrary(anyLong(), any(LibraryRequest.class));
  }

  @Test
  void shouldReturnNotFoundWhenUpdateInvalidLibrary() throws Exception {
    when(libraryService.updateLibrary(anyLong(), any(LibraryRequest.class)))
      .thenThrow(LibraryNotFoundException.class);
    
    mockMvc
      .perform(
        put("/libraries/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(libraries.getFirst())))
      .andExpect(status().isNotFound());

    verify(libraryService).updateLibrary(anyLong(), any(LibraryRequest.class));
  }

  @Test
  void shouldReturnOkWhenDeleteLibrary() throws Exception {
    doNothing().when(libraryService).deleteLibrary(1L);

    mockMvc.perform(delete("/libraries/1"))
      .andExpect(status().isOk());

    verify(libraryService).deleteLibrary(1L);
  }

  @Test
  void shouldReturnNotFoundWhenDeleteInvalidLibrary() throws Exception {
    doThrow(LibraryNotFoundException.class).when(libraryService).deleteLibrary(1L);

    mockMvc.perform(delete("/libraries/1"))
      .andExpect(status().isNotFound());
    
    verify(libraryService).deleteLibrary(1L);
  }

}
