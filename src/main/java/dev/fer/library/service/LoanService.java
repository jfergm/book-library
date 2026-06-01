package dev.fer.library.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.fer.library.dto.request.LoanRequest;
import dev.fer.library.dto.response.LoanResponse;
import dev.fer.library.entity.BookCopy;
import dev.fer.library.entity.Loan;
import dev.fer.library.entity.User;
import dev.fer.library.enums.BookCopyStatus;
import dev.fer.library.enums.LoanStatus;
import dev.fer.library.exception.BadRequestException;
import dev.fer.library.exception.LoanNotFoundException;
import dev.fer.library.mapper.LoanMapper;
import dev.fer.library.repository.LoanRepository;
import dev.fer.library.repository.UserRepository;

@Service
public class LoanService {
  private LoanRepository loanRepository;
  private LoanMapper loanMapper;
  private UserRepository userrRepository;
  private BookCopyService bookCopyService;

  public LoanService(
    LoanRepository loanRepository, 
    LoanMapper loanMapper,
    UserRepository userRepository,
    BookCopyService bookCopyService
  ) {
    this.loanRepository = loanRepository;
    this.loanMapper = loanMapper;
    this.userrRepository = userRepository;
    this.bookCopyService = bookCopyService;
  }
    
  public LoanResponse getLoan(Long id) {
    Loan loan = loanRepository.findById(id).orElseThrow(LoanNotFoundException::new);

    return loanMapper.toResponse(loan);
  }

  public List<LoanResponse> getLoans() {
    List<Loan> loans = (List<Loan>) loanRepository.findAll();

    return loanMapper.toResponseList(loans);
  }

  @Transactional
  public LoanResponse createLoan(LoanRequest request) {
    User user = userrRepository.findById(request.userId()).orElseThrow(() -> new BadRequestException("invalid user"));
    BookCopy bookCopy = bookCopyService.getEntity(request.bookCopyId()).orElseThrow(() -> new BadRequestException("invalid book copy"));

    if (bookCopy.getStatus() != BookCopyStatus.AVAILABLE) {
      throw new BadRequestException("book copy is not available");
    }

    Loan newLoan = loanMapper.toEntity(request, user, bookCopy);   
    bookCopyService.checkOut(bookCopy); 
    return loanMapper.toResponse(loanRepository.save(newLoan));
  }

  @Transactional
  public LoanResponse cancelLoan(long id) {
    Loan loan = loanRepository.findById(id).orElseThrow(LoanNotFoundException::new);

    if (loan.getStatus() != LoanStatus.ACTIVE) {
      throw new BadRequestException();
    }

    loan.cancel();
    bookCopyService.toProcessing(loan.getBookCopy());
    return loanMapper.toResponse(loanRepository.save(loan));
  }

  @Transactional
  public LoanResponse closeLoan(long id) {
    Loan loan = loanRepository.findById(id).orElseThrow(LoanNotFoundException::new);

    if (loan.getStatus() != LoanStatus.ACTIVE) {
      throw new BadRequestException();
    }

    loan.close(new Date());
    bookCopyService.toProcessing(loan.getBookCopy());
    return loanMapper.toResponse(loanRepository.save(loan));
  }
}
