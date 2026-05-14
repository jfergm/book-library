package dev.fer.library.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.fer.library.dto.request.SectionRequest;
import dev.fer.library.dto.response.SectionResponse;
import dev.fer.library.exception.BadRequestException;
import dev.fer.library.exception.SectionNotFoundException;
import dev.fer.library.service.SectionService;
import dev.fer.library.utils.TestUtils;

@WebMvcTest(SectionController.class)
public class SectionControllerTest {
  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  SectionService sectionService;

  private List<SectionResponse> sections;

  @BeforeEach
  void setUp() {
    sections = new ArrayList<>();
    sections.add(new SectionResponse(1L, 1L, "lit", "Literature", ""));
    sections.add(new SectionResponse(2L, 1L, "ns", "Natural science", ""));
    sections.add(new SectionResponse(3L, 1L, "psc", "Psicology", ""));
  }

  @Test
  void shouldReturnSection() throws Exception {
    when(sectionService.getSection(1L)).thenReturn(sections.getFirst());

    mockMvc.perform(get("/sections/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value("1"))
      .andExpect(jsonPath("$.floorId").value("1"))
      .andExpect(jsonPath("$.code").value("lit"))
      .andExpect(jsonPath("$.label").value("Literature"))
      .andExpect(jsonPath("$.description").value(""));
    
    verify(sectionService).getSection(1L);
  }

  @Test
  void shouldReturnNotFoundWhenInvalidSectionId() throws Exception {
    when(sectionService.getSection(1L)).thenThrow(SectionNotFoundException.class);

    mockMvc.perform(get("/sections/1"))
      .andExpect(status().isNotFound());
    
    verify(sectionService).getSection(1L);
  }

  @Test
  void shouldReturnSectionList() throws Exception {
    when(sectionService.getSections()).thenReturn(sections);

    mockMvc.perform(get("/sections"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(3)));
    
    verify(sectionService).getSections();
  }

  @Test
  void shouldReturnCreatedAndLocation() throws Exception {
    SectionRequest request = new SectionRequest(
      1L,
      "lit",
      "Literature",
      "Description"
    );

    when(sectionService.createSection(any(SectionRequest.class))).thenReturn(sections.getFirst());
    mockMvc.perform(post("/sections").contentType(MediaType.APPLICATION_JSON).content(TestUtils.objectAsJson(request)))
      .andExpect(status().isCreated())
      .andExpect(header().string("Location", containsString("/sections/1")));
  
    verify(sectionService).createSection(any(SectionRequest.class));  
  }

  @Test
  void shouldReturnBadRequestWhenInvalidFloor() throws Exception {
    SectionRequest request = new SectionRequest(
      1L,
      "lit",
      "Literature",
      "Description"
    );

    when(sectionService.createSection(any(SectionRequest.class))).thenThrow(BadRequestException.class);
    
    mockMvc.perform(post("/sections").contentType(MediaType.APPLICATION_JSON).content(TestUtils.objectAsJson(request)))
      .andExpect(status().isBadRequest());
  
    verify(sectionService).createSection(any(SectionRequest.class));  
  }

  @Test
  void shouldReturnNoContentWhenUpdateSection() throws Exception {
    when(sectionService.updateSection(anyLong(), any())).thenReturn(sections.getFirst());
    SectionRequest request = new SectionRequest(1L, "code", "label", "description");
    mockMvc
      .perform(
        put("/sections/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(request)))
      .andExpect(status().isNoContent());

    verify(sectionService).updateSection(anyLong(), any());
  }

  @Test
  void shouldReturnNotFoundWhenUpdateInvalidSection() throws Exception {
    when(sectionService.updateSection(anyLong(), any())).thenThrow(SectionNotFoundException.class);
    SectionRequest request = new SectionRequest(1L, "code", "label", "description");
    mockMvc
      .perform(
        put("/sections/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(request)))
      .andExpect(status().isNotFound());

    verify(sectionService).updateSection(anyLong(), any());
  }

  @Test
  void shouldReturnNotFoundWhenUpdateInvalidFloor() throws Exception {
    when(sectionService.updateSection(anyLong(), any())).thenThrow(BadRequestException.class);
    SectionRequest request = new SectionRequest(1L, "code", "label", "description");
    mockMvc
      .perform(
        put("/sections/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(request)))
      .andExpect(status().isBadRequest());

    verify(sectionService).updateSection(anyLong(), any());
  }
}
