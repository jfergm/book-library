package dev.fer.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.fer.library.dto.response.LoanResponse;
import dev.fer.library.service.LoanService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/loans")
public class LoanController {

  private LoanService loanService;

  protected LoanController(LoanService loanService) {
    this.loanService = loanService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<LoanResponse> getLoan(@PathVariable Long id) {
    LoanResponse response = loanService.getLoan(id);
    return ResponseEntity.ok(response);
  }

  @GetMapping("")
  public ResponseEntity<List<LoanResponse>> getLoans() {
    return ResponseEntity.ok(loanService.getLoans());
  } 
}
