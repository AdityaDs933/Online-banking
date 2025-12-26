package com.aditya;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountServiceImpl implements AccountService {

    private final DBConnection dbConnection;

    public AccountServiceImpl() {
        this.dbConnection = null;
    }

    // test-friendly constructor
    public AccountServiceImpl(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public boolean transfer(String fromType, String fromAccountNumber, String toType, String toAccountNumber, String customerId, float amount) throws SQLException {
        DBConnection db = (this.dbConnection != null) ? this.dbConnection : new DBConnection();
        try (Connection conn = db.openConn()) {
            try {
                conn.setAutoCommit(false);

                // determine account numbers if not provided
                if (fromAccountNumber == null || fromAccountNumber.isEmpty()) {
                    if ("Checking".equalsIgnoreCase(fromType)) {
                        fromAccountNumber = new CheckingAccount().getAccno(customerId);
                    } else {
                        fromAccountNumber = new SavingAccount().getAccno(customerId);
                    }
                }
                if (toAccountNumber == null || toAccountNumber.isEmpty()) {
                    if ("Checking".equalsIgnoreCase(toType)) {
                        toAccountNumber = new CheckingAccount().getAccno(customerId);
                    } else {
                        toAccountNumber = new SavingAccount().getAccno(customerId);
                    }
                }

                // withdraw from source
                if ("Checking".equalsIgnoreCase(fromType)) {
                    CheckingAccount src = new CheckingAccount(fromAccountNumber);
                    src.setAmount(amount);
                    src.setCheckingAccountNumber(fromAccountNumber);
                    if (!src.withdrawWithConnection(conn, customerId)) {
                        conn.rollback();
                        return false;
                    }
                } else {
                    SavingAccount src = new SavingAccount(fromAccountNumber);
                    src.setAmount(amount);
                    src.setSavingAccountNumber(fromAccountNumber);
                    if (!src.withdrawWithConnection(conn, customerId)) {
                        conn.rollback();
                        return false;
                    }
                }

                // deposit to target
                if ("Checking".equalsIgnoreCase(toType)) {
                    CheckingAccount tgt = new CheckingAccount(toAccountNumber);
                    tgt.setAmount(amount);
                    tgt.setCheckingAccountNumber(toAccountNumber);
                    if (!tgt.depositWithConnection(conn, customerId)) {
                        conn.rollback();
                        return false;
                    }
                } else {
                    SavingAccount tgt = new SavingAccount(toAccountNumber);
                    tgt.setAmount(amount);
                    tgt.setSavingAccountNumber(toAccountNumber);
                    if (!tgt.depositWithConnection(conn, customerId)) {
                        conn.rollback();
                        return false;
                    }
                }

                // record transaction
                String insertSQL = "INSERT INTO Transactions(TransactionNumber, TransactionType, TransactionAmount, TransactionTime, TransactionDate, FromAccount, ToAccount, CustomerID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                    String txnId = java.util.UUID.randomUUID().toString();
                    java.time.LocalTime nowTime = java.time.LocalTime.now();
                    java.time.LocalDate nowDate = java.time.LocalDate.now();
                    ps.setString(1, txnId);
                    ps.setString(2, "Transfer");
                    ps.setFloat(3, amount);
                    ps.setString(4, nowTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
                    ps.setString(5, nowDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    ps.setString(6, fromAccountNumber);
                    ps.setString(7, toAccountNumber);
                    ps.setString(8, customerId);
                    ps.executeUpdate();
                }

                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}
