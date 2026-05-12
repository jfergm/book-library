package dev.fer.library.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.fer.library.dto.request.LibraryRequest;
import dev.fer.library.dto.response.LibraryResponse;
import dev.fer.library.entity.Library;

public class LibraryMapperTest {
  private LibraryMapper libraryMapper;

  @BeforeEach
  void setUp() {
    libraryMapper = new LibraryMapper();
  }

  @Test
  void shouldConvertRequestToEntity() {
    LibraryRequest request = new LibraryRequest("Library");

    Library library = libraryMapper.toEntity(request);

    assertThat(library).isNotNull();
    assertThat(library.getId()).isNull();
    assertThat(library.getName()).isEqualTo("Library");
    
  }

  @Test
  void shouldConvertEntityToResponse() {
    Library library = new Library(1L, "Library");

    LibraryResponse response = libraryMapper.toResponse(library);

    assertThat(response).isNotNull();
    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.name()).isEqualTo("Library");
  }

  @Test
  void shouldConvertoToResponseList() {
    List<Library> libraries = new ArrayList<>();
    libraries.add(new Library(1L ,"Library 1"));
    libraries.add(new Library(2L ,"Library 2"));

    List<LibraryResponse> responseList = libraryMapper.toResponseList(libraries);

    assertThat(responseList).hasSize(2);
    assertThat(responseList.getFirst().id()).isEqualTo(1L);
    assertThat(responseList.getFirst().name()).isEqualTo("Library 1");
    assertThat(responseList.getLast().id()).isEqualTo(2L);
    assertThat(responseList.getLast().name()).isEqualTo("Library 2");
  }
}
