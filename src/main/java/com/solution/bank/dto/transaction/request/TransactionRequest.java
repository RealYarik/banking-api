package com.solution.bank.dto.transaction.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransactionRequest {

	private String accountNumber;
	private BigDecimal amount;
	private String source;
	private String currency;
	private String atmLocation;
	private String transferPurpose;
	private String fromAccountNumber;
	private String toAccountNumber;
}
