-- Flyway migration V1: initial schema

CREATE TABLE Account(
    Username VARCHAR(100) PRIMARY KEY,
    Password VARCHAR(255) NOT NULL,
    Name VARCHAR(100) NOT NULL
);

CREATE TABLE CheckingAccount(
    CheckingAccountNumber VARCHAR(50) PRIMARY KEY,
    CustomerName VARCHAR(100) NOT NULL,
    Balance DOUBLE PRECISION NOT NULL,
    CustomerID VARCHAR(100) NOT NULL
);

CREATE TABLE SavingAccount(
    SavingAccountNumber VARCHAR(50) PRIMARY KEY,
    CustomerName VARCHAR(100) NOT NULL,
    Balance DOUBLE PRECISION NOT NULL,
    CustomerID VARCHAR(100) NOT NULL
);

CREATE TABLE Transactions(
    TransactionNumber VARCHAR(50) PRIMARY KEY,
    TransactionType VARCHAR(50) NOT NULL,
    TransactionAmount DOUBLE PRECISION NOT NULL,
    TransactionTime VARCHAR(20) NOT NULL,
    TransactionDate VARCHAR(20) NOT NULL,
    FromAccount VARCHAR(50),
    ToAccount VARCHAR(50),
    CustomerID VARCHAR(100) NOT NULL
);
