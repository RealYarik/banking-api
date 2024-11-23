package com.solution.bank.domain.account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.solution.bank.domain.AbstractUuidEntity;
import com.solution.bank.domain.transaction.AccountTransaction;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bank_account")
@Getter
@Setter
public class BankAccount extends AbstractUuidEntity<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "account_number", unique = true, nullable = false)
	private String accountNumber;

	@Column(name = "owner_name", nullable = false)
	private String ownerName;

	@Column(name = "balance", nullable = false)
	private BigDecimal balance;

	@Column(name = "currency", nullable = false, length = 3)
	private String currency;

	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AccountTransaction> transactions = new ArrayList<>();
}
