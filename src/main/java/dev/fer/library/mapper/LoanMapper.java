package dev.fer.library.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.fer.library.dto.request.LoanRequest;
import dev.fer.library.dto.response.LoanResponse;
import dev.fer.library.entity.BookCopy;
import dev.fer.library.entity.Loan;
import dev.fer.library.entity.User;
import dev.fer.library.enums.LoanStatus;
import dev.fer.library.exception.BadRequestException;

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

  public Loan toEntity(LoanRequest request, User user, BookCopy bookCopy) {

    if ((user.getId() != request.userId()) || (bookCopy.getId() != request.bookCopyId())) {
      throw new BadRequestException();
    }

    if (request.loanDate().after(request.dueDate())) {
      throw new BadRequestException("Due date can't be before loan date");
    }

    return new Loan(null, user, bookCopy, request.loanDate(), request.dueDate(), null, LoanStatus.ACTIVE, request.notes());
  }
}
