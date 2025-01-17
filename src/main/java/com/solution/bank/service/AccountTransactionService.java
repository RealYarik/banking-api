package com.solution.bank.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.solution.bank.dto.transaction.request.TransactionRequest;
import com.solution.bank.dto.transaction.response.TransactionResponse;
import com.solution.bank.repository.AccountTransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountTransactionService {

	private final AccountTransactionRepository accountTransactionRepository;
	private final PlatformTransactionManager transactionManager;

	public TransactionResponse deposit(TransactionRequest request) {
		return processTransaction(request, "Deposit", BigDecimal::add, accountTransactionRepository::logDepositTransaction);
	}

	public TransactionResponse withdraw(TransactionRequest request) {
		return processTransaction(request, "Withdrawal", BigDecimal::subtract, accountTransactionRepository::logWithdrawalTransaction);
	}

	public TransactionResponse transfer(TransactionRequest request) {
		TransactionStatus status = beginTransaction();
		try {
			TransactionResponse validationResponse = validateAccounts(request.getFromAccountNumber(), request.getToAccountNumber());
			if (validationResponse != null) {
				rollbackTransaction(status);
				return validationResponse;
			}

			checkAmount(request.getAmount(), "Transfer amount must be positive.");

			BigDecimal fromBalance = accountTransactionRepository.getBalance(request.getFromAccountNumber());
			BigDecimal toBalance = accountTransactionRepository.getBalance(request.getToAccountNumber());

			if (fromBalance.compareTo(request.getAmount()) < 0) {
				rollbackTransaction(status);
				return new TransactionResponse(false, "Insufficient funds in the sender's account.", null);
			}

			BigDecimal newFromBalance = fromBalance.subtract(request.getAmount());
			accountTransactionRepository.updateBalance(request.getFromAccountNumber(), newFromBalance);

			BigDecimal newToBalance = toBalance.add(request.getAmount());
			accountTransactionRepository.updateBalance(request.getToAccountNumber(), newToBalance);

			accountTransactionRepository.logTransferTransaction(request);

			commitTransaction(status);

			return new TransactionResponse(true, "Transfer successful.", newFromBalance);
		} catch (Exception e) {
			rollbackTransaction(status);
			return new TransactionResponse(false, "Error processing transfer: " + e.getMessage(), null);
		}
	}

	private TransactionResponse processTransaction(TransactionRequest request, String transactionType,
		BalanceOperation balanceOperation, TransactionLogger transactionLogger) {
		TransactionStatus status = beginTransaction();
		try {
			if (!accountTransactionRepository.existsByAccountNumber(request.getAccountNumber())) {
				rollbackTransaction(status);
				return new TransactionResponse(false, "Account does not exist.", null);
			}

			checkAmount(request.getAmount(), transactionType + " amount must be positive.");

			BigDecimal balance = accountTransactionRepository.getBalance(request.getAccountNumber());
			BigDecimal newBalance = balanceOperation.apply(balance, request.getAmount());

			if (transactionType.equals("Withdrawal") && newBalance.compareTo(BigDecimal.ZERO) < 0) {
				rollbackTransaction(status);
				return new TransactionResponse(false, "Insufficient funds.", null);
			}

			accountTransactionRepository.updateBalance(request.getAccountNumber(), newBalance);

			transactionLogger.log(request);

			commitTransaction(status);

			return new TransactionResponse(true, transactionType + " successful. New balance: " + newBalance, newBalance);
		} catch (Exception e) {
			rollbackTransaction(status);
			return new TransactionResponse(false, "Error processing " + transactionType.toLowerCase() + ": " + e.getMessage(), null);
		}
	}

	private TransactionStatus beginTransaction() {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		return transactionManager.getTransaction(def);
	}

	private void commitTransaction(TransactionStatus status) {
		transactionManager.commit(status);
	}

	private void rollbackTransaction(TransactionStatus status) {
		transactionManager.rollback(status);
	}

	private static void checkAmount(BigDecimal amount, String errorMessage) {
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException(errorMessage);
		}
	}

	private TransactionResponse validateAccounts(String fromAccountNumber, String toAccountNumber) {
		if (fromAccountNumber.equals(toAccountNumber)) {
			return new TransactionResponse(false, "Cannot transfer to the same account.", null);
		}

		if (!accountTransactionRepository.existsByAccountNumber(fromAccountNumber)) {
			return new TransactionResponse(false, "Sender account does not exist.", null);
		}

		if (!accountTransactionRepository.existsByAccountNumber(toAccountNumber)) {
			return new TransactionResponse(false, "Recipient account does not exist.", null);
		}

		return null;
	}

	@FunctionalInterface
	private interface BalanceOperation {

		BigDecimal apply(BigDecimal balance, BigDecimal amount);
	}

	@FunctionalInterface
	private interface TransactionLogger {

		void log(TransactionRequest request);
	}
}