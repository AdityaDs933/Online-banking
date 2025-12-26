/******************************************************************************
*	Program Author: Dr. Yongming Tang for CSCI 6810 Java and the Internet	  *
*	Date: October, 2013													      *
*******************************************************************************/
package com.aditya;

import java.lang.*; //including Java packages used by this program
import java.sql.*;
import com.aditya.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckingAccount
{   //Instance Variables
	private String CheckingAccountNumber, CustomerName, CustomerID;
	private float Balance = -1, Amount = -1;
    
	// expose account number for servlet transaction logic when necessary
	public String getCheckingAccountNumber() { return CheckingAccountNumber; }
	public void setCheckingAccountNumber(String v) { this.CheckingAccountNumber = v; }

		public CheckingAccount(String CA_Num, String Cust_Name, String Cust_ID, String Amt) { //Constructor One with three parameters
		CheckingAccountNumber = CA_Num;
		CustomerName = Cust_Name;
		CustomerID = Cust_ID;
		//Balance = Float.parseFloat(Bal);
		Amount = Float.parseFloat(Amt);
	}



	public CheckingAccount(String CA_Num) { //Constructor Two with one parameter
		CheckingAccountNumber = CA_Num;
	}
	public CheckingAccount() { //Constructor with no parameters for fetching the account no.
		}

    private static final Logger logger = LoggerFactory.getLogger(CheckingAccount.class);

	public void setAmount(float amt) { this.Amount = amt; }


    public boolean openAcct() {
	     boolean done = false;
				try {
				    if (!done) {
				        DBConnection ToDB = new DBConnection(); //Have a connection to the DB
				        Connection DBConn = ToDB.openConn();
				        String checkSQL = "SELECT CheckingAccountNumber FROM CheckingAccount WHERE CheckingAccountNumber = ?";
				        PreparedStatement checkStmt = DBConn.prepareStatement(checkSQL);
				        checkStmt.setString(1, CheckingAccountNumber);
				        ResultSet Rslt = checkStmt.executeQuery();
				        done = !Rslt.next();
				        checkStmt.close();
				        if (done) {
						    String insertSQL = "INSERT INTO CheckingAccount(CheckingAccountNumber, CustomerName, Balance, CustomerID) VALUES (?, ?, ?, ?)";
						    PreparedStatement insertStmt = DBConn.prepareStatement(insertSQL);
						    insertStmt.setString(1, CheckingAccountNumber);
						    insertStmt.setString(2, CustomerName);
						    insertStmt.setFloat(3, Amount);
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
				} catch (java.lang.Exception e) {
					done = false;
					logger.error("Exception in openAcct: {}", e.getMessage(), e);
				}
	    return done;
	}
	public String getAccno(String C_ID) {  //Method to return a CheckingAccount No.
			try {
			        DBConnection ToDB = new DBConnection(); //Have a connection to the DB
			        Connection DBConn = ToDB.openConn();
			        String sql = "SELECT CheckingAccountNumber FROM CheckingAccount WHERE CustomerID = ?";
			        PreparedStatement Stmt = DBConn.prepareStatement(sql);
			        Stmt.setString(1, C_ID);
			        ResultSet Rslt = Stmt.executeQuery();

			        while (Rslt.next()) {
					CheckingAccountNumber = Rslt.getString("CheckingAccountNumber");

				    }
				    Stmt.close();
				    ToDB.closeConn();
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
		    return CheckingAccountNumber;
	}
    public float getBalance() {  //Method to return a CheckingAccount balance
		try {
		        DBConnection ToDB = new DBConnection(); //Have a connection to the DB
		        Connection DBConn = ToDB.openConn();
		        String sql = "SELECT Balance FROM CheckingAccount WHERE CheckingAccountNumber = ?";
		        PreparedStatement Stmt = DBConn.prepareStatement(sql);
		        Stmt.setString(1, CheckingAccountNumber);
		        ResultSet Rslt = Stmt.executeQuery();

		        while (Rslt.next()) {
					Balance = Rslt.getFloat(1);
			    }
			    Stmt.close();
			    ToDB.closeConn();
	    }
	    catch(java.sql.SQLException e) {
	        logger.error("SQLException in getBalance: {}", e.getMessage(), e);
	        SQLException cur = e;
	        while (cur != null) {
	            logger.debug("SQLState: {}", cur.getSQLState());
	            logger.debug("Message: {}", cur.getMessage());
	            logger.debug("Vendor: {}", cur.getErrorCode());
	            cur = cur.getNextException();
	        }
	    }
	    catch (java.lang.Exception e) {
	        logger.error("Exception in getBalance: {}", e.getMessage(), e);
	    }
	    return Balance;
	}

    public float getBalance(String ChkAcctNumber) {  //Method to return a CheckingAccount balance
		try {
		        DBConnection ToDB = new DBConnection(); //Have a connection to the DB
		        Connection DBConn = ToDB.openConn();
		        String sql = "SELECT Balance FROM CheckingAccount WHERE CheckingAccountNumber = ?";
		        PreparedStatement Stmt = DBConn.prepareStatement(sql);
		        Stmt.setString(1, ChkAcctNumber);
		        ResultSet Rslt = Stmt.executeQuery();

		        while (Rslt.next()) {
					Balance = Rslt.getFloat(1);
			    }
			    Stmt.close();
			    ToDB.closeConn();
	    }
	    catch(java.sql.SQLException e) {
	        logger.error("SQLException in getBalance(ChkAcctNumber): {}", e.getMessage(), e);
	        SQLException cur = e;
	        while (cur != null) {
	            logger.debug("SQLState: {}", cur.getSQLState());
	            logger.debug("Message: {}", cur.getMessage());
	            logger.debug("Vendor: {}", cur.getErrorCode());
	            cur = cur.getNextException();
	        }
	    }
	    catch (java.lang.Exception e) {
	        logger.error("Exception in getBalance(ChkAcctNumber): {}", e.getMessage(), e);
	    }
	    return Balance;
	}

	public boolean deposit(String C_ID){
		boolean done = CheckingAccountNumber != null && !CheckingAccountNumber.isEmpty()
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

	// New method: performs deposit using provided connection and PreparedStatement
	public boolean depositWithConnection(Connection conn, String C_ID) throws SQLException {
		boolean done = CheckingAccountNumber != null && !CheckingAccountNumber.isEmpty()
				&& C_ID != null && !C_ID.isEmpty();
		if (!done) return false;
		String selectSQL = "SELECT Balance FROM CheckingAccount WHERE CheckingAccountNumber = ? AND CustomerID = ?";
		try (PreparedStatement ps = conn.prepareStatement(selectSQL)) {
			ps.setString(1, CheckingAccountNumber);
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
		String updateSQL = "UPDATE CheckingAccount SET Balance = ? WHERE CheckingAccountNumber = ?";
		try (PreparedStatement ups = conn.prepareStatement(updateSQL)) {
			ups.setFloat(1, Balance);
			ups.setString(2, CheckingAccountNumber);
			int updated = ups.executeUpdate();
			return updated > 0;
		}
	}
	public boolean Withdraw(String C_ID){
		boolean done = CheckingAccountNumber != null && !CheckingAccountNumber.isEmpty()
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

	// New method: performs withdraw using provided connection and PreparedStatement
	public boolean withdrawWithConnection(Connection conn, String C_ID) throws SQLException {
		boolean done = CheckingAccountNumber != null && !CheckingAccountNumber.isEmpty()
				&& C_ID != null && !C_ID.isEmpty();
		if (!done) return false;
		String selectSQL = "SELECT Balance FROM CheckingAccount WHERE CheckingAccountNumber = ? AND CustomerID = ?";
		try (PreparedStatement ps = conn.prepareStatement(selectSQL)) {
			ps.setString(1, CheckingAccountNumber);
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
			String updateSQL = "UPDATE CheckingAccount SET Balance = ? WHERE CheckingAccountNumber = ?";
			try (PreparedStatement ups = conn.prepareStatement(updateSQL)) {
				ups.setFloat(1, Balance);
				ups.setString(2, CheckingAccountNumber);
				int updated = ups.executeUpdate();
				return updated > 0;
			}
		}
		return false;
	   }
}