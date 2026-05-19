package dev.fer.library.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.fer.library.dto.request.AuthorRequest;
import dev.fer.library.dto.response.AuthorResponse;
import dev.fer.library.exception.AuthorNotFoundException;
import dev.fer.library.service.AuthorService;
import dev.fer.library.utils.TestUtils;

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

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {
  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  AuthorService authorService;

  private List<AuthorResponse> authors;

  @BeforeEach
  void setUp() {
    authors = new ArrayList<>();

    authors.add(new AuthorResponse(1L, "Rainbow Rowell"));
    authors.add(new AuthorResponse(2L, "Haruki Murakami"));
    authors.add(new AuthorResponse(3L, "Julio Cortazar"));
  }

  @Test
  void shouldReturnAuthor() throws Exception {
    when(authorService.getAuthor(1L)).thenReturn(authors.getFirst());

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

  @Test
  void shouldReturnAuthorList() throws Exception {
    when(authorService.getAuthors()).thenReturn(authors);

    mockMvc.perform(get("/authors"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(3)));
    
    verify(authorService).getAuthors();
  }

  @Test
  void shouldReturnCreatedAndLocation() throws Exception {
    when(authorService.createAuthor(any())).thenReturn(authors.getFirst());

    AuthorRequest request = new AuthorRequest("Rowell Rainbow");

    mockMvc.perform(post("/authors").contentType(MediaType.APPLICATION_JSON).content(TestUtils.objectAsJson(request)))
      .andExpect(status().isCreated())
      .andExpect(header().string("Location", containsString("/authors/1")));

    verify(authorService).createAuthor(any());
  }
}
