package dev.fer.library.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import dev.fer.library.dto.response.LoanResponse;
import dev.fer.library.entity.BookCopy;
import dev.fer.library.entity.Loan;
import dev.fer.library.entity.User;
import dev.fer.library.enums.LoanStatus;

public class LoanMapperTest {
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

    assertThat(responseList.size()).isEqualTo(1);
  }
}
