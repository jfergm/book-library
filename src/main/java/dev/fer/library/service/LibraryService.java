package dev.fer.library.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.fer.library.dto.request.LibraryRequest;
import dev.fer.library.dto.response.LibraryResponse;
import dev.fer.library.entity.Library;
import dev.fer.library.exception.LibraryNotFoundException;
import dev.fer.library.mapper.LibraryMapper;
import dev.fer.library.repository.LibraryRepository;

@Service
public class LibraryService {

  private LibraryRepository libraryRepository;

  private LibraryMapper libraryMapper;

  protected LibraryService(LibraryRepository libraryRepository, LibraryMapper libraryMapper) {
    this.libraryRepository = libraryRepository;
    this.libraryMapper = libraryMapper;
  }

  public LibraryResponse getLibaryByID(Long id) {
    Optional<Library> library = libraryRepository.findById(id);

    if (library.isEmpty()) {
      throw new LibraryNotFoundException();
    }

    return libraryMapper.toResponse(library.get());
  }

  public List<LibraryResponse> getLibraries(Pageable pageable) {
    return libraryMapper.toResponseList(
      libraryRepository.findAll(pageable).getContent());
  }

  public LibraryResponse createLibrary(LibraryRequest libraryRequest) {
    Library library = libraryMapper.toEntity(libraryRequest);

    return libraryMapper.toResponse(libraryRepository.save(library));
  }

  public LibraryResponse updateLibrary(Long id, LibraryRequest update) {
    Optional<Library> library = libraryRepository.findById(id);

    if (library.isEmpty()) {
      throw new LibraryNotFoundException();
    }

    Library updated = new Library(library.get().getId(), update.name());

    libraryRepository.save(updated);

    return libraryMapper.toResponse(updated);
  }

  public void deleteLibrary(Long id) {
    if (libraryRepository.existsById(id)) {
      libraryRepository.deleteById(id);
      return;
    }

    throw new LibraryNotFoundException();
  }
}
