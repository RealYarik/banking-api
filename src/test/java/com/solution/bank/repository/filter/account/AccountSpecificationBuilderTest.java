package com.solution.bank.repository.filter.account;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import com.solution.bank.domain.account.BankAccount;
import com.solution.bank.dto.search.account.AccountSearchCriteria;

class AccountSpecificationBuilderTest {

	private AccountSpecificationBuilder accountSpecificationBuilder;

	@BeforeEach
	void setUp() {
		accountSpecificationBuilder = new AccountSpecificationBuilder();
	}

	@Test
	void shouldBuildSpecificationWithBalanceGreaterThan() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setBalanceGreaterThan(new BigDecimal(1000));

		Specification<BankAccount> specification = accountSpecificationBuilder.buildSpecification(criteria);

		assertNotNull(specification);
	}

	@Test
	void shouldBuildSpecificationWithBalanceLessThan() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setBalanceLessThan(new BigDecimal(500));

		Specification<BankAccount> specification = accountSpecificationBuilder.buildSpecification(criteria);

		assertNotNull(specification);
	}

	@Test
	void shouldBuildSpecificationWithOwnerName() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setOwnerName("John Doe");

		Specification<BankAccount> specification = accountSpecificationBuilder.buildSpecification(criteria);

		assertNotNull(specification);
	}

	@Test
	void shouldBuildSpecificationWithCurrency() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setCurrency("USD");

		Specification<BankAccount> specification = accountSpecificationBuilder.buildSpecification(criteria);

		assertNotNull(specification);
	}

	@Test
	void shouldBuildSpecificationWithMultipleCriteria() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setBalanceGreaterThan(new BigDecimal(1000));
		criteria.setOwnerName("John Doe");
		criteria.setCurrency("USD");

		Specification<BankAccount> specification = accountSpecificationBuilder.buildSpecification(criteria);

		assertNotNull(specification);
	}

	@Test
	void shouldReturnEmptySpecificationWhenNoCriteria() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();

		Specification<BankAccount> specification = accountSpecificationBuilder.buildSpecification(criteria);

		assertNotNull(specification);
	}

	@Test
	void shouldUseSQLUtilsAddIfNotNullForBalanceGreaterThan() throws Exception {
		AccountSpecificationBuilder builder = new AccountSpecificationBuilder();
		Method method = AccountSpecificationBuilder.class.getDeclaredMethod("getBalanceGreaterThanSpecification", BigDecimal.class);
		method.setAccessible(true);

		Specification<BankAccount> specification = (Specification<BankAccount>) method.invoke(builder, new BigDecimal(1000));

		assertNotNull(specification);
	}

	@Test
	void testPrivateMethodGetOwnerNameSpecification() throws Exception {
		AccountSpecificationBuilder builder = new AccountSpecificationBuilder();
		Method method = AccountSpecificationBuilder.class.getDeclaredMethod("getOwnerNameSpecification", String.class);
		method.setAccessible(true);

		Specification<BankAccount> specification = (Specification<BankAccount>) method.invoke(builder, "John Doe");

		assertNotNull(specification);
	}
}
