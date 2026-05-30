package dev.fer.library.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import dev.fer.library.entity.BookCopy;

public interface BookCopyRepository extends CrudRepository<BookCopy, Long>, PagingAndSortingRepository<BookCopy, Long> {

}
