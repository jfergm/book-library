package dev.fer.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
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

  private List<Shelf> shelves;

  @BeforeEach
  void setUp() {
    shelfService = new ShelfService(shelfRepository, shelfMapper);

    shelves = new ArrayList<>();

    Bookcase bookcase = new Bookcase(1L, null, null, null);
    shelves.add(new Shelf(1L, "A1", "Shelf A1", bookcase));
    shelves.add(new Shelf(2L, "A2", "Shelf A2", bookcase));
    shelves.add(new Shelf(3L, "A3", "Shelf A3", bookcase));
  }
  
  @Test
  void shouldReturnShelf() {
    when(shelfRepository.findById(1L)).thenReturn(Optional.of(shelves.getFirst()));

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

  @Test
  void shouldReturnShelvesList() {
    when(shelfRepository.findAll()).thenReturn(shelves);

    List<ShelfResponse> res = shelfService.getShelves();

    assertThat(res.size()).isEqualTo(3);
    verify(shelfRepository).findAll();
  }

}
