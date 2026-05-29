package dev.fer.library.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import dev.fer.library.dto.request.LoanRequest;
import dev.fer.library.dto.response.LoanResponse;
import dev.fer.library.entity.BookCopy;
import dev.fer.library.entity.Loan;
import dev.fer.library.entity.User;
import dev.fer.library.enums.LoanStatus;
import dev.fer.library.exception.BadRequestException;

class LoanMapperTest {
  private LoanMapper mapper = new LoanMapper();
  
  private Date date = new Date(1779256800000L); // 2026 may 20
  private Date datePlus7Days = new Date(date.getTime() + 604800000);
  private Loan loan = new Loan(1L, new User(1L, null, null, null ), new BookCopy(1L, null, null, null, null), date, datePlus7Days, null, LoanStatus.ACTIVE, "");

  @Test
  void shouldConvertToResponse() {
    LoanResponse response = mapper.toResponse(loan);

    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.userId()).isEqualTo(1L);
    assertThat(response.bookCopyId()).isEqualTo(1L);
    assertThat(response.loanDate()).isEqualTo(date);
    assertThat(response.dueDate()).isEqualTo(datePlus7Days);
    assertThat(response.returnDate()).isNull();
    assertThat(response.notes()).isEmpty();
  }

  @Test
  void shouldConvertToResponseList() {
    List<Loan> loans = List.of(loan);

    List<LoanResponse> responseList = mapper.toResponseList(loans);

    assertThat(responseList).hasSize(1);
  }

  @Test
  void shouldConvertRequestToEntity() {
    LoanRequest request = new LoanRequest(
      1L, 
      1L,
      date,
      datePlus7Days,
      ""
    );

    Loan loanEntity = mapper.toEntity(request, loan.getUser(), loan.getBookCopy());

    assertThat(loanEntity.getId()).isNull();
    assertThat(loanEntity.getUser().getId()).isEqualTo(1L);
    assertThat(loanEntity.getBookCopy().getId()).isEqualTo(1L);
    assertThat(loanEntity.getLoanDate()).isEqualTo(date);
    assertThat(loanEntity.getDueDate()).isEqualTo(datePlus7Days);
    assertThat(loanEntity.getReturnDate()).isNull();
    assertThat(loanEntity.getStatus()).isEqualTo(LoanStatus.ACTIVE);
  }

  @Test
  void shouldThrowWhenUserNotMatch() {
    LoanRequest request = new LoanRequest(
      2L,
      1L,
      date,
      datePlus7Days,
      ""
    );

    assertThrows(BadRequestException.class, () -> mapper.toEntity(request, loan.getUser(), loan.getBookCopy()));
  }

  @Test
  void shouldThrowWhenBookCopyNotMatch() {
    LoanRequest request = new LoanRequest(
      1L,
      2L,
      date,
      datePlus7Days,
      ""
    );

    assertThrows(BadRequestException.class, () -> mapper.toEntity(request, loan.getUser(), loan.getBookCopy()));
  }

  @Test
  void shouldThrowWhenLoanDateIsAfterDueDate() {
    LoanRequest request = new LoanRequest(
      1L,
      1L,
      date,
      new Date(date.getTime() - 604800000),
      ""
    );

    assertThrows(BadRequestException.class, () -> mapper.toEntity(request, loan.getUser(), loan.getBookCopy()));
  }

  @Test
  void shouldConvertToCancelEntity() {
    Loan cancel = mapper.toCancelEntity(loan);

    assertThat(cancel.getId()).isEqualTo(loan.getId());
    assertThat(cancel.getUser()).isEqualTo(loan.getUser());
    assertThat(cancel.getBookCopy()).isEqualTo(loan.getBookCopy());
    assertThat(cancel.getLoanDate()).isEqualTo(loan.getLoanDate());
    assertThat(cancel.getDueDate()).isEqualTo(loan.getDueDate());
    assertThat(cancel.getReturnDate()).isEqualTo(loan.getReturnDate());
    assertThat(cancel.getNotes()).isEqualTo(loan.getNotes());
    assertThat(cancel.getStatus()).isEqualTo(LoanStatus.CANCELED);
  }

  @Test
  void shouldConvertToCloseEntity() {
    Date returnDate = new Date();
    Loan cancel = mapper.toCloseEntity(loan, returnDate);

    assertThat(cancel.getId()).isEqualTo(loan.getId());
    assertThat(cancel.getUser()).isEqualTo(loan.getUser());
    assertThat(cancel.getBookCopy()).isEqualTo(loan.getBookCopy());
    assertThat(cancel.getLoanDate()).isEqualTo(loan.getLoanDate());
    assertThat(cancel.getDueDate()).isEqualTo(loan.getDueDate());
    assertThat(cancel.getReturnDate()).isEqualTo(returnDate);
    assertThat(cancel.getNotes()).isEqualTo(loan.getNotes());
    assertThat(cancel.getStatus()).isEqualTo(LoanStatus.CLOSED);
  }
}
