DROP TABLE IF EXISTS account_transaction;
DROP TABLE IF EXISTS bank_account;

CREATE TABLE bank_account (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	uuid VARCHAR(36) NOT NULL UNIQUE,
	account_number VARCHAR(255) NOT NULL UNIQUE,
	balance DECIMAL(38, 2) NOT NULL,
	currency VARCHAR(3) NOT NULL,
	owner_name VARCHAR(255) NOT NULL
);

CREATE TABLE account_transaction (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	transaction_type VARCHAR(31) NOT NULL,
	uuid VARCHAR(36) NOT NULL UNIQUE,
	amount DECIMAL(38, 2) NOT NULL,
	currency VARCHAR(255) NOT NULL,
	transaction_date DATETIME(6) NOT NULL,
	source VARCHAR(255),
	transfer_purpose VARCHAR(255),
	atm_location VARCHAR(255),
	account_id BIGINT NOT NULL,
	target_account_id BIGINT,
	CONSTRAINT FK_account FOREIGN KEY (account_id) REFERENCES bank_account (id),
	CONSTRAINT FK_target_account FOREIGN KEY (target_account_id) REFERENCES bank_account (id)
);