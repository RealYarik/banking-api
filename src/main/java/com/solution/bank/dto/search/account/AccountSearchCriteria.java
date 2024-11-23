package com.solution.bank.dto.search.account;

import java.math.BigDecimal;

import com.solution.bank.dto.search.SearchCriteria;

import lombok.Data;

@Data
public class AccountSearchCriteria implements SearchCriteria {

	private BigDecimal balanceGreaterThan;
	private BigDecimal balanceLessThan;
	private String ownerName;
	private String currency;
}