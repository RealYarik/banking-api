package com.solution.bank.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solution.bank.domain.account.BankAccount;
import com.solution.bank.dto.account.BankAccountDTO;
import com.solution.bank.dto.account.CreateBankAccountDTO;
import com.solution.bank.dto.search.account.AccountSearchCriteria;
import com.solution.bank.mapper.BankAccountMapper;
import com.solution.bank.repository.BankAccountRepository;
import com.solution.bank.repository.filter.account.AccountSpecificationBuilder;
import com.solution.bank.util.RandomUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BankAccountService {

	private final AccountSpecificationBuilder accountSpecificationBuilder;
	private final BankAccountRepository bankAccountRepository;
	private final BankAccountMapper bankAccountMapper;

	public List<BankAccountDTO> searchAccounts(AccountSearchCriteria criteria) {
		Specification<BankAccount> specification = accountSpecificationBuilder.buildSpecification(criteria);
		return bankAccountMapper.toDTOList(bankAccountRepository.findAll(specification));
	}

	public BankAccountDTO getAccountByNumber(String accountNumber) {
		BankAccount bankAccount = bankAccountRepository.findBankAccountByAccountNumber(accountNumber)
			.orElseThrow(() -> new IllegalArgumentException("Account not found"));
		return bankAccountMapper.toDTO(bankAccount);
	}

	@Transactional
	public BankAccountDTO createAccount(CreateBankAccountDTO createBankAccountDTO) {
		BankAccount bankAccount = bankAccountMapper.toEntity(createBankAccountDTO);
		bankAccount.setAccountNumber(RandomUtils.generateAccountNumber());
		bankAccount.setBalance(new BigDecimal(0));

		BankAccount savedAccount = bankAccountRepository.save(bankAccount);

		return bankAccountMapper.toDTO(savedAccount);
	}
}
