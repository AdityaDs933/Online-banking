
package com.aditya;

import java.lang.*; //including Java packages used by this program
import java.sql.*;
import com.aditya.*;

/**
 * Account class implementing AccountService interface.
 * Uses parameterized queries to prevent SQL injection.
 */
public class Account implements AccountService
{
	private String Username, Password, Password1, Name;

	public Account(String UN, String PassW, String PassW1, String NM) {
		Username = UN;
		Password = PassW;
		Password1 = PassW1;
		Name = NM;
	}

	public Account(String UN, String PassW) {
		Username = UN;
		Password = PassW;
	}

    public boolean signUp() {
		if (!InputValidator.isValidUsername(Username) || 
		    !InputValidator.isValidPassword(Password) || 
		    !Password.equals(Password1)) {
			return false;
		}
		boolean done = true;
		try {
		    DBConnection ToDB = new DBConnection();
		    Connection DBConn = ToDB.openConn();
		    String checkSQL = "SELECT Username FROM Account WHERE Username = ?";
		    PreparedStatement checkStmt = DBConn.prepareStatement(checkSQL);
		    checkStmt.setString(1, Username);
		    ResultSet Rslt = checkStmt.executeQuery();
		    done = !Rslt.next();
		    checkStmt.close();
		    
		    if (done) {
				String insertSQL = "INSERT INTO Account(Username, Password, Name) VALUES (?, ?, ?)";
				PreparedStatement insertStmt = DBConn.prepareStatement(insertSQL);
				insertStmt.setString(1, Username);
				insertStmt.setString(2, Password);
				insertStmt.setString(3, Name);
				insertStmt.executeUpdate();
				insertStmt.close();
			}
		    ToDB.closeConn();
		}
	    catch(java.sql.SQLException e)
	    {         done = false;
				 System.out.println("SQLException: " + e);
				 while (e != null)
				 {   System.out.println("SQLState: " + e.getSQLState());
					 System.out.println("Message: " + e.getMessage());
					 System.out.println("Vendor: " + e.getErrorCode());
					 e = e.getNextException();
					 System.out.println("");
				 }
	    }
	    catch (java.lang.Exception e)
	    {         done = false;
				 System.out.println("Exception: " + e);
				 e.printStackTrace ();
	    }
	    return done;
	}
  public String signIn()
  {
		boolean done = !Username.equals("") && !Password.equals("");
		try
		{
		    if (done)
		    {
		        DBConnection ToDB = new DBConnection();
		        Connection DBConn = ToDB.openConn();
		        String sql = "SELECT Name FROM Account WHERE Username = ? AND Password = ?";
		        PreparedStatement pstmt = DBConn.prepareStatement(sql);
		        pstmt.setString(1, Username);
		        pstmt.setString(2, Password);
		        ResultSet Rslt = pstmt.executeQuery();
		        done = Rslt.next();
		        if (done) {
				     Name = Rslt.getString(1);
			    }
			    pstmt.close();
			    ToDB.closeConn();
			}
	    }
	    catch(java.sql.SQLException e)
	    {         done = false;
				 System.out.println("SQLException: " + e);
				 while (e != null)
				 {   System.out.println("SQLState: " + e.getSQLState());
					 System.out.println("Message: " + e.getMessage());
					 System.out.println("Vendor: " + e.getErrorCode());
					 e = e.getNextException();
					 System.out.println("");
				 }
	    }
	    catch (java.lang.Exception e)
	    {         done = false;
				 System.out.println("Exception: " + e);
				 e.printStackTrace ();
	    }
	    return done ? Name : "";
	}

	public boolean changePassword(String NewPassword) {
		if (!InputValidator.isValidPassword(NewPassword)) {
			return false;
		}
		boolean done = false;
		try {
		        DBConnection ToDB = new DBConnection();
		        Connection DBConn = ToDB.openConn();
		        String checkSQL = "SELECT * FROM Account WHERE Username = ? AND Password = ?";
		        PreparedStatement checkStmt = DBConn.prepareStatement(checkSQL);
		        checkStmt.setString(1, Username);
		        checkStmt.setString(2, Password);
		        ResultSet Rslt = checkStmt.executeQuery();
		        if (Rslt.next()) {
				    String updateSQL = "UPDATE Account SET Password = ? WHERE Username = ?";
				    PreparedStatement updateStmt = DBConn.prepareStatement(updateSQL);
				    updateStmt.setString(1, NewPassword);
				    updateStmt.setString(2, Username);
				    updateStmt.executeUpdate();
				    updateStmt.close();
                    done = true;
				}
		        checkStmt.close();
		        ToDB.closeConn();
		}
	    catch(java.sql.SQLException e)
	    {         done = false;
				 System.out.println("SQLException: " + e);
				 while (e != null)
				 {   System.out.println("SQLState: " + e.getSQLState());
					 System.out.println("Message: " + e.getMessage());
					 System.out.println("Vendor: " + e.getErrorCode());
					 e = e.getNextException();
					 System.out.println("");
				 }
	    }
	    catch (java.lang.Exception e)
	    {         done = false;
				 System.out.println("Exception: " + e);
				 e.printStackTrace ();
	    }
	    return done;
	}

	@Override
	public String getName() {
		return Name;
	}