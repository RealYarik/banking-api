package com.solution.bank.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.solution.bank.domain.account.BankAccount;
import com.solution.bank.domain.transaction.AccountTransaction;
import com.solution.bank.domain.transaction.DepositTransaction;
import com.solution.bank.domain.transaction.TransferTransaction;
import com.solution.bank.domain.transaction.WithdrawalTransaction;
import com.solution.bank.dto.account.BankAccountDTO;
import com.solution.bank.dto.account.CreateBankAccountDTO;
import com.solution.bank.dto.transaction.AccountTransactionDTO;

class BankAccountMapperTest {

	private final BankAccountMapper mapper = Mappers.getMapper(BankAccountMapper.class);

	@Test
	void shouldMapBankAccountToDTO() {
		BankAccount bankAccount = new BankAccount();
		bankAccount.setId(1L);
		bankAccount.setBalance(new BigDecimal(1000));
		bankAccount.setOwnerName("John Doe");

		BankAccountDTO dto = mapper.toDTO(bankAccount);

		assertNotNull(dto);
		assertEquals(bankAccount.getId(), dto.getId());
		assertEquals(bankAccount.getBalance(), dto.getBalance());
		assertEquals(bankAccount.getOwnerName(), dto.getOwnerName());
	}

	@Test
	void shouldMapBankAccountDTOToEntity() {
		BankAccountDTO bankAccountDTO = new BankAccountDTO();
		bankAccountDTO.setId(1L);
		bankAccountDTO.setBalance(new BigDecimal(1000));
		bankAccountDTO.setOwnerName("John Doe");

		List<AccountTransactionDTO> transactionDTOList = new ArrayList<>();
		AccountTransactionDTO transactionDTO = new AccountTransactionDTO();
		transactionDTO.setTransactionType("DEPOSIT");
		transactionDTO.setAmount(new BigDecimal(2000));
		transactionDTOList.add(transactionDTO);

		BankAccount entity = mapper.toEntity(bankAccountDTO);

		assertNotNull(entity);
		assertEquals(bankAccountDTO.getId(), entity.getId());
		assertEquals(bankAccountDTO.getBalance(), entity.getBalance());
		assertEquals(bankAccountDTO.getOwnerName(), entity.getOwnerName());

		List<AccountTransaction> transactionList = mapper.mapToTransactionList(transactionDTOList);
		assertNotNull(transactionList);
		assertEquals(1, transactionList.size());
		assertEquals("DepositTransaction", transactionList.get(0).getClass().getSimpleName());
	}


	@Test
	void shouldMapCreateBankAccountDTOToEntity() {
		CreateBankAccountDTO createDTO = new CreateBankAccountDTO();
		createDTO.setOwnerName("Jane Doe");
		createDTO.setCurrency("USD");

		BankAccount entity = mapper.toEntity(createDTO);

		assertNotNull(entity);
		assertEquals(createDTO.getOwnerName(), entity.getOwnerName());
		assertEquals(createDTO.getCurrency(), entity.getCurrency());
	}

	@Test
	void shouldMapAccountTransactionDTOToTransaction() {
		AccountTransactionDTO transactionDTO = new AccountTransactionDTO();
		transactionDTO.setTransactionType("DEPOSIT");
		transactionDTO.setAmount(new BigDecimal(200));

		DepositTransaction transaction = (DepositTransaction) mapper.mapToTransaction(transactionDTO);

		assertNotNull(transaction);
		assertEquals(transactionDTO.getAmount(), transaction.getAmount());
	}

	@Test
	void shouldMapAccountTransactionDTOToTransactionThrowException() {
		AccountTransactionDTO transactionDTO = new AccountTransactionDTO();
		transactionDTO.setTransactionType("NONE");
		transactionDTO.setAmount(new BigDecimal(200));

		assertThrows(IllegalArgumentException.class, () ->mapper.mapToTransaction(transactionDTO));
	}

	@Test
	void shouldMapAccountTransactionDTOToWithdrawalTransaction() {
		AccountTransactionDTO transactionDTO = new AccountTransactionDTO();
		transactionDTO.setTransactionType("WITHDRAWAL");
		transactionDTO.setAmount(new BigDecimal(200));

		WithdrawalTransaction transaction = (WithdrawalTransaction) mapper.mapToTransaction(transactionDTO);

		assertNotNull(transaction);
		assertEquals(transactionDTO.getAmount(), transaction.getAmount());
	}

