package com.solution.bank.repository;

import java.math.BigDecimal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.solution.bank.dto.transaction.request.TransactionRequest;

import lombok.RequiredArgsConstructor;

/**
 * By using raw SQL queries, we avoid issues with Hibernate's caching mechanisms or potential inconsistencies
 * that might arise in complex transactional scenarios.
 * This is especially important in a banking application, where consistency and isolation are crucial.
 */
@Repository
@RequiredArgsConstructor
public class AccountTransactionRepository {

	private final JdbcTemplate jdbcTemplate;

	public BigDecimal getBalance(String accountNumber) {
		String query = "SELECT balance FROM bank_account WHERE account_number = ?";
		return jdbcTemplate.queryForObject(query, BigDecimal.class, accountNumber);
	}

	public void updateBalance(String accountNumber, BigDecimal newBalance) {
		String query = "UPDATE bank_account SET balance = ? WHERE account_number = ?";
		jdbcTemplate.update(query, newBalance, accountNumber);
	}

	public void logDepositTransaction(TransactionRequest request) {
		String query = "INSERT INTO account_transaction (account_id, transaction_date, amount, currency, transaction_type, source) " +
			"VALUES ((SELECT id FROM bank_account WHERE account_number = ?), NOW(), ?, ?, 'DEPOSIT', ?)";
		jdbcTemplate.update(query, request.getAccountNumber(), request.getAmount(), request.getCurrency(), request.getSource());
	}

	public void logWithdrawalTransaction(TransactionRequest request) {
		String query = "INSERT INTO account_transaction (account_id, transaction_date, amount, currency, transaction_type, atm_location) " +
			"VALUES ((SELECT id FROM bank_account WHERE account_number = ?), NOW(), ?, ?, 'WITHDRAWAL', ?)";
		jdbcTemplate.update(query, request.getAccountNumber(), request.getAmount(), request.getCurrency(), request.getAtmLocation());
	}

	public void logTransferTransaction(TransactionRequest request) {
		String query = "INSERT INTO account_transaction (account_id, transaction_date, amount, currency, transaction_type, transfer_purpose) " +
			"VALUES ((SELECT id FROM bank_account WHERE account_number = ?), NOW(), ?, ?, 'TRANSFER', ?)";
		jdbcTemplate.update(query, request.getFromAccountNumber(), request.getAmount(), request.getCurrency(), request.getTransferPurpose());

		query = "INSERT INTO account_transaction (account_id, transaction_date, amount, currency, transaction_type, transfer_purpose) " +
			"VALUES ((SELECT id FROM bank_account WHERE account_number = ?), NOW(), ?, ?, 'TRANSFER', ?)";
		jdbcTemplate.update(query, request.getToAccountNumber(), request.getAmount(), request.getCurrency(), request.getTransferPurpose());
	}
}
