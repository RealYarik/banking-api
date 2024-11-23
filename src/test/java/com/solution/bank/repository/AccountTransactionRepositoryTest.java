package com.solution.bank.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import com.solution.bank.dto.transaction.request.TransactionRequest;

@JdbcTest
@Import(AccountTransactionRepository.class)
class AccountTransactionRepositoryTest {

	@Autowired
	private AccountTransactionRepository repository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@BeforeEach
	void setUp() {
		jdbcTemplate.execute("RUNSCRIPT FROM 'classpath:schema.sql'");
		jdbcTemplate.execute("RUNSCRIPT FROM 'classpath:data.sql'");
	}

	@Test
	void shouldGetBalance() {
		BigDecimal balance = repository.getBalance("ACCT-001");
		assertThat(balance).isEqualByComparingTo(BigDecimal.valueOf(1000.00));
	}

	@Test
	void shouldUpdateBalance() {
		repository.updateBalance("ACCT-001", BigDecimal.valueOf(1200.00));
		BigDecimal balance = repository.getBalance("ACCT-001");
		assertThat(balance).isEqualByComparingTo(BigDecimal.valueOf(1200.00));
	}

	@Test
	void shouldLogTransferTransaction() {
		TransactionRequest request = new TransactionRequest();
		request.setFromAccountNumber("ACCT-001");
		request.setToAccountNumber("ACCT-002");
		request.setAmount(BigDecimal.valueOf(150.00));
		request.setCurrency("USD");
		request.setTransferPurpose("Test transfer");

		repository.logTransferTransaction(request);

		Integer count = jdbcTemplate.queryForObject(
			"SELECT COUNT(*) FROM account_transaction WHERE transaction_type = 'TRANSFER'",
			Integer.class
		);
		assertThat(count).isEqualTo(1);
	}

	@Test
	void shouldLogDepositTransaction() {
		TransactionRequest request = new TransactionRequest();
		request.setAccountNumber("ACCT-001");
		request.setAmount(BigDecimal.valueOf(500.00));
		request.setCurrency("USD");
		request.setSource("Cash Deposit");

		repository.logDepositTransaction(request);

		Integer count = jdbcTemplate.queryForObject(
			"SELECT COUNT(*) FROM account_transaction WHERE transaction_type = 'DEPOSIT' AND account_id = (SELECT id FROM bank_account WHERE account_number = ?)",
			Integer.class, "ACCT-001"
		);

		assertThat(count).isEqualTo(3);
	}

	@Test
	void shouldLogWithdrawalTransaction() {
		TransactionRequest request = new TransactionRequest();
		request.setAccountNumber("ACCT-001");
		request.setAmount(BigDecimal.valueOf(200.00));
		request.setCurrency("USD");
		request.setAtmLocation("ATM-001");

		repository.logWithdrawalTransaction(request);

		Integer count = jdbcTemplate.queryForObject(
			"SELECT COUNT(*) FROM account_transaction WHERE transaction_type = 'WITHDRAWAL' AND account_id = (SELECT id FROM bank_account WHERE account_number = ?)",
			Integer.class, "ACCT-001"
		);

		assertThat(count).isEqualTo(1);
	}

	@Test
	void shouldCheckAccountExists() {
		boolean exists = repository.existsByAccountNumber("ACCT-001");
		assertThat(exists).isTrue();

		boolean notExists = repository.existsByAccountNumber("ACCT-999");
		assertThat(notExists).isFalse();
	}
}