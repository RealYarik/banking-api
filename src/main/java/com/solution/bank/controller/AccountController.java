package com.solution.bank.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.solution.bank.domain.account.Account;
import com.solution.bank.dto.search.account.AccountSearchCriteria;
import com.solution.bank.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@PostMapping("/search")
	public List<Account> searchAccounts(@RequestBody AccountSearchCriteria searchCriteria) {
		return accountService.searchAccounts(searchCriteria);
	}

	@GetMapping("/{accountId}")
	public Account getAccount(@PathVariable Long accountId) {
		return accountService.getAccountById(accountId);
	}

	@PostMapping("/create")
	public Account createAccount(@RequestBody Account account) {
		return accountService.createAccount(account);
	}

	@PostMapping("/{accountId}/deposit")
	public Account deposit(@PathVariable Long accountId, @RequestParam BigDecimal amount) {
		return accountService.deposit(accountId, amount);
	}

	@PostMapping("/{accountId}/withdraw")
	public Account withdraw(@PathVariable Long accountId, @RequestParam BigDecimal amount) {
		return accountService.withdraw(accountId, amount);
	}

	@PostMapping("/transfer")
	public String transfer(@RequestParam Long fromAccountId, @RequestParam Long toAccountId, @RequestParam BigDecimal amount) {
		return accountService.transfer(fromAccountId, toAccountId, amount);
	}
}
