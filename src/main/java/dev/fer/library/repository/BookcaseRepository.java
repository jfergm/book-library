package dev.fer.library.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import dev.fer.library.entity.Bookcase;

public interface BookcaseRepository extends CrudRepository<Bookcase, Long>, PagingAndSortingRepository<Bookcase, Long> {
}
