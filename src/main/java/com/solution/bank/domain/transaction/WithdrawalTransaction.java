package com.solution.bank.domain.transaction;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("WITHDRAWAL")
@Getter
@Setter
public class WithdrawalTransaction extends AccountTransaction {

	@Column
	private String atmLocation;
}
