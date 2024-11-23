package com.solution.bank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solution.bank.dto.transaction.request.TransactionRequest;
import com.solution.bank.dto.transaction.response.TransactionResponse;
import com.solution.bank.service.AccountTransactionService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@AllArgsConstructor
public class AccountTransactionController {

	private final AccountTransactionService accountTransactionService;

	@PostMapping("/deposit")
	public ResponseEntity<TransactionResponse> deposit(@RequestBody TransactionRequest request) {
		TransactionResponse response = accountTransactionService.deposit(request);

		return getTransactionResponse(response);
	}

	@PostMapping("/withdraw")
	public ResponseEntity<TransactionResponse> withdraw(@RequestBody TransactionRequest request) {
		TransactionResponse response = accountTransactionService.withdraw(request);

		return getTransactionResponse(response);
	}

	@PostMapping("/transfer")
	public ResponseEntity<TransactionResponse> transfer(@RequestBody TransactionRequest request) {
		TransactionResponse response = accountTransactionService.transfer(request);

		return getTransactionResponse(response);
	}

	private static ResponseEntity<TransactionResponse> getTransactionResponse(TransactionResponse response) {
		return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.status(400).body(response);
	}
}
