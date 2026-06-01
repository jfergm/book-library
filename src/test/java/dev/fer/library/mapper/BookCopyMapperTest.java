package dev.fer.library.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import dev.fer.library.dto.request.BookCopyRequest;
import dev.fer.library.dto.request.BookCopyUpdateRequest;
import dev.fer.library.dto.request.BookCopyUpdateShelfRequest;
import dev.fer.library.dto.response.BookCopyResponse;
import dev.fer.library.entity.Book;
import dev.fer.library.entity.BookCopy;
import dev.fer.library.entity.Shelf;
import dev.fer.library.enums.BookCopyStatus;
import dev.fer.library.exception.BadRequestException;

class BookCopyMapperTest {
  BookCopyMapper mapper = new BookCopyMapper();

  @Test
  void shouldConvertToResponse() {
    BookCopy bookCopy = new BookCopy(
      1L, 
      new Book(1L, null, null, null),
      new Shelf(1L, null, null, null), 
      "BK123", 
      BookCopyStatus.AVAILABLE
    );

    BookCopyResponse response = mapper.toResponse(bookCopy);

    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.bookId()).isEqualTo(1L);
    assertThat(response.shelfId()).isEqualTo(1L);
    assertThat(response.code()).isEqualTo("BK123");
    assertThat(response.status()).isEqualTo(BookCopyStatus.AVAILABLE.name());
  }

  @Test
  void shouldConvertToEntity() {
    BookCopyRequest request = new BookCopyRequest(1L);
    Book book = new Book(1L, null, null, null);

    BookCopy bookCopy = mapper.toEntity(request, book);

    assertThat(bookCopy.getId()).isNull();
    assertThat(bookCopy.getShelf()).isNull();
    assertThat(bookCopy.getCode()).isEmpty();
    assertThat(bookCopy.getBook().getId()).isEqualTo(request.bookId());
    assertThat(bookCopy.getStatus()).isEqualTo(BookCopyStatus.PROCESSING);

  }

  @Test
  void shouldThrowWhenConvertToEntityWithInvalidBook() {
    BookCopyRequest request = new BookCopyRequest(2L);
    Book book = new Book(1L, null, null, null);

    assertThrows(BadRequestException.class, () -> mapper.toEntity(request, book));
  }

  @Test
  void shouldConvertToResponseWithoutShelf() {
    BookCopy bookCopy = new BookCopy(
      1L, 
      new Book(1L, null, null, null),
      null, 
      "", 
      BookCopyStatus.PROCESSING
    );

    BookCopyResponse response = mapper.toResponse(bookCopy);

    assertThat(response.id()).isNotNull();
    assertThat(response.bookId()).isNotNull();
    assertThat(response.shelfId()).isNull();
    assertThat(response.code()).isEmpty();
  }

  @Test
  void shouldConvterToUpdateEntity() {
    BookCopy bookCopy =  new BookCopy(
      1L, 
      new Book(1L, null, null, null),
      new Shelf(1L, null, null, null), 
      "BK123", 
      BookCopyStatus.AVAILABLE
    );

    BookCopyUpdateRequest request = new BookCopyUpdateRequest("NEWCODE");

    BookCopy toUpdate = mapper.toUpdateEntity(bookCopy, request);

    assertThat(toUpdate.getId()).isEqualTo(bookCopy.getId());
    assertThat(toUpdate.getBook()).isEqualTo(bookCopy.getBook());
    assertThat(toUpdate.getShelf()).isEqualTo(bookCopy.getShelf());
    assertThat(toUpdate.getStatus()).isEqualTo(bookCopy.getStatus());
    assertThat(toUpdate.getCode()).isEqualTo(request.code());
  }

  @Test
  void shouldConvterToUpdateShelfEntity() {
    BookCopy bookCopy =  new BookCopy(
      1L, 
      new Book(1L, null, null, null),
      new Shelf(1L, null, null, null), 
      "BK123", 
      BookCopyStatus.AVAILABLE
    );

    BookCopyUpdateShelfRequest request = new BookCopyUpdateShelfRequest(2L);
    Shelf shelf = new Shelf(2L, null, null, null);

    BookCopy toUpdate = mapper.toUpdateShelfEntity(request, bookCopy, shelf);

    assertThat(toUpdate.getId()).isEqualTo(bookCopy.getId());
    assertThat(toUpdate.getBook()).isEqualTo(bookCopy.getBook());
    assertThat(toUpdate.getShelf()).isEqualTo(shelf);
    assertThat(toUpdate.getStatus()).isEqualTo(bookCopy.getStatus());
    assertThat(toUpdate.getCode()).isEqualTo(bookCopy.getCode());
  }

  @Test
  void shouldThrowWhenConvterToUpdateShelfEntityWithDiferentShelf() {
    BookCopy bookCopy =  new BookCopy(
      1L, 
      new Book(1L, null, null, null),
      new Shelf(1L, null, null, null), 
      "BK123", 
      BookCopyStatus.AVAILABLE
    );

    BookCopyUpdateShelfRequest request = new BookCopyUpdateShelfRequest(2L);
    Shelf shelf = new Shelf(1L, null, null, null);

    assertThrows(
      BadRequestException.class, 
      () -> mapper.toUpdateShelfEntity(request, bookCopy, shelf));
  }

  @Test
  void shouldThrowWhenConvterToUpdateShelfEntityWhenNullShelfAndNotNullRequest() {
    BookCopy bookCopy =  new BookCopy(
      1L,
      new Book(1L, null, null, null),
      new Shelf(1L, null, null, null), 
      "BK123", 
      BookCopyStatus.AVAILABLE
    );

    BookCopyUpdateShelfRequest request = new BookCopyUpdateShelfRequest(2L);
    Shelf shelf = null;

    assertThrows(
      BadRequestException.class, 
      () -> mapper.toUpdateShelfEntity(request, bookCopy, shelf));
  }

  @Test
  void shouldThrowWhenConvterToUpdateShelfEntityWhenNotNullShelfAndNullRequest() {
    BookCopy bookCopy =  new BookCopy(
      1L,
      new Book(1L, null, null, null),
      new Shelf(1L, null, null, null), 
      "BK123", 
      BookCopyStatus.AVAILABLE
    );

    BookCopyUpdateShelfRequest request = new BookCopyUpdateShelfRequest(null);
    Shelf shelf = new Shelf(1L, null, null, null);

    assertThrows(
      BadRequestException.class, 
      () -> mapper.toUpdateShelfEntity(request, bookCopy, shelf));
  }

  @Test
  void shouldConvertToCheckedOutEntity() {
    BookCopy bookCopy =  new BookCopy(
      1L,
      new Book(1L, null, null, null),
      new Shelf(1L, null, null, null), 
      "BK123", 
      BookCopyStatus.AVAILABLE
    );

    BookCopy checkedOut = mapper.toCheckedOutEntity(bookCopy);

    assertThat(checkedOut.getId()).isEqualTo(bookCopy.getId());
    assertThat(checkedOut.getBook()).isEqualTo(bookCopy.getBook());
    assertThat(checkedOut.getShelf()).isEqualTo(bookCopy.getShelf());
    assertThat(checkedOut.getCode()).isEqualTo(bookCopy.getCode());
    assertThat(checkedOut.getStatus()).isEqualTo(BookCopyStatus.CHECKED_OUT);
  }

  @Test
  void shouldConvertToProcessingEntity() {
    BookCopy bookCopy =  new BookCopy(
      1L,
      new Book(1L, null, null, null),
      new Shelf(1L, null, null, null), 
      "BK123", 
      BookCopyStatus.CHECKED_OUT
    );

    BookCopy checkedOut = mapper.toProcessingEntity(bookCopy);

    assertThat(checkedOut.getId()).isEqualTo(bookCopy.getId());
    assertThat(checkedOut.getBook()).isEqualTo(bookCopy.getBook());
    assertThat(checkedOut.getShelf()).isEqualTo(bookCopy.getShelf());
    assertThat(checkedOut.getCode()).isEqualTo(bookCopy.getCode());
    assertThat(checkedOut.getStatus()).isEqualTo(BookCopyStatus.PROCESSING);
  }
}
