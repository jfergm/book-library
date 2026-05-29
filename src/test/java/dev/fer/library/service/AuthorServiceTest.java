package dev.fer.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.fer.library.dto.request.AuthorRequest;
import dev.fer.library.dto.response.AuthorResponse;
import dev.fer.library.entity.Author;
import dev.fer.library.exception.AuthorNotFoundException;
import dev.fer.library.mapper.AuthorMapper;
import dev.fer.library.repository.AuthorRepository;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {
  AuthorService authorService;

  @Mock
  AuthorRepository authorRepository;

  AuthorMapper authorMapper = new AuthorMapper();

  List<Author> authors;

  @BeforeEach
  void setUp() {
    authorService = new AuthorService(authorRepository, authorMapper);

    authors = new ArrayList<>();

    authors.add(new Author(1L, "Rainbow Rowell"));
    authors.add(new Author(2L, "Haruki Murakami"));
    authors.add(new Author(3L, "Julio Cortazar"));
  }

  @Test
  void shouldReturnAuthor() {
    when(authorRepository.findById(1L)).thenReturn(Optional.of(authors.getFirst()));

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

  @Test
  void shouldReturnAuthorList() {
    when(authorRepository.findAll()).thenReturn(authors);

    List<AuthorResponse> authorsList = authorService.getAuthors();
    
    assertThat(authorsList.size()).isEqualTo(3);
    assertThat(authorsList.get(0).id()).isEqualTo(1L);
    assertThat(authorsList.get(1).id()).isEqualTo(2L);
    assertThat(authorsList.get(2).id()).isEqualTo(3L);
    
    verify(authorRepository).findAll();
  }

  @Test
  void shouldCreateAuthor() {
    when(authorRepository.save(any())).thenReturn(authors.getFirst());
    AuthorRequest request = new AuthorRequest("Rainbow Rowell");
    AuthorResponse created = authorService.createAuthor(request);

    assertThat(created.id()).isNotNull();
    assertThat(created.name()).isEqualTo("Rainbow Rowell");
    verify(authorRepository).save(any());
  }

  @Test
  void shouldUpdateAuthor() {
    when(authorRepository.findById(1L)).thenReturn(Optional.of(authors.getFirst()));
    when(authorRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

    AuthorRequest request = new AuthorRequest("New author name");

    AuthorResponse updated = authorService.updateAuthor(1L, request);

    assertThat(updated.id()).isEqualTo(1L);
    assertThat(updated.name()).isEqualTo("New author name");

    verify(authorRepository).findById(anyLong());
    verify(authorRepository).save(any());
  }

  @Test
  void shouldThrowWhenUpdateInvalidAuthor() {
    when(authorRepository.findById(1L)).thenReturn(Optional.empty());

    AuthorRequest request = new AuthorRequest("New author name");

    assertThrows(AuthorNotFoundException.class, () -> authorService.updateAuthor(1L, request));
    verify(authorRepository).findById(anyLong());
    verify(authorRepository, times(0)).save(any());
  }

  @Test
  void shouldDeleteAuthor() {
    when(authorRepository.existsById(1L)).thenReturn(true);

    authorService.deleteAuthor(1L);

    verify(authorRepository).existsById(1L);
    verify(authorRepository).deleteById(1L);
  }

  @Test
  void shouldThrowWhenDeleteInvalidAuthor() {
    when(authorRepository.existsById(1L)).thenReturn(false);

    assertThrows(AuthorNotFoundException.class, () -> authorService.deleteAuthor(1L));

    verify(authorRepository).existsById(1L);
    verify(authorRepository, times(0)).deleteById(1L);
  }
}
