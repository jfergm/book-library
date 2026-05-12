package dev.fer.library.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.fer.library.dto.request.LibraryRequest;
import dev.fer.library.dto.response.LibraryResponse;
import dev.fer.library.entity.Library;

@Component
public class LibraryMapper {
  public Library toEntity(LibraryRequest request) {
    Library library = new Library(null, request.name());

    return library;
  }

  public LibraryResponse toResponse(Library library) {
    return new LibraryResponse(
      library.getId(),
      library.getName()
    );
  }

  public List<LibraryResponse> toResponseList(List<Library> libraries) {
    return libraries.stream().map(this::toResponse).toList();
  }
}
