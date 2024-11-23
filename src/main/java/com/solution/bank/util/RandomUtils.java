package com.solution.bank.util;

import java.util.Random;
import java.util.UUID;

public class RandomUtils {

	private static final Random random = new Random();

	private RandomUtils() {
	}

	public static String generateAccountNumber() {
		int randomNumber = random.nextInt(1000000);
		return "ACCT-" + String.format("%06d", randomNumber);
	}

	public static String generateUuid() {
		return UUID.randomUUID().toString();
	}
}
