package dev.fer.library.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.fer.library.dto.request.LoanRequest;
import dev.fer.library.dto.response.LoanResponse;
import dev.fer.library.enums.LoanStatus;
import dev.fer.library.exception.BadRequestException;
import dev.fer.library.exception.LoanNotFoundException;
import dev.fer.library.security.CustomUserDetailsService;
import dev.fer.library.security.SecurityConfig;
import dev.fer.library.service.JwtService;
import dev.fer.library.service.LoanService;
import dev.fer.library.utils.TestUtils;

@WebMvcTest(LoanController.class)
@Import(SecurityConfig.class)
@WithMockUser
class LoanControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  CustomUserDetailsService customUserDetailsService;

  @MockitoBean
  JwtService jwtService;

  @MockitoBean
  LoanService loanService;

  private List<LoanResponse> loans;
  
  Date basDate = new Date(1779256800000L); // 20 may 2026

  @BeforeEach
  void setUp() {
    loans = new ArrayList<>();

    loans.add(
      new LoanResponse(
        1L, 
        1L, 
        1L, 
        basDate, 
        new Date(basDate.getTime() + 604800000), // + 7 days
        null, 
        LoanStatus.ACTIVE, 
        ""
      ) 
    );
    loans.add(
      new LoanResponse(
        2L, 
        2L, 
        2L, 
        basDate, 
        new Date(basDate.getTime() + 604800000), // + 7 days
        null, 
        LoanStatus.CANCELED, 
        ""
      ) 
    );
    loans.add(
      new LoanResponse(
        3L, 
        3L, 
        3L, 
        basDate, 
        new Date(basDate.getTime() + 604800000), // + 7 days
        null, 
        LoanStatus.CLOSED, 
        ""
      ) 
    );
  }

  @Test
  void shouldReturnLoan() throws Exception {
    
    when(loanService.getLoan(anyLong())).thenReturn(loans.getFirst());

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

  @Test
  void shouldReturnLoanList() throws Exception {
    when(loanService.getLoans()).thenReturn(loans);

    mockMvc.perform(get("/loans"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(3)));

    verify(loanService).getLoans();
  }

  @Test
  void shouldReturnCreatedAndLocation() throws Exception {
    when(loanService.createLoan(any())).thenReturn(loans.getFirst());

    LoanRequest loanRequest = new LoanRequest(1L, 1L, basDate, basDate, "");

    mockMvc
      .perform(
        post("/loans")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(loanRequest)))
      .andExpect(status().isCreated())
      .andExpect(header().string("Location", containsString("/loans/1")));
    
    verify(loanService).createLoan(any());
  }

  @Test
  void shouldReturnBadRequestWhenServiceThrow() throws Exception {
    when(loanService.createLoan(any())).thenThrow(BadRequestException.class);
    LoanRequest loanRequest = new LoanRequest(3L, 1L, basDate, basDate, "");

    mockMvc
      .perform(
        post("/loans")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(loanRequest)))
      .andExpect(status().isBadRequest());
    
    verify(loanService).createLoan(any());
  }

  @Test
  void shouldReturnCanceledLoan() throws Exception {
    when(loanService.cancelLoan(anyLong())).thenReturn(
      loans.get(1)
    );
    mockMvc.perform(post("/loans/2/cancel"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value("2"))
      .andExpect(jsonPath("$.status").value("CANCELED"));

    verify(loanService).cancelLoan(anyLong());
  }

  @Test
  void ShouldReturnNotFoundWhenCancelInvalidLoan() throws Exception {
    when(loanService.cancelLoan(anyLong())).thenThrow(LoanNotFoundException.class);
    mockMvc.perform(post("/loans/2/cancel"))
      .andExpect(status().isNotFound());

    verify(loanService).cancelLoan(anyLong());
  }

  @Test
  void ShouldReturnNotFoundWhenCancelClosedLoan() throws Exception {
    when(loanService.cancelLoan(anyLong())).thenThrow(BadRequestException.class);
    mockMvc.perform(post("/loans/2/cancel"))
      .andExpect(status().isBadRequest());

    verify(loanService).cancelLoan(anyLong());
  }

  @Test
  void shouldReturnClosedLoan() throws Exception {
    when(loanService.closeLoan(anyLong())).thenReturn(
      loans.get(2)
    );
    mockMvc.perform(post("/loans/3/close"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value("3"))
      .andExpect(jsonPath("$.status").value("CLOSED"));

    verify(loanService).closeLoan(anyLong());
  }

  @Test
  void shouldReturnNotFloundWhenCloseInvalidLoan() throws Exception {
    when(loanService.closeLoan(anyLong())).thenThrow(LoanNotFoundException.class);
    mockMvc.perform(post("/loans/2/close"))
      .andExpect(status().isNotFound());

    verify(loanService).closeLoan(anyLong());
  }

  @Test
  void shouldReturnBadRequestWhenCloseCanceledLoan() throws Exception {
    when(loanService.closeLoan(anyLong())).thenThrow(BadRequestException.class);
    mockMvc.perform(post("/loans/2/close"))
      .andExpect(status().isBadRequest());

    verify(loanService).closeLoan(anyLong());
  }
}
