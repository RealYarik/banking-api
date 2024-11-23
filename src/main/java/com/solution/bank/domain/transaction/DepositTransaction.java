package com.solution.bank.domain.transaction;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("DEPOSIT")
@Getter
@Setter
public class DepositTransaction extends AccountTransaction {

	@Column
	private String source;
}