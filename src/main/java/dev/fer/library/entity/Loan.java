package dev.fer.library.entity;

import java.util.Date;

import dev.fer.library.enums.LoanStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "loans")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Loan {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "book_copy_id")
  private BookCopy bookCopy;

  @Column(name = "loan_date")
  private Date loanDate;

  @Column(name = "due_date")
  private Date dueDate;

  @Column(name = "return_date")
  private Date returnDate;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private LoanStatus status;

  @Column(name = "notes")
  private String notes;

  public void cancel() {
    this.status = LoanStatus.CANCELED;
  }

  public void close(Date returnDate) {
    this.returnDate = returnDate;
    this.status = LoanStatus.CLOSED;
  }
}
