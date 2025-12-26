package com.aditya;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.mindrot.jbcrypt.BCrypt;

public class AccountServiceImplIntegrationTest {

    static DBConnection testDb;

    @BeforeAll
    static void setup() throws Exception {
        testDb = new DBConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "org.h2.Driver");
        try (Connection conn = testDb.openConn(); Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE Account(Username VARCHAR(100) PRIMARY KEY, Password VARCHAR(100), Name VARCHAR(100))");
            st.execute("CREATE TABLE CheckingAccount(CheckingAccountNumber VARCHAR(50) PRIMARY KEY, CustomerName VARCHAR(100), Balance DOUBLE, CustomerID VARCHAR(100))");
            st.execute("CREATE TABLE SavingAccount(SavingAccountNumber VARCHAR(50) PRIMARY KEY, CustomerName VARCHAR(100), Balance DOUBLE, CustomerID VARCHAR(100))");
            st.execute("CREATE TABLE Transactions(TransactionNumber VARCHAR(50) PRIMARY KEY, TransactionType VARCHAR(50), TransactionAmount DOUBLE, TransactionTime VARCHAR(20), TransactionDate VARCHAR(20), FromAccount VARCHAR(50), ToAccount VARCHAR(50), CustomerID VARCHAR(100))");

            String hashed = BCrypt.hashpw("secret", BCrypt.gensalt());
            st.execute("INSERT INTO Account(Username, Password, Name) VALUES('user1','" + hashed + "','User One')");
            st.execute("INSERT INTO CheckingAccount(CheckingAccountNumber, CustomerName, Balance, CustomerID) VALUES('CHK1','User One',100.0,'user1')");
            st.execute("INSERT INTO SavingAccount(SavingAccountNumber, CustomerName, Balance, CustomerID) VALUES('SAV1','User One',50.0,'user1')");
        }
    }

    @AfterAll
    static void teardown() {
        DBConnection.shutdownPool();
    }

    @Test
    void transferShouldMoveFunds() throws Exception {
        AccountServiceImpl svc = new AccountServiceImpl(testDb);
        boolean ok = svc.transfer("Checking", "CHK1", "Saving", "SAV1", "user1", 30f);
        assertTrue(ok);

        try (Connection conn = testDb.openConn(); Statement st = conn.createStatement()) {
            ResultSet r1 = st.executeQuery("SELECT Balance FROM CheckingAccount WHERE CheckingAccountNumber='CHK1'");
            assertTrue(r1.next());
            assertEquals(70.0, r1.getDouble(1), 0.001);

            ResultSet r2 = st.executeQuery("SELECT Balance FROM SavingAccount WHERE SavingAccountNumber='SAV1'");
            assertTrue(r2.next());
            assertEquals(80.0, r2.getDouble(1), 0.001);
        }
    }
}
