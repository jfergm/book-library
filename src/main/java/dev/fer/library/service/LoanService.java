package dev.fer.library.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.fer.library.dto.response.LoanResponse;
import dev.fer.library.entity.Loan;
import dev.fer.library.exception.LoanNotFoundException;
import dev.fer.library.mapper.LoanMapper;
import dev.fer.library.repository.LoanRepository;

@Service
public class LoanService {
  private LoanRepository loanRepository;
  private LoanMapper loanMapper;

  public LoanService(LoanRepository loanRepository, LoanMapper loanMapper) {
    this.loanRepository = loanRepository;
    this.loanMapper = loanMapper;
  }
    
  public LoanResponse getLoan(Long id) {
    Loan loan = loanRepository.findById(id).orElseThrow(() -> new LoanNotFoundException());

    return loanMapper.toResponse(loan);
  }

  public List<LoanResponse> getLoans() {
    List<Loan> loans = (List<Loan>) loanRepository.findAll();

    return loanMapper.toResponseList(loans);
  }
}
