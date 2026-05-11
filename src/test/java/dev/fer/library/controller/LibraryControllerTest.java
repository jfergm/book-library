package dev.fer.library.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.fer.library.entity.Library;
import dev.fer.library.exception.LibraryNotFoundException;
import dev.fer.library.service.LibraryService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(LibraryController.class)
public class LibraryControllerTest {
  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  LibraryService libraryService;

  private List<Library> libraries;

  @BeforeEach
  void setUp() {
    this.libraries = new ArrayList<>();

    libraries.add(new Library(1L, "Library 1"));
    libraries.add(new Library(2L, "Library 2"));
    libraries.add(new Library(3L, "Library 3"));
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

}
