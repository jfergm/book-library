package dev.fer.library.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.fer.library.dto.request.BookRequest;
import dev.fer.library.dto.response.BookResponse;
import dev.fer.library.entity.Author;
import dev.fer.library.entity.Book;
import dev.fer.library.exception.BadRequestException;
import dev.fer.library.exception.BookNotFoundException;
import dev.fer.library.mapper.BookMapper;
import dev.fer.library.repository.AuthorRepository;
import dev.fer.library.repository.BookRepository;

@Service
public class BookService {

  private BookRepository bookRepository;

  private BookMapper bookMapper;

  private AuthorRepository authorRepository;

  public BookService(BookRepository bookRepository, BookMapper bookMapper, AuthorRepository authorRepository) {
    this.bookRepository = bookRepository;
    this.bookMapper = bookMapper;
    this.authorRepository = authorRepository;
  }
  
  public BookResponse getBook(Long id) {
    Optional<Book> book = bookRepository.findById(id);

    if (book.isEmpty()) {
      throw new BookNotFoundException();
    }

    return bookMapper.toResponse(book.get());
  }

  public List<BookResponse> getBooks(Pageable pageable) {
    return bookMapper.toResponseList(
      (List<Book>) bookRepository.findAll(pageable).getContent());
  }

  public BookResponse createBook(BookRequest request) {
    Optional<Author> author = authorRepository.findById(request.authorId());

    if (author.isEmpty()) {
      throw new BadRequestException();
    }

    Book book = bookMapper.toEntity(request, author.get());

    return bookMapper.toResponse(bookRepository.save(book));
  }

  public BookResponse updateBook(Long id, BookRequest request) {
    Optional<Book> bookOptional = bookRepository.findById(id);

    if (bookOptional.isEmpty()) {
      throw new BookNotFoundException();
    }

    Book book = bookOptional.get();
    Optional<Author> author = Optional.of(book.getAuthor());

    if (book.getAuthor().getId() != request.authorId()) {
      author = authorRepository.findById(request.authorId());
    }

    if (author.isEmpty()) {
      throw new BadRequestException();
    }

    Book updated = new Book(book.getId(), request.title(), request.isbn(), author.get());

    return bookMapper.toResponse(bookRepository.save(updated));
  }

  public void deleteBook(Long id) {
    if (bookRepository.existsById(id)) {
      bookRepository.deleteById(id);
      return;
    }

    throw new BookNotFoundException();
  }
}
