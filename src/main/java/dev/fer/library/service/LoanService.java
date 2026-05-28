package dev.fer.library.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import dev.fer.library.dto.request.LoanRequest;
import dev.fer.library.dto.response.LoanResponse;
import dev.fer.library.entity.BookCopy;
import dev.fer.library.entity.Loan;
import dev.fer.library.entity.User;
import dev.fer.library.enums.LoanStatus;
import dev.fer.library.exception.BadRequestException;
import dev.fer.library.exception.LoanNotFoundException;
import dev.fer.library.mapper.LoanMapper;
import dev.fer.library.repository.BookCopyRepository;
import dev.fer.library.repository.LoanRepository;
import dev.fer.library.repository.UserRepository;

@Service
public class LoanService {
  private LoanRepository loanRepository;
  private LoanMapper loanMapper;
  private UserRepository userrRepository;
  private BookCopyRepository bookCopyRepository;

  public LoanService(
    LoanRepository loanRepository, 
    LoanMapper loanMapper,
    UserRepository userRepository,
    BookCopyRepository bookCopyRepository
  ) {
    this.loanRepository = loanRepository;
    this.loanMapper = loanMapper;
    this.userrRepository = userRepository;
    this.bookCopyRepository = bookCopyRepository;
  }
    
  public LoanResponse getLoan(Long id) {
    Loan loan = loanRepository.findById(id).orElseThrow(() -> new LoanNotFoundException());

    return loanMapper.toResponse(loan);
  }

  public List<LoanResponse> getLoans() {
    List<Loan> loans = (List<Loan>) loanRepository.findAll();

    return loanMapper.toResponseList(loans);
  }

  public LoanResponse createLoan(LoanRequest request) {
    User user = userrRepository.findById(request.userId()).orElseThrow(() -> new BadRequestException("invalid user"));
    BookCopy bookCopy = bookCopyRepository.findById(request.bookCopyId()).orElseThrow(() -> new BadRequestException("invalid book copy"));

    Loan newLoan = loanMapper.toEntity(request, user, bookCopy);

    return loanMapper.toResponse(loanRepository.save(newLoan));
  }

  public LoanResponse cancelLoan(long id) {
    Loan loan = loanRepository.findById(id).orElseThrow(() -> new LoanNotFoundException());

    if (loan.getStatus() == LoanStatus.CLOSED) {
      throw new BadRequestException();
    }

    Loan canceled = loanMapper.toCancelEntity(loan);

    return loanMapper.toResponse(loanRepository.save(canceled));
  }

  public LoanResponse closeLoan(long id) {
    Loan loan = loanRepository.findById(id).orElseThrow(() -> new LoanNotFoundException());

    if (loan.getStatus() == LoanStatus.CANCELED) {
      throw new BadRequestException();
    }

    Loan closed = loanMapper.toCloseEntity(loan, new Date());

    return loanMapper.toResponse(loanRepository.save(closed));
  }
}
