package dev.fer.library.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import dev.fer.library.entity.Floor;

public interface FloorRepository extends CrudRepository<Floor, Long>, PagingAndSortingRepository<Floor, Long> {

}
