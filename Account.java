
package com.aditya;

import java.lang.*; //including Java packages used by this program
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;
import com.aditya.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Account class implementing AccountService interface.
 * Uses parameterized queries to prevent SQL injection.
 */
public class Account implements AccountService
{
	private String Username, Password, Password1, Name;
	private DBConnection injectedDB;
	private static final Logger logger = LoggerFactory.getLogger(Account.class);

	public Account(String UN, String PassW, String PassW1, String NM) {
		Username = UN;
		Password = PassW;
		Password1 = PassW1;
		Name = NM;
	}

	// Test-friendly constructor with DB injection
	public Account(String UN, String PassW, String PassW1, String NM, DBConnection db) {
		this(UN, PassW, PassW1, NM);
		this.injectedDB = db;
	}

	public Account(String UN, String PassW) {
		Username = UN;
		Password = PassW;
	}

	// Test-friendly constructor with DB injection
	public Account(String UN, String PassW, DBConnection db) {
		this(UN, PassW);
		this.injectedDB = db;
	}

    public boolean signUp() {
		if (!InputValidator.isValidUsername(Username) ||
			!InputValidator.isValidPassword(Password) ||
			!Password.equals(Password1)) {
			return false;
		}
		DBConnection db = (injectedDB != null) ? injectedDB : new DBConnection();
		try (Connection conn = db.openConn();
			 PreparedStatement checkStmt = conn.prepareStatement("SELECT 1 FROM Account WHERE Username = ?")) {
			checkStmt.setString(1, Username);
			try (ResultSet rs = checkStmt.executeQuery()) {
				if (rs.next()) return false; // username exists
			}

			String insertSQL = "INSERT INTO Account(Username, Password, Name) VALUES (?, ?, ?)";
			try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
				String hashed = BCrypt.hashpw(Password, BCrypt.gensalt());
				insertStmt.setString(1, Username);
				insertStmt.setString(2, hashed);
				insertStmt.setString(3, Name);
				insertStmt.executeUpdate();
			}
			return true;
		} catch (SQLException e) {
			logger.error("signUp failed: {}", e.getMessage(), e);
			return false;
		}
	}
  public String signIn()
  {
		if (Username == null || Username.isEmpty() || Password == null || Password.isEmpty()) return "";
		DBConnection db = (injectedDB != null) ? injectedDB : new DBConnection();
		try (Connection conn = db.openConn();
			 PreparedStatement ps = conn.prepareStatement("SELECT Password, Name FROM Account WHERE Username = ?")) {
			ps.setString(1, Username);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					String storedHash = rs.getString(1);
					if (BCrypt.checkpw(Password, storedHash)) {
						Name = rs.getString(2);
						return Name;
					}
				}
			}
		} catch (SQLException e) {
			logger.error("signIn failed: {}", e.getMessage(), e);
		}
		return "";
	}

	public boolean changePassword(String NewPassword) {
		if (!InputValidator.isValidPassword(NewPassword)) {
			return false;
		}
		DBConnection db = (injectedDB != null) ? injectedDB : new DBConnection();
		try (Connection conn = db.openConn();
			 PreparedStatement checkStmt = conn.prepareStatement("SELECT Password FROM Account WHERE Username = ?")) {
			checkStmt.setString(1, Username);
			try (ResultSet rs = checkStmt.executeQuery()) {
				if (rs.next()) {
					String storedHash = rs.getString(1);
					if (BCrypt.checkpw(Password, storedHash)) {
						String newHashed = BCrypt.hashpw(NewPassword, BCrypt.gensalt());
						try (PreparedStatement updateStmt = conn.prepareStatement("UPDATE Account SET Password = ? WHERE Username = ?")) {
							updateStmt.setString(1, newHashed);
							updateStmt.setString(2, Username);
							updateStmt.executeUpdate();
							return true;
						}
					}
				}
			}
		} catch (SQLException e) {
			logger.error("changePassword failed: {}", e.getMessage(), e);
		}
		return false;
	}

	@Override
	public String getName() {
		return Name;
	}