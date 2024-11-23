package com.solution.bank.repository.filter.account;

import static org.springframework.data.jpa.domain.Specification.where;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.solution.bank.domain.account.BankAccount;
import com.solution.bank.dto.search.account.AccountSearchCriteria;
import com.solution.bank.repository.filter.SpecificationBuilder;
import com.solution.bank.util.SQLUtils;

@Component
public class AccountSpecificationBuilder implements SpecificationBuilder<BankAccount, AccountSearchCriteria> {

	@Override
	public Specification<BankAccount> buildSpecification(AccountSearchCriteria criteria) {
		Specification<BankAccount> specification = (root, query, cb) -> cb.conjunction();

		List<Specification<BankAccount>> specs = new ArrayList<>();

		SQLUtils.addIfNotNull(specs, criteria.getBalanceGreaterThan(), this::getBalanceGreaterThanSpecification);
		SQLUtils.addIfNotNull(specs, criteria.getBalanceLessThan(), this::getBalanceLessThanSpecification);
		SQLUtils.addIfNotEmpty(specs, criteria.getOwnerName(), this::getOwnerNameSpecification);
		SQLUtils.addIfNotEmpty(specs, criteria.getCurrency(), this::getCurrencySpecification);

		for (Specification<BankAccount> spec : specs) {
			specification = where(specification).and(spec);
		}

		return specification;
	}

	private Specification<BankAccount> getBalanceGreaterThanSpecification(BigDecimal amount) {
		return (root, query, cb) -> cb.greaterThan(root.get("balance"), amount);
	}

	private Specification<BankAccount> getBalanceLessThanSpecification(BigDecimal amount) {
		return (root, query, cb) -> cb.lessThan(root.get("balance"), amount);
	}

	private Specification<BankAccount> getOwnerNameSpecification(String ownerName) {
		return (root, query, cb) -> cb.like(root.get("ownerName"), "%" + ownerName + "%");
	}

	private Specification<BankAccount> getCurrencySpecification(String currency) {
		return (root, query, cb) -> cb.equal(root.get("currency"), currency);
	}
}
