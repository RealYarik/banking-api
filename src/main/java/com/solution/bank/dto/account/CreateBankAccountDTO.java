package com.solution.bank.dto.account;

import lombok.Data;

@Data
public class CreateBankAccountDTO {

	private String ownerName;
	private String currency;
}