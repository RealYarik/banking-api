# Banking Solution

Banking Solution is a Spring Boot application designed to manage bank accounts and transactions.

## Requirements
- **Java 17** or higher
- **Maven 3.3.2** or higher
- **Docker**
- **MySQL 8.0**

## Setup and Run Instructions
### Running Locally Without Docker
1. **Clone the repository**:
   ```bash
   git clone https://github.com/RealYarik/banking-api
   cd banking-api
   ```
2. **Configure the database**:
- Create a MySQL database named banking_db.
- Update the database credentials in the application.yml file.

3. **Run the application**:

   ```bash
   mvn clean spring-boot:run
   ```
4. **Access the application at http://localhost:8080.**

### Running With Docker Compose
1. **Clone the repository**:
   ```bash
   git clone https://github.com/RealYarik/banking-api
   cd banking-api
   ```
2. **Build the JAR File**
   ```bash
   mvn clean package
   ```
2. **Start the containers**:
   ```bash
   docker-compose up --build
   ```


## Generate and View Code Coverage Report
### Run tests with JaCoCo:
   ```bash
mvn clean test jacoco:report
   ```
### View the report:

- Open the following file in your browser:
   ```bash
   target/site/jacoco/index.html
   ```
The report provides a detailed breakdown of code coverage, including line and branch coverage.

# API Endpoints

This application provides RESTful API endpoints for managing bank accounts and transactions. Below are the details of the available endpoints.

---

## **Account Endpoints**

### **1. Get All Accounts**
**URL:** `/api/accounts`  
**Method:** `GET`  
**Description:** Retrieves a list of all bank accounts.

#### Response:
```json
[
   {
      "id": 1,
      "accountNumber": "ACCT-284127",
      "ownerName": "John Doe",
      "balance": 1000.00,
      "currency": "USD",
      "transactions": []
   },
   {
      "id": 2,
      "accountNumber": "ACCT-284141",
      "ownerName": "Jane Doe",
      "balance": 1500.00,
      "currency": "USD",
      "transactions": []
   }
]
```
### **2. Get Account By Account Number**
**URL:** `/api/accounts/{accountNumber}`  
**Method:** `GET`  
**Description:** Retrieves account details for a specific account number.

#### Response:
```json
{
   "id": 1,
   "accountNumber": "ACCT-284127",
   "ownerName": "John Doe",
   "balance": 1000.00,
   "currency": "USD",
   "transactions": []
}
```

### **3. Create Account**
**URL:** `/api/accounts/create`  
**Method:** `POST`  
**Description:** Creates a new bank account.

#### Request Body:
```json
{
   "ownerName": "John Doe",
   "currency": "USD"
}
```
#### Response:
```json
{
   "id": 1,
   "accountNumber": "ACCT-284127",
   "ownerName": "John Doe",
   "balance": 0.00,
   "currency": "USD",
   "transactions": []
}
```

### **4. Search Accounts**
**URL:** `/api/accounts/search`  
**Method:** `POST`  
**Description:** Searches for accounts based on specific criteria.

#### Request Body:
```json
{
   "balanceGreaterThan": 1000.00,
   "currency": "USD"
}

```
#### Response:
```json
[
   {
      "id": 2,
      "accountNumber": "ACCT-284141",
      "ownerName": "Jane Doe",
      "balance": 1500.00,
      "currency": "USD",
      "transactions": []
   }
]
```

## **Transaction Endpoints**

### **1. Deposit**
**URL:** `/api/transactions/deposit`  
**Method:** `POST`  
**Description:** Adds funds to an account.

#### Request Body:
```json
{
  "accountNumber": "ACCT-284127",
  "amount": 500.00,
  "source": "ATM",
  "currency": "USD",
  "atmLocation": "test ATM"
}
```
#### Response (Success):
```json
{
   "success": true,
   "message": "Deposit successful. New balance: 1500.00",
   "updatedBalance": 1500.00
}
```
#### Response (Failure):
```json
{
   "success": false,
   "message": "Account does not exist",
   "updatedBalance": null
}
```

### **2. Withdraw**
**URL:** `/api/transactions/withdraw`  
**Method:** `POST`  
**Description:** Withdraws funds from an account.

#### Request Body:
```json
{
   "accountNumber": "ACCT-284127",
   "amount": 200.00,
   "source": "ATM",
   "currency": "USD",
   "atmLocation": "test ATM"
}
```
#### Response (Success):
```json
{
   "success": true,
   "message": "Withdrawal successful. New balance: 800.00",
   "updatedBalance": 800.00
}
```
#### Response (Failure):
```json
{
   "success": false,
   "message": "Insufficient funds",
   "updatedBalance": null
}
```

### **3. Transfer**
**URL:** `/api/transactions/transfer`  
**Method:** `POST`  
**Description:** Transfers funds between accounts.

#### Request Body:
```json
{
   "fromAccountNumber": "ACCT-284127",
   "toAccountNumber": "ACCT-284175",
   "amount": 100.00,
   "currency": "USD",
   "transferPurpose": "Bill Payment"
}
```
#### Response (Success):
```json
{
   "success": true,
   "message": "Transfer successful. New balance in sender account: 700.00",
   "updatedBalance": 700.00
}
```
#### Response (Failure):
```json
{
   "success": false,
   "message": "Insufficient funds in the sender's account",
   "updatedBalance": null
}
```

### Additional info:
Native SQL was chosen for `AccountTransactionRepository` instead of 
Hibernate to achieve better performance, fine-grained control over
complex transactions, and optimized custom queries specific to the banking 
operations, avoiding the overhead of Hibernate’s object-relational mapping,
while also ensuring security and preventing issues with caching in banking applications.