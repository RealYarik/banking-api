INSERT INTO bank_account (uuid, account_number, balance, currency, owner_name)
VALUES
	('UUID-1', 'ACCT-001', 1000.00, 'USD', 'John Doe'),
	('UUID-2', 'ACCT-002', 2000.00, 'USD', 'Jane Smith');

INSERT INTO account_transaction (transaction_type, uuid, amount, currency, transaction_date, account_id, target_account_id)
VALUES
	('DEPOSIT', 'TXN-001', 100.00, 'USD', CURRENT_TIMESTAMP, 1, NULL),
	('DEPOSIT', 'TXN-002', 50.00, 'USD', CURRENT_TIMESTAMP, 1, 2);
