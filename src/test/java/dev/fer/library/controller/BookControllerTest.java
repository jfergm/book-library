package dev.fer.library.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.fer.library.dto.request.BookRequest;
import dev.fer.library.dto.response.BookResponse;
import dev.fer.library.exception.BadRequestException;
import dev.fer.library.exception.BookNotFoundException;
import dev.fer.library.service.BookService;
import dev.fer.library.utils.TestUtils;

@WebMvcTest(BookController.class)
public class BookControllerTest {
  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  BookService bookService;

  List<BookResponse> books;

  @BeforeEach
  void setUp() {
    books = new ArrayList<>();

    books.add(new BookResponse(1L, "Eleanor & Park", "ISBN123", 1L));
    books.add(new BookResponse(2L, "Kafka On The Shore", "ISBN456", 2L));
    books.add(new BookResponse(3L, "Another Book", "ISBN789", 3L));
  }

  @Test
  void shouldReturnBook() throws Exception {
    when(bookService.getBook(1L)).thenReturn(books.getFirst());

    mockMvc.perform(get("/books/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value("1"))
      .andExpect(jsonPath("$.title").value("Eleanor & Park"))
      .andExpect(jsonPath("$.isbn").value("ISBN123"))
      .andExpect(jsonPath("$.authorId").value("1"));

    verify(bookService).getBook(1L);
  }

  @Test
  void shouldReturnNotFoundWhenInvalidBook() throws Exception {
    when(bookService.getBook(1L)).thenThrow(BookNotFoundException.class);

    mockMvc.perform(get("/books/1"))
      .andExpect(status().isNotFound());

    verify(bookService).getBook(1L);
  }

  @Test
  void shouldReturnBooksList() throws Exception {
    when(bookService.getBooks()).thenReturn(books);
    
    mockMvc.perform(get("/books"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(3)));

    verify(bookService).getBooks();
  }

  @Test
  void shouldReturnCreatedAndLocationWhenCreateBook() throws Exception {
    when(bookService.createBook(any())).thenReturn(books.getFirst());

    BookRequest bookRequest = new BookRequest("New Book", "NEWISBN123", 1L);

    mockMvc
      .perform(
        post("/books")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(bookRequest)))
      .andExpect(status().isCreated())
      .andExpect(header().string("Location", containsString("/books/1")));
  }

  @Test
  void shouldReturnBadRequestWhenCreateBookWithInvalidAuthor() throws Exception {
    when(bookService.createBook(any())).thenThrow(BadRequestException.class);

    BookRequest bookRequest = new BookRequest("New Book", "NEWISBN123", 1L);

    mockMvc
      .perform(
        post("/books")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(bookRequest)))
      .andExpect(status().isBadRequest());
  }
}
