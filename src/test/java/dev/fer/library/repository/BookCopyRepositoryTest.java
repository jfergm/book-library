package dev.fer.library.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import dev.fer.library.entity.Author;
import dev.fer.library.entity.Book;
import dev.fer.library.entity.BookCopy;
import dev.fer.library.entity.Bookcase;
import dev.fer.library.entity.Floor;
import dev.fer.library.entity.Library;
import dev.fer.library.entity.Section;
import dev.fer.library.entity.Shelf;
import dev.fer.library.enums.BookCopyStatus;

@DataJpaTest
public class BookCopyRepositoryTest {
  @Autowired
  BookCopyRepository bookCopyRepository;

  @Autowired
  BookRepository bookRepository;

  @Autowired
  AuthorRepository authorRepository;

  @Autowired
  ShelfRepository shelfRepository;

  @Autowired
  BookcaseRepository bookcaseRepository;

  @Autowired
  SectionRepository sectionRepository;

  @Autowired
  FloorRepository floorRepository;

  @Autowired
  LibraryRepository libraryRepository;

  private BookCopy bookCopy;

  @BeforeEach
  void setUp() {
    Library library = libraryRepository.save(new Library(null, "Library"));

    Floor floor = floorRepository.save(new Floor(null, library, "1", ""));

    Section section = sectionRepository.save(new Section(null, floor, "sc", "Science", ""));

    Bookcase bookcase = bookcaseRepository.save(new Bookcase(null, "c", "l", section));

    Shelf shelf = shelfRepository.save(new Shelf(null, "A1", "Shelf A1", bookcase));

    Author author = authorRepository.save(new Author(null, "Raibow Rowell"));
    Book book = bookRepository.save(new Book(null, "Eleanor & Park", "ISBN123", author));

    bookCopy = bookCopyRepository.save(new BookCopy(null, book, shelf, "BK123", BookCopyStatus.AVAILABLE));
  }

  @Test
  void shouldInsertBookCopy() {
    BookCopy newBookCopy = bookCopyRepository.save(
      new BookCopy(null, bookCopy.getBook(), bookCopy.getShelf(), "BKN123", BookCopyStatus.PROCESSING)
    );
  
    assertThat(newBookCopy.getId()).isNotNull();
    assertThat(newBookCopy.getBook().getId()).isNotNull();
    assertThat(newBookCopy.getShelf().getId()).isNotNull();
    assertThat(newBookCopy.getCode()).isEqualTo("BKN123");
    assertThat(newBookCopy.getStatus()).isEqualTo(BookCopyStatus.PROCESSING);
  }

  @Test
  void shouldReturnBookCopy() {
    BookCopy insterted = bookCopyRepository.findById(bookCopy.getId()).get();

    assertThat(insterted.getId()).isEqualTo(bookCopy.getId());
    assertThat(insterted.getBook()).isEqualTo(bookCopy.getBook());
    assertThat(insterted.getShelf()).isEqualTo(bookCopy.getShelf());
    assertThat(insterted.getCode()).isEqualTo(bookCopy.getCode());
    assertThat(insterted.getStatus()).isEqualTo(bookCopy.getStatus());
  }

  @Test
  void shouldUpdateBookCopy() {
    BookCopy toUpdate = new BookCopy(bookCopy.getId(), bookCopy.getBook(), bookCopy.getShelf(), "NEWCODE", BookCopyStatus.CHECKED_OUT);
    bookCopyRepository.save(toUpdate);

    BookCopy updated = bookCopyRepository.findById(bookCopy.getId()).get();

    assertThat(updated.getId()).isEqualTo(toUpdate.getId());
    assertThat(updated.getCode()).isEqualTo(toUpdate.getCode());
    assertThat(updated.getStatus()).isEqualTo(toUpdate.getStatus());

    // Should permit nullable shelf
    toUpdate = new BookCopy(bookCopy.getId(), bookCopy.getBook(), null, updated.getCode(), updated.getStatus());
    bookCopyRepository.save(toUpdate);

    updated = bookCopyRepository.findById(bookCopy.getId()).get();

    assertThat(updated.getId()).isEqualTo(toUpdate.getId());
    assertThat(updated.getShelf()).isNull();
  }

  @Test
  void shouldReturnTrueWhenExist() {

    assertThat(bookCopyRepository.existsById(bookCopy.getId())).isTrue();
  }

  @Test
  void shouldReturnFalseWhenNotExist() {

    assertThat(bookCopyRepository.existsById(99999L)).isFalse();
  }

  @Test
  void shouldDeleteBookCopy() {
    assertThat(bookCopyRepository.existsById(bookCopy.getId())).isTrue();
    bookCopyRepository.deleteById(bookCopy.getId());
    assertThat(bookCopyRepository.existsById(bookCopy.getId())).isFalse();

  }

}
