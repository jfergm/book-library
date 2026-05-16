package dev.fer.library.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.fer.library.dto.response.ShelfResponse;
import dev.fer.library.entity.Bookcase;
import dev.fer.library.entity.Shelf;

public class ShelfMapperTest {
  ShelfMapper mapper = new ShelfMapper();

  private List<Shelf> shelves;

  @BeforeEach
  void setUp() {
    shelves = new ArrayList<>();

    Bookcase bookcase = new Bookcase(1L, null, null, null);

    shelves.add(new Shelf(1L, "A1", "Shelf A1", bookcase));
    shelves.add(new Shelf(2L, "A2", "Shelf A2", bookcase));
    shelves.add(new Shelf(3L, "A3", "Shelf A3", bookcase));
  }

  @Test
  void shouldConvertToResponse() {
    Shelf shelf = shelves.getFirst();

    ShelfResponse response = mapper.toResponse(shelf);

    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.code()).isEqualTo("A1");
    assertThat(response.label()).isEqualTo("Shelf A1");
    assertThat(response.bookcaseId()).isEqualTo(1L);
  }

  @Test
  void shouldConvertToResponseList() {
    List<ShelfResponse> shelvesResponse = mapper.toResponseList(shelves);

    assertThat(shelvesResponse.size()).isEqualTo(3);
    assertThat(shelvesResponse.get(0).id()).isEqualTo(1L);
    assertThat(shelvesResponse.get(1).id()).isEqualTo(2L);
    assertThat(shelvesResponse.get(2).id()).isEqualTo(3L);
  }
}
