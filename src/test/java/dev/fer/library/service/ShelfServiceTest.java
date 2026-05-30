package dev.fer.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
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

import dev.fer.library.dto.request.ShelfRequest;
import dev.fer.library.dto.response.ShelfResponse;
import dev.fer.library.entity.Bookcase;
import dev.fer.library.entity.Shelf;
import dev.fer.library.exception.BadRequestException;
import dev.fer.library.exception.ShelfNotFoundException;
import dev.fer.library.mapper.ShelfMapper;
import dev.fer.library.repository.BookcaseRepository;
import dev.fer.library.repository.ShelfRepository;

@ExtendWith(MockitoExtension.class)
class ShelfServiceTest {
  @Mock
  ShelfRepository shelfRepository;

  @Mock
  BookcaseRepository bookcaseRepository;

  ShelfMapper shelfMapper = new ShelfMapper();

  ShelfService shelfService;

  private List<Shelf> shelves;

  @BeforeEach
  void setUp() {
    shelfService = new ShelfService(shelfRepository, shelfMapper, bookcaseRepository);

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

    assertThat(res).hasSize(3);
    verify(shelfRepository).findAll();
  }

  @Test
  void shouldCreateShelf() {
    when(bookcaseRepository.findById(anyLong())).thenReturn(
      Optional.of(shelves.getFirst().getBookcase())
    );
    when(shelfRepository.save(any())).thenReturn(shelves.getFirst());

    ShelfRequest request = new ShelfRequest("A1", "Shelf A1", 1L);

    ShelfResponse created = shelfService.createShelf(request);

    assertThat(created.id()).isEqualTo(1L);
    assertThat(created.code()).isEqualTo("A1");
    assertThat(created.label()).isEqualTo("Shelf A1");
    assertThat(created.bookcaseId()).isEqualTo(1L);

    verify(shelfRepository).save(any());
    verify(bookcaseRepository).findById(anyLong());
  }

  @Test
  void shouldThrowWhenCreateWithInvalidBookcase() {
    when(bookcaseRepository.findById(anyLong())).thenReturn(Optional.empty());

    ShelfRequest request = new ShelfRequest("A1", "Shelf A1", 1L);

    assertThrows(BadRequestException.class, () -> shelfService.createShelf(request));    
    verify(shelfRepository, times(0)).save(any());
    verify(bookcaseRepository).findById(anyLong());
  }

  @Test
  void shouldUpdateShelf() {
    when(shelfRepository.findById(1L)).thenReturn(Optional.of(shelves.getFirst()));
    when(shelfRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

    ShelfRequest update = new ShelfRequest("NC", "New code", 1L);

    ShelfResponse shelf = shelfService.updateShelf(1L, update);

    assertThat(shelf.code()).isEqualTo("NC");
    assertThat(shelf.label()).isEqualTo("New code");
    assertThat(shelf.id()).isEqualTo(1L);
    assertThat(shelf.bookcaseId()).isEqualTo(1L);

    verify(shelfRepository).findById(1L);
    verify(shelfRepository).save(any(Shelf.class));
  }

  @Test
  void shouldFindBookcaseWhenUpdateWithNewBookcase() {
    when(shelfRepository.findById(1L)).thenReturn(Optional.of(shelves.getFirst()));
    when(bookcaseRepository.findById(anyLong())).thenReturn(Optional.of(
      new Bookcase(3L, null, null, null)
    ));
    when(shelfRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

    ShelfRequest update = new ShelfRequest("NC", "New code", 3L);

    ShelfResponse shelf = shelfService.updateShelf(1L, update);

    assertThat(shelf.bookcaseId()).isEqualTo(3L);

    verify(shelfRepository).findById(1L);
    verify(bookcaseRepository).findById(3L);
    verify(shelfRepository).save(any(Shelf.class));
  }

  @Test
  void shouldThrowNotFoundWhenUpdateInvalidShelf() {
    when(shelfRepository.findById(1L)).thenReturn(Optional.empty());

    ShelfRequest update = new ShelfRequest("NC", "New code", 1L);

    assertThrows(ShelfNotFoundException.class, () ->  shelfService.updateShelf(1L, update));

    verify(shelfRepository).findById(1L);
    verify(shelfRepository, times(0)).save(any(Shelf.class));
  }

  @Test
  void shouldThrowBadRequestWhenUpdateWithInvalidBookcase() {
    when(shelfRepository.findById(1L)).thenReturn(Optional.of(shelves.getFirst()));
    when(bookcaseRepository.findById(anyLong())).thenReturn(Optional.empty());

    ShelfRequest update = new ShelfRequest("NC", "New code", 3L);

    assertThrows(BadRequestException.class, () ->  shelfService.updateShelf(1L, update));

    verify(shelfRepository).findById(1L);
    verify(shelfRepository).findById(1L);
    verify(shelfRepository, times(0)).save(any(Shelf.class));
  }

  @Test
  void shouldDeleteShelf() {
    when(shelfRepository.existsById(1L)).thenReturn(true);
    doNothing().when(shelfRepository).deleteById(1L);
    shelfService.deleteShelf(1L);

    verify(shelfRepository).existsById(1L);
    verify(shelfRepository).deleteById(1L);
  }

  @Test
  void shouldThrowWhenDeletingInvalidShelf() {
    when(shelfRepository.existsById(1L)).thenReturn(false);
    assertThrows(ShelfNotFoundException.class, () -> shelfService.deleteShelf(1L));

    verify(shelfRepository).existsById(1L);
    verify(shelfRepository, times(0)).deleteById(1L);
  }
}
