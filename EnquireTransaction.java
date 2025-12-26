package com.aditya;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnquireTransaction {
	private static final Logger logger = LoggerFactory.getLogger(EnquireTransaction.class);
	public List<?>[] FetchTransaction(String Uname, String StartDate, String EndDate) {
		boolean done = StartDate != null && EndDate != null && !StartDate.isEmpty() && !EndDate.isEmpty();
		List<String> C_Names = new ArrayList<>();
		List<List<Object>> Rows = new ArrayList<>();
		if (!done) {
			return new List[] { C_Names, Rows };
		}

		C_Names.add("Trans_Number");
		C_Names.add("Trans_Amount");
		C_Names.add("Trans_Time");
		C_Names.add("Trans_Date");
		C_Names.add("From_Acc");
		C_Names.add("To_Acc");
		C_Names.add("Trans_Type");

		DBConnection ToDB = new DBConnection();
		String sql = "SELECT * FROM Transactions WHERE CustomerID = ? AND TransactionDate BETWEEN ? AND ?";
		try (Connection DBConn = ToDB.openConn(); PreparedStatement stmt = DBConn.prepareStatement(sql)) {
			stmt.setString(1, Uname);
			stmt.setString(2, StartDate);
			stmt.setString(3, EndDate);
			try (ResultSet rslt = stmt.executeQuery()) {
				ResultSetMetaData rsmd = rslt.getMetaData();
				while (rslt.next()) {
					Rows.add(ToDB.getNextRow(rslt, rsmd));
				}
			}
		} catch (SQLException e) {
			logger.error("SQLException while fetching transactions: {}", e.getMessage(), e);
			while (e != null) {
				logger.debug("SQLState: {}", e.getSQLState());
				logger.debug("Message: {}", e.getMessage());
				logger.debug("Vendor: {}", e.getErrorCode());
				e = e.getNextException();
			}
		} catch (Exception e) {
			logger.error("Exception while fetching transactions: {}", e.getMessage(), e);
		}
		return new List[] { C_Names, Rows };
	}
}
									Rows.add(ToDB.getNextRow(rslt, rsmd));

								}

							}

