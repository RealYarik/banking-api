package com.solution.bank.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;

import com.solution.bank.domain.account.BankAccount;
import com.solution.bank.domain.transaction.AccountTransaction;
import com.solution.bank.domain.transaction.DepositTransaction;
import com.solution.bank.domain.transaction.TransferTransaction;
import com.solution.bank.domain.transaction.WithdrawalTransaction;
import com.solution.bank.dto.account.BankAccountDTO;
import com.solution.bank.dto.account.CreateBankAccountDTO;
import com.solution.bank.dto.transaction.AccountTransactionDTO;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {

	BankAccountDTO toDTO(BankAccount bankAccount);

	BankAccount toEntity(BankAccountDTO bankAccount);

	BankAccount toEntity(CreateBankAccountDTO bankAccount);

	List<BankAccountDTO> toDTOList(List<BankAccount> bankAccounts);

	DepositTransaction toDepositEntity(AccountTransactionDTO transactionDTO);

	WithdrawalTransaction toWithdrawalEntity(AccountTransactionDTO transactionDTO);

	TransferTransaction toTransferEntity(AccountTransactionDTO transactionDTO);

	default AccountTransaction mapToTransaction(AccountTransactionDTO transactionDTO) {
		return switch (transactionDTO.getTransactionType()) {
			case "DEPOSIT" -> toDepositEntity(transactionDTO);
			case "WITHDRAWAL" -> toWithdrawalEntity(transactionDTO);
			case "TRANSFER" -> toTransferEntity(transactionDTO);
			default -> throw new IllegalArgumentException("Unknown transaction type: " + transactionDTO.getTransactionType());
		};
	}

	default AccountTransactionDTO toDTO(AccountTransaction accountTransaction) {
		AccountTransactionDTO dto = toTransactionDTO(accountTransaction);
		if (accountTransaction instanceof DepositTransaction) {
			dto.setTransactionType("DEPOSIT");
		} else if (accountTransaction instanceof WithdrawalTransaction) {
			dto.setTransactionType("WITHDRAWAL");
		} else if (accountTransaction instanceof TransferTransaction) {
			dto.setTransactionType("TRANSFER");
		} else {
			throw new IllegalArgumentException("Unknown transaction type: " + accountTransaction.getClass().getSimpleName());
		}
		return dto;
	}

	AccountTransactionDTO toTransactionDTO(AccountTransaction accountTransaction);

	default List<AccountTransaction> mapToTransactionList(List<AccountTransactionDTO> transactionDTOList) {
		if (transactionDTOList == null) {
			return new ArrayList<>();
		}
		return transactionDTOList.stream()
			.map(this::mapToTransaction).toList();
	}

	default List<AccountTransactionDTO> mapToDtoList(List<AccountTransaction> transactionList) {
		return transactionList.stream()
			.map(this::toDTO).toList();
	}
}

