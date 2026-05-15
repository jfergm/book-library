package dev.fer.library.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.fer.library.dto.response.BookcaseResponse;
import dev.fer.library.entity.Bookcase;
import dev.fer.library.entity.Section;

public class BookcaseMapperTest {
  private BookcaseMapper mapper = new BookcaseMapper();

  private List<Bookcase> bookcases;

  @BeforeEach
  void setUp() {
    bookcases = new ArrayList<>();

    Section section = new Section(1L, null, null, null, null);
    bookcases.add(new Bookcase(1L, "1A", "Bookcase 1A", section));
    bookcases.add(new Bookcase(2L, "2A", "Bookcase 2A", section));
    bookcases.add(new Bookcase(3L, "3A", "Bookcase 3A", section));
  }

  @Test
  void shouldConvertEntityToResponse() {
    Bookcase bookcase = bookcases.getFirst();

    BookcaseResponse response = mapper.toResponse(bookcase);

    assertThat(response.id()).isEqualTo(bookcase.getId());
  }

  @Test
  void shouldConvertToResponseList() {
    List<BookcaseResponse> bookcasesResponse = mapper.toResponseList(bookcases);

    assertThat(bookcasesResponse.size()).isEqualTo(3);
    assertThat(bookcasesResponse.get(0).id()).isEqualTo(1L);
    assertThat(bookcasesResponse.get(1).id()).isEqualTo(2L);
    assertThat(bookcasesResponse.get(2).id()).isEqualTo(3L);
  }
}
