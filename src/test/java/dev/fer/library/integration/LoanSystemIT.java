package dev.fer.library.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.time.Duration;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import dev.fer.library.dto.request.LoanRequest;
import dev.fer.library.dto.response.BookCopyResponse;
import dev.fer.library.dto.response.LoanResponse;
import dev.fer.library.enums.LoanStatus;
import dev.fer.library.integration.fixtures.LibraryFixture;
import dev.fer.library.integration.fixtures.UserFixture;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
@AutoConfigureTestRestTemplate
@Testcontainers
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class LoanSystemIT {
    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer(
      DockerImageName.parse("postgres:alpine3.22")
    );

  @Autowired
  TestRestTemplate restTemplate;

  private LibraryFixture libraryFixture;
  private UserFixture userFixture;
  private HttpHeaders headers;

  private BookCopyResponse availableBookCopy;

  @BeforeEach
  void setup() {
    userFixture = new UserFixture(restTemplate);
    
    this.headers = new HttpHeaders();
    this.headers.add("Authorization", "Bearer " + userFixture.getLoginResponse().token());
    
    libraryFixture = new LibraryFixture(restTemplate, this.headers);

    this.availableBookCopy = libraryFixture.createAvailableBookCopyForLoan();
  }

  @Test
  void shouldBorrowAndReturnAvailableBook() {

    // Create the loan
    ResponseEntity<Void> createdLoan = createLoan(availableBookCopy.id());
    assertThat(createdLoan.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(createdLoan.getHeaders().getLocation()).isNotNull();

    ResponseEntity<LoanResponse> loanResponse = getLoan(createdLoan.getHeaders().getLocation());
    assertThat(loanResponse.getBody().bookCopyId()).isEqualTo(availableBookCopy.id());
    assertThat(loanResponse.getBody().status()).isEqualTo(LoanStatus.ACTIVE);
    assertThat(loanResponse.getBody().returnDate()).isNull();

    ResponseEntity<BookCopyResponse> bookCopyResponse = getBookCopy("/book-copies/" + availableBookCopy.id());
    assertThat(bookCopyResponse.getBody().status()).isEqualTo("CHECKED_OUT");

    // Close the loan
    ResponseEntity<Void> returnResponse = closeLoan(getCloseLocation(createdLoan.getHeaders()));
    assertThat(returnResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

    loanResponse = getLoan(createdLoan.getHeaders().getLocation());
    assertThat(loanResponse.getBody().status()).isEqualTo(LoanStatus.CLOSED);
    assertThat(loanResponse.getBody().returnDate()).isNotNull();

    bookCopyResponse = getBookCopy("/book-copies/" + availableBookCopy.id());
    assertThat(bookCopyResponse.getBody().status()).isEqualTo("PROCESSING");
  }

  @Test
  void shouldNotCreateLoanWhenBookIsNotAvailable() {
    // Processing BookCopy
    BookCopyResponse processingBookCopy = libraryFixture.createProcessingBookCopy();
    assertThat(processingBookCopy.status()).isEqualTo("PROCESSING");

    ResponseEntity<Void> loanResponse = createLoan(processingBookCopy.id());
    assertThat(loanResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void shouldNotCreateLoanForAlreadyBorrowedBookCopy() {
    ResponseEntity<Void> loan = createLoan(availableBookCopy.id());
    assertThat(loan.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    ResponseEntity<Void> createdLoan = createLoan(availableBookCopy.id());
    assertThat(createdLoan.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void shouldNotCloseAlreadyClosedLoan() {
    ResponseEntity<Void> loanResponse = createLoan(availableBookCopy.id());
    ResponseEntity<Void> closeLoan = closeLoan(getCloseLocation(loanResponse.getHeaders()));
    assertThat(closeLoan.getStatusCode()).isEqualTo(HttpStatus.OK);

    ResponseEntity<Void> closedResponse = closeLoan(getCloseLocation(loanResponse.getHeaders()));
    assertThat(closedResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void shouldNotCloseNonExistingLoan() {
    ResponseEntity<Void> closeResponse = closeLoan("/loans/999/close");
    assertThat(closeResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturn404WhenLoanDoesNotExist() {
    ResponseEntity<Void> response =
    restTemplate.exchange(
        "/loans/999999",
        HttpMethod.GET,
        new HttpEntity<>(headers),
        Void.class
    );

    assertThat(response.getStatusCode())
    .isEqualTo(HttpStatus.NOT_FOUND);  
  }

  private ResponseEntity<Void> closeLoan(String location) {
    return restTemplate.exchange(
      location,
      HttpMethod.POST,
      new HttpEntity<>(headers),
      Void.class
    );
  }

  private ResponseEntity<LoanResponse> getLoan(URI location) {
    return restTemplate.exchange(
      location,
      HttpMethod.GET,
      new HttpEntity<>(headers),
      LoanResponse.class
    );
  } 

  private ResponseEntity<BookCopyResponse> getBookCopy(String location) {
    return restTemplate.exchange(
      location,
      HttpMethod.GET,
      new HttpEntity<>(headers),
      BookCopyResponse.class
    );
  }

  private ResponseEntity<Void> createLoan(Long bookCopyId) {
    LoanRequest loanRequest = new LoanRequest(
      userFixture.getLoginResponse().id(),
      bookCopyId, 
      new Date(), 
      new Date(System.currentTimeMillis() + Duration.ofDays(7).toMillis()),
      ""
    );

    return restTemplate.exchange(
      "/loans",
      HttpMethod.POST,
      new HttpEntity<>(loanRequest, headers),
      Void.class
    );
  }

  private String getCloseLocation(HttpHeaders headers) {
    return headers.getLocation().toString() + "/close";
  }
  
}
