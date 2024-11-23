package com.solution.bank.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.solution.bank.dto.account.BankAccountDTO;
import com.solution.bank.dto.account.CreateBankAccountDTO;
import com.solution.bank.dto.search.account.AccountSearchCriteria;
import com.solution.bank.service.BankAccountService;

@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {

	private final BankAccountService bankAccountService;

	public BankAccountController(BankAccountService accountService) {
		this.bankAccountService = accountService;
	}

	@GetMapping
	public List<BankAccountDTO> getAccounts() {
		return bankAccountService.getAccounts();
	}

	@PostMapping("/search")
	public List<BankAccountDTO> searchAccounts(@RequestBody AccountSearchCriteria searchCriteria) {
		return bankAccountService.searchAccounts(searchCriteria);
	}

	@GetMapping("/{accountNumber}")
	public BankAccountDTO getAccount(@PathVariable String accountNumber) {
		return bankAccountService.getAccountByNumber(accountNumber);
	}

	@PostMapping("/create")
	public BankAccountDTO createAccount(@RequestBody CreateBankAccountDTO account) {
		return bankAccountService.createAccount(account);
	}
}
