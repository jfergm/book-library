package dev.fer.library.integration.fixtures;

import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import dev.fer.library.dto.request.AuthorRequest;
import dev.fer.library.dto.request.BookCopyRequest;
import dev.fer.library.dto.request.BookCopyUpdateShelfRequest;
import dev.fer.library.dto.request.BookRequest;
import dev.fer.library.dto.request.BookcaseRequest;
import dev.fer.library.dto.request.FloorRequest;
import dev.fer.library.dto.request.LibraryRequest;
import dev.fer.library.dto.request.SectionRequest;
import dev.fer.library.dto.request.ShelfRequest;
import dev.fer.library.dto.response.AuthorResponse;
import dev.fer.library.dto.response.BookCopyResponse;
import dev.fer.library.dto.response.BookResponse;
import dev.fer.library.dto.response.BookcaseResponse;
import dev.fer.library.dto.response.FloorResponse;
import dev.fer.library.dto.response.LibraryResponse;
import dev.fer.library.dto.response.SectionResponse;
import dev.fer.library.dto.response.ShelfResponse;

public class LibraryFixture {
  private final TestRestTemplate restTemplate;
  private final HttpHeaders headers;

  public LibraryFixture(TestRestTemplate restTemplate, HttpHeaders headers) {
    this.restTemplate = restTemplate;

    this.headers = headers;
  }

  public BookCopyResponse createAvailableBookCopyForLoan() {
    BookResponse book = createBook("Available Book");

    BookCopyRequest request = new BookCopyRequest(book.id());
    
    ResponseEntity<Void> createdResponse = restTemplate.exchange(
      "/book-copies",
      HttpMethod.POST,
      new HttpEntity<>(request, headers),
      Void.class
    );

    ShelfResponse shelf = createShelf();
    BookCopyUpdateShelfRequest updateRequest = new BookCopyUpdateShelfRequest(shelf.id());

    restTemplate.exchange(
      createdResponse.getHeaders().getLocation().toString() + "/shelf",
      HttpMethod.PUT, 
      new HttpEntity<>(updateRequest, headers), 
      Void.class
    );

    ResponseEntity<BookCopyResponse> bookCopyResponse = restTemplate.exchange(
      createdResponse.getHeaders().getLocation(),
      HttpMethod.GET,
      new HttpEntity<>(headers),
      BookCopyResponse.class
    );

    return bookCopyResponse.getBody();

  }

  public BookCopyResponse createProcessingBookCopy() {
    BookResponse book = createBook("Processing Book");

    BookCopyRequest request = new BookCopyRequest(book.id());
    
    ResponseEntity<Void> createdResponse = restTemplate.exchange(
      "/book-copies",
      HttpMethod.POST,
      new HttpEntity<>(request, headers),
      Void.class
    );

    ResponseEntity<BookCopyResponse> bookCopyResponse = restTemplate.exchange(
      createdResponse.getHeaders().getLocation(),
      HttpMethod.GET,
      new HttpEntity<>(headers),
      BookCopyResponse.class
    );

    return bookCopyResponse.getBody(); 
  }

  private LibraryResponse createLibrary() {
    LibraryRequest request = new LibraryRequest("Library One");

    ResponseEntity<Void> libraryResponse = restTemplate.exchange(
      "/libraries",
      HttpMethod.POST,
      new HttpEntity<>(request, headers),
      Void.class
    );

    ResponseEntity<LibraryResponse> response = restTemplate.exchange(
      libraryResponse.getHeaders().getLocation(),
      HttpMethod.GET,
      new HttpEntity<>(headers),
      LibraryResponse.class
    );

    return response.getBody();
  }

  private FloorResponse createFloor() {
    LibraryResponse library = createLibrary();
    FloorRequest request = new FloorRequest(library.id(), "f1", "Floor 1");

    ResponseEntity<Void> libraryResponse = restTemplate.exchange(
      "/floors",
      HttpMethod.POST,
      new HttpEntity<>(request, headers),
      Void.class
    );

    ResponseEntity<FloorResponse> response = restTemplate.exchange(
      libraryResponse.getHeaders().getLocation(),
      HttpMethod.GET,
      new HttpEntity<>(headers),
      FloorResponse.class
    );

    return response.getBody();
  }

  private SectionResponse createSection() {
    FloorResponse floor = createFloor();
    SectionRequest request = new SectionRequest(floor.id(), "s1", "Section 1", "Section 1");

    ResponseEntity<Void> sectionResponse = restTemplate.exchange(
      "/sections",
      HttpMethod.POST,
      new HttpEntity<>(request, headers),
      Void.class
    );

    ResponseEntity<SectionResponse> response = restTemplate.exchange(
      sectionResponse.getHeaders().getLocation(),
      HttpMethod.GET,
      new HttpEntity<>(headers),
      SectionResponse.class
    );

    return response.getBody();
  }

  private BookcaseResponse createBookCase() {
    SectionResponse section = createSection();
    BookcaseRequest request = new BookcaseRequest(section.id(), "bc1", "Bookcase 1");

    ResponseEntity<Void> sectionResponse = restTemplate.exchange(
      "/bookcases",
      HttpMethod.POST,
      new HttpEntity<>(request, headers),
      Void.class
    );

    ResponseEntity<BookcaseResponse> response = restTemplate.exchange(
      sectionResponse.getHeaders().getLocation(),
      HttpMethod.GET,
      new HttpEntity<>(headers),
      BookcaseResponse.class
    );

    return response.getBody();
  }

  private ShelfResponse createShelf() {
    BookcaseResponse bookcase = createBookCase();
    ShelfRequest request = new ShelfRequest("shlf1", "Shelf 1", bookcase.id());

    ResponseEntity<Void> sectionResponse = restTemplate.exchange(
      "/shelves",
      HttpMethod.POST,
      new HttpEntity<>(request, headers),
      Void.class
    );

    ResponseEntity<ShelfResponse> response = restTemplate.exchange(
      sectionResponse.getHeaders().getLocation(),
      HttpMethod.GET,
      new HttpEntity<>(headers),
      ShelfResponse.class
    );

    return response.getBody();
  }

  private AuthorResponse createAuthor() {
    AuthorRequest request = new AuthorRequest("Author One");

    ResponseEntity<Void> authorResponse = restTemplate.exchange(
      "/authors",
      HttpMethod.POST,
      new HttpEntity<>(request, headers),
      Void.class
    );

    ResponseEntity<AuthorResponse> response = restTemplate.exchange(
      authorResponse.getHeaders().getLocation(),
      HttpMethod.GET,
      new HttpEntity<>(headers),
      AuthorResponse.class
    );

    return response.getBody();
  }

  private BookResponse createBook(String title) {
    AuthorResponse author = createAuthor();
    BookRequest request = new BookRequest(title, "ISBN" + title.replace(" ", ""), author.id());

    ResponseEntity<Void> bookResponse = restTemplate.exchange(
      "/books",
      HttpMethod.POST,
      new HttpEntity<>(request, headers),
      Void.class
    );

    ResponseEntity<BookResponse> response = restTemplate.exchange(
      bookResponse.getHeaders().getLocation(),
      HttpMethod.GET,
      new HttpEntity<>(headers),
      BookResponse.class
    );

    return response.getBody();
  }
}
