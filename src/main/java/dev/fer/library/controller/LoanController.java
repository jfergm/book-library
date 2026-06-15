package dev.fer.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.fer.library.dto.request.LoanRequest;
import dev.fer.library.dto.response.LoanResponse;
import dev.fer.library.service.LoanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Loans", description = "Operations to manage Book Loans")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/loans")
public class LoanController {

  private LoanService loanService;

  protected LoanController(LoanService loanService) {
    this.loanService = loanService;
  }

  @Operation(summary = "Get Loan", description = "Get Loan by ID")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Loan fetched successfully"),
    @ApiResponse(responseCode = "404", description = "Loan not found", content = @Content)
  })
  @GetMapping("/{id}")
  public ResponseEntity<LoanResponse> getLoan(@PathVariable Long id) {
    LoanResponse response = loanService.getLoan(id);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Get Loans", description = "Get all Loans")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Loans fetched successfully"),
  })
  @GetMapping("")
  public ResponseEntity<List<LoanResponse>> getLoans(Pageable pageable) {
    return ResponseEntity.ok(loanService.getLoans(pageable));
  }

  @Operation(summary = "Add Loan", description = "Create Loan, Book Copy status will be changed to CHECKED_OUT")
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201", 
      description = "Loan created sucessfully", 
      headers = @Header(
        name = "Location",
        description = "Location with the resource created"
      )
    ),
    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
  })
  @PostMapping("")
  public ResponseEntity<Void> createLoan(@RequestBody @Valid LoanRequest request) {
    LoanResponse created = loanService.createLoan(request);

    URI location = ServletUriComponentsBuilder
      .fromCurrentRequestUri()
      .path("/{id}")
      .buildAndExpand(created.id())
      .toUri();

    return ResponseEntity.created(location).build();
  } 

  @Operation(summary = "Cancel Loan", description = "Cancel Loan. Book Copy status will be changed to PROCESSING")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Loan canceled successfully"),
    @ApiResponse(responseCode = "404", description = "Loan not found", content = @Content)
  })
  @PostMapping("/{id}/cancel")
  public ResponseEntity<LoanResponse> cancelLoan(@PathVariable Long id) {
    LoanResponse canceled = loanService.cancelLoan(id);

    return ResponseEntity.ok(canceled);
  }
  
  @Operation(summary = "Close Loan", description = "Close Loan. Book Copy status will be changed to PROCESSING")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Loan closed successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
    @ApiResponse(responseCode = "404", description = "Loan not found", content = @Content)
  })
  @PostMapping("/{id}/close")
  public ResponseEntity<LoanResponse> closeLoan(@PathVariable Long id) {
    LoanResponse closed = loanService.closeLoan(id);
    return ResponseEntity.ok(closed);
  }
}
