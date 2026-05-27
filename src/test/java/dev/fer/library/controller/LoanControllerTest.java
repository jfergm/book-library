package dev.fer.library.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.fer.library.dto.response.LoanResponse;
import dev.fer.library.enums.LoanStatus;
import dev.fer.library.exception.LoanNotFoundException;
import dev.fer.library.security.CustomUserDetailsService;
import dev.fer.library.security.SecurityConfig;
import dev.fer.library.service.JwtService;
import dev.fer.library.service.LoanService;

@WebMvcTest(LoanController.class)
@Import(SecurityConfig.class)
@WithMockUser
public class LoanControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  CustomUserDetailsService customUserDetailsService;

  @MockitoBean
  JwtService jwtService;

  @MockitoBean
  LoanService loanService;

  @Test
  void shouldReturnLoan() throws Exception {
    Date date = new Date(1779256800000L); // 2026 may 20
    when(loanService.getLoan(anyLong())).thenReturn(
      new LoanResponse(
        1L, 
        1L, 
        1L, 
        date, 
        new Date(date.getTime() + 604800000), 
        null, 
        LoanStatus.ACTIVE, 
        ""
      )    
    );

    mockMvc.perform(get("/loans/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value("1"))
      .andExpect(jsonPath("$.userId").value("1"))
      .andExpect(jsonPath("$.loanDate").value(containsString("2026-05-20")))
      .andExpect(jsonPath("$.dueDate").value(containsString("2026-05-27")))
      .andExpect(jsonPath("$.returnDate").value(nullValue()))
      .andExpect(jsonPath("$.status").value(LoanStatus.ACTIVE.name()))
      .andExpect(jsonPath("$.notes").isEmpty());

    verify(loanService).getLoan(anyLong());
  }

  @Test
  void shouldReturnNotFoundWhenInvalidLoan() throws Exception {
    when(loanService.getLoan(anyLong())).thenThrow(LoanNotFoundException.class);

    mockMvc.perform(get("/loans/1"))
      .andExpect(status().isNotFound());

    verify(loanService).getLoan(anyLong());
  }
}
