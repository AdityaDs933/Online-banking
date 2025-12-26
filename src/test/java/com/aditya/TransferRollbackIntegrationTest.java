package com.aditya;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class TransferRollbackIntegrationTest {

    private static final String JDBC_URL = "jdbc:h2:mem:transferRollback;DB_CLOSE_DELAY=-1";
    private static DBConnection testDb;

    @BeforeAll
    static void beforeAll() {
        DBConnection.shutdownPool();

        Flyway.configure()
                .dataSource(JDBC_URL, "sa", "")
                .locations("classpath:db/migration")
                .load()
                .migrate();

        testDb = new DBConnection(JDBC_URL, "org.h2.Driver");
    }

    @AfterAll
    static void afterAll() {
        DBConnection.shutdownPool();
    }

    @BeforeEach
    void seed() throws Exception {
        try (Connection conn = testDb.openConn(); Statement st = conn.createStatement()) {
            assertNotNull(conn, "Failed to open H2 connection");
            st.execute("DELETE FROM Transactions");
            st.execute("DELETE FROM CheckingAccount");
            st.execute("DELETE FROM SavingAccount");
            st.execute("DELETE FROM Account");
        }

        try (Connection conn = testDb.openConn()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Account(Username, Password, Name) VALUES(?, ?, ?)")) {
                ps.setString(1, "user1");
                ps.setString(2, "x");
                ps.setString(3, "User One");
                ps.executeUpdate();

                ps.setString(1, "user2");
                ps.setString(2, "x");
                ps.setString(3, "User Two");
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO CheckingAccount(CheckingAccountNumber, CustomerName, Balance, CustomerID) VALUES(?, ?, ?, ?)")) {
                ps.setString(1, "CHK1");
                ps.setString(2, "User One");
                ps.setDouble(3, 100.0);
                ps.setString(4, "user1");
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO SavingAccount(SavingAccountNumber, CustomerName, Balance, CustomerID) VALUES(?, ?, ?, ?)")) {
                ps.setString(1, "SAV1");
                ps.setString(2, "User One");
                ps.setDouble(3, 50.0);
                ps.setString(4, "user1");
                ps.executeUpdate();

                // Different owner (user2)
                ps.setString(1, "SAV2");
                ps.setString(2, "User Two");
                ps.setDouble(3, 80.0);
                ps.setString(4, "user2");
                ps.executeUpdate();
            }
        }
    }

    @Test
    void transferShouldRollbackWhenDestinationNotOwnedByCustomer() throws Exception {
        AccountServiceImpl svc = new AccountServiceImpl(testDb);

        boolean ok = svc.transfer("Checking", "CHK1", "Saving", "SAV2", "user1", 30f);
        assertFalse(ok, "Expected transfer to fail when destination isn't owned by customer");

        assertEquals(100.0, getCheckingBalance("CHK1"), 0.001);
        assertEquals(80.0, getSavingBalance("SAV2"), 0.001);
        assertEquals(0, countTransfersForCustomer("user1"));
    }

    @Test
    void transferShouldRollbackWhenInsufficientFunds() throws Exception {
        // Reduce CHK1 balance below transfer amount
        try (Connection conn = testDb.openConn(); PreparedStatement ps = conn.prepareStatement(
                "UPDATE CheckingAccount SET Balance=? WHERE CheckingAccountNumber=?")) {
            ps.setDouble(1, 10.0);
            ps.setString(2, "CHK1");
            ps.executeUpdate();
        }

        AccountServiceImpl svc = new AccountServiceImpl(testDb);
        boolean ok = svc.transfer("Checking", "CHK1", "Saving", "SAV1", "user1", 30f);
        assertFalse(ok, "Expected transfer to fail when insufficient funds");

        assertEquals(10.0, getCheckingBalance("CHK1"), 0.001);
        assertEquals(50.0, getSavingBalance("SAV1"), 0.001);
        assertEquals(0, countTransfersForCustomer("user1"));
    }

    private double getCheckingBalance(String accountNumber) throws Exception {
        try (Connection conn = testDb.openConn(); PreparedStatement ps = conn.prepareStatement(
                "SELECT Balance FROM CheckingAccount WHERE CheckingAccountNumber=?")) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                return rs.getDouble(1);
            }
        }
    }

    private double getSavingBalance(String accountNumber) throws Exception {
        try (Connection conn = testDb.openConn(); PreparedStatement ps = conn.prepareStatement(
                "SELECT Balance FROM SavingAccount WHERE SavingAccountNumber=?")) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                return rs.getDouble(1);
            }
        }
    }

    private int countTransfersForCustomer(String customerId) throws Exception {
        try (Connection conn = testDb.openConn(); PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM Transactions WHERE TransactionType='Transfer' AND CustomerID=?")) {
            ps.setString(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                return rs.getInt(1);
            }
        }
    }
}
