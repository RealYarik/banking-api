package com.solution.bank.domain.account;

import java.math.BigDecimal;

import com.solution.bank.domain.AbstractUuidEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "account")
@Getter
@Setter
public class Account extends AbstractUuidEntity<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(unique = true, nullable = false)
	private String accountNumber;

	@Column(nullable = false)
	private String ownerName;

	@Column(nullable = false)
	private BigDecimal balance;

	@Column(nullable = false)
	private String currency;
}
