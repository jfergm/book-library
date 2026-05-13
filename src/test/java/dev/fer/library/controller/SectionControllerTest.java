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

import dev.fer.library.dto.response.SectionResponse;
import dev.fer.library.exception.SectionNotFoundException;
import dev.fer.library.service.SectionService;

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
}
