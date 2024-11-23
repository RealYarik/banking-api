package com.solution.bank.repository.filter;

import org.springframework.data.jpa.domain.Specification;

import com.solution.bank.dto.search.SearchCriteria;

public interface SpecificationBuilder<T, V extends SearchCriteria> {
	Specification<T> buildSpecification(V criteria);
}