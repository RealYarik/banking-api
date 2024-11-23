package com.solution.bank.domain.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.solution.bank.domain.AbstractUuidEntity;
import com.solution.bank.domain.account.BankAccount;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "account_transaction")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "transaction_type", discriminatorType = DiscriminatorType.STRING)
public abstract class AccountTransaction extends AbstractUuidEntity<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "account_id", nullable = false)
	private BankAccount account;

	@Column(nullable = false)
	private LocalDateTime transactionDate;

	@Column(nullable = false)
	private BigDecimal amount;

	@Column(nullable = false)
	private String currency;
}
