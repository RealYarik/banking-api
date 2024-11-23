package com.solution.bank.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RandomUtilsTest {

	@Test
	void shouldGenerateAccountNumberWithCorrectFormat() {
		String accountNumber = RandomUtils.generateAccountNumber();

		assertNotNull(accountNumber);
		assertTrue(accountNumber.startsWith("ACCT-"));
		assertEquals(11, accountNumber.length());
		assertTrue(accountNumber.substring(5).matches("\\d{6}"));
	}

	@Test
	void shouldGenerateUuidWithCorrectFormat() {
		String uuid = RandomUtils.generateUuid();

		assertNotNull(uuid);
		assertTrue(uuid.matches("^[a-f0-9\\-]{36}$"));
	}
}
