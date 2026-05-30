package dev.fer.library.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import dev.fer.library.entity.Author;
import dev.fer.library.entity.Book;

@DataJpaTest
class BookRepositoryTest {
  @Autowired
  private BookRepository bookRepository;

  @Autowired 
  private AuthorRepository authorRepository;

  private Book book;

  @BeforeEach
  void setUp() {
    Author author = authorRepository.save(new Author(null, "Rainbow Rowell"));
    book = bookRepository.save(new Book(null, "Eleanor & Park", "ISBN123", author));
  }

  @Test
  void shouldInsertBook() {
    Book newBook = new Book(null, "Attachments", "ISBN456", book.getAuthor());
    
    bookRepository.save(newBook);
    assertThat(newBook.getId()).isNotNull();
    assertThat(newBook.getTitle()).isEqualTo("Attachments");    
    assertThat(newBook.getIsbn()).isEqualTo("ISBN456");
    assertThat(newBook.getAuthor()).isNotNull();
  }

  @Test
  void shouldReturnBookById() {
    Book retrieved = bookRepository.findById(book.getId()).get();

    assertThat(retrieved.getId()).isEqualTo(book.getId());
  }

  @Test
  void shouldReturnNullWhenNotExist() {
    Optional<Book> retrieved = bookRepository.findById(999L);
    
    assertThat(retrieved).isEmpty();
  }

  @Test
  void shouldReturnTrueWhenBookExist() {
    assertThat(bookRepository.existsById(book.getId())).isTrue();
  }

  @Test
  void shouldReturnFalseWhenBookNotExist() {
    assertThat(bookRepository.existsById(99L)).isFalse();
  }

  @Test
  void shouldUpdateBook() {
    Book toUpdate = new Book(book.getId(), "New title", "NEWISBN", book.getAuthor());
    
    bookRepository.save(toUpdate);

    Book updated = bookRepository.findById(book.getId()).get();

    assertThat(updated.getId()).isEqualTo(toUpdate.getId());
    assertThat(updated.getTitle()).isEqualTo(toUpdate.getTitle());
    assertThat(updated.getIsbn()).isEqualTo(book.getIsbn());
    assertThat(updated.getAuthor().getId()).isEqualTo(book.getAuthor().getId());
    
  }
}
