package dev.fer.library.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.fer.library.entity.Library;
import dev.fer.library.exception.LibraryNotFoundException;
import dev.fer.library.repository.LibraryRepository;

@Service
public class LibraryService {

  private LibraryRepository libraryRepository;

  protected LibraryService(LibraryRepository libraryRepository) {{
    this.libraryRepository = libraryRepository;
  }}

  public Library getLibaryByID(Long id) {
    Optional<Library> library = libraryRepository.findById(id);

    if (library.isEmpty()) {
      throw new LibraryNotFoundException();
    }

    return library.get();
  }

  public List<Library> getLibraries() {
    return (List<Library>) libraryRepository.findAll();
  }

  public Library createLibrary(Library library) {
    return libraryRepository.save(library);
  }
}
