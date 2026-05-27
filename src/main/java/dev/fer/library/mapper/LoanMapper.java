package dev.fer.library.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.fer.library.dto.response.LoanResponse;
import dev.fer.library.entity.Loan;

@Component
public class LoanMapper {

  public LoanResponse toResponse(Loan loan) {
    return new LoanResponse(
      loan.getId(),
      loan.getUser().getId(), 
      loan.getBookCopy().getId(), 
      loan.getLoanDate(), 
      loan.getDueDate(), 
      loan.getReturnDate(), 
      loan.getStatus(), 
      loan.getNotes()
    );
  }

  public List<LoanResponse> toResponseList(List<Loan> loans) {
    return loans.stream().map(this::toResponse).toList();
  }
}
