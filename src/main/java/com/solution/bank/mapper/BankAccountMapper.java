package com.solution.bank.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.solution.bank.domain.account.BankAccount;
import com.solution.bank.dto.account.BankAccountDTO;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {

	BankAccountDTO toDTO(BankAccount bankAccount);

	BankAccount toEntity(BankAccountDTO bankAccount);

	List<BankAccountDTO> toDTOList(List<BankAccount> bankAccounts);
}
