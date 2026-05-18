package dev.fer.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.fer.library.dto.response.AuthorResponse;
import dev.fer.library.entity.Author;
import dev.fer.library.exception.AuthorNotFoundException;
import dev.fer.library.mapper.AuthorMapper;
import dev.fer.library.repository.AuthorRepository;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {
  AuthorService authorService;

  @Mock
  AuthorRepository authorRepository;

  AuthorMapper authorMapper = new AuthorMapper();

  @BeforeEach
  void setUp() {
    authorService = new AuthorService(authorRepository, authorMapper);
  }

  @Test
  void shouldReturnAuthor() {
    when(authorRepository.findById(1L)).thenReturn(
      Optional.of(new Author(1L, "Rainbow Rowell"))
    );

    AuthorResponse author = authorService.getAuthor(1L);

    assertThat(author.id()).isEqualTo(1L);
    assertThat(author.name()).isEqualTo("Rainbow Rowell");

    verify(authorRepository).findById(1L);
  }

  @Test
  void shouldThrowNotFoundWhenInvalidAuthorId() {
    when(authorRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(AuthorNotFoundException.class, () -> authorService.getAuthor(1L));
    verify(authorRepository).findById(1L);
  }
}
