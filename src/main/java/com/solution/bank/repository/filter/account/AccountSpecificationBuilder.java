package com.solution.bank.repository.filter.account;

import static org.springframework.data.jpa.domain.Specification.where;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.solution.bank.domain.account.Account;
import com.solution.bank.dto.search.account.AccountSearchCriteria;
import com.solution.bank.repository.filter.SpecificationBuilder;
import com.solution.bank.util.SQLUtils;

@Component
public class AccountSpecificationBuilder implements SpecificationBuilder<Account, AccountSearchCriteria> {

	@Override
	public Specification<Account> buildSpecification(AccountSearchCriteria criteria) {
		Specification<Account> specification = (root, query, cb) -> cb.conjunction();

		List<Specification<Account>> specs = new ArrayList<>();

		SQLUtils.addIfNotNull(specs, criteria.getBalanceGreaterThan(), this::getBalanceGreaterThanSpecification);
		SQLUtils.addIfNotNull(specs, criteria.getBalanceLessThan(), this::getBalanceLessThanSpecification);
		SQLUtils.addIfNotEmpty(specs, criteria.getOwnerName(), this::getOwnerNameSpecification);
		SQLUtils.addIfNotNull(specs, criteria.getCreationDate(), this::getCreationDateSpecification);
		SQLUtils.addIfNotEmpty(specs, criteria.getCurrency(), this::getCurrencySpecification);

		for (Specification<Account> spec : specs) {
			specification = where(specification).and(spec);
		}

		return specification;
	}

	private Specification<Account> getBalanceGreaterThanSpecification(Double amount) {
		return (root, query, cb) -> cb.greaterThan(root.get("balance"), amount);
	}

	private Specification<Account> getBalanceLessThanSpecification(Double amount) {
		return (root, query, cb) -> cb.lessThan(root.get("balance"), amount);
	}

	private Specification<Account> getOwnerNameSpecification(String ownerName) {
		return (root, query, cb) -> cb.like(root.get("ownerName"), "%" + ownerName + "%");
	}

	private Specification<Account> getCreationDateSpecification(Object creationDate) {
		return (root, query, cb) -> cb.equal(root.get("creationDate"), creationDate);
	}

	private Specification<Account> getCurrencySpecification(String currency) {
		return (root, query, cb) -> cb.equal(root.get("currency"), currency);
	}
}