	@Test
	void shouldMapAccountTransactionDTOToTransferTransaction() {
		AccountTransactionDTO transactionDTO = new AccountTransactionDTO();
		transactionDTO.setTransactionType("TRANSFER");
		transactionDTO.setAmount(new BigDecimal(200));

		TransferTransaction transaction = (TransferTransaction) mapper.mapToTransaction(transactionDTO);

		assertNotNull(transaction);
		assertEquals(transactionDTO.getAmount(), transaction.getAmount());
	}

	@Test
	void shouldMapTransactionToDTO() {
		DepositTransaction transaction = new DepositTransaction();
		transaction.setAmount(new BigDecimal(200));

		AccountTransactionDTO transactionDTO = mapper.toDTO(transaction);

		assertNotNull(transactionDTO);
		assertEquals(transaction.getAmount(), transactionDTO.getAmount());
		assertEquals("DEPOSIT", transactionDTO.getTransactionType());
	}

	@Test
	void shouldMapTransactionToDTOThrowException() {
		DepositTransaction transaction = new DepositTransaction();
		transaction.setAmount(new BigDecimal(200));

		assertThrows(IllegalArgumentException.class, () -> mapper.toDTO(new AccountTransaction() {}));
	}

	@Test
	void shouldMapTransferTransactionToDTO() {
		TransferTransaction transaction = new TransferTransaction();
		transaction.setAmount(new BigDecimal(200));

		AccountTransactionDTO transactionDTO = mapper.toDTO(transaction);

		assertNotNull(transactionDTO);
		assertEquals(transaction.getAmount(), transactionDTO.getAmount());
		assertEquals("TRANSFER", transactionDTO.getTransactionType());
	}

	@Test
	void shouldMapToWithdrawalEntity() {
		AccountTransactionDTO transactionDTO = new AccountTransactionDTO();
		transactionDTO.setTransactionType("WITHDRAWAL");
		transactionDTO.setAmount(new BigDecimal(500));

		WithdrawalTransaction transaction = mapper.toWithdrawalEntity(transactionDTO);

		assertNotNull(transaction);
		assertEquals("500", transaction.getAmount().toString());
	}

	@Test
	void shouldMapToTransferEntity() {
		AccountTransactionDTO transactionDTO = new AccountTransactionDTO();
		transactionDTO.setTransactionType("TRANSFER");
		transactionDTO.setAmount(new BigDecimal("1000.00"));

		TransferTransaction transaction = mapper.toTransferEntity(transactionDTO);

		assertNotNull(transaction);
		assertEquals("1000.00", transaction.getAmount().toString());
	}

	@Test
	void shouldMapToAccountTransactionDTOList() {
		List<AccountTransaction> transactionList = List.of(
			new DepositTransaction(),
			new WithdrawalTransaction()
		);

		List<AccountTransactionDTO> transactionDTOList = mapper.mapToDtoList(transactionList);

		assertNotNull(transactionDTOList);
		assertEquals(2, transactionDTOList.size());
		assertEquals("DEPOSIT", transactionDTOList.get(0).getTransactionType());
		assertEquals("WITHDRAWAL", transactionDTOList.get(1).getTransactionType());
	}

	@Test
	void shouldMapToDTOList() {
		List<BankAccount> bankAccounts = List.of(
			new BankAccount(),
			new BankAccount()
		);

		List<BankAccountDTO> dtoList = mapper.toDTOList(bankAccounts);

		assertNotNull(dtoList);
		assertEquals(2, dtoList.size());
	}

	@Test
	void shouldReturnEmptyListWhenInputIsEmpty() {
		List<BankAccount> bankAccounts = List.of();

		List<BankAccountDTO> dtoList = mapper.toDTOList(bankAccounts);

		assertNotNull(dtoList);
		assertTrue(dtoList.isEmpty());
	}

	@Test
	void shouldReturnNullWhenInputIsNull() {
		List<BankAccountDTO> dtoList = mapper.toDTOList(null);

		assertNull(dtoList);
	}
}
