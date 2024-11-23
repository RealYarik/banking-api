package com.solution.bank.dto.transaction;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountTransactionDTO {

	private Long id;
	private BigDecimal amount;
	private String transactionType;
	private String transactionDate;
}
