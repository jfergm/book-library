package dev.fer.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {
  @Mock
  private LoanRepository loanRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private BookCopyRepository bookCopyRepository;

  private LoanMapper loanMapper = new LoanMapper();
  
  private LoanService loanService;

  private Date baseDate = new Date(1779256800000L); // 2026 may 20

  private List<Loan> loans;


  @BeforeEach
  void setUp() {
    loanService = new LoanService(loanRepository, loanMapper, userRepository, bookCopyRepository);

    loans = new ArrayList<>();

    loans.add(
      new Loan(
        1L, 
        new User(1L, null, null, null), 
        new BookCopy(1L, null, null, null, null), 
        baseDate, 
        new Date(baseDate.getTime() + 604800000), // + 7 days
        null, 
        LoanStatus.ACTIVE, 
        ""
      )
    );

    loans.add(
      new Loan(
        2L, 
        new User(2L, null, null, null), 
        new BookCopy(2L, null, null, null, null), 
        baseDate, 
        new Date(baseDate.getTime() + 604800000), // + 7 days
        null, 
        LoanStatus.ACTIVE, 
        ""
      )
    );

    loans.add(
      new Loan(
        3L, 
        new User(3L, null, null, null), 
        new BookCopy(3L, null, null, null, null), 
        baseDate, 
        new Date(baseDate.getTime() + 604800000), // + 7 days
        null, 
        LoanStatus.CLOSED, 
        ""
      )
    );
  }

  @Test
  void shouldReturnLoan() {
    when(loanRepository.findById(1L)).thenReturn(
      Optional.of(loans.getFirst())
    );

    LoanResponse loan = loanService.getLoan(1L);

    assertThat(loan.id()).isEqualTo(1L);
    assertThat(loan.userId()).isEqualTo(1L);
    assertThat(loan.bookCopyId()).isEqualTo(1L);
    assertThat(loan.loanDate()).isEqualTo(baseDate);
    assertThat(loan.dueDate()).isEqualTo(new Date(baseDate.getTime() + 604800000));
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

  @Test
  void shouldReturnLoansList() {
    when(loanRepository.findAll()).thenReturn(loans);

    List<LoanResponse> response = loanService.getLoans();

    assertThat(response.size()).isEqualTo(3);

    verify(loanRepository).findAll();
  }

  @Test
  void shouldCreateLoan() {
    when(userRepository.findById(1L)).thenReturn(
      Optional.of(loans.getFirst().getUser())
    );
    when(bookCopyRepository.findById(1L)).thenReturn(
      Optional.of(loans.getFirst().getBookCopy())
    );
    when(loanRepository.save(any())).thenReturn(loans.getFirst());

    LoanRequest request = new LoanRequest(1L, 1L, baseDate, new Date(baseDate.getTime() + 604800000), "");
    LoanResponse response = loanService.createLoan(request);

    assertThat(response.id()).isNotNull();
    assertThat(response.userId()).isEqualTo(1L);
    assertThat(response.bookCopyId()).isEqualTo(1L);
    assertThat(response.loanDate()).isEqualTo(baseDate);
    assertThat(response.dueDate()).isEqualTo(new Date(baseDate.getTime() + 604800000));
    assertThat(response.returnDate()).isNull();
    assertThat(response.status()).isEqualTo(LoanStatus.ACTIVE);

    verify(userRepository).findById(1L);
    verify(bookCopyRepository).findById(1L);
    verify(loanRepository).save(any());
  }

  @Test
  void shouldThrowWhenCreateLoanWithInvalidUser() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    LoanRequest request = new LoanRequest(1L, 1L, baseDate, new Date(baseDate.getTime() + 604800000), "");

    assertThrows(BadRequestException.class, () -> loanService.createLoan(request));

    verify(userRepository).findById(1L);
    verify(bookCopyRepository, times(0)).findById(1L);
    verify(loanRepository, times(0)).save(any());
  }

  @Test
  void shouldThrowWhenCreateLoanWithInvalidBookCopy() {
    when(userRepository.findById(1L)).thenReturn(
      Optional.of(loans.getFirst().getUser())
    );
    when(bookCopyRepository.findById(1L)).thenReturn(Optional.empty());

    LoanRequest request = new LoanRequest(1L, 1L, baseDate, new Date(baseDate.getTime() + 604800000), "");

    assertThrows(BadRequestException.class, () -> loanService.createLoan(request));

    verify(userRepository).findById(1L);
    verify(bookCopyRepository).findById(1L);
    verify(loanRepository, times(0)).save(any());
  }

  @Test
  void shouldThrowWhenCreateLoanWithInvalidLoanAndReturnDate() {
    when(userRepository.findById(1L)).thenReturn(
      Optional.of(loans.getFirst().getUser())
    );
    when(bookCopyRepository.findById(1L)).thenReturn(
      Optional.of(loans.getFirst().getBookCopy())
    );

    LoanRequest request = new LoanRequest(1L, 1L, baseDate, new Date(baseDate.getTime() - 604800000), "");

    assertThrows(BadRequestException.class, () -> loanService.createLoan(request));

    verify(userRepository).findById(1L);
    verify(bookCopyRepository).findById(1L);
    verify(loanRepository, times(0)).save(any());
  }

  @Test
  void shouldCancelLoan() {
    when(loanRepository.findById(1L)).thenReturn(Optional.of(loans.getFirst()));
    when(loanRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

    LoanResponse loanResponse = loanService.cancelLoan(1L);

    assertThat(loanResponse.id()).isEqualTo(1L);
    assertThat(loanResponse.status()).isEqualTo(LoanStatus.CANCELED);

    verify(loanRepository).findById(1L);
    verify(loanRepository).save(any(Loan.class));
  }

  @Test
  void shouldThrowNotFoundWhenInvalidLoan() {
    when(loanRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(LoanNotFoundException.class, () -> loanService.cancelLoan(1L));

    verify(loanRepository).findById(1L);
    verify(loanRepository, times(0)).save(any());
  }

  @Test
  void shouldThrowBadRequestWhenLoanStatusIsClosed() {
    when(loanRepository.findById(2L)).thenReturn(Optional.of(loans.getLast()));

    assertThrows(BadRequestException.class, () -> loanService.cancelLoan(2L));

    verify(loanRepository).findById(2L);
    verify(loanRepository, times(0)).save(any(Loan.class));
  }
}
