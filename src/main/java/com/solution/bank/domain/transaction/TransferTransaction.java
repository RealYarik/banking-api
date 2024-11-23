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

	@ManyToOne(optional = true)
	@JoinColumn(name = "target_account_id")
	private BankAccount targetAccount;

	@Column
	private String transferPurpose;
}
