package com.solution.bank.dto.transaction.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TransactionResponse {

	private boolean success;
	private String message;
	private BigDecimal updatedBalance;
}