package dev.fer.library.entity;

import dev.fer.library.enums.BookCopyStatus;
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
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "book_copies")
public class BookCopy {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "book_id")
  private Book book;

  @ManyToOne
  @JoinColumn(name = "shelf_id")
  private Shelf shelf;

  @Column(name = "code")
  private String code;

  @Enumerated(EnumType.STRING)
  private BookCopyStatus status;

  public void checkOut() {
    this.status = BookCopyStatus.CHECKED_OUT;
  }

  public void setToProcessing() {
    this.status = BookCopyStatus.PROCESSING;
  }
}
