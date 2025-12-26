/******************************************************************************
*	Program Author: Kavita Mishra for CSCI 6810 Java and the Internet	  *
*	Date: September, 2018													      *
*******************************************************************************/
package com.aditya;

import java.lang.*; //including Java packages used by this program
import java.sql.*;
import com.aditya.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SavingAccount
{   //Instance Variables
	private String SavingAccountNumber, CustomerName, CustomerID;
	private float Balance = -1, Amount = -1;

	private static final Logger logger = LoggerFactory.getLogger(SavingAccount.class);

	// expose account number for servlet transaction logic when necessary
	public String getSavingAccountNumber() { return SavingAccountNumber; }
	public void setSavingAccountNumber(String v) { this.SavingAccountNumber = v; }

	public SavingAccount(String SA_Num, String Cust_Name, String Cust_ID, String Amt) { //Constructor One with three parameters
		SavingAccountNumber = SA_Num;
		CustomerName = Cust_Name;
		CustomerID = Cust_ID;
		Amount = Float.parseFloat(Amt);
	}

	public SavingAccount(String SA_Num) { //Constructor Two with one parameter
		SavingAccountNumber = SA_Num;
	}
	public SavingAccount(){
	}
	public void setAmount(float amt) { this.Amount = amt; }
    public boolean openAcct() {
	     boolean done = false;
				try {
				    if (!done) {
				        DBConnection ToDB = new DBConnection(); //Have a connection to the DB
				        Connection DBConn = ToDB.openConn();
				        String checkSQL = "SELECT SavingAccountNumber FROM SavingAccount WHERE SavingAccountNumber = ?";
				        PreparedStatement checkStmt = DBConn.prepareStatement(checkSQL);
				        checkStmt.setString(1, SavingAccountNumber);
				        ResultSet Rslt = checkStmt.executeQuery();
				        done = !Rslt.next();
				        checkStmt.close();
				        if (done) {
						    String insertSQL = "INSERT INTO SavingAccount(SavingAccountNumber, CustomerName, Balance, CustomerID) VALUES (?, ?, ?, ?)";
						    PreparedStatement insertStmt = DBConn.prepareStatement(insertSQL);
						    insertStmt.setString(1, SavingAccountNumber);
						    insertStmt.setString(2, CustomerName);
						    insertStmt.setString(3, String.valueOf(Amount));
						    insertStmt.setString(4, CustomerID);
						    insertStmt.executeUpdate();
						    insertStmt.close();
					    }
					    ToDB.closeConn();
					}
				}
			    catch(java.sql.SQLException e) {
			        done = false;
			        logger.error("SQLException in openAcct: {}", e.getMessage(), e);
			        SQLException cur = e;
			        while (cur != null) {
			            logger.debug("SQLState: {}", cur.getSQLState());
			            logger.debug("Message: {}", cur.getMessage());
			            logger.debug("Vendor: {}", cur.getErrorCode());
			            cur = cur.getNextException();
			        }
			    }
			    catch (java.lang.Exception e) {
			        done = false;
			        logger.error("Exception in openAcct: {}", e.getMessage(), e);
			    }
	    return done;
	}
	public String getAccno(String C_ID) {  //Method to return a SavingAccount No.
					try {
					    DBConnection ToDB = new DBConnection();
					    String sql = "SELECT SavingAccountNumber FROM SavingAccount WHERE CustomerID = ?";
					    try (Connection DBConn = ToDB.openConn(); PreparedStatement Stmt = DBConn.prepareStatement(sql)) {
					        Stmt.setString(1, C_ID);
					        try (ResultSet Rslt = Stmt.executeQuery()) {
					            while (Rslt.next()) {
					                SavingAccountNumber = Rslt.getString("SavingAccountNumber");
					            }
					        }
					    }
					}
			    catch(java.sql.SQLException e) {
			        logger.error("SQLException in getAccno: {}", e.getMessage(), e);
			        SQLException cur = e;
			        while (cur != null) {
			            logger.debug("SQLState: {}", cur.getSQLState());
			            logger.debug("Message: {}", cur.getMessage());
			            logger.debug("Vendor: {}", cur.getErrorCode());
			            cur = cur.getNextException();
			        }
			    }
			    return SavingAccountNumber;
	}
    public float getBalance() {  //Method to return a SavingAccount balance
			try {
			    DBConnection ToDB = new DBConnection();
			    String sql = "SELECT Balance FROM SavingAccount WHERE SavingAccountNumber = ?";
			    try (Connection DBConn = ToDB.openConn(); PreparedStatement Stmt = DBConn.prepareStatement(sql)) {
			        Stmt.setString(1, SavingAccountNumber);
			        try (ResultSet Rslt = Stmt.executeQuery()) {
			            while (Rslt.next()) {
			                Balance = Rslt.getFloat(1);
			            }
			        }
			    }
			}
		    catch(java.sql.SQLException e) {
		        logger.error("SQLException in getBalance: {}", e.getMessage(), e);
		    }
		    catch (java.lang.Exception e) {
		        logger.error("Exception in getBalance: {}", e.getMessage(), e);
		    }
	    return Balance;
	}

    public float getBalance(String SaveAcctNumber) {  //Method to return a SavingAccount balance
		try {
		    DBConnection ToDB = new DBConnection();
		    String sql = "SELECT Balance FROM SavingAccount WHERE SavingAccountNumber = ?";
		    try (Connection DBConn = ToDB.openConn(); PreparedStatement Stmt = DBConn.prepareStatement(sql)) {
		        Stmt.setString(1, SaveAcctNumber);
		        try (ResultSet Rslt = Stmt.executeQuery()) {
		            while (Rslt.next()) {
		                Balance = Rslt.getFloat(1);
		            }
		        }
		    }
		}
	    catch(java.sql.SQLException e) {
	        logger.error("SQLException in getBalance(SaveAcctNumber): {}", e.getMessage(), e);
	    }
	    catch (java.lang.Exception e) {
	        logger.error("Exception in getBalance(SaveAcctNumber): {}", e.getMessage(), e);
	    }
	    return Balance;
	}
	public boolean deposit(String C_ID){

		boolean done = SavingAccountNumber != null && !SavingAccountNumber.isEmpty()
				&& C_ID != null && !C_ID.isEmpty();
		if (!done) return false;
		DBConnection DBconn = new DBConnection();
		try (Connection conn = DBconn.openConn()) {
			return depositWithConnection(conn, C_ID);
		} catch (Exception e) {
			logger.error("Exception in deposit: {}", e.getMessage(), e);
			return false;
		}
		}

	// New method: deposit using provided connection and PreparedStatement
	public boolean depositWithConnection(Connection conn, String C_ID) throws SQLException {
		boolean done = SavingAccountNumber != null && !SavingAccountNumber.isEmpty()
				&& C_ID != null && !C_ID.isEmpty();
		if (!done) return false;
		String selectSQL = "SELECT Balance FROM SavingAccount WHERE SavingAccountNumber = ? AND CustomerID = ?";
		try (PreparedStatement ps = conn.prepareStatement(selectSQL)) {
			ps.setString(1, SavingAccountNumber);
			ps.setString(2, C_ID);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Balance = rs.getFloat(1);
				} else {
					return false;
				}
			}
		}
		Balance = Balance + Amount;
		String updateSQL = "UPDATE SavingAccount SET Balance = ? WHERE SavingAccountNumber = ?";
		try (PreparedStatement ups = conn.prepareStatement(updateSQL)) {
			ups.setFloat(1, Balance);
			ups.setString(2, SavingAccountNumber);
			int updated = ups.executeUpdate();
			return updated > 0;
		}
	}
		public boolean Withdraw(String C_ID){

			boolean done = SavingAccountNumber != null && !SavingAccountNumber.isEmpty()
					&& C_ID != null && !C_ID.isEmpty();
			if (!done) return false;
			DBConnection DBconn = new DBConnection();
			try (Connection conn = DBconn.openConn()) {
				return withdrawWithConnection(conn, C_ID);
			} catch (Exception e) {
				logger.error("Exception in Withdraw: {}", e.getMessage(), e);
				return false;
			}
		}

		// New method: withdraw using provided connection and PreparedStatement
		public boolean withdrawWithConnection(Connection conn, String C_ID) throws SQLException {
			boolean done = SavingAccountNumber != null && !SavingAccountNumber.isEmpty()
					&& C_ID != null && !C_ID.isEmpty();
			if (!done) return false;
			String selectSQL = "SELECT Balance FROM SavingAccount WHERE SavingAccountNumber = ? AND CustomerID = ?";
			try (PreparedStatement ps = conn.prepareStatement(selectSQL)) {
				ps.setString(1, SavingAccountNumber);
				ps.setString(2, C_ID);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						Balance = rs.getFloat(1);
					} else {
						return false;
					}
				}
			}
			if (Balance >= Amount) {
				Balance = Balance - Amount;
				String updateSQL = "UPDATE SavingAccount SET Balance = ? WHERE SavingAccountNumber = ?";
				try (PreparedStatement ups = conn.prepareStatement(updateSQL)) {
					ups.setFloat(1, Balance);
					ups.setString(2, SavingAccountNumber);
					int updated = ups.executeUpdate();
					return updated > 0;
				}
			}
			return false;
		   }
}