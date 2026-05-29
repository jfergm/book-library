package dev.fer.library.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.fer.library.dto.request.BookCopyRequest;
import dev.fer.library.dto.request.BookCopyUpdateRequest;
import dev.fer.library.dto.request.BookCopyUpdateShelfRequest;
import dev.fer.library.dto.response.BookCopyResponse;
import dev.fer.library.entity.Book;
import dev.fer.library.entity.BookCopy;
import dev.fer.library.entity.Shelf;
import dev.fer.library.exception.BadRequestException;
import dev.fer.library.exception.BookCopyNotFoundException;
import dev.fer.library.mapper.BookCopyMapper;
import dev.fer.library.repository.BookCopyRepository;
import dev.fer.library.repository.BookRepository;
import dev.fer.library.repository.ShelfRepository;

@Service
public class BookCopyService {

  private BookCopyRepository bookCopyRepository;

  private BookCopyMapper bookCopyMapper;

  private BookRepository bookRepository;

  private ShelfRepository shelfRepository;

  public BookCopyService(
    BookCopyRepository bookCopyRepository, 
    BookCopyMapper bookCopyMapper, 
    BookRepository bookRepository,
    ShelfRepository shelfRepository
  ) {
    this.bookCopyRepository = bookCopyRepository;
    this.bookCopyMapper = bookCopyMapper;
    this.bookRepository = bookRepository;
    this.shelfRepository = shelfRepository;
  }

  public BookCopyResponse getBookCopy(Long id) {
    Optional<BookCopy> bookCopy = bookCopyRepository.findById(id);

    if (bookCopy.isEmpty()) {
      throw new BookCopyNotFoundException();
    }

    return bookCopyMapper.toResponse(bookCopy.get());
  }

  public BookCopyResponse createBookCopy(BookCopyRequest request) {
    Optional<Book> book = bookRepository.findById(request.bookId());

    if (book.isEmpty()) {
      throw new BadRequestException();
    }

    BookCopy bookCopy = bookCopyMapper.toEntity(request, book.get());

    return bookCopyMapper.toResponse(bookCopyRepository.save(bookCopy));
  }

  public BookCopyResponse updateBookCopy(Long id, BookCopyUpdateRequest request) {
    Optional<BookCopy> bookCopy = bookCopyRepository.findById(id);

    if (bookCopy.isEmpty()) {
      throw new BookCopyNotFoundException();
    }

    BookCopy updated = bookCopyMapper.toUpdateEntity(bookCopy.get(), request);

    return bookCopyMapper.toResponse(bookCopyRepository.save(updated));
  }

  public BookCopyResponse updateBookCopyShelf(Long id, BookCopyUpdateShelfRequest request) {
    Optional<BookCopy> bookCopy = bookCopyRepository.findById(id);

    if (bookCopy.isEmpty()) {
      throw new BookCopyNotFoundException();
    }

    Shelf shelf = null;

    if (request.shelfId() != null) {
      shelf = shelfRepository
        .findById(request.shelfId())
        .orElseThrow(() -> new BadRequestException());
    }
    
    BookCopy updated = 
      bookCopyMapper.toUpdateShelfEntity(request, bookCopy.get(), shelf);

    return bookCopyMapper.toResponse(bookCopyRepository.save(updated));

  }

  public void deleteBookCopy(Long id) {
    if (bookCopyRepository.existsById(id)) {
      bookCopyRepository.deleteById(id);
      return;
    }

    throw new BookCopyNotFoundException();
  }

  public Optional<BookCopy> getEntity(long id) {
    return bookCopyRepository.findById(id);
  }

  public BookCopy checkOut(BookCopy bookCopy) {
    BookCopy checkedOut = bookCopyMapper.toCheckedOutEntity(bookCopy);
    return bookCopyRepository.save(checkedOut);
  }
}
