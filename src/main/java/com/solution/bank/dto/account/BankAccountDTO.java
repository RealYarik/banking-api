package com.solution.bank.dto.account;

import java.math.BigDecimal;
import java.util.List;

import com.solution.bank.dto.transaction.AccountTransactionDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountDTO {

	private Long id;
	private String accountNumber;
	private String ownerName;
	private BigDecimal balance;
	private String currency;
	private List<AccountTransactionDTO> transactions;
}
