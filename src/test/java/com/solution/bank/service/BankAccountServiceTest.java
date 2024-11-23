package com.solution.bank.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import com.solution.bank.domain.account.BankAccount;
import com.solution.bank.dto.account.BankAccountDTO;
import com.solution.bank.dto.account.CreateBankAccountDTO;
import com.solution.bank.dto.search.account.AccountSearchCriteria;
import com.solution.bank.mapper.BankAccountMapper;
import com.solution.bank.repository.BankAccountRepository;
import com.solution.bank.repository.filter.account.AccountSpecificationBuilder;
import com.solution.bank.util.RandomUtils;

class BankAccountServiceTest {

	@Mock
	private AccountSpecificationBuilder accountSpecificationBuilder;

	@Mock
	private BankAccountRepository bankAccountRepository;

	@Mock
	private BankAccountMapper bankAccountMapper;

	private BankAccountService bankAccountService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		bankAccountService = new BankAccountService(accountSpecificationBuilder, bankAccountRepository, bankAccountMapper);
	}

	@Test
	void shouldSearchAccountsSuccessfully() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();

		List<BankAccount> bankAccounts = List.of(new BankAccount(), new BankAccount());

		Specification<BankAccount> specification = mock(Specification.class);

		when(accountSpecificationBuilder.buildSpecification(criteria)).thenReturn(specification);
		when(bankAccountRepository.findAll(specification)).thenReturn(bankAccounts);

		List<BankAccountDTO> bankAccountDTOList = List.of(new BankAccountDTO(), new BankAccountDTO());
		when(bankAccountMapper.toDTOList(bankAccounts)).thenReturn(bankAccountDTOList);

		List<BankAccountDTO> result = bankAccountService.searchAccounts(criteria);

		assertNotNull(result);
		assertEquals(2, result.size());
		verify(bankAccountRepository).findAll(specification);
		verify(bankAccountMapper).toDTOList(bankAccounts);
	}

	@Test
	void shouldGetAccountByNumberSuccessfully() {
		String accountNumber = "123456";
		BankAccount bankAccount = new BankAccount();
		bankAccount.setAccountNumber(accountNumber);
		BankAccountDTO bankAccountDTO = new BankAccountDTO();

		when(bankAccountRepository.findBankAccountByAccountNumber(accountNumber)).thenReturn(Optional.of(bankAccount));
		when(bankAccountMapper.toDTO(bankAccount)).thenReturn(bankAccountDTO);

		BankAccountDTO result = bankAccountService.getAccountByNumber(accountNumber);

		assertNotNull(result);
		verify(bankAccountRepository).findBankAccountByAccountNumber(accountNumber);
		verify(bankAccountMapper).toDTO(bankAccount);
	}

	@Test
	void shouldThrowExceptionWhenAccountNotFound() {
		String accountNumber = "123456";

		when(bankAccountRepository.findBankAccountByAccountNumber(accountNumber)).thenReturn(Optional.empty());

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
			bankAccountService.getAccountByNumber(accountNumber)
		);

		assertEquals("Account not found", exception.getMessage());
	}

	@Test
	void shouldCreateAccountSuccessfully() {
		try (var mock = mockStatic(RandomUtils.class)) {
			mock.when(RandomUtils::generateAccountNumber).thenReturn("123456789");

			CreateBankAccountDTO createBankAccountDTO = new CreateBankAccountDTO();
			BankAccount bankAccount = new BankAccount();
			BankAccount savedAccount = new BankAccount();
			savedAccount.setAccountNumber("123456789");
			savedAccount.setBalance(BigDecimal.ZERO);

			BankAccountDTO bankAccountDTO = new BankAccountDTO();
			bankAccountDTO.setAccountNumber("123456789");
			bankAccountDTO.setBalance(BigDecimal.ZERO);

			when(bankAccountMapper.toEntity(createBankAccountDTO)).thenReturn(bankAccount);
			when(bankAccountRepository.save(bankAccount)).thenReturn(savedAccount);
			when(bankAccountMapper.toDTO(savedAccount)).thenReturn(bankAccountDTO);

			BankAccountDTO result = bankAccountService.createAccount(createBankAccountDTO);

			assertNotNull(result);
			assertEquals("123456789", result.getAccountNumber());
			verify(bankAccountRepository).save(bankAccount);
		}
	}
}