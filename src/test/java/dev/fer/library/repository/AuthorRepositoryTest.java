package dev.fer.library.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

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
}
