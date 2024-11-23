package com.solution.bank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import com.solution.bank.dto.transaction.request.TransactionRequest;
import com.solution.bank.dto.transaction.response.TransactionResponse;
import com.solution.bank.repository.AccountTransactionRepository;

class AccountTransactionServiceTest {

	@Mock
	private AccountTransactionRepository accountTransactionRepository;

	@Mock
	private PlatformTransactionManager transactionManager;

	@Mock
	private TransactionStatus transactionStatus;

	private AccountTransactionService accountTransactionService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		accountTransactionService = new AccountTransactionService(accountTransactionRepository, transactionManager);
	}

	@Test
	void shouldDepositSuccessfully() {
		TransactionRequest request = new TransactionRequest();
		request.setAccountNumber("123");
		request.setAmount(new BigDecimal(100));

		BigDecimal initialBalance = new BigDecimal(200);
		BigDecimal expectedBalance = initialBalance.add(request.getAmount());

		when(accountTransactionRepository.existsByAccountNumber(request.getAccountNumber())).thenReturn(true);
		when(accountTransactionRepository.getBalance(request.getAccountNumber())).thenReturn(initialBalance);

		accountTransactionService.deposit(request);

		verify(accountTransactionRepository).updateBalance(request.getAccountNumber(), expectedBalance);
	}

	@Test
	void shouldReturnErrorWhenAccountNotFoundForDeposit() {
		TransactionRequest request = new TransactionRequest();
		request.setAccountNumber("123");
		request.setAmount(new BigDecimal(100));

		when(accountTransactionRepository.existsByAccountNumber(request.getAccountNumber())).thenReturn(false);

		TransactionResponse response = accountTransactionService.deposit(request);

		assertFalse(response.isSuccess());
		assertEquals("Account does not exist.", response.getMessage());
	}

	@Test
	void shouldReturnErrorWhenInsufficientFundsForWithdrawal() {
		TransactionRequest request = new TransactionRequest();
		request.setAccountNumber("123");
		request.setAmount(new BigDecimal(500));

		BigDecimal initialBalance = new BigDecimal(200);

		when(accountTransactionRepository.existsByAccountNumber(request.getAccountNumber())).thenReturn(true);
		when(accountTransactionRepository.getBalance(request.getAccountNumber())).thenReturn(initialBalance);

		TransactionResponse response = accountTransactionService.withdraw(request);

		assertFalse(response.isSuccess());
		assertEquals("Insufficient funds.", response.getMessage());
	}

	@Test
	void shouldTransferSuccessfully() {
		TransactionRequest request = new TransactionRequest();
		request.setFromAccountNumber("fromAccount");
		request.setToAccountNumber("toAccount");
		request.setAmount(new BigDecimal(100));

		BigDecimal fromInitialBalance = new BigDecimal(200);
		BigDecimal toInitialBalance = new BigDecimal(200);

		when(accountTransactionRepository.existsByAccountNumber(request.getFromAccountNumber())).thenReturn(true);
		when(accountTransactionRepository.existsByAccountNumber(request.getToAccountNumber())).thenReturn(true);
		when(accountTransactionRepository.getBalance(request.getFromAccountNumber())).thenReturn(fromInitialBalance);
		when(accountTransactionRepository.getBalance(request.getToAccountNumber())).thenReturn(toInitialBalance);

		accountTransactionService.transfer(request);

		verify(accountTransactionRepository).updateBalance(request.getFromAccountNumber(), fromInitialBalance.subtract(request.getAmount()));
		verify(accountTransactionRepository).updateBalance(request.getToAccountNumber(), toInitialBalance.add(request.getAmount()));
	}

	@Test
	void shouldReturnErrorWhenInsufficientFundsForTransfer() {
		TransactionRequest request = new TransactionRequest();
		request.setFromAccountNumber("fromAccount");
		request.setToAccountNumber("toAccount");
		request.setAmount(new BigDecimal(500));

		BigDecimal fromInitialBalance = new BigDecimal(200);
		BigDecimal toInitialBalance = new BigDecimal(200);

		when(accountTransactionRepository.existsByAccountNumber(request.getFromAccountNumber())).thenReturn(true);
		when(accountTransactionRepository.existsByAccountNumber(request.getToAccountNumber())).thenReturn(true);
		when(accountTransactionRepository.getBalance(request.getFromAccountNumber())).thenReturn(fromInitialBalance);
		when(accountTransactionRepository.getBalance(request.getToAccountNumber())).thenReturn(toInitialBalance);

		TransactionResponse response = accountTransactionService.transfer(request);

		assertFalse(response.isSuccess());
		assertEquals("Insufficient funds in the sender's account.", response.getMessage());
	}

	@Test
	void shouldReturnErrorWhenTransferToSameAccount() {
		TransactionRequest request = new TransactionRequest();
		request.setFromAccountNumber("sameAccount");
		request.setToAccountNumber("sameAccount");
		request.setAmount(new BigDecimal(100));

		TransactionResponse response = accountTransactionService.transfer(request);

		assertFalse(response.isSuccess());
		assertEquals("Cannot transfer to the same account.", response.getMessage());
	}
}
