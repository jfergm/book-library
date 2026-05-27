package dev.fer.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.fer.library.dto.response.LoanResponse;
import dev.fer.library.entity.BookCopy;
import dev.fer.library.entity.Loan;
import dev.fer.library.entity.User;
import dev.fer.library.enums.LoanStatus;
import dev.fer.library.exception.LoanNotFoundException;
import dev.fer.library.mapper.LoanMapper;
import dev.fer.library.repository.LoanRepository;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {
  @Mock
  private LoanRepository loanRepository;

  private LoanMapper loanMapper = new LoanMapper();
  
  private LoanService loanService;

  private Date date = new Date(1779256800000L); // 2026 may 20

  @BeforeEach
  void setUp() {
    loanService = new LoanService(loanRepository, loanMapper);
  }

  @Test
  void shouldReturnLoan() {
    when(loanRepository.findById(1L)).thenReturn(
      Optional.of(
        new Loan(
          1L, 
          new User(1L, null, null, null), 
          new BookCopy(1L, null, null, null, null), 
          date, 
          new Date(date.getTime() + 604800000), // + 7 days
          null, 
          LoanStatus.ACTIVE, 
          ""
        )
      )
    );

    LoanResponse loan = loanService.getLoan(1L);

    assertThat(loan.id()).isEqualTo(1L);
    assertThat(loan.userId()).isEqualTo(1L);
    assertThat(loan.bookCopyId()).isEqualTo(1L);
    assertThat(loan.loanDate()).isEqualTo(date);
    assertThat(loan.dueDate()).isEqualTo(new Date(date.getTime() + 604800000));
    assertThat(loan.returnDate()).isNull();
    assertThat(loan.notes()).isEmpty();

    verify(loanRepository).findById(1L);
  }

    @Test
  void shouldThrowWhenInvalidLoan() {
    when(loanRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(LoanNotFoundException.class, () -> loanService.getLoan(1L));

    verify(loanRepository).findById(1L);
  }
}
