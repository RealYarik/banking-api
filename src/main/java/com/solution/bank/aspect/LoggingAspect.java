package com.solution.bank.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.solution.bank.dto.account.BankAccountDTO;
import com.solution.bank.dto.transaction.request.TransactionRequest;
import com.solution.bank.dto.transaction.response.TransactionResponse;

@Aspect
@Component
public class LoggingAspect {

	private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

	@Pointcut("within(com.solution.bank.service.AccountTransactionService)")
	public void accountTransactionMethods() {
	}

	@Pointcut("within(com.solution.bank.service.BankAccountService)")
	public void bankAccountMethods() {
	}

	@Before("accountTransactionMethods() || bankAccountMethods()")
	public void logBefore() {
		logger.info("Method execution started");
	}

	@AfterReturning(pointcut = "accountTransactionMethods() || bankAccountMethods()", returning = "result")
	public void logAfterSuccess(Object result) {
		if (result instanceof TransactionResponse transactionResponse) {
			logger.info("Transaction executed successfully with result: {}", transactionResponse);
		} else {
			logger.info("Method executed successfully with result: {}", result);
		}
	}

	@AfterThrowing(pointcut = "accountTransactionMethods() || bankAccountMethods()", throwing = "ex")
	public void logAfterException(Throwable ex) {
		logger.error("Method execution failed with exception: ", ex);
	}

	@AfterReturning(pointcut = "execution(* com.solution.bank.service.BankAccountService.createAccount(..))", returning = "result")
	public void logAccountCreation(Object result) {
		if (result instanceof BankAccountDTO accountDTO) {
			logger.info("Account created with account number: {}", accountDTO.getAccountNumber());
		}
	}

	@Before("execution(* com.solution.bank.service.AccountTransactionService.deposit(..)) || " +
		"execution(* com.solution.bank.service.AccountTransactionService.withdraw(..)) || " +
		"execution(* com.solution.bank.service.AccountTransactionService.transfer(..))")
	public void logTransactionRequest(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		if (args.length > 0 && args[0] instanceof TransactionRequest transactionRequest) {
			logger.info("Processing transaction for account: {}", transactionRequest.getAccountNumber());
		}
	}
}