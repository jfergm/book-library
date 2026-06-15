package dev.fer.library.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import dev.fer.library.entity.Author;

@DataJpaTest
class AuthorRepositoryTest {
  @Autowired
  private AuthorRepository authorRepository;

  private Author author;

  @BeforeEach
  void setUp() {
    author = authorRepository.save(new Author(null, "Rainbow Rowell"));
  }

  @Test
  void shouldInsertAuthor() {
    Author inserted = new Author(null, "García Márquez");
    
    authorRepository.save(inserted);
    assertThat(inserted.getId()).isNotNull();
    assertThat(inserted.getName()).isEqualTo("García Márquez");
  }

  @Test
  void shouldReturnAuthorById() {
    Author returned = authorRepository.findById(author.getId()).get();

    assertThat(returned.getId()).isEqualTo(author.getId());
  }

  @Test
  void shouldReturnNullWhenNotExist() {
    Optional<Author> missed = authorRepository.findById(999L);
    
    assertThat(missed).isEmpty();
  }

  @Test
  void shouldReturnTrueWhenAuthorExist() {
    assertThat(authorRepository.existsById(author.getId())).isTrue();
  }

  @Test
  void shouldReturnFalseWhenAuthorNotExist() {
    assertThat(authorRepository.existsById(99L)).isFalse();
  }

  @Test
  void shouldUpdateAuthor() {    
    Author updated = new Author(author.getId(), "Updated");
    
    authorRepository.save(updated);

    Author changed = authorRepository.findById(author.getId()).get();

    assertThat(changed.getName()).isEqualTo("Updated");
    assertThat(changed.getId()).isEqualTo(updated.getId());
  }

  @Test
  void shouldReturnAuthorsPaginated() {
    authorRepository.save(new Author(null, "AUTH 1"));
    authorRepository.save(new Author(null, "AUTH 2"));
    authorRepository.save(new Author(null, "AUTH 3"));

    Page<Author> authorsPage = authorRepository.findAll(PageRequest.of(0, 1));
    assertThat(authorsPage.getContent()).hasSize(1);
    assertThat(authorsPage.getTotalElements()).isEqualTo(4);
    assertThat(authorsPage.getTotalPages()).isEqualTo(4);
    assertThat(authorsPage.getNumber()).isZero();
    assertThat(authorsPage.isFirst()).isTrue();
    assertThat(authorsPage.isLast()).isFalse();
  }
}
