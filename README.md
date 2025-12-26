# Online-Banking-System-using-Java
This was a project in my Java class where I had to develop an online banking system with the use cases provided by the professor.

Based on the use case, I had to do the following so that a customer can open a checking account and/or a savings account.
1.	Create tables CheckingAccount(CheckingAccountNumber, CustomerName, Balance, CustomerID), SavingsAccount(SavingsAccountNumber, CustomerName, Balance, InterestRate, CustomerID), Transactions(TransactionNumber, TransactionAmount, TransactionType, TransactionTime, TransactionDate, FromAccount, ToAccount, CustomerID). Use varchar(50) for any non-numerical value including AccountNumber and TransactionNumber, and float for any numerical value. CustomerID here means Username.
2.	Study entity class CheckingAccount.java, and then develop your entity classes SavingsAccount.java and Transaction.java that has a method for recording a transaction.
3.	Add necessary statements to OpenBankAccountControl.java to open both Checking and Savings accounts, and record the transactions of opening deposits.

 In continuation to the above, had to do then do the following:
 
Develop use cases of Account Overview, Inquire Transactions, Deposit, and Withdraw by using the services from your bank as examples. And develop your Java programs to fully implement the functionalities with access to database. Assume that a customer has two accounts, Savings Account and Checking Account. Account Overview shows the current balances of customers’ Savings Account and Checking Account. Inquire Transactions allow customers to search specific transactions by entering a starting date and ending date.
1.	Develop methods deposit(), withdraw() in your classes CheckingAccount and SavingsAccount, getBalance() and calculateInterests() in class SavingsAccount,  searchTransaction() in class Transactions. Then implement Transfer, Deposit, and Withdraw functionalities. It means that your program shows Transfer, Deposit, or Withdraw after successful login. Your current program is to show Open Bank Account window after successful login. Modifications are necessary. 
2.	Organize these functionalities as tabs in one window. The default tab is Account Overview. Please combine Open Account into this application.  Show the window after successful login. These tabs must be in your window, Account Overview, Open Account, Deposit, Withdraw, Transfer and Inquire Transactions subtabs.

## Database setup (Flyway)

This project includes Flyway migrations under `src/main/resources/db/migration`.

Set DB connection environment variables:

```bash
export JDBC_URL=jdbc:sqlserver://127.0.0.1:1433;databaseName=JavaClass;integratedSecurity=true;
export JDBC_USER=
export JDBC_PASSWORD=
export JDBC_DRIVER=com.microsoft.sqlserver.jdbc.SQLServerDriver
```

Run migrations (requires Maven):

```bash
mvn flyway:migrate
```

## Run locally

### Prerequisites

- JDK 17
- Maven 3.9+
- A servlet container (Tomcat 9+ recommended)

### 1) Configure DB

Set env vars (example for SQL Server):

```bash
export JDBC_URL='jdbc:sqlserver://127.0.0.1:1433;databaseName=JavaClass;integratedSecurity=true;'
export JDBC_USER=''
export JDBC_PASSWORD=''
export JDBC_DRIVER='com.microsoft.sqlserver.jdbc.SQLServerDriver'
```

### 2) Run migrations

```bash
mvn -q flyway:migrate
```

### 3) Run tests

```bash
mvn -q test
mvn -q verify
```

### 4) Build WAR and deploy

```bash
mvn -q package
```

Deploy `target/online-banking.war` to your Tomcat `webapps/` directory, then start Tomcat.

## CSRF protection

CSRF protection is enforced for all POST requests via `CSRFFilter`.
Every POST form must include:

```html
<input type="hidden" name="_csrf" value="${csrfToken}" />
```

 
