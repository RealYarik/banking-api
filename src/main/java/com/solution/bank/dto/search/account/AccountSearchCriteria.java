package com.solution.bank.dto.search.account;

import java.util.Date;

import com.solution.bank.dto.search.SearchCriteria;

import lombok.Data;

@Data
public class AccountSearchCriteria implements SearchCriteria {

	private Double balanceGreaterThan;
	private Double balanceLessThan;
	private String ownerName;
	private Date creationDate;
	private String currency;
}