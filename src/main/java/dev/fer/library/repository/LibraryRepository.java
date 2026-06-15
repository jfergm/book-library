package dev.fer.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import dev.fer.library.entity.Library;

public interface LibraryRepository extends CrudRepository<Library, Long>, PagingAndSortingRepository<Library, Long> {
  Page<Library> findAll(Pageable pageable);
}
