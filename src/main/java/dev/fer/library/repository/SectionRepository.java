package dev.fer.library.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import dev.fer.library.entity.Section;

public interface SectionRepository extends CrudRepository<Section, Long>, PagingAndSortingRepository<Section, Long> {

}
