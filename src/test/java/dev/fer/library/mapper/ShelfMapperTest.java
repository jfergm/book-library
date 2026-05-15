package dev.fer.library.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import dev.fer.library.dto.response.ShelfResponse;
import dev.fer.library.entity.Bookcase;
import dev.fer.library.entity.Shelf;

public class ShelfMapperTest {
  ShelfMapper mapper = new ShelfMapper();

  @Test
  void shouldConvertToResponse() {
    Shelf shelf = new Shelf(1L, "A1", "Shelf A1", new Bookcase(1L, null, null, null));
  
    ShelfResponse response = mapper.toResponse(shelf);

    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.code()).isEqualTo("A1");
    assertThat(response.label()).isEqualTo("Shelf A1");
    assertThat(response.bookcaseId()).isEqualTo(1L);
  }
}
