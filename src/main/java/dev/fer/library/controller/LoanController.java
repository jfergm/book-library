package dev.fer.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.fer.library.dto.request.LoanRequest;
import dev.fer.library.dto.response.LoanResponse;
import dev.fer.library.service.LoanService;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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

  @PostMapping("")
  public ResponseEntity<Void> createLoan(@RequestBody LoanRequest request) {
    LoanResponse created = loanService.createLoan(request);

    URI location = ServletUriComponentsBuilder
      .fromCurrentRequestUri()
      .path("/{id}")
      .buildAndExpand(created.id())
      .toUri();

    return ResponseEntity.created(location).build();
  } 

  @PostMapping("/{id}/cancel")
  public ResponseEntity<LoanResponse> cancelLoan(@PathVariable Long id) {
    LoanResponse canceled = loanService.cancelLoan(id);

    return ResponseEntity.ok(canceled);
  }
  
  
}
