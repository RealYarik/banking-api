package com.solution.bank.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solution.bank.dto.account.BankAccountDTO;
import com.solution.bank.dto.account.CreateBankAccountDTO;
import com.solution.bank.dto.search.account.AccountSearchCriteria;
import com.solution.bank.service.BankAccountService;

@WebMvcTest(BankAccountController.class)
class BankAccountControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BankAccountService bankAccountService;

	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
	}

	@Test
	void shouldReturnAccountsWhenSearchCriteriaIsValid() throws Exception {
		AccountSearchCriteria searchCriteria = new AccountSearchCriteria();
		searchCriteria.setOwnerName("John Doe");
		searchCriteria.setBalanceGreaterThan(100.0);
		searchCriteria.setBalanceLessThan(1000.0);

		BankAccountDTO bankAccountDTO = new BankAccountDTO();
		bankAccountDTO.setOwnerName("John Doe");
		bankAccountDTO.setCurrency("USD");

		List<BankAccountDTO> accounts = List.of(bankAccountDTO);
		given(bankAccountService.searchAccounts(searchCriteria)).willReturn(accounts);

		mockMvc.perform(post("/api/accounts/search")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(searchCriteria)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$[0].ownerName").value("John Doe"));
	}

	@Test
	void shouldCreateAccountWhenValidDataIsProvided() throws Exception {
		CreateBankAccountDTO createAccountDTO = new CreateBankAccountDTO();
		createAccountDTO.setOwnerName("John Doe");
		createAccountDTO.setCurrency("USD");

		BankAccountDTO bankAccountDTO = new BankAccountDTO();
		bankAccountDTO.setOwnerName("John Doe");
		bankAccountDTO.setCurrency("USD");

		given(bankAccountService.createAccount(createAccountDTO)).willReturn(bankAccountDTO);

		mockMvc.perform(post("/api/accounts/create")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(createAccountDTO)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.ownerName").value("John Doe"))
			.andExpect(jsonPath("$.currency").value("USD"));
	}

	@Test
	void shouldReturnAccountWhenAccountNumberIsValid() throws Exception {
		String accountNumber = "123456789";
		BankAccountDTO bankAccountDTO = new BankAccountDTO();
		bankAccountDTO.setOwnerName("John Doe");
		bankAccountDTO.setCurrency("USD");

		given(bankAccountService.getAccountByNumber(accountNumber)).willReturn(bankAccountDTO);

		mockMvc.perform(get("/api/accounts/{accountNumber}", accountNumber))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.ownerName").value("John Doe"))
			.andExpect(jsonPath("$.currency").value("USD"));
	}
}