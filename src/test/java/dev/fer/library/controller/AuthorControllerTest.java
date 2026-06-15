package dev.fer.library.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.fer.library.dto.request.AuthorRequest;
import dev.fer.library.dto.response.AuthorResponse;
import dev.fer.library.exception.AuthorNotFoundException;
import dev.fer.library.security.CustomUserDetailsService;
import dev.fer.library.security.SecurityConfig;
import dev.fer.library.service.AuthorService;
import dev.fer.library.service.JwtService;
import dev.fer.library.utils.TestUtils;

import static org.assertj.core.api.Assertions.assertThat;
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

@WebMvcTest(AuthorController.class)
@Import(SecurityConfig.class)
@WithMockUser
class AuthorControllerTest {
  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  AuthorService authorService;

  @MockitoBean
  CustomUserDetailsService customUserDetailsService;

  @MockitoBean
  JwtService jwtService;

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
    when(authorService.getAuthors(any(Pageable.class))).thenReturn(authors);

    mockMvc.perform(get("/authors"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(3)));
    
    verify(authorService).getAuthors(any(Pageable.class));
  }

  @Test
  void shouldPassPagination() throws Exception {
    when(authorService.getAuthors(any(Pageable.class))).thenReturn(authors);

    mockMvc
      .perform(get("/authors")
        .param("page", "2")
        .param("size", "15"))
      .andExpect(status().isOk());
      
    ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);

    verify(authorService).getAuthors(captor.capture());

    Pageable pageable = captor.getValue();

    assertThat(pageable.getPageNumber()).isEqualTo(2);
    assertThat(pageable.getPageSize()).isEqualTo(15);
    verify(authorService).getAuthors(any(Pageable.class));
  }

@Test
  void shouldPassDefaultPagination() throws Exception {
    when(authorService.getAuthors(any(Pageable.class))).thenReturn(authors);

    mockMvc
      .perform(get("/authors"))
      .andExpect(status().isOk());
      
    ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);

    verify(authorService).getAuthors(captor.capture());

    Pageable pageable = captor.getValue();

    assertThat(pageable.getPageNumber()).isEqualTo(0);
    assertThat(pageable.getPageSize()).isEqualTo(20);
    verify(authorService).getAuthors(any(Pageable.class));
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

    @Test
  void shouldReturnBadRequestWhenCreateWithInvalidData() throws Exception {
    AuthorRequest request = new AuthorRequest(null);

    mockMvc
      .perform(
        post("/authors")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(request)))
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnNoContentWhenUpdateAuthor() throws Exception {
    when(authorService.updateAuthor(anyLong(), any())).thenReturn(authors.getFirst());

    AuthorRequest request = new AuthorRequest("New Author name");

    mockMvc
      .perform(
        put("/authors/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(request)))
      .andExpect(status().isNoContent());
    
    verify(authorService).updateAuthor(anyLong(), any());
  }

  @Test
  void shouldReturnBadRequestWhenUpdateAuthorWithInvalidData() throws Exception {
    AuthorRequest request = new AuthorRequest("");

    mockMvc
      .perform(
        put("/authors/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(request)))
      .andExpect(status().isBadRequest());    
  }

  @Test
  void shouldReturnNotFoundWhenUpdateInvalidAuthor() throws Exception {
    when(authorService.updateAuthor(anyLong(), any())).thenThrow(AuthorNotFoundException.class);

    AuthorRequest request = new AuthorRequest("New Author name");

    mockMvc
      .perform(
        put("/authors/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(request)))
      .andExpect(status().isNotFound());
    
    verify(authorService).updateAuthor(anyLong(), any());
  }

  @Test
  void shouldReturnOkWhenDeleteAuthor() throws Exception {
    doNothing().when(authorService).deleteAuthor(1L);

    mockMvc.perform(delete("/authors/1"))
      .andExpect(status().isOk());
    
    verify(authorService).deleteAuthor(1L);
  }

  @Test
  void shouldReturnNotFoundWhenDeleteAuthor() throws Exception {
    doThrow(AuthorNotFoundException.class).when(authorService).deleteAuthor(1L);

    mockMvc.perform(delete("/authors/1"))
      .andExpect(status().isNotFound());
    
    verify(authorService).deleteAuthor(1L);
  }

  @Test
  @WithAnonymousUser
  void shouldReturnForbiddenWhenNoAuth() throws Exception {
    mockMvc.perform(get("/authors"))
      .andExpect(status().isForbidden());
  }
}
