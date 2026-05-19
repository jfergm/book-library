package dev.fer.library.service;

import dev.fer.library.mapper.AuthorMapper;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.fer.library.dto.request.AuthorRequest;
import dev.fer.library.dto.response.AuthorResponse;
import dev.fer.library.entity.Author;
import dev.fer.library.exception.AuthorNotFoundException;
import dev.fer.library.repository.AuthorRepository;

@Service
public class AuthorService {

  private AuthorRepository authorRepository;
  private AuthorMapper authorMapper;

  public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
    this.authorRepository = authorRepository;
    this.authorMapper = authorMapper;
  }

  public AuthorResponse getAuthor(Long id) {
    Optional<Author> author = authorRepository.findById(id);

    if (author.isEmpty()) {
      throw new AuthorNotFoundException();
    }

    return authorMapper.toResponse(author.get());
  }

  public List<AuthorResponse> getAuthors() {
    return authorMapper.toResponseList((List<Author>) authorRepository.findAll());
  }

  public AuthorResponse createAuthor(AuthorRequest request) {
    Author author = authorMapper.toEntity(request);

    return authorMapper.toResponse(authorRepository.save(author));
  }

  public AuthorResponse updateAuthor(Long id, AuthorRequest request) {
    Optional<Author> author = authorRepository.findById(id);

    if (author.isEmpty()) {
      throw new AuthorNotFoundException();
    }

    Author updated = new Author(author.get().getId(), request.name());

    return authorMapper.toResponse(authorRepository.save(updated));
  }
}
