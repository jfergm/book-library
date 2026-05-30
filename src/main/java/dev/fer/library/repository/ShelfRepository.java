package dev.fer.library.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import dev.fer.library.entity.Shelf;

public interface ShelfRepository extends CrudRepository<Shelf, Long>, PagingAndSortingRepository<Shelf, Long> {

}
