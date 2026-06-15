package dev.fer.library.controller;

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

import dev.fer.library.dto.request.ShelfRequest;
import dev.fer.library.dto.response.ShelfResponse;
import dev.fer.library.exception.BadRequestException;
import dev.fer.library.exception.ShelfNotFoundException;
import dev.fer.library.security.CustomUserDetailsService;
import dev.fer.library.security.SecurityConfig;
import dev.fer.library.service.JwtService;
import dev.fer.library.service.ShelfService;
import dev.fer.library.utils.TestUtils;

@WebMvcTest(ShelfController.class)
@Import(SecurityConfig.class)
@WithMockUser
class ShelfControllerTest {
  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  ShelfService shelfService;

  @MockitoBean
  CustomUserDetailsService customUserDetailsService;

  @MockitoBean
  JwtService jwtService;

  private List<ShelfResponse> shelves;

  @BeforeEach
  void setUp() {
    shelves = new ArrayList<>();

    shelves.add(new ShelfResponse(1L, "A1", "Shelf A1", 1L));
    shelves.add(new ShelfResponse(2L, "A2", "Shelf A2", 1L));
    shelves.add(new ShelfResponse(3L, "A3", "Shelf A3", 1L));
  }

  @Test
  void shouldReturnShelf() throws Exception {
    when(shelfService.getShelf(1L)).thenReturn(shelves.getFirst());

    mockMvc.perform(get("/shelves/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value("1"))
      .andExpect(jsonPath("$.code").value("A1"))
      .andExpect(jsonPath("$.label").value("Shelf A1"))
      .andExpect(jsonPath("$.bookcaseId").value("1"));

    verify(shelfService).getShelf(1L);
  }

  @Test
  void shouldReturnNotFoundWhenInvalidShelf() throws Exception {
    when(shelfService.getShelf(1L)).thenThrow(ShelfNotFoundException.class);

    mockMvc.perform(get("/shelves/1"))
      .andExpect(status().isNotFound());

    verify(shelfService).getShelf(1L);
  }

  @Test
  void shouldReturnShelvesList() throws Exception {
    when(shelfService.getShelves(any(Pageable.class))).thenReturn(shelves);

    mockMvc.perform(get("/shelves"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(3)));

    verify(shelfService).getShelves(any(Pageable.class));
  }

  @Test
  void shouldPassPagination() throws Exception {
    when(shelfService.getShelves(any(Pageable.class))).thenReturn(shelves);

    mockMvc
      .perform(get("/shelves")
        .param("page", "2")
        .param("size", "15"))
      .andExpect(status().isOk());

    ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);

    verify(shelfService).getShelves(captor.capture());

    Pageable pageable = captor.getValue();

    assertThat(pageable.getPageNumber()).isEqualTo(2);
    assertThat(pageable.getPageSize()).isEqualTo(15);

    verify(shelfService).getShelves(any(Pageable.class));
  }

  @Test
  void shouldPassDefaultPagination() throws Exception {
    when(shelfService.getShelves(any(Pageable.class))).thenReturn(shelves);

    mockMvc
      .perform(get("/shelves"))
      .andExpect(status().isOk());

    ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);

    verify(shelfService).getShelves(captor.capture());

    Pageable pageable = captor.getValue();

    assertThat(pageable.getPageNumber()).isZero();
    assertThat(pageable.getPageSize()).isEqualTo(20);

    verify(shelfService).getShelves(any(Pageable.class));
  }

  @Test
  void shouldReturnCreatedAndLocation() throws Exception {
    when(shelfService.createShelf(any())).thenReturn(shelves.getFirst());

    ShelfRequest request = new ShelfRequest("A1", "Shelf A1", 1L);

    mockMvc.perform(post("/shelves").contentType(MediaType.APPLICATION_JSON).content(TestUtils.objectAsJson(request)))
      .andExpect(status().isCreated())
      .andExpect(header().string("Location", containsString("/shelves/1")));
    
    verify(shelfService).createShelf(any());
  }

  @Test
  void shouldReturnBadRequestWhenCreateWithInvalidData() throws Exception {
    ShelfRequest request = new ShelfRequest("A1", "Shelf A1", null);

    mockMvc.perform(post("/shelves").contentType(MediaType.APPLICATION_JSON).content(TestUtils.objectAsJson(request)))
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequest() throws Exception {
    when(shelfService.createShelf(any())).thenThrow(BadRequestException.class);

    ShelfRequest request = new ShelfRequest("A1", "Shelf A1", 1L);

    mockMvc.perform(post("/shelves").contentType(MediaType.APPLICATION_JSON).content(TestUtils.objectAsJson(request)))
      .andExpect(status().isBadRequest());
    
    verify(shelfService).createShelf(any());
  }

  @Test
  void shouldReturnNoContentWhenSuccessUpdate() throws Exception {
    when(shelfService.updateShelf(anyLong(), any())).thenReturn(shelves.getFirst());

    ShelfRequest shelfRequest = new ShelfRequest("nc", "new code", 1L);

    mockMvc
      .perform(put("/shelves/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(shelfRequest)))
      .andExpect(status().isNoContent());

    verify(shelfService).updateShelf(anyLong(), any());
  }

  @Test
  void shouldReturnBadRequestWhenUpdateWithInvalidData() throws Exception {
    ShelfRequest shelfRequest = new ShelfRequest("nc", "new code", null);

    mockMvc
      .perform(put("/shelves/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(shelfRequest)))
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturn404WhenUpdateInvalidShelf() throws Exception {
    when(shelfService.updateShelf(anyLong(), any())).thenThrow(ShelfNotFoundException.class);

    ShelfRequest shelfRequest = new ShelfRequest("nc", "new code", 1L);

    mockMvc
      .perform(put("/shelves/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(shelfRequest)))
      .andExpect(status().isNotFound());

    verify(shelfService).updateShelf(anyLong(), any());
  }

  @Test
  void shouldReturn400WhenUpdateWithInvalidBookcase() throws Exception {
    when(shelfService.updateShelf(anyLong(), any())).thenThrow(BadRequestException.class);

    ShelfRequest shelfRequest = new ShelfRequest("nc", "new code", 1L);

    mockMvc
      .perform(put("/shelves/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(shelfRequest)))
      .andExpect(status().isBadRequest());

    verify(shelfService).updateShelf(anyLong(), any());
  }

  @Test
  void shouldReturnOkWhenSuccessDelte() throws Exception {
    doNothing().when(shelfService).deleteShelf(1L);

    mockMvc.perform(delete("/shelves/1"))
      .andExpect(status().isOk());
  }

  @Test
  void shouldReturnNotFoundWhenDeleteInvalidShelf() throws Exception {
    doThrow(ShelfNotFoundException.class).when(shelfService).deleteShelf(1L);

    mockMvc.perform(delete("/shelves/1"))
      .andExpect(status().isNotFound());
  }

  @Test
  @WithAnonymousUser
  void shouldReturnForbiddenWhenNoAuth() throws Exception {
    mockMvc.perform(get("/shelves"))
      .andExpect(status().isForbidden());
  }
}
