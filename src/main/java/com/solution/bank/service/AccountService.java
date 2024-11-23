package com.solution.bank.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.solution.bank.domain.account.Account;
import com.solution.bank.dto.search.account.AccountSearchCriteria;
import com.solution.bank.repository.AccountRepository;
import com.solution.bank.repository.filter.account.AccountSpecificationBuilder;

@Service
public class AccountService {

	private final AccountSpecificationBuilder accountSpecificationBuilder;

	private final AccountRepository accountRepository;

	@Autowired
	public AccountService(AccountSpecificationBuilder accountSpecificationBuilder, AccountRepository accountRepository) {
		this.accountSpecificationBuilder = accountSpecificationBuilder;
		this.accountRepository = accountRepository;
	}

	public List<Account> searchAccounts(AccountSearchCriteria criteria) {
		Specification<Account> specification = accountSpecificationBuilder.buildSpecification(criteria);
		return accountRepository.findAll(specification);
	}

	public Account getAccountById(Long accountId) {
		//todo need to implement getAccountById
		return null;
	}

	public Account deposit(Long accountId, BigDecimal amount) {
		//todo need to implement deposit
		return null;
	}

	public Account withdraw(Long accountId, BigDecimal amount) {
		//todo need to implement withdraw
		return null;
	}

	public String transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
		//todo need to implement transfer
		return null;
	}

	public Account createAccount(Account account) {
		//todo need to implement createAccount
		return null;
	}
}
