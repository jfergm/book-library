package dev.fer.library.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import dev.fer.library.entity.Loan;

public interface LoanRepository extends CrudRepository<Loan, Long>, PagingAndSortingRepository<Loan, Long> {

}
