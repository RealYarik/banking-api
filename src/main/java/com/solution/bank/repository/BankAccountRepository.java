package com.solution.bank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.solution.bank.domain.account.BankAccount;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long>, JpaSpecificationExecutor<BankAccount> {

	Optional<BankAccount> findBankAccountByAccountNumber(String accountNumber);

	boolean existsByAccountNumber(String accountNumber);
}