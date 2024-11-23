package com.solution.bank.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solution.bank.dto.transaction.request.TransactionRequest;
import com.solution.bank.dto.transaction.response.TransactionResponse;
import com.solution.bank.service.AccountTransactionService;

@WebMvcTest(AccountTransactionController.class)
class AccountTransactionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AccountTransactionService accountTransactionService;

	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
	}

	@Test
	void shouldDepositSuccessfully() throws Exception {
		TransactionRequest request = new TransactionRequest();
		request.setAccountNumber("123456789");
		request.setAmount(new BigDecimal(100));
		request.setSource("ATM");
		request.setCurrency("USD");
		request.setAtmLocation("Location A");
		request.setTransferPurpose("Deposit to account");
		request.setFromAccountNumber(null);
		request.setToAccountNumber(null);

		TransactionResponse response = new TransactionResponse(true, "Deposit successful", new BigDecimal(100));
		given(accountTransactionService.deposit(request)).willReturn(response);

		mockMvc.perform(post("/api/transactions/deposit")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.message").value("Deposit successful"));
	}

	@Test
	void shouldReturnBadRequestForFailedDeposit() throws Exception {
		TransactionRequest request = new TransactionRequest();
		request.setAccountNumber("123456789");
		request.setAmount(new BigDecimal(100));
		request.setSource("ATM");
		request.setCurrency("USD");
		request.setAtmLocation("Location A");
		request.setTransferPurpose("Deposit to account");
		request.setFromAccountNumber(null);
		request.setToAccountNumber(null);

		TransactionResponse response = new TransactionResponse(false, "Deposit failed", new BigDecimal(0));
		given(accountTransactionService.deposit(request)).willReturn(response);

		mockMvc.perform(post("/api/transactions/deposit")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.message").value("Deposit failed"));
	}

	@Test
	void shouldWithdrawSuccessfully() throws Exception {
		TransactionRequest request = new TransactionRequest();
		request.setAccountNumber("123456789");
		request.setAmount(new BigDecimal(50));
		request.setSource("ATM");
		request.setCurrency("USD");
		request.setAtmLocation("Location B");
		request.setTransferPurpose("Cash withdrawal");
		request.setFromAccountNumber("123456789");
		request.setToAccountNumber(null);

		TransactionResponse response = new TransactionResponse(true, "Withdrawal successful", new BigDecimal(50));
		given(accountTransactionService.withdraw(request)).willReturn(response);

		mockMvc.perform(post("/api/transactions/withdraw")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.message").value("Withdrawal successful"));
	}

	@Test
	void shouldReturnBadRequestForFailedWithdraw() throws Exception {
		TransactionRequest request = new TransactionRequest();
		request.setAccountNumber("123456789");
		request.setAmount(new BigDecimal(50));
		request.setSource("ATM");
		request.setCurrency("USD");
		request.setAtmLocation("Location B");
		request.setTransferPurpose("Cash withdrawal");
		request.setFromAccountNumber("123456789");
		request.setToAccountNumber(null);

		TransactionResponse response = new TransactionResponse(false, "Insufficient balance", new BigDecimal(50));
		given(accountTransactionService.withdraw(request)).willReturn(response);

		mockMvc.perform(post("/api/transactions/withdraw")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.message").value("Insufficient balance"));
	}

	@Test
	void shouldTransferSuccessfully() throws Exception {
		TransactionRequest request = new TransactionRequest();
		request.setAccountNumber("123456789");
		request.setAmount(new BigDecimal(200));
		request.setSource("Online transfer");
		request.setCurrency("USD");
		request.setAtmLocation(null);
		request.setTransferPurpose("Payment");
		request.setFromAccountNumber("123456789");
		request.setToAccountNumber("987654321");

		TransactionResponse response = new TransactionResponse(true, "Transfer successful", new BigDecimal(200));
		given(accountTransactionService.transfer(request)).willReturn(response);

		mockMvc.perform(post("/api/transactions/transfer")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.message").value("Transfer successful"));
	}

	@Test
	void shouldReturnBadRequestForFailedTransfer() throws Exception {
		TransactionRequest request = new TransactionRequest();
		request.setAccountNumber("123456789");
		request.setAmount(new BigDecimal(200));
		request.setSource("Online transfer");
		request.setCurrency("USD");
		request.setAtmLocation(null);
		request.setTransferPurpose("Payment");
		request.setFromAccountNumber("123456789");
		request.setToAccountNumber("987654321");

		TransactionResponse response = new TransactionResponse(false, "Transfer failed", new BigDecimal(0));
		given(accountTransactionService.transfer(request)).willReturn(response);

		mockMvc.perform(post("/api/transactions/transfer")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.message").value("Transfer failed"));
	}
}