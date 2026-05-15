package dev.fer.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.fer.library.dto.response.ShelfResponse;
import dev.fer.library.entity.Bookcase;
import dev.fer.library.entity.Shelf;
import dev.fer.library.exception.ShelfNotFoundException;
import dev.fer.library.mapper.ShelfMapper;
import dev.fer.library.repository.ShelfRepository;

@ExtendWith(MockitoExtension.class)
public class ShelfServiceTest {
  @Mock
  ShelfRepository shelfRepository;

  ShelfMapper shelfMapper = new ShelfMapper();

  ShelfService shelfService;

  @BeforeEach
  void setUp() {
    shelfService = new ShelfService(shelfRepository, shelfMapper);
  }
  
  @Test
  void shouldReturnShelf() {
    when(shelfRepository.findById(1L)).thenReturn(
      Optional.of(
        new Shelf(
          1L,
          "A1",
          "Shelf A1",
          new Bookcase(1L, null, null, null)
        )
      )
    );

    ShelfResponse shelf = shelfService.getShelf(1L);
    
    assertThat(shelf).isNotNull();
    assertThat(shelf.id()).isEqualTo(1L);
    assertThat(shelf.code()).isEqualTo("A1");
    assertThat(shelf.label()).isEqualTo("Shelf A1");
    assertThat(shelf.bookcaseId()).isEqualTo(1L);

    verify(shelfRepository).findById(1L);
  }

  @Test
  void shouldThrowshelfNotFoundWhenInvalidShelf() {
    when(shelfRepository.findById(1L)).thenReturn(Optional.empty());
    
    assertThrows(ShelfNotFoundException.class, () -> shelfService.getShelf(1L));

    verify(shelfRepository).findById(1L);
  }

}
