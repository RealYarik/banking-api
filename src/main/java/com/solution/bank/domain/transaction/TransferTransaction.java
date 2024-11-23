package com.solution.bank.domain.transaction;

import com.solution.bank.domain.account.BankAccount;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("TRANSFER")
@Getter
@Setter
public class TransferTransaction extends AccountTransaction {

	@ManyToOne(optional = false)
	@JoinColumn(name = "target_account_id", nullable = false)
	private BankAccount targetAccount;

	@Column(nullable = false)
	private String transferPurpose;
}
