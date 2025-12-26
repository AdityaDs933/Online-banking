package com.aditya;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Transaction implements TransactionService {
	private String TransactionNumber, TransactionType, TransactionTime, TransactionDate, FromAccount, ToAccount, CustomerID;
	private float Amount = -1;
	private String Startdate, Enddate;
	private List<List<String>> TransStore = new ArrayList<>();
	private static final Logger logger = LoggerFactory.getLogger(Transaction.class);

	public Transaction() {}

	public Transaction(String AccountType, String Cust_ID, String Amt){
		ToAccount = AccountType;
		CustomerID = Cust_ID;
		Amount = Float.parseFloat(Amt);
	}

	public Transaction(String ToAcc, String FromAcc, String Cust_ID, String Amt, String Trans_type) {
		ToAccount = ToAcc;
		FromAccount = FromAcc;
		CustomerID = Cust_ID;
		TransactionType = Trans_type;
		Amount = Float.parseFloat(Amt);
	}

	public Transaction(String SD, String ED){
		Startdate = SD;
		Enddate = ED;
	}

	@Override
	public String recordTransaction() {
		DBConnection ToDB = new DBConnection();
		try (Connection conn = ToDB.openConn()) {
			TransactionNumber = java.util.UUID.randomUUID().toString();
			LocalTime nowTime = LocalTime.now();
			LocalDate nowDate = LocalDate.now();
			DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
			DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			TransactionTime = nowTime.format(formatterTime);
			TransactionDate = nowDate.format(formatterDate);
			String sql = "INSERT INTO Transactions(TransactionNumber, TransactionType, TransactionAmount, TransactionTime, TransactionDate, FromAccount, ToAccount, CustomerID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, TransactionNumber);
				ps.setString(2, TransactionType);
				ps.setFloat(3, Amount);
				ps.setString(4, TransactionTime);
				ps.setString(5, TransactionDate);
				ps.setString(6, FromAccount);
				ps.setString(7, ToAccount);
				ps.setString(8, CustomerID);
				ps.executeUpdate();
			}
			return TransactionNumber;
		} catch (SQLException e) {
			logger.error("Failed to record transaction: {}", e.getMessage(), e);
			return null;
		}
	}

	@Override
	public List<List<String>> searchTransaction(String UName) {
		TransStore.clear();
		if (Startdate == null || Enddate == null || Startdate.isEmpty() || Enddate.isEmpty()) return TransStore;
		DBConnection ToDB = new DBConnection();
		String sql = "SELECT TransactionNumber, TransactionAmount, TransactionType, TransactionTime, TransactionDate, FromAccount, ToAccount FROM Transactions WHERE CustomerID = ? AND TransactionDate BETWEEN ? AND ?";
		try (Connection conn = ToDB.openConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, UName);
			ps.setString(2, Startdate);
			ps.setString(3, Enddate);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					List<String> cols = new ArrayList<>();
					cols.add(rs.getString("TransactionNumber"));
					cols.add(rs.getString("TransactionAmount"));
					cols.add(rs.getString("TransactionType"));
					cols.add(rs.getString("TransactionTime"));
					cols.add(rs.getString("TransactionDate"));
					cols.add(rs.getString("FromAccount"));
					cols.add(rs.getString("ToAccount"));
					TransStore.add(cols);
				}
			}
		} catch (SQLException e) {
			logger.error("Error searching transactions: {}", e.getMessage(), e);
		}
		return TransStore;
	}
}