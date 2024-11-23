package com.solution.bank.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solution.bank.dto.transaction.request.TransactionRequest;
import com.solution.bank.dto.transaction.response.TransactionResponse;
import com.solution.bank.repository.AccountTransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountTransactionService {

	private final AccountTransactionRepository accountTransactionRepository;

	@Transactional
	public TransactionResponse deposit(TransactionRequest request) {
		return processTransaction(request, "Deposit", BigDecimal::add, accountTransactionRepository::logDepositTransaction);
	}

	@Transactional
	public TransactionResponse withdraw(TransactionRequest request) {
		return processTransaction(request, "Withdrawal", BigDecimal::subtract, accountTransactionRepository::logWithdrawalTransaction);
	}

	@Transactional
	public TransactionResponse transfer(TransactionRequest request) {
		try {
			checkAmount(request.getAmount(), "Transfer amount must be positive.");

			BigDecimal fromBalance = accountTransactionRepository.getBalance(request.getFromAccountNumber());
			BigDecimal toBalance = accountTransactionRepository.getBalance(request.getToAccountNumber());

			if (fromBalance.compareTo(request.getAmount()) < 0) {
				return new TransactionResponse(false, "Insufficient funds in the sender's account.", null);
			}

			BigDecimal newFromBalance = fromBalance.subtract(request.getAmount());
			accountTransactionRepository.updateBalance(request.getFromAccountNumber(), newFromBalance);

			BigDecimal newToBalance = toBalance.add(request.getAmount());
			accountTransactionRepository.updateBalance(request.getToAccountNumber(), newToBalance);

			accountTransactionRepository.logTransferTransaction(request);

			return new TransactionResponse(true, "Transfer successful.", newFromBalance);
		} catch (Exception e) {
			return new TransactionResponse(false, "Error processing transfer: " + e.getMessage(), null);
		}
	}

	private TransactionResponse processTransaction(TransactionRequest request, String transactionType,
		BalanceOperation balanceOperation, TransactionLogger transactionLogger) {
		try {
			checkAmount(request.getAmount(), transactionType + " amount must be positive.");

			BigDecimal balance = accountTransactionRepository.getBalance(request.getAccountNumber());
			if (balanceOperation.apply(balance, request.getAmount()).compareTo(balance) < 0 && transactionType.equals("Withdrawal")) {
				return new TransactionResponse(false, "Insufficient funds.", null);
			}

			BigDecimal newBalance = balanceOperation.apply(balance, request.getAmount());
			accountTransactionRepository.updateBalance(request.getAccountNumber(), newBalance);

			transactionLogger.log(request);

			return new TransactionResponse(true, transactionType + " successful. New balance: " + newBalance, newBalance);
		} catch (Exception e) {
			return new TransactionResponse(false, "Error processing " + transactionType.toLowerCase() + ": " + e.getMessage(), null);
		}
	}

	private static void checkAmount(BigDecimal amount, String errorMessage) {
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException(errorMessage);
		}
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